package kinectPlayground;

import java.util.ArrayList;

import midi.PitchBar;
import controlP5.CheckBox;
import controlP5.ControlP5;
import controlP5.Textlabel;
import controlP5.Toggle;

import processing.core.PApplet;
import processing.core.PVector;
import themidibus.MidiBus;
//import processing.opengl.*;

import SimpleOpenNI.SimpleOpenNI;

public class AppletMainMidi extends PApplet {
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
	ControlP5 cp5;
	Textlabel availOutputsLabel;
	CheckBox availOutputsCheckbox;	
	PitchBar pitchBar;	
	int lastMapped = -1;
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
		pitchBar = new PitchBar(this);		
		MidiBus.list();
		myBus = new MidiBus(this, -1, -1);
		
		cp5 = new ControlP5(this);
		cp5.addButton("b1").setPosition(100, 120).setSize(200, 19);

		availOutputsLabel = cp5.addTextlabel("label")
				.setText("Available Outputs:")
				.setPosition(20, 20)
				.setFont(createFont("Verdana", 14));

		availOutputsCheckbox = cp5.addCheckBox("availOutCheckBoxHandler").setPosition(20, 40)
				.setColorForeground(color(120)).setColorActive(color(255))
				.setColorLabel(color(255)).setSize(10, 10);
		
		String[] availOutputs = MidiBus.availableOutputs();
		String[] attachedOutpus = myBus.attachedOutputs();
		
		for (int i = 0; i < availOutputs.length; i++) {
			String output = availOutputs[i];
			availOutputsCheckbox.addItem(output, i);
			for(int j = 0; j < attachedOutpus.length; j++){
				if(attachedOutpus[j] == output){
					availOutputsCheckbox.activate(output);
				}
			}
		}
		
		//printAttachedOutputs();		
		
	}	
	
	public void availOutCheckBoxHandler(float[] a) {
		
		for(int i = 0; i < a.length; i++){
			Toggle item = availOutputsCheckbox.getItem(i);
			if(a[i] == 1.0f){
				if(!isAttachedOutput(item.getName())){
					if(!myBus.addOutput(i)){
						println("Adding Device FAILD!");
						availOutputsCheckbox.deactivate(i);
					} else {
						println("Device Successfully Added!");
					}
				}
			} else {
				if(isAttachedOutput(item.getName())){
					if(myBus.removeOutput(i)){
						println("Device Successfully Removed!");
					} else {
						println("Removing Device FAILD!");
						availOutputsCheckbox.activate(i);
					}
					
				}
			}
		}
	}
	
	private Boolean isAttachedOutput (String name){
	//	println("name"+name);
		String[] attachedOutputs = myBus.attachedOutputs();
		for(int i = 0; i < attachedOutputs.length; i++){
		//	println("name2"+attachedOutputs[i]);
			if(name == attachedOutputs[i]){
				println("isAttached " + name);
				return true;
			}
		}
		return false;
	}	

	private void printAvailableInputs() {
		println("Available Inputs:");
		printIOString(myBus.availableInputs());
	}
	
	private void printAvailableOuputs() {
		println("Available Outputs:");
		printIOString(myBus.availableOutputs());
	}

	private void printAttachedOutputs() {
		println("Atached Outputs");
		printIOString(myBus.attachedOutputs());
	}

	private void printIOString(String[] iostrings) {
		for (int i = 0; i < iostrings.length; i++) {
			println("[" + i + "]" + iostrings[i]);
		}

	}
	
	void dump(String s){
		println(s);
	}

	public void b1(int theValue) {
		println("a button event from b1: " + theValue);
		printAvailableOuputs();
		printAttachedOutputs();
		
		int channel = 0;
		int pitch = 69;
		int velocity = 127;

		myBus.sendNoteOn(channel, pitch, velocity); // Send a Midi noteOn
		delay(200);
		myBus.sendNoteOff(channel, pitch, velocity); // Send a Midi nodeOff
		delay(100);
	}

	public void draw(){
		//Midi 
		
		int channel = 0;
		int velocity = 127;
		int mapped = pitchBar.display();
		if(mapped != -1){
			
			if(lastMapped != -1){
				if(lastMapped <= mapped){
					mapped = mapped - lastMapped;
				} else {
					mapped = lastMapped - mapped;
				}
				if(mapped <= 20){
					return;
				}
				myBus.sendNoteOff(channel, lastMapped, velocity);
			}
			
			int pitch = mapped;
			
			
			myBus.sendNoteOn(channel, pitch, velocity);
		}
		
		
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
	
	public void checkBB(User user){
		ArrayList<BoundingBox> bBox=stage.bBox;
		
		for (BoundingBox box: bBox) {
			if (box.checkPoint(user.getLeftHand())
					 || box.checkPoint(user.getRightHand())) {						
				
				System.out.println(user.getRightHand());
				box.color=255;					
			}
			else box.color=123;
			
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
