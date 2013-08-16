import processing.core.PVector;
import processing.core.PApplet;
import java.util.List;
import java.util.ArrayList;

/**
 * Walls are barriers which can block balls, forces, or both.  It is
 * a represented as a graph composed of WallNode and WallEdge objects.
 * A WallEdge is a tuple of WallNodes.  An edge has one of three types, 
 * as above.  Deleting node removes its associated edges.  Deleting an
 * edge does not affect its endpoint nodes.
 */
public class Walls extends GameElement {

	private List<WallNode> nodes = new ArrayList<WallNode>();
	private List<WallEdge> ball_deflecting = new ArrayList<WallEdge>();
	private List<WallEdge> force_deflecting = new ArrayList<WallEdge>();
	private List<WallEdge> both_deflecting = new ArrayList<WallEdge>();
	private List<WallEdge> edges = new ArrayList<WallEdge>();	
	private static int node_color;
	private static float node_radius;
	private static int number;
	private static int nodeNumber;

	public Walls(PApplet par, String id, PVector ip) {
		super(par, id);
		position = ip;
		node_radius = 5f;
		node_color = parent.color(30);
		++number;
	}

	/**
     * WallNode is a class which represents a node in the 
     * graph of walls.
     */
	public class WallNode extends GameElement {
		
		private Walls myWalls;			//The walls object associated with this
		private float effective_radius = Walls.getNodeRadius() + 5;
		private boolean moving = false;

		public WallNode(PApplet par, String id, Walls w, PVector p) {
			super(par, id);
			position = p;
			myWalls = w;
			setPriority(2);
		}

		public WallNode(PApplet par, String id, Walls w, PVector p, boolean m) {
			this(par, id, w, p);
			moving = m;
		}

/* 
 * FOCUS LOGIC
 */	

		@Override
		public void whileUnfocused() {
			render();
		}

		@Override
		public void onFocus() {
			effective_radius = 4 * Walls.getNodeRadius();
		}

		@Override
		public void whileFocused() {
			highlight();
			drawTempNode();
			render();
		}

		public void onUnfocus() {
			effective_radius = Walls.getNodeRadius() + 5;
		}

/*
 * Interaction methods
 */

		@Override
		public void mousePressed() {
			if(parent.mouseButton == parent.RIGHT) {
				myWalls.removeNode(this);
				return;
			} else if(parent.mouseButton == parent.LEFT) {
				changeSettings();
			}
		}

		@Override
		public void mouseDragged() {
			changeSettings();
		}

		@Override
		public void mouseReleased() {
			moving = false;
		}

		public boolean dropFocus() {
			return !mouseOver() && !moving;
		}

		public boolean mouseOver() {
			return parent.dist(position.x, position.y, parent.mouseX, parent.mouseY) <= effective_radius;
		}

/*
 * Drawing methods!
 */

		private void render() {
			parent.fill(Walls.getNodeColor());
			parent.ellipse(position.x, position.y, 2 * Walls.getNodeRadius(), 2 * Walls.getNodeRadius()); 
		}

		private void highlight() {
			float nr = Walls.getNodeRadius();
			parent.fill(parent.color(255,255,255,10));
			parent.ellipse(position.x, position.y, 2 * effective_radius, 2 * effective_radius);
		}

		private void drawTempNode() {
			PVector toMouse = PVector.sub(new PVector(parent.mouseX, parent.mouseY), position);
			if(toMouse.mag() >= Walls.getNodeRadius()) {
				parent.fill(150);
				parent.stroke(parent.color(100));
				parent.strokeWeight(7);
				parent.line(position.x, position.y, parent.mouseX, parent.mouseY);
				parent.noStroke();
				parent.ellipse(parent.mouseX, parent.mouseY, 2 * Walls.getNodeRadius(), 2 * Walls.getNodeRadius());
			}
		} 
/*
 * Movement methods
 */

		private void changeSettings() {
			if(moving || parent.dist(position.x, position.y, parent.mouseX, parent.mouseY) < Walls.getNodeRadius()) {
				if(!moving) moving = true;
				position = new PVector(parent.mouseX, parent.mouseY);
			} else {
				myWalls.addNode(this, new PVector(parent.mouseX, parent.mouseY), true);
				effective_radius = Walls.getNodeRadius();		
			}
		}
	} 
	//End of inner class WallNode


	/**
   * WallEdge is a class which represents a pair of nodes
   * connected by an edge.
   */
	public class WallEdge {
		public WallNode node1;
		public WallNode node2;
		public WallEdge(WallNode n1, WallNode n2) {
			if(n1.getPosition().y > n2.getPosition().y) {
				node1 = n2;
				node2 = n1;
			} else {
				node2 = n2;
				node1 = n1;
			}
		}
	} 
	//End of inner class WallEdge

	public void addNode(PVector loc) {
			nodes.add(new WallNode(parent, getID() + ".wallNode#" + nodeNumber, this, loc));
	}

	public void addNode(WallNode w, PVector loc, boolean m) {
		WallNode temp_node = new WallNode(parent, getID() + ".wallNode#" + nodeNumber, this, loc, m);
		nodes.add(temp_node);
		WallEdge temp_edge = new WallEdge(w, temp_node);
		ElementFactory.ElementType	 t = ElementFactory.getType();
		switch(t) {
			case BALL_BARRIER:
				ball_deflecting.add(temp_edge);
				break;

			case FORCE_BARRIER:
				force_deflecting.add(temp_edge);
				break;

			case BOTH_BARRIER:
				both_deflecting.add(temp_edge);
				break;

			default:
				both_deflecting.add(temp_edge);
				break;
		}
		edges.add(temp_edge);
		++nodeNumber;
	}

	public static int getNodeNumber() {
		return nodeNumber;
	}

	public static int getNumber() {
		return number;
	}

	public List<WallEdge> getForceWalls() {
		List<WallEdge> temp = new ArrayList<WallEdge>();
		temp.addAll(force_deflecting);
		temp.addAll(both_deflecting);
		return temp;
	}

	public List<WallEdge> getBallWalls() {
		List<WallEdge> temp = new ArrayList<WallEdge>();
		temp.addAll(ball_deflecting);	
		temp.addAll(both_deflecting);
		return temp;
	}
	
	public static int getNodeColor() {
		return node_color;
	}

	public static float getNodeRadius() {
		return node_radius;
	}

	@Override	
	public void whileUnfocused() {
		renderEdges();
	}

	public void renderEdges() {
//		parent.fill(40);
//		for(WallNode node : nodes) {
//			parent.ellipse(node.getPosition().x, node.getPosition().y, 2 * Walls.getNodeRadius() + 7, 2 * Walls.getNodeRadius() + 7);
//		}
		parent.strokeWeight(7);
		for(WallEdge we : ball_deflecting) {
			parent.stroke(parent.color(200,80,80,100));
			parent.line(we.node1.getPosition().x, we.node1.getPosition().y, we.node2.getPosition().x, we.node2.getPosition().y);
		}
		for(WallEdge we : force_deflecting) {
			parent.stroke(parent.color(80,80,200,100));
			parent.line(we.node1.getPosition().x, we.node1.getPosition().y, we.node2.getPosition().x, we.node2.getPosition().y);
		}
		for(WallEdge we : both_deflecting) {
			parent.stroke(parent.color(140,80,140,100));
			parent.line(we.node1.getPosition().x, we.node1.getPosition().y, we.node2.getPosition().x, we.node2.getPosition().y);
		}
		parent.noStroke();
		parent.strokeWeight(1);
	}

	public void removeNode(WallNode node) {
		ball_deflecting.remove(node);
		force_deflecting.remove(node);
		both_deflecting.remove(node);
		edges.remove(node);
	}

	public void actOn(GameBall ball) {
		List<WallEdge> relevant = getBallWalls();
			for(WallEdge edge : relevant) {
			PVector intersection = new PVector(0,0);
			if(intersect(ball.getPosition(), PVector.add(ball.getPosition(), ball.getVelocity()), edge.node1.getPosition(), edge.node2.getPosition(), intersection)) {
				
				//Compute where the ball WOULD have been the next frame
				PVector ball_next = PVector.add(ball.getPosition(), ball.getVelocity());

				//Rotate the edge vector 90 degrees clockwise to get the comparison vector
				PVector edgeVector = PVector.sub(edge.node2.getPosition(), edge.node1.getPosition());
				float angle = PVector.angleBetween(edgeVector, ball.getVelocity());
				edgeVector = new PVector(-edgeVector.y, edgeVector.x);

				//Find the angle between that vector and the edge vector, and set the rotation direction appropriately
				float checkangle = PVector.angleBetween(edgeVector, ball.getVelocity()); 
				int dir = parent.degrees(checkangle) <= 90 ? -1 : 1;

				//Spin the ball's velocity by the right amount in the right direction and then place us on the intersect
				ball.getVelocity().rotate(dir * 2 * angle);
//				System.out.println(intersection.x + ", " + intersection.y);
				ball.setPosition(intersection);

				//Find how far off the intersection the ball would have gotten by next time tick, and advance that far
				PVector jump = ball.getVelocity().get();
				jump.setMag(parent.dist(ball_next.x, ball_next.y, intersection.x, intersection.y));
				ball.getPosition().add(jump);
			}
		}
	}

	public static boolean permit(ForceObject force, GameBall ball) {
		for(GameElement ge : World.getElementsByClass(Walls.class)) {
			Walls w = (Walls) ge;
			List<WallEdge> relevant = w.getForceWalls();
			for(WallEdge edge : relevant) {
				if(intersect(force.getPosition(), ball.getPosition(), edge.node1.getPosition(), edge.node2.getPosition(), null)) {
					return false;
				}	
			}
		}
		return true;
	}

	private static boolean intersect(PVector A1, PVector A2, PVector B1, PVector B2, PVector intersection) {
		float mA = (A2.y - A1.y) / (A2.x - A1.x);
		float mB = (B2.y - B1.y) / (B2.x - B1.x);

		float intX = ((mA * A1.x) - A1.y - (mB * B1.x) + B1.y) / (mA - mB);
		float intY = (mA * (intX - A1.x)) + A1.y;

		if(within(intX, A1.x, A2.x) &&
			within(intX, B1.x, B2.x) &&
			within(intY, A1.y, A2.y) &&
			within(intY, B1.y, B2.y)) {
			if(intersection != null) {
				intersection.x = intX;
				intersection.y = intY;
			}
			return true;
		}
		return false;
	
	}

	private static boolean within(float a, float E1, float E2) {
    return a >= E1 && a <= E2 || a >= E2 && a <= E1;
	}

	//Walls elements themselves never ask for focus from the world, 
  //only their constituent nodes do.
	@Override
	public void run() {
		return;
	}

	@Override
	public boolean mouseOver() {
		return false;
	}
}
