import processing.core.*;

public class EzekielTwo extends PApplet {
	World world;
	float pwidth;
	float pheight;

  // preferred width and height variables
  int _w;
  int _h;

  /** Constructs a new instance of the game.
   * @param w width of the game applet, in pixels
   * @param h height of the game applet, in pixels
   */
  public EzekielTwo( int w, int h ) {
    super();

    _w = w;
    _h = h;
  }

	@Override
	public void setup() {
    size( _w, _h );

		if(frame != null)
			frame.setResizable(true);

		ElementFactory.setParent(this);
		world = new World();
		BackgroundElement background = new BackgroundElement(this, "ballgame.BackgroundElement");

		noStroke();	
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
