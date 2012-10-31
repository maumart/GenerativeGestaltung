package kinectPlayground;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import themidibus.MidiBus;
//import processing.opengl.*;

import SimpleOpenNI.SimpleOpenNI;

public class AppletMainMidiBasic extends PApplet {
	private static final long serialVersionUID = 1L;

	SimpleOpenNI kinect;
	boolean initialized;
	Kinect k;	
	Stage stage;
	PApplet papplet=this;
	ArrayList<PVector> joints= new ArrayList<PVector>();
	ArrayList<User> users= new ArrayList<User>();
	
	int max=640*480;
	int[] userPixels = new int[max];
	
	// Midi
	public MidiBus myBus;	
	// Midi

	public void setup(){		
		k = new Kinect(this);
		kinect = k.getKinect();
		stage = new Stage(kinect, k, this);	

		//Processing Stuff
		this.size(1440, 720);
		//this.size(kinect.depthWidth(),kinect.depthHeight());
		this.stroke(255);
		this.strokeWeight(5);
		this.noFill();  
		this.smooth();
		this.frameRate(60);	
		
		
		// Midi
		MidiBus.list();		
		myBus = new MidiBus(this, "Real Time Sequencer", "Microsoft MIDI Mapper");
		
	}	
	
 
	public void draw(){		
		
		//initialized = k.checkAvailability();
		//this.translate(0,0);
		
		this.background(0);
		
		// Kinect updaten jeden Frame --> WICHTIG
		kinect.update();		

		//Userpixel in Array speichern
		//kinect.getUserPixels(1, userPixels);
						
		// Buehne erstellen		
		stage.createScene();
		
		//Geht im aktuell Treiber nicht 
		//kinect.moveKinect(50);		
		
		// Userskelett zeichnen
		//if (users.size() > 0) {
		for (User user: users) {			
			if(kinect.isTrackingSkeleton(user.userId)) {
				user.updateLimbs();
				//user.drawSkeleton();				
				checkBB(user);
				
			}			
		
		}			
		//}			
		
		
	}
	
	void noteOn(int channel, int pitch, int velocity) {
		// Receive a noteOn
		println();
		println("Note On:");
		println("--------");
		println("Channel:"+channel);
		println("Pitch:"+pitch);
		println("Velocity:"+velocity);
	}

	void noteOff(int channel, int pitch, int velocity) {
		// Receive a noteOff
		println();
		println("Note Off:");
		println("--------");
		println("Channel:"+channel);
		println("Pitch:"+pitch);
		println("Velocity:"+velocity);
	}

	void controllerChange(int channel, int number, int value) {
		// Receive a controllerChange
		println();
		println("Controller Change:");
		println("--------");
		println("Channel:"+channel);
		println("Number:"+number);
		println("Value:"+value);
	}

	
	public void checkBB(User user){
		ArrayList<BoundingBox> bBox=stage.bBox;
		
		// Midi
		int channel = 0;
		int pitch = 64;
		int velocity = 127;
		
		//BB durchlaufen
		for (BoundingBox box: bBox) {
			
			//Linke Hand
			if (box.checkPoint(user.getLeftHand())){
				int pitchLeft=box.checkYPos(user.getLeftHand());
				myBus.sendNoteOn(channel, pitchLeft, velocity); // Send a Midi noteOn
				delay(100);
			}
			
			//Rechte Hand
			if (box.checkPoint(user.getRightHand())){
				int pitchRight=box.checkYPos(user.getRightHand());
				myBus.sendNoteOn(channel, pitchRight, velocity); // Send a Midi noteOn
				delay(100);				
			}
			
			//Beide			
			if (box.checkPoint(user.getLeftHand())
					 || box.checkPoint(user.getRightHand())) {						
				
				//Hover Box
				box.color=box.hoverColor;				
				
				/*
				int value=box.checkYPos(user.getLeftHand());
				System.out.println(value);
				//myBus.sendNoteOn(channel, (int) Math.round(Math.random()*100), velocity); // Send a Midi noteOn
				myBus.sendNoteOn(channel, value, velocity); // Send a Midi noteOn
				delay(100);
				
				//int i = map(value, istart, istop, ostart, ostop)
				 * 
				 * */
				 
				
			}
			else box.color=box.normColor;
			
			myBus.sendNoteOff(channel, pitch, velocity); // Send a Midi nodeOff
			//delay(100);
			
		}		
		
	}
	
	// Callbacks Simpleopenni

	// Calback Neuer User
	public void onNewUser(int userId){
		println("Neuer User erkannt");
		kinect.startPoseDetection("Psi",userId);
		kinect.startTrackingSkeleton(userId);
		
		// Erkannter User zur Collection hinzufuegen
		User user= new User(papplet, kinect, userId);
		users.add(user);		
	}
	
	// Callback Kalibrierungsbeginn
	public void onStartCalibration(int userId){
		println("onStartCalibration - userId: " + userId);
	}
	// Callback Kalibrierungsende
	public void onEndCalibration(int userId, boolean successfull){
		println("onEndCalibration - userId: " + userId + ", successfull: " + successfull);

		if (successfull){ 
			println("  User calibrated !!!");
			kinect.startTrackingSkeleton(userId);
			
		} else { 
			println("  Failed to calibrate user !!!");
			println("  Start pose detection");
			kinect.startPoseDetection("Psi",userId);
		}
	}
	// Callback Pose Begin 
	public void onStartPose(String pose,int userId){
		println("onStartPose - userId: " + userId + ", pose: " + pose);
		println(" stop pose detection");

		kinect.stopPoseDetection(userId); 
		kinect.requestCalibrationSkeleton(userId, true);

	}
	// Callback Pose Ende
	public void onEndPose(String pose,int userId){
		println("onEndPose - userId: " + userId + ", pose: " + pose);
	}	

}
