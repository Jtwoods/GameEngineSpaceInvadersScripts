
import objectModel.ObjectMap;
import eventModel.Event;
import eventModel.EventManager;
import eventModel.EventMethod;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import exceptions.NullArgumentException;
import processing.core.PConstants;
import processing.core.PVector;

public class KeyPressed extends EventMethod {

	public KeyPressed() {
		super();
		super.signature = new String[3];
		signature[0] = "MovingLeft";
		signature[1] = "MovingRight";
		signature[2] = "ObjectMap";
	}
	
	@Override
	public String getName() {
		return "KeyPressed";
	}

	@Override
	public EventParameter invoke(EventParameter params) {
		
		int KeyPress = (int) params.additional[0];
		int Key = (char) params.additional[1];
		
		ObjectParameter player = params.objectParameters.get(0);
		
		boolean right = (boolean)player.properties.get(0)[0];
		boolean left = (boolean)player.properties.get(1)[0];
		
		ObjectMap map = (ObjectMap)player.properties.get(2)[0];
		
		if(KeyPress == PConstants.LEFT) {
			
			left = true;
			right = false;
			
			//Pack them up.
			Object[] leftMove = new Object[1];
			leftMove[0] = left;
			Object[] rightMove = new Object[1];
			rightMove[0] = right;
			
			player.properties.set(0, rightMove);
			player.properties.set(1, leftMove);
			params.objectParameters.set(0, player);
			
		}
		else if(KeyPress == PConstants.RIGHT) {
			
			left = false;
			right = true;
			
			//Pack them up.
			Object[] leftMove = new Object[1];
			leftMove[0] = left;
			Object[] rightMove = new Object[1];
			rightMove[0] = right;
			
			player.properties.set(0, rightMove);
			player.properties.set(1, leftMove);
			params.objectParameters.set(0, player);
		}
		if(Key == ' ') {
			float y = 10000f;
		
			try {
				y = (float)map.get("Position").getParameter("PlayerShot", 1);
			} catch (NullArgumentException e) {
				e.printStackTrace();
			}
			
			if(Math.abs(y - 20000f) < 0.01) {
				
				//Get the event manager.
				EventManager eManager = EventManager.getManager();
				
				//Create object parameter.
				ObjectParameter p = new ObjectParameter();
				p.GUID = params.objectParameters.get(0).GUID;
				
				//Create a make shot event parameter.
				EventParameter param = new EventParameter();
				param.objectParameters.add(p);
				
				
				//Add the event to the EventManager's queue.
				eManager.addEvent(new Event("MakeShot", param));
			}
		}
		
		return params;
	}

}