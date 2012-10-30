package kinectPlayground;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class User {
	SimpleOpenNI kinect;
	int userId;
	float scaleFactor=1.5f;
	PApplet p;
	public PVector leftHand;
	public PVector rightHand;
	
	public User(PApplet p, SimpleOpenNI kinect, int userId){
		this.kinect=kinect;
		this.userId=userId;
		this.p= p;	
	}
	
	public PVector getLeftHand(){		
		return leftHand;
	}
	
	public PVector getRightHand(){		
		return rightHand;
	}
	
	public void updateLimbs(){		
		PVector rightHandReal = new PVector();
		PVector rightHandProjected =  new PVector();
		
		PVector leftHandReal = new PVector();
		PVector leftHandProjected =  new PVector();
		
		PVector leftHandProjectedUnscalled = new PVector();
		PVector rightHandProjectedUnscalled =  new PVector();
		
		//Handposition auslesen 
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HAND,rightHandReal);
		kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HAND,leftHandReal);

		// Pos Konvertieren
		kinect.convertRealWorldToProjective(rightHandReal, rightHandProjected);
		kinect.convertRealWorldToProjective(rightHandReal, rightHandProjectedUnscalled);
		
		kinect.convertRealWorldToProjective(leftHandReal, leftHandProjected);
		kinect.convertRealWorldToProjective(leftHandReal, leftHandProjected);
		kinect.convertRealWorldToProjective(leftHandReal, leftHandProjectedUnscalled);
		
		//Vergroesserung der Vektoren		
		rightHandProjected.mult(scaleFactor);
		leftHandProjected.mult(scaleFactor);		
		
		p.color(123,123,123);
		p.fill(0,255);	

		// SKalierungsfaktor fuer 3D
		float sfaktorr= (p.map(rightHandProjected.z,300,3000,7,1));			

		// Handboxen zeichnen x y z
		float rectSize=50*sfaktorr;		
		p.rect(leftHandProjected.x-rectSize/2,leftHandProjected.y-rectSize/2,rectSize,rectSize);
		p.rect(rightHandProjected.x-rectSize/2,rightHandProjected.y-rectSize/2,rectSize,rectSize);		
		
		this.rightHand=rightHandProjectedUnscalled;
		this.leftHand=leftHandProjectedUnscalled;
		
	}
	/*
	public void drawSkeleton() {
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
		
		p.stroke(20);
		p.fill(255);
		
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
		p.line(head.x,head.y,neck.x,neck.y);
		p.line(neck.x,neck.y,shoulderLeft.x,shoulderLeft.y);
		p.line(neck.x,neck.y,shoulderRight.x,shoulderRight.y);
		p.line(shoulderLeft.x,shoulderLeft.y,elbowLeft.x,elbowLeft.y);
		p.line(shoulderRight.x,shoulderRight.y,elbowRight.x,elbowRight.y);		
		p.line(elbowLeft.x,elbowLeft.y,handLeft.x,handLeft.y);
		p.line(elbowRight.x,elbowRight.y,handRight.x,handRight.y);
		
		//System.out.println(handLeft);

	}
	*/
}
