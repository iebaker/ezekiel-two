import processing.core.PVector;
import processing.core.PApplet;

/**
 * GameElement is the abstract superclass which represents any element of the
 * game.  Each game element carries an implementation of run() which, if the mouse
 * is over it, requests from the world to be the focus element every 20 milliseconds.
 * GameElement contains three callback methods: onFocus(), which is called when the
 * element is granted the focus, whileFocused(), which is called every tick while the
 * element is focused, and onUnfocus(), which is called when the element loses focus.
 *
 * @author Izaak Baker
 */
public abstract class GameElement implements Runnable {
	PVector position;
	float myWidth;
	float myHeight;
    PApplet parent;
    boolean discarded;
	boolean editing;

	private int priority;
	private Thread me = new Thread(this);
	private String id;


	/**
	 * Constructor
	 *
	 * @param p 	the parent PApplet, who will be drawn to during the game
	 * @param i 	an Identifying string used to index this element in World's element map
	 */
 	public GameElement(PApplet p, String i) {
		id = i;
		parent = p;
		me.start();

		World.registerGameElement(this);
		World.requestFocus(this);
	}


    public void discard() {
		discarded = true; 
		me.interrupt();
	}
    
	/**
  	 * 
	 */	 
    public boolean isDiscarded() {
		return discarded; 
	}
	

	/**
	 * Called when the element is not in focus
	 */
	public void whileUnfocused() {
		return;
	}	

	public void mousePressed() {
		return;
	}

	public void mouseDragged() {
		return;
	}

	public void mouseReleased() {
		return;
	}

	public void keyPressed() {
		return;
	}

    /** 
     * A callback method which is called when the element gains focus.
     */
    public void onFocus() {
      return; 
    }
    

    /**
     * A method which is called every frame while the element is in focus
     */
    public void whileFocused() {
      return; 
    }
    

    /**
     * A method which is called when the element loses focus
     */
    public void onUnfocus() {
      return; 
    }


    /**
     * Accesssor method for the position of the object
     *
     * @return 		a PVector representing the object's position
     */
	public PVector getPosition() {
 		return position;
	}


	public void setPosition(PVector p) {
		position = new PVector(p.x, p.y);
	}

	/**
	 * Setter method for the priority (rendering/focus order) of the
	 * element
	 */
	public void setPriority(int n) {
		priority = n;
	}


	/**
	 * Getter for the priority (rendering/focus order) of the element
	 *
	 * @return 		an int representing the element's priority
	 */
	public int getPriority() {
		return priority;
	}


	/**
	 * Getter for the ID of the element
	 *
	 * @return 		a String which is the ID of the element
	 */
	public String getID() {
		return id;
	}


	/**
	 * Whether or not the element is willing to relinquish focus to an
	 * element of lesser priority
	 *
	 * @return 		<code>true</code> if the element is willing to relinquish
	 * 				its focus, <code> false </code> otherwise.
	 */
	public boolean dropFocus() {
		return !mouseOver();
	}


	/**
	 * Whether or not the mouse is interacting with the element
	 *
	 * @return 		<code>true</code> if the mouse is over the element, <code>false</code>
	 *				otherwise
	 */
	boolean mouseOver() {
  		if(position != null) {
			return parent.mouseX >= position.x 
				&& parent.mouseX <= position.x + myWidth
				&& parent.mouseY >= position.y 
				&& parent.mouseY <= position.y + myHeight;
		}
		return false;
	}


	/**
	 * A callback method which is called when the user forces the
	 * dropping of all focus.
	 */
	public void onForceDropFocus() {
		return;
	}


	/**
	 * Requests the focus if the element is being interacted with
	 */
	@Override
	public void run() {
		while( !me.interrupted() ) {
			if(mouseOver() && !(World.getFocus() == this)) {
				World.requestFocus(this);
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) { return; }
		}
	}
}
