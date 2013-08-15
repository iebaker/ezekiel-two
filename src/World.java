import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.lang.Class;
import processing.core.PApplet;

public class World {
	private static HashMap<String, GameElement> elements = new HashMap<String, GameElement>();
	private static HashMap<Class<?>, List<GameElement>> elementsByClass = new HashMap<Class<?>, List<GameElement>>();
	private static GameElement focus;
	private static List<GameElement> deleted = new ArrayList<GameElement>();    
  private static List<Class<?>> classOrder = new ArrayList<Class<?>>();  

	static {
		classOrder.add(BackgroundElement.class);
		classOrder.add(GameBall.class);
		classOrder.add(Scatterer.class);
		classOrder.add(Portals.class);
		classOrder.add(ForceObject.class);
		classOrder.add(Walls.class);
		classOrder.add(Walls.WallNode.class);
		classOrder.add(DrawMenu.class);
	}

	public static void registerGameElement(GameElement ge) {
		elements.put(ge.getID(), ge);
		Class<?> element_class = ge.getClass();
		if(!elementsByClass.containsKey(element_class)) {
			List<GameElement> temp = new ArrayList<GameElement>();
			temp.add(ge);
			elementsByClass.put(element_class, temp);
		} else {
			elementsByClass.get(element_class).add(ge);
		}
	}

	public static GameElement getGameElement(String id) {
		return elements.get(id);
	}

	public static List<GameElement> getElementsByClass(Class<?> c) {
	    if(elementsByClass.containsKey(c)) {
			return elementsByClass.get(c);
		} else {
			return new ArrayList<GameElement>();
		}
	}

	public static void clearBalls() {
		ArrayList<GameElement> tbd = new ArrayList<GameElement>();	
		for(GameElement ge : getElementsByClass(GameBall.class)) {
			tbd.add(ge);
		}
		markForDeletion(tbd);
	}

	public static void clearActors() {
		ArrayList<GameElement> allActors = new ArrayList<GameElement>();
		allActors.addAll(getElementsByClass(ForceObject.class));
		allActors.addAll(getElementsByClass(Portals.class));
		allActors.addAll(getElementsByClass(Walls.class));
		allActors.addAll(getElementsByClass(Walls.WallNode.class));
		allActors.addAll(getElementsByClass(Scatterer.class));
		markForDeletion(allActors);
	}

	public static void markForDeletion(ArrayList<GameElement> elements) {
		synchronized(deleted) {
			deleted.addAll(elements);
		}
	}

	public static boolean requestFocus(GameElement ge) {
		//If this is the first object to request focus, give it focus
		if(focus == null) {
			focus = ge;
			focus.onFocus();
			return true;
		}       
	
		//Otherwise, if the requesting object has a higher priority, award it focus
		if(ge.getPriority() > focus.getPriority()) {
			focus.onUnfocus();
			focus = ge;
      focus.onFocus();
			return true;
		} 

		//If they have the same priority, or if the requesting object has a lower priority, 
		//then the current focus' dropFocus() method is called to determine whether it is willing
		//to relinquish the focus.
		else {
			if(focus.dropFocus()) {
				focus.onUnfocus();
				focus = ge;
				focus.onFocus();
				return true;
			} else {
				return false;
			}
		}
	}

	public static void deleteElement(GameElement ge) {
		synchronized(deleted) {
			deleted.add(ge);
		}
	}
	
	public static void destroyElement(GameElement ge) {
		elementsByClass.get(ge.getClass()).remove(ge);
		elements.remove(ge.getID());
		ge.discard();
	}

	public static void tick() {
		for(GameElement elem : deleted) {
			destroyElement(elem);
		}
		for(Class<?> c : classOrder) {
			for(GameElement elem : getElementsByClass(c)) {
				if(!elem.isDiscarded()) {
					if(focus != null && focus == elem) {
						elem.whileFocused();
					}
					elem.whileUnfocused();
					handleElement(elem);
				}
			}
		}
	}

	public static void handleElement(GameElement elem) {
		if(elem instanceof ForceObject) {
			ForceObject fo = (ForceObject) elem;
			for(GameElement ge : getElementsByClass(GameBall.class)) {
  				GameBall gb = (GameBall) ge;
				if(gb != null) {
					fo.actOn(gb);
				}
			}
		} else if(elem instanceof Portals) {
 			Portals p = (Portals) elem;
				for(GameElement ge : getElementsByClass(GameBall.class)) {
  				GameBall gb = (GameBall) ge;
  				if(gb != null) {
    				p.actOn(gb); 
				}
			} 
		} else if(elem instanceof Scatterer) {
			Scatterer s = (Scatterer) elem;
			for(GameElement ge : getElementsByClass(GameBall.class)) {
				GameBall gb = (GameBall) ge;
				if(gb != null) {
					s.actOn(gb);
				}
			}
		} else if(elem instanceof Walls) {
			Walls w = (Walls) elem;	
			for(GameElement ge : getElementsByClass(GameBall.class)) {
				GameBall gb = (GameBall) ge;
				if(gb != null) {
					w.actOn(gb);
				}
			}
		}
	}

	public static GameElement getFocus() {
 		return focus; 
	}

	public static boolean inFocus(GameElement ge) {
		return focus == ge;
	}
}
