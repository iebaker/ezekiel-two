import processing.core.PVector;
import processing.core.PApplet;

public class Portals extends GameElement {
	private PVector startPosition;
	private PVector endPosition;
	private PlacementState state;
	private MouseEnd mouseEnd;
	private int renderColor;
	private float startRadius = 35;
	private float endRadius = 10;
	private static int number = 0;
	private boolean movingStart = false;
	private boolean movingEnd = false;

	private enum PlacementState {
		PLACING, DONE
	}

	private enum MouseEnd {
		START, END, NONE
	}

	public Portals(PApplet p, String i, PVector pos) {
		super(p, i);

		startPosition = pos;
		state = PlacementState.PLACING;
		int total = 255;
		float red = parent.random(200);
		total -= red;
		float green = parent.random(200);
		total -= green;
		float blue = total;
		renderColor = parent.color(red, green, blue ,150);
		setPriority(1);

		++number;
	}

	public void whileUnfocused() {
		drawOpening();

		if(state == PlacementState.PLACING) {
			if(parent.mousePressed) placeEnd();
		} else {
			drawEnd();
		}

		parent.noStroke();
	}

	public void whileFocused() {
		parent.fill(255,255,255,20);
		parent.ellipse(startPosition.x, startPosition.y, 2 * startRadius + 18, 2 * startRadius + 18);
		if(state == PlacementState.DONE) {
			parent.ellipse(endPosition.x, endPosition.y, 2 * endRadius + 10, 2 * endRadius + 10);
			if(parent.mousePressed) {
				changeSettings();
			} else {
  				movingStart = false;
 				movingEnd = false; 
			}
		}
	}

	public void changeSettings() {
  		if(state == PlacementState.DONE) {
			switch(mouseEnd) {
				case START:
					movingStart = true;
					break;

				case END:
					movingEnd = true;
					break;
				}

			if(movingStart) {
  				startPosition = new PVector(parent.mouseX, parent.mouseY); 
			} else if(movingEnd) {
  				endPosition = new PVector(parent.mouseX, parent.mouseY); 
			}
  		}
	}

	private void drawOpening() {
		parent.stroke(30);
		parent.strokeWeight(3);
		parent.noFill();
		parent.ellipse(startPosition.x, startPosition.y, 2 * startRadius + 5, 2 * startRadius + 5);
		parent.stroke(renderColor);
		parent.ellipse(startPosition.x, startPosition.y, 2 * startRadius - 1, 2 * startRadius - 1);
	}

	private void drawEnd() {
  		if(state == PlacementState.DONE) {
			parent.noStroke();
			parent.fill(renderColor);
			parent.ellipse(endPosition.x, endPosition.y, 2 * endRadius, 2 * endRadius);
  		}
	}

    public void placeEnd() {
        endPosition = new PVector(parent.mouseX, parent.mouseY);
        state = PlacementState.DONE; 
    }

	public static int getNumber() {
		return number;
	}

	public void actOn(GameBall ball) {
		if(state == PlacementState.DONE) {
			PVector newBallPosition = new PVector(ball.getPosition().x, ball.getPosition().y);
			PVector offset;
			if(PVector.sub(ball.getPosition(), startPosition).mag() <= startRadius + 5) 	{
                ball.setPosition(endPosition);
			}                  
		}
	}

	@Override 
	public boolean dropFocus() {
		return !mouseOver() && state == PlacementState.DONE && !parent.mousePressed && !movingStart && !movingEnd;
	}

	@Override
	public boolean mouseOver() {
		float sDist = parent.dist(parent.mouseX, parent.mouseY, startPosition.x, startPosition.y);
		float eDist = (state == PlacementState.DONE) ? parent.dist(parent.mouseX, parent.mouseY, endPosition.x, endPosition.y) : Float.MAX_VALUE;

		if(sDist <= startRadius) {
			mouseEnd = MouseEnd.START;
			return true;
		}

		if(eDist <= endRadius) {
			mouseEnd = MouseEnd.END;
			return true;
		}

		mouseEnd = MouseEnd.NONE;
		return false;
	}

  	public  int dim(int c, float amt, float op) {
    	int newC = parent.color(parent.red(c) * amt, parent.green(c) * amt, parent.blue(c) * amt, op);
    	return newC;
  	}

  	public  int dim(int c, float amt) {
    	return dim(c, amt, 255);
  	}
}
