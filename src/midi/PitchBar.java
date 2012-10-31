package midi;
import java.awt.geom.Point2D;

import processing.core.PApplet;

public class PitchBar {

	PApplet parent;

	int rectX, rectY;
	int rectColor;
	int rectHeight;
	int rectWidth;

	boolean hover;

	public PitchBar(PApplet p) {
		parent = p;
		
		rectHeight = 200;
		
		rectWidth = 40;
		rectColor = parent.color(0, 255, 0);
		hover = false;
		rectX = 50;
		rectY = 150;

	}

	public int display() {
		hover = overRect(rectX, rectY, rectWidth, rectHeight);
		int mapped = -1;
		if(hover){
			int offset = rectY + rectHeight;
			parent.background(rectColor);
			int pos = parent.mouseY-rectY;
			
			mapped = (int) parent.map(pos, 0, rectHeight, 127, 0);
			
			parent.println("mapped val"+ mapped);
			
			
			
		} else {
			parent.background(0);
		}
		parent.stroke(255);
		parent.fill(rectColor);
		parent.rect(rectX, rectY, rectWidth, rectHeight);
		
		return mapped;
	}
	

	boolean overRect(int x, int y, int width, int height) {
		if (parent.mouseX >= x && parent.mouseX <= x + width
				&& parent.mouseY >= y && parent.mouseY <= y + height) {
			return true;
		} else {
			return false;
		}
	}

}
