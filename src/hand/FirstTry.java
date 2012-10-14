package hand;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.*;

import SimpleOpenNI.SimpleOpenNI;

public class FirstTry extends PApplet {
	SimpleOpenNI kinect;

	// Kamerabilder
	PImage depth;
	PImage rgb;
	PImage scene;
	
	float scaleFactor=1.5f;
	int max=640*480;
	int[] userPixels = new int[max];
	
	ArrayList<PVector> joints= new ArrayList<PVector>();

	public void setup(){		
		Kinect k = new Kinect(this);
		kinect = k.getKinect();

		//Processing Stuff
		this.size(1280, 720);
		//this.size(kinect.depthWidth(),kinect.depthHeight());
		this.stroke(255);
		this.strokeWeight(5);
		this.noFill();  
		this.smooth();  
		//size(context.depthWidth(), context.depthHeight());
		this.frameRate(60);
	}	

	public void draw(){
		this.background(0);
		// Kinect updaten jeden Frame --> WICHTIG		
		kinect.update();

		//Userpixel in Array speichern
		kinect.getUserPixels(1, userPixels);		
		
		// Buehne erstellen
		this.createScene();		

		// Ueber erkannte User iterieren
		int[] userList = kinect.getUsers();
		for(int i=0;i<userList.length;i++) {
			if(kinect.isTrackingSkeleton(userList[i]))
				// Skelett zeichnen
				this.drawSkeleton(userList[i]);				
		}

		if(kinect.isTrackingSkeleton(1)) {	 
			// Skelett zeichnen
			this.drawHands(1);
		}	
		
		//this.setSize(1600, 900);
	}
	
	private void createScene(){
		// Scene = Mit Skelett ect.
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
	}

	private void drawHands(int userId){
		//Haende	
		PVector rightHand = new PVector();
		PVector rightHand2 =  new PVector();
		PVector leftHand = new PVector();
		PVector leftHand2 =  new PVector();
		
		//Handposition auslesen 
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HAND,rightHand);
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HAND,leftHand);

		// Pos Konvertieren
		kinect.convertRealWorldToProjective(rightHand, rightHand2); 
		kinect.convertRealWorldToProjective(leftHand, leftHand2);
		
		//Vergroesserung der Vektoren
		rightHand2.mult(scaleFactor);
		leftHand2.mult(scaleFactor);

		color(255,0,255);
		fill(0,255);	

			// SKalierungsfaktor fuer 3D
			//int sfaktorr=(int) (map(rightHand2.z,0,4000,150,10));
			//int sfaktorl=(int) (map(leftHand2.z,0,4000,150,10));	

		// Handboxen zeichnen x y z
		float rectSize=50*scaleFactor;		
		rect(leftHand2.x-rectSize/2,leftHand2.y-rectSize/2,rectSize,rectSize);
		rect(rightHand2.x-rectSize/2,rightHand2.y-rectSize/2,rectSize,rectSize);		
		
		//System.out.println(rightHand2);		
	}	

	private void drawSkeleton(int userId) {
		//Koerperteilevektoren
		PVector head = new PVector();
		PVector neck = new PVector();		
		PVector shoulderLeft = new PVector();
		PVector shoulderRight = new PVector();
		PVector elbowLeft = new PVector();
		PVector elbowRight = new PVector();
		PVector handLeft = new PVector();
		PVector handRight = new PVector();
		PVector torso = new PVector();
		PVector hipLeft = new PVector();
		PVector hipRight = new PVector();
		PVector kneeLeft = new PVector();
		PVector kneeRight = new PVector();
		PVector footRight = new PVector();
		PVector footLeft = new PVector();
		
		// Vektoren fuellen
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_HEAD,head);
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_NECK,neck);
		
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER,shoulderLeft);
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER,shoulderRight);
		
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW,elbowRight);
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_ELBOW,elbowLeft);		
		
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HAND,handRight);
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HAND,handLeft);
		
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_TORSO,torso);
		
		//Konvertieren in Buehne
		kinect.convertRealWorldToProjective(head, head);
		kinect.convertRealWorldToProjective(neck, neck);				
		kinect.convertRealWorldToProjective(torso, torso);
		kinect.convertRealWorldToProjective(shoulderLeft, shoulderLeft);
		kinect.convertRealWorldToProjective(shoulderRight, shoulderRight);
		kinect.convertRealWorldToProjective(elbowRight, elbowRight);
		kinect.convertRealWorldToProjective(elbowLeft, elbowLeft);
		kinect.convertRealWorldToProjective(handRight, handRight);
		kinect.convertRealWorldToProjective(handLeft, handLeft);		
		
		this.stroke(20);
		this.fill(255);
		
		//Multiplizieren Skaliserungsfaktor
		head.mult(scaleFactor);
		neck.mult(scaleFactor);
		shoulderLeft.mult(scaleFactor);
		shoulderRight.mult(scaleFactor);
		elbowLeft.mult(scaleFactor);
		elbowRight.mult(scaleFactor);		
		handLeft.mult(scaleFactor);
		handRight.mult(scaleFactor);	
		
		//Liniezeichnen
		line(head.x,head.y,neck.x,neck.y);
		line(neck.x,neck.y,shoulderLeft.x,shoulderLeft.y);
		line(neck.x,neck.y,shoulderRight.x,shoulderRight.y);
		line(shoulderLeft.x,shoulderLeft.y,elbowLeft.x,elbowLeft.y);
		line(shoulderRight.x,shoulderRight.y,elbowRight.x,elbowRight.y);		
		line(elbowLeft.x,elbowLeft.y,handLeft.x,handLeft.y);
		line(elbowRight.x,elbowRight.y,handRight.x,handRight.y);
		
		
		//BoundingBox bBox= this.drawBoundingBox();
		
		BoundingBox bBox = new BoundingBox(50*scaleFactor, 50*scaleFactor, 100*scaleFactor, 100*scaleFactor);
		rect(bBox.x,bBox.y,bBox.width,bBox.height);	
		
		//Boundingbox Testen
		if (bBox.checkPoint(handLeft) || bBox.checkPoint(handRight)){ 
			System.out.println("Inside");
			textSize(50);
			text("Inside", scene.width/2,scene.height/2);
		}
		

	}
	
	private BoundingBox drawBoundingBox(){
		BoundingBox bBox = new BoundingBox(50*scaleFactor, 50*scaleFactor, 50*scaleFactor, 50*scaleFactor);
		rect(bBox.x,bBox.y,bBox.width,bBox.height);	
		return bBox;
		
	}
	
	private void drawSkeletonOld(int userId) {		
		//OLD
		/*
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);
	
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);
	
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);
	
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
	
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_LEFT_HIP);
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP, SimpleOpenNI.SKEL_LEFT_KNEE);
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE, SimpleOpenNI.SKEL_LEFT_FOOT);
	
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_RIGHT_HIP);
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP, SimpleOpenNI.SKEL_RIGHT_KNEE);
			kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE, SimpleOpenNI.SKEL_RIGHT_FOOT);  
		*/		
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
