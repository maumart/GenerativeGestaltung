package passKinectAround;

import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;

public class Kinect {
	public SimpleOpenNI context;
	public boolean initialized;
	
	public Kinect(PApplet p) {
		this.context = new SimpleOpenNI(p,SimpleOpenNI.RUN_MODE_MULTI_THREADED);	
		this.settings();
		this.initialized=false;
	}
	
	public SimpleOpenNI getKinect(){
		if (context.init()) {
			return context;
		}
		else return null;
	}
	
	public boolean checkAvailability(){
		if (context.init()) {
			this.initialized=true;
		}
		return initialized;		
	}
	
	private void settings(){ 
	  // Quellen aktivieren
      context.enableDepth();  
	  //context.enableIR();
	  context.enableRGB();
	  //context.enableHands();
	  context.enableGesture();
	  
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
	  
	  //return context;
	}

}
