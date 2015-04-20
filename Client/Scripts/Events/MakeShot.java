import eventModel.EventMethod;
import eventModel.EventParameter;
import exceptions.NullArgumentException;
import objectModel.ObjectMap;


public class MakeShot extends EventMethod {

	public MakeShot() {
		super();
		signature = new String[2];
		signature[0] = "ObjectMap";
		signature[1] = "Position";
	}
	
	@Override
	public String getName() {
		return "MakeShot";
	}

	@Override
	public EventParameter invoke(EventParameter params) {

		ObjectMap map = (ObjectMap)params.objectParameters.get(0).properties.get(0)[0];
		float x = (float)params.objectParameters.get(0).properties.get(1)[0];
		float y = (float)params.objectParameters.get(0).properties.get(1)[1];
		
		try {
			map.get("Position").addParameter("PlayerShot", 0, x);
			map.get("Position").addParameter("PlayerShot", 1, y - 15f);
		} catch (NullArgumentException e) {
			e.printStackTrace();
		}
		
		return params;
	}
	
}
