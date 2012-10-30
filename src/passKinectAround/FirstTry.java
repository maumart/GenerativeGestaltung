package passKinectAround;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
//import processing.opengl.*;

import SimpleOpenNI.SimpleOpenNI;

public class FirstTry extends PApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SimpleOpenNI kinect;
	boolean initialized; 
	Kinect k;
	int counter;

	// Kamerabilder
	PImage depth;
	PImage rgb;
	PImage scene;	
	Test2 t;
		
	float scaleFactor=1.5f;
	int max=640*480;
	int[] userPixels = new int[max];
	
	ArrayList<PVector> joints= new ArrayList<PVector>();

	public void setup(){		
		k = new Kinect(this);
		kinect = k.getKinect();

		t = new Test2(kinect, k, this);
		
		//Processing Stuff
		this.size(1280, 720);
		//this.size(kinect.depthWidth(),kinect.depthHeight());
		this.stroke(255);
		this.strokeWeight(5);
		this.noFill();  
		this.smooth();
		this.frameRate(60);
	}	

	public void draw(){
		boolean b = k.checkAvailability();		
		System.out.println(counter);
		
		this.background(0);
		// Kinect updaten jeden Frame --> WICHTIG
		kinect.update();	

		//Userpixel in Array speichern
		kinect.getUserPixels(1, userPixels);
						
		// Buehne erstellen
		//if (counter > 100) 
		//this.createScene();	
		//t.test(rgb, this, kinect);
		t.test();
		
		
		//this.setSize(1600, 900);
		counter++;
	}
	
	private void createScene(){
		// Scene = Mit Skelett ect.
		/*
		scene=kinect.sceneImage(); 
		depth=kinect.depthImage();	
		rgb=kinect.rgbImage();

		// Notwendig um ueber Array zu iterieren
		//depth.loadPixels();
		
		// Sceneimages
		image(scene,0,0,scene.width*scaleFactor,scene.height*scaleFactor);
		image(rgb,960,0,320,240);
		image(depth,960,240,320,240);
		image(scene,960,480,320,240);
		
			*/
		
		//t.test(rgb, this, kinect);
		
	
	}	
	
	// Calback Neuer User
	public void onNewUser(int userId){
		println("Neuer User");
		kinect.startPoseDetection("Psi",userId);
		kinect.startTrackingSkeleton(userId);
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
