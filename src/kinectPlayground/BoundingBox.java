package kinectPlayground;

import processing.core.PVector;
import processing.core.PApplet; 
import processing.core.*;

public class BoundingBox {
	public float x;
	public float y;
	public float width;
	public float height;
	public int normColor=123;
	public int color=123;
	public int hoverColor=255;
	public int pitch;
	
	public BoundingBox(float x, float y, float width, float height, int pitch) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;	
		//this.pitch= (int) Math.round(Math.random()*100);
		this.pitch= pitch;
		
	}
	
	public void hoverColor(){
		
	}
	
	public String toString(){		
		String s = Float.toString(x)+","+Float.toString(y)+","+Float.toString(width)+","+Float.toString(height);		
		return s;
	}
	
	public boolean checkPoint(PVector pv){
		if (pv.x > x &&	pv.y > y &&
				pv.x < x + width &&	pv.y < y + height){							
					return true; 
		}		
		return false;		
	}
	
	public int checkYPos(PVector pv){
		float value=PApplet.map(pv.y, 0,960,0, height);		
		return Math.round(value);		
	}
	
}
