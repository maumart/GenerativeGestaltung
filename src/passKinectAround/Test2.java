package passKinectAround;

import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PImage;

public class Test2 {
	PImage scene;
	PImage depth;
	PImage rgb;
	Kinect k;
	SimpleOpenNI kinect;
	PApplet p;
	
	
	public Test2(SimpleOpenNI kinect, Kinect k, PApplet p){
		this.kinect=kinect;
		this.k=k;
		this.p=p;
		
	}
	public void test(){
		
		scene=kinect.sceneImage(); 
		depth=kinect.depthImage();	
		rgb=kinect.rgbImage();

		// Notwendig um ueber Array zu iterieren
		//depth.loadPixels();
		
		// Sceneimages
		p.image(scene,0,0,scene.width,scene.height);
		p.image(rgb,960,0,320,240);
		p.image(depth,960,240,320,240);
		p.image(scene,960,480,320,240);	
		
		//p.image(image,0,0);
		
		//System.out.println();
		
		
	}
	
}
