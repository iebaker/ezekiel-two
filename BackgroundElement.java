import processing.core.PApplet;
import processing.core.PVector;

class BackgroundElement extends GameElement {
	private int bgcolor;

	public BackgroundElement(PApplet par, String id) {
		super(par, id);
        position = new PVector(0,0);
        myWidth = parent.width;
        myHeight = parent.height;
        setPriority(0);
	}

	@Override
	public void whileUnfocused() {
        myWidth = parent.width;
        myHeight = parent.height;
		return;
	}

	@Override
	public void whileFocused() {
		if(parent.keyPressed) {
			if(parent.key == 'f') {
				ForceObject temp = new ForceObject(parent, "ballgame.ForceObject#" + ForceObject.getNumber(), new PVector(parent.mouseX, parent.mouseY), 0, 0);
			} else if(parent.key == 'p') {
				Portals temp = new Portals(parent, "ballgame.portals#" + Portals.getNumber(), new PVector(parent.mouseX, parent.mouseY));
			} else if(parent.key == 'b') {
  				GameBall gb = new GameBall(parent, "ballgame.GameBall#" + GameBall.getNumber(), new PVector(parent.mouseX,parent.mouseY), new PVector(0,0)); 
			}
		}
	}

}
