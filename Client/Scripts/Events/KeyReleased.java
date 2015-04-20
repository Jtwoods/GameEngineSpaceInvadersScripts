
import eventModel.EventMethod;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import processing.core.PConstants;
import processing.core.PVector;

public class KeyReleased extends EventMethod {

	public KeyReleased() {
		super();
		super.signature = new String[2];
		signature[0] = "MovingRight";
		signature[1] = "MovingLeft";
	}
	@Override
	public String getName() {
		return "KeyReleased";
	}

	@Override
	public EventParameter invoke(EventParameter params) {
		
		int KeyPress = (int) params.additional[0];
		
		ObjectParameter player = params.objectParameters.get(0);
		
		boolean right = (boolean)player.properties.get(0)[0];
		boolean left = (boolean)player.properties.get(1)[0];
		
		if(KeyPress == PConstants.RIGHT || KeyPress == PConstants.LEFT) {
			
			left = false;
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
		
		
		return params;
	}

}