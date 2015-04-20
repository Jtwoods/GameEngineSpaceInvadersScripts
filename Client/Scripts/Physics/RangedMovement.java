
import eventModel.Event;
import eventModel.EventManager;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import actionModel.ActionMethod;
import actionModel.ActionParameter;
import processing.core.PVector;

public class RangedMovement extends ActionMethod {

	public RangedMovement() {
		super();
		signature = new String[4];
		signature[0] = "Position";
		signature[1] = "Range";
		signature[2] = "MovingRight";
		signature[3] = "MovingLeft";
	}
	
	@Override
	public String getName() {
		return "RangedMovement";
	}

	@Override
	public ActionParameter invoke(ActionParameter params) {

		Object[] position = (Object[])params.properties.get(0);
		Object[] range = (Object[])params.properties.get(1);
		boolean right = (boolean)params.properties.get(2)[0];
		boolean left = (boolean)params.properties.get(3)[0];

		if((float)position[0] < (float)range[0]) {
			position[0] = range[0];
			
		} else if ((float)position[0] > (float)range[1]) {
			position[0] = range[1];
		}
		if((float)position[1] < (float)range[2]) {
			position[1] = range[2];
		} else if ((float)position[1] > (float)range[3]) {
			position[1] = range[3];
		} else if (right) {
			float x = (float)position[0];
			x -= 4;
			position[0] = x;
		} else if (left) {
			float x = (float)position[0];
			x += 4;
			position[0] = x;
		}
		
		Object[] toAddOne = new Object[2];
		toAddOne[0] = position[0];
		toAddOne[1] = position[1];
		
		params.properties.set(0, toAddOne);
		
		return params;
	}
	
}
