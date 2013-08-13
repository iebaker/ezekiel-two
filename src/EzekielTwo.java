import processing.core.*;
import java.awt.Dimension;

public class EzekielTwo extends PApplet {
	World world;
	float pwidth;
	float pheight;


	@Override
	public void setup() {
		size(900,700);
		if(frame != null) {
			frame.setResizable(true);
		}
		noStroke();	
		ElementFactory.setParent(this);
		world = new World();
		BackgroundElement background = new BackgroundElement(this, "ballgame.BackgroundElement");
		pwidth = width;
		pheight = height;
	}

	@Override
	public void draw() {
		background(50);
		world.tick();
	}

	@Override
	public void mousePressed() {
		World.getFocus().mousePressed();
	}

	@Override
	public void mouseDragged() {
		World.getFocus().mouseDragged();
	}

	@Override
	public void mouseReleased() {
		World.getFocus().mouseReleased();
	}

	@Override
	public void keyPressed() {
		World.getFocus().keyPressed();
	}
}
