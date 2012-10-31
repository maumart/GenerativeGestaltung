package midi;
import processing.core.PApplet;
import themidibus.MidiBus;
import controlP5.CheckBox;
import controlP5.ControlP5;
import controlP5.Textlabel;
import controlP5.Toggle;


public class TestSketch extends PApplet {

	public MidiBus myBus;

	ControlP5 cp5;
	Textlabel availOutputsLabel;
	CheckBox availOutputsCheckbox;
	
	PitchBar pitchBar;
	
	int lastMapped = -1;


	public void setup() {
		size(600, 600);
		background(0);

		pitchBar = new PitchBar(this);
		
		MidiBus.list();
		myBus = new MidiBus(this, -1, -1);
		
		cp5 = new ControlP5(this);
		cp5.addButton("b1").setPosition(100, 120).setSize(200, 19);

		availOutputsLabel = cp5.addTextlabel("label")
				.setText("Available Outputs:")
				.setPosition(20, 20)
				.setFont(createFont("Verdana", 14));

		availOutputsCheckbox = cp5.addCheckBox("availOutCheckBoxHandler").setPosition(20, 40)
				.setColorForeground(color(120)).setColorActive(color(255))
				.setColorLabel(color(255)).setSize(10, 10);
		
		String[] availOutputs = MidiBus.availableOutputs();
		String[] attachedOutpus = myBus.attachedOutputs();
		for (int i = 0; i < availOutputs.length; i++) {
			String output = availOutputs[i];
			availOutputsCheckbox.addItem(output, i);
			for(int j = 0; j < attachedOutpus.length; j++){
				if(attachedOutpus[j] == output){
					availOutputsCheckbox.activate(output);
				}
			}
		}
		
		printAttachedOutputs();


	}

	public void draw() {
		int channel = 0;
		int velocity = 127;
		int mapped = pitchBar.display();
		if(mapped != -1){
			
			if(lastMapped != -1){
				if(lastMapped <= mapped){
					mapped = mapped - lastMapped;
				} else {
					mapped = lastMapped - mapped;
				}
				if(mapped <= 20){
					return;
				}
				myBus.sendNoteOff(channel, lastMapped, velocity);
			}
			
			int pitch = mapped;
			
			
			myBus.sendNoteOn(channel, pitch, velocity);
		}
	}

	public void availOutCheckBoxHandler(float[] a) {
	
		for(int i = 0; i < a.length; i++){
			Toggle item = availOutputsCheckbox.getItem(i);
			if(a[i] == 1.0f){
				if(!isAttachedOutput(item.getName())){
					if(!myBus.addOutput(i)){
						println("Adding Device FAILD!");
						availOutputsCheckbox.deactivate(i);
					} else {
						println("Device Successfully Added!");
					}
				}
			} else {
				if(isAttachedOutput(item.getName())){
					if(myBus.removeOutput(i)){
						println("Device Successfully Removed!");
					} else {
						println("Removing Device FAILD!");
						availOutputsCheckbox.activate(i);
					}
					
				}
			}
		}
	}
	
	private Boolean isAttachedOutput (String name){
	//	println("name"+name);
		String[] attachedOutputs = myBus.attachedOutputs();
		for(int i = 0; i < attachedOutputs.length; i++){
		//	println("name2"+attachedOutputs[i]);
			if(name == attachedOutputs[i]){
				println("isAttached " + name);
				return true;
			}
		}
		return false;
	}
	
	
	

	private void printAvailableInputs() {
		println("Available Inputs:");
		printIOString(myBus.availableInputs());
	}
	
	private void printAvailableOuputs() {
		println("Available Outputs:");
		printIOString(myBus.availableOutputs());
	}

	private void printAttachedOutputs() {
		println("Atached Outputs");
		printIOString(myBus.attachedOutputs());
	}

	private void printIOString(String[] iostrings) {
		for (int i = 0; i < iostrings.length; i++) {
			println("[" + i + "]" + iostrings[i]);
		}

	}
	
	void dump(String s){
		println(s);
	}

	public void b1(int theValue) {
		println("a button event from b1: " + theValue);
		printAvailableOuputs();
		printAttachedOutputs();
		
		int channel = 0;
		int pitch = 69;
		int velocity = 127;

		myBus.sendNoteOn(channel, pitch, velocity); // Send a Midi noteOn
		delay(200);
		myBus.sendNoteOff(channel, pitch, velocity); // Send a Midi nodeOff
		delay(100);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "TestSketch" });
	}

}