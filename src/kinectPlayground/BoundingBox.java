package kinectPlayground;

import processing.core.PVector;

public class BoundingBox {
	public float x;
	public float y;
	public float width;
	public float height;
	public int color=123;	
	public int hover=255;
	
	public BoundingBox(float x, float y, float width, float height) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;		
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
	
}
