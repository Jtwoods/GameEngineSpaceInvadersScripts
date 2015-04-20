import actionModel.ActionMethod;
import actionModel.ActionParameter;


public class ShotMovement extends ActionMethod {

	public ShotMovement() {
		super();
		signature = new String[3];
		signature[0] = "Position";
		signature[1] = "Range";
		signature[2] = "ShotSpeed";
	}
	
	@Override
	public String getName() {
		return "ShotMovement";
	}

	@Override
	public ActionParameter invoke(ActionParameter params) {

		Object[] position = (Object[])params.properties.get(0);
		Object[] range = (Object[])params.properties.get(1);
		float moveSpeed = (float)params.properties.get(2)[0];

		if((float)position[1] < (float)range[2]) {
			position[1] = 20000f;
		} else if ((float)position[1] > (float)range[3]) {
			position[1] = 20000f;
		} else {
			float y = (float)position[1];
			y += moveSpeed;
			position[1] = y;
		}
		
		Object[] toAddOne = new Object[2];
		toAddOne[0] = position[0];
		toAddOne[1] = position[1];
		
		params.properties.set(0, toAddOne);
		
		return params;
	}
	
}
