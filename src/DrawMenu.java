import processing.core.PApplet;
import processing.core.PVector;
import java.util.HashMap;

public class DrawMenu extends GameElement {
	private PVector mprev;
	private PVector mcurr;
	private float radius = 60;
	private boolean done = false;
	private HashMap<Integer, String> map = new HashMap<Integer, String>();

	public DrawMenu(PApplet par, String i, PVector p) {
		super(par, i);
		position = p;
		setPriority(4);	
		mprev = new PVector(parent.mouseX, parent.mouseY);
		mcurr = new PVector(parent.mouseX, parent.mouseY);
		createMap();
	}

	private void createMap() {
		map.put(0, "F");
		map.put(1, "S");
		map.put(2, "Clr-F");
		map.put(3, "W(b)");
		map.put(4, "W(fb)");
		map.put(5, "W(f)");
		map.put(6, "Clr-B");
		map.put(7, "P");
	}

	public void whileUnfocused() {
		if(done) {
			World.deleteElement(this);
		}
	}

	@Override
	public void whileFocused() {
//		System.out.println("whileFocused : " + done);
		renderMenu();
		mcurr = new PVector(parent.mouseX, parent.mouseY);
		if(parent.dist(position.x, position.y, mprev.x, mprev.y) <= radius) {
			if(parent.dist(position.x, position.y, mcurr.x, mcurr.y) > radius) {
				setDrawType(mcurr);
				done = true;
			}
		}
		mprev = new PVector(mcurr.x, mcurr.y);
	}

	private void setDrawType(PVector loc) {
		boolean right = loc.x >= position.x;
		PVector comparator = new PVector(0,1);
		PVector relative = PVector.sub(loc, position);
		float angle = PVector.angleBetween(relative, comparator);

		if(within(angle, 0, parent.PI/8)) {
			ElementFactory.setType(ElementFactory.ElementType.FORCE_OBJECT);
		} else if(within(angle, parent.PI/8, 3 * parent.PI/8)) {
			if(right) {
				ElementFactory.setType(ElementFactory.ElementType.PORTALS);
			} else {
				ElementFactory.setType(ElementFactory.ElementType.SCATTERER);
			}
		} else if(within(angle, 3 * parent.PI/8, 5 * parent.PI/8)) {
			if(right) {
				World.clearBalls();
			} else {
				World.clearActors();
			}
		} else if(within(angle, 5 * parent.PI/8, 7 * parent.PI/8)) {
			if(right) {
				ElementFactory.setType(ElementFactory.ElementType.FORCE_BARRIER);
			} else {
				ElementFactory.setType(ElementFactory.ElementType.BALL_BARRIER);
			}
		} else {
			ElementFactory.setType(ElementFactory.ElementType.BOTH_BARRIER);
		}
	}

	private boolean within(float A, float B, float C) {
		return A >= B && A <= C;
	}

	private void renderMenu() {
		parent.stroke(parent.color(255,255,255,40));
		parent.noFill();
		parent.strokeWeight(radius);
		parent.ellipse(position.x, position.y, 2 * radius, 2 * radius);
		parent.noStroke();
		parent.fill(0);
		PVector to = new PVector(0, radius);
		for(int i = 0; i < 8; ++i) {
			PVector at = PVector.add(position, to);
			parent.textAlign(parent.CENTER, parent.CENTER);
			parent.text(map.get(i) + "", at.x, at.y);
			to.rotate(parent.QUARTER_PI);
		}
	}
	
	@Override
	public boolean mouseOver() {
		return parent.dist(position.x, position.y, parent.mouseX, parent.mouseY) <= radius + radius/2;
	}

	@Override
	public boolean dropFocus() {
	//	System.out.println( "Dropping menu." );
		return done && !mouseOver() && !parent.mousePressed;
	}
}
