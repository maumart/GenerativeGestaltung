package hand;

import processing.core.PApplet;
import SimpleOpenNI.SimpleOpenNI;

public class FirstTry extends PApplet {
	SimpleOpenNI context;
		
	public void setup(){
	  context = new SimpleOpenNI(this,SimpleOpenNI.RUN_MODE_MULTI_THREADED);   
	  
	  // Quellen aktivieren
	  context.enableDepth();  
	  //context.enableIR();
	  context.enableRGB();
	  //context.enableHands();
	  //context.enableGesture();
	  
	  // Alle Bones
	  context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
	  
	  // Spiegeln
	  context.setMirror(true);
	  
	  // Gesten
	  context.addGesture("Wave");
	  context.addGesture("Click");
	  context.addGesture("RaiseHand");  
	  
	  //Enable Scene
	  context.enableScene(640,480,60);	  
	  
	  stroke(255);
	  strokeWeight(12);
	  noFill();  
	  smooth();  
	  //size(context.depthWidth(), context.depthHeight());
	  frameRate(60);
	  size(1280, 480);
	}
	
	public void draw(){
		// Kinect updaten --> WICHTIG
		context.update();
		//Depth Image
		image(context.depthImage(),0,0);
		//RGB Image
		image(context.rgbImage(),640,0);
	}
	
	// Calback Neuer User
	public void onNewUser(int userId){
		println("Neuer User");		  
	}

}
