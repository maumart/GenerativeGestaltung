package kinectPlayground;

import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PImage;

public class Stage {
	PImage scene;
	PImage depth;
	PImage rgb;
	Kinect k;
	SimpleOpenNI kinect;
	PApplet p;
	float scaleFactor=1.5f;	
	
	public Stage(SimpleOpenNI kinect, Kinect k, PApplet p){
		this.kinect=kinect;
		this.k=k;
		this.p=p;		
	}
	
	public void createScene(){		
		scene=kinect.sceneImage(); 
		depth=kinect.depthImage();	
		rgb=kinect.rgbImage();		

		// Notwendig um ueber Array zu iterieren
		//depth.loadPixels();
		
		// Sceneimages
		p.image(scene,0,0,scene.width*scaleFactor,scene.height*scaleFactor);
		//p.image(rgb,960,0,320,240);
		p.image(rgb,960,0,480,360);
		p.image(depth,960,360,480,360);
		//p.image(scene,960,480,320,240);		
		
	}
	
}
