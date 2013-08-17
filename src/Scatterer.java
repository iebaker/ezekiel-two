import processing.core.PApplet;
import processing.core.PVector;

public class Scatterer extends GameElement implements Actor {
	private float radius = 10;
	private float side_length = (2 * radius)/(float)Math.sqrt(2);
	private float outer_radius = radius + 5;
	private float outer_side_length = (2 * outer_radius)/(float)Math.sqrt(2);

	private static int number = 0;

	public Scatterer(PApplet par, String id, PVector p) {
		super(par, id);
		position = p;

		++number;
setPriority(2);
	}

	public static int getNumber() {
		return number;
	}

	@Override
	public void whileUnfocused() {
		parent.fill(20);
    	parent.quad(position.x, position.y - radius, position.x + radius, position.y, position.x, position.y + radius, position.x - radius, position.y);

    	parent.rectMode(parent.CENTER);
    	parent.rect(position.x, position.y, side_length, side_length);
    	parent.rectMode(parent.CORNER);
	}

	@Override
	public boolean mouseOver() {
		return parent.dist(position.x, position.y, parent.mouseX, parent.mouseY) <= radius + 10;
	}

	@Override
	public boolean dropFocus() {
		return !mouseOver() && !parent.mousePressed;
	}

	@Override 
	public void whileFocused() {
		parent.fill(255,255,255,20);
		parent.quad(position.x, position.y - outer_radius, position.x + outer_radius, position.y, position.x, position.y + outer_radius, position.x - outer_radius, position.y);
		parent.rectMode(parent.CENTER);
		parent.rect(position.x, position.y, outer_side_length, outer_side_length);
		parent.rectMode(parent.CORNER);
	}

	@Override
	public void mousePressed() {
		if(parent.mouseButton == parent.RIGHT) {
			World.deleteElement(this);
			return;
		} else {
			changeSettings();
		}
	}

	@Override
	public void mouseDragged() {
		changeSettings();
	}

	private void changeSettings() {
		position = new PVector(parent.mouseX, parent.mouseY);
	}


  /** Scatter balls if within action radius.
   * Points the ball in a random direction and moves it to the scatterer.
   * @param b the ball to be acted upon
   */
	public void actOn(GameBall b) {
		if(parent.dist(position.x, position.y, b.getPosition().x, b.getPosition().y) <= radius) {
			PVector rand = PVector.random2D();
			rand.setMag( b.getVelocity().mag() );
			b.setPosition( PVector.add( position, rand.setMag( null, radius + 10 ) ) );
			b.setVelocity( rand );
		}
	}
}
