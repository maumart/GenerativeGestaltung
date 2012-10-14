package hand;

import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;

public class Kinect {
	private SimpleOpenNI context;
	public String test;
	
	public Kinect(PApplet p) {
		this.context = new SimpleOpenNI(p,SimpleOpenNI.RUN_MODE_MULTI_THREADED);	
		this.settings();
	}
	
	public SimpleOpenNI getKinect(){		
		return context;
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
