import processing.core.*;

public class BallGame extends PApplet {
  World world;

  @Override
  public void setup() {
      size(600, 600);
      if(frame != null) {
        frame.setResizable(true); 
      }
      noStroke();
      world = new World();
      BackgroundElement background = new BackgroundElement(this, "ballgame.BackgroundElement");
  }

  @Override
  public void draw() {
      background(50);
      world.tick();
  }

  public static void main( String[] args ) {
    PApplet.main( new String[] { "--present", "BallGame" } );
  }
}
