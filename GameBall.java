import processing.core.PVector;
import processing.core.PApplet;

public class GameBall extends GameElement {
	private PVector velocity;
static int number;
	
	public GameBall(PApplet par, String id, PVector p, PVector v) {
		super(par, id);
		position = p;
		velocity = v;
++number;
	}

	public void update(PVector acceleration) {
		velocity.add(acceleration);
	}

	public void whileUnfocused() {
  parent.fill(parent.color(255,255,255,100));
  parent.ellipse(position.x, position.y, 10, 10);
  parent.fill(240);
		parent.ellipse(position.x, position.y, 3, 3);
next();
	}

	public void next() {
		velocity.limit(7);
		position.add(velocity);
	}

public static int getNumber() {
return number;
}

public PVector getVelocity() {
  return velocity; 
}
}
