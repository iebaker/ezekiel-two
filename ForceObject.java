import processing.core.PVector;
import processing.core.PApplet;

public class ForceObject extends GameElement {
    static int number;
	private float attraction;
	private float rotation;
	private float radius = 5;
    int rendercolor = 0;
    boolean moving = false;

	public ForceObject(PApplet par, String id, PVector p, float a, float r) {
		super(par, id);
		position = p;
		attraction = a;
		rotation = r;
        setPriority(2);
        rendercolor = parent.color(255);

        ++number;
	}

	public void actOn(GameBall ball) {
		float mag = 1/parent.sqrt(PVector.dist(position, ball.getPosition()));
		PVector attr = PVector.sub(position, ball.getPosition());
		attr.normalize();
		PVector spin = new PVector(attr.y, -attr.x);
		attr.mult(attraction * 13 * mag);
		spin.mult(rotation * 4 * mag);
		ball.update(PVector.add(attr, spin));
	}


    public void onFocus() {
        rendercolor = parent.color(0);
        radius = 60; 
    }

	public void whileFocused() {
		parent.fill(235);

        if(parent.mousePressed) {
           changeSettings(parent.mouseX, parent.mouseY); 
        } else {
           moving = false; 
        }

	}

     public void onUnfocus() {
        rendercolor = parent.color(0);
        radius = 5; 
    }
        
    private void drawGraph() {
        boolean focus = World.inFocus(this);
        float aDist = focus ? 120 : 26;
        float rDist = focus ? 70 : 26;

        //Set color for background ellipse (if highlighted);
        parent.fill(parent.color(255,255,255,10));

        //Draw the background (menu) ellipse
        if(focus) parent.ellipse(position.x, position.y, 120, 120);

        //Draw the attraction meter
        if(attraction > 0) {
            parent.fill(parent.color(240,190,90,80));
            parent.arc(position.x, position.y, aDist, aDist, -parent.HALF_PI, (parent.PI * attraction) - parent.HALF_PI); 
        } else {
            parent.fill(parent.color(240,190,90,80));
            parent.arc(position.x, position.y, aDist, aDist, (parent.PI * attraction) - parent.HALF_PI, -parent.HALF_PI);
        }

        //Draw the rotation meter
        if(rotation > 0) {
            parent.fill(parent.color(200,100,200,130));
            parent.arc(position.x, position.y, rDist, rDist, -parent.HALF_PI, (parent.PI * rotation) - parent.HALF_PI);
        } else {
            parent.fill(parent.color(200,100,200,130));
            parent.arc(position.x, position.y, rDist, rDist, (parent.PI * rotation) -parent.HALF_PI, -parent.HALF_PI); 
        }
    }
        
    private void changeSettings(float x, float y) {
        PVector reference = new PVector(0, 1);
        PVector relative = PVector.sub(position, new PVector(x, y));
        float angle = PVector.angleBetween(reference, relative);
        float ratio = angle/parent.PI;

        //If we're moving, change the position
        if(moving) {
            position = new PVector(x, y);
            return; 
        }

        //If we're over the center, set moving
        if(relative.mag() <= 10) {
            moving = true;
            return; 
        }

        //If we're on the right of the center, deal with positive forces
        if(x > position.x) {
            if(relative.mag() >= 35) {
                attraction = ratio;
            } else {
                rotation = ratio;
            } 
        } 

            //Otherwise, deal with negative forces
        else {
            if(relative.mag() >= 35) {
                attraction = -ratio;
            } else {
                rotation = -ratio;
            }
        }
    }

	public void whileUnfocused() {
        parent.fill(parent.color(0,0,0,100));
        parent.ellipse(position.x, position.y, 26, 26); 
        drawGraph();
		parent.fill(rendercolor);
		parent.ellipse(position.x, position.y, 20, 20);
	}

    @Override
	boolean mouseOver() {
		return parent.dist(position.x, position.y, parent.mouseX, parent.mouseY) <= radius;
	}

    @Override
    public boolean dropFocus() {
        return !mouseOver() && !parent.mousePressed; 
    }

    public static int getNumber() {
          return number; 
    }
}
