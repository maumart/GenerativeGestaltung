package processing;

import processing.core.PApplet;

public class ProcessingColor extends PApplet {	
	
	public void setup() 
	{
		this.size(640, 360);
		this.noStroke();
		this.frameRate(60);
	}
	
	public void draw() 
	{
		
		colorMode(RGB, 100);
		this.background(mouseX);
		color(0);
		System.out.println(mouseX);
	  
	}
}

