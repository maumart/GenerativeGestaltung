package kinectPlayground;

import java.util.ArrayList;

import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PImage;
import sun.org.mozilla.javascript.internal.Evaluator;

public class Stage {
	PImage scene;
	PImage depth;
	PImage rgb;
	Kinect k;
	SimpleOpenNI kinect;
	PApplet p;
	float scaleFactor=1.5f;	
	public ArrayList<BoundingBox> bBox= new ArrayList<BoundingBox>();
	
	public Stage(SimpleOpenNI kinect, Kinect k, PApplet p){
		this.kinect=kinect;
		this.k=k;
		this.p=p;
		createBoundingBox();
	}
	
	public void createScene(){		
		scene=kinect.sceneImage(); 
		depth=kinect.depthImage();	
		rgb=kinect.rgbImage();		
		
		// Sceneimages
		p.image(scene,0,0,scene.width*scaleFactor,scene.height*scaleFactor);
		
		//p.image(rgb,960,0,320,240);
		p.image(rgb,960,0,480,360);
		p.image(depth,960,360,480,360);
		//p.image(scene,960,480,320,240);
		
		drawBoundingBox();
	}
	
	public void createBoundingBox(){		
		BoundingBox box1 = new BoundingBox(0,0,150,320);
		BoundingBox box2 = new BoundingBox(160,0,150,320);
		BoundingBox box3 = new BoundingBox(320,0,150,320);
		BoundingBox box4 = new BoundingBox(480,0,150,320);
		
		bBox.add(box1);
		bBox.add(box2);
		bBox.add(box3);
		bBox.add(box4);
		
	}
	
	public void drawBoundingBox(){
		for (BoundingBox box : bBox) {			
			p.noStroke();			
			p.fill(box.color,128,200,128);
			//p.rect(box.x,box.y,box.width,box.height);
			p.rect(box.x*scaleFactor,box.y*scaleFactor,box.width*scaleFactor,box.height*scaleFactor);
			
		}
		
	}
	
	
}
