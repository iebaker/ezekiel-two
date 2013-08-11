World world;

void setup() {
    size(600, 600);
    if(frame != null) {
      frame.setResizable(true); 
    }
    noStroke();
    world = new World();
    BackgroundElement background = new BackgroundElement(this, "ballgame.BackgroundElement");
}

void draw() {
  	background(50);
  	world.tick();
}

void keyPressed() {
  	
}
