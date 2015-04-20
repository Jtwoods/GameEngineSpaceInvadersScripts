import java.awt.Rectangle;

import objectModel.ObjectMap;
import objectModel.Property;
import eventModel.Event;
import eventModel.EventManager;
import eventModel.EventMethod;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import exceptions.NullArgumentException;
import processing.core.PVector;

public class Collision extends EventMethod {

	private static final float BUFFER = 25;

	public Collision() {
		super();
		super.signature = new String[1];
		signature[0] = "Position";

		// Time is being passed in the additional block at index 0.
	}

	@Override
	public String getName() {
		return "Collision";
	}

	@Override
	public EventParameter invoke(EventParameter params) {

		// Unpack object one's parameters.
		ObjectParameter one = params.objectParameters.get(0);
		ObjectParameter two = params.objectParameters.get(1);
		
		float xOne = (float) one.properties.get(0)[0];
		float yOne = (float) one.properties.get(0)[1];

		float xTwo = (float) two.properties.get(0)[0];
		float yTwo = (float) two.properties.get(0)[1];

		if (!((one.GUID.toUpperCase().contains("PLAYER") && two.GUID.toUpperCase().contains("PLAYER")) || (one.GUID.toUpperCase().contains("MOVING") && two.GUID.toUpperCase().contains("PLAYER")) || (one.GUID.toUpperCase().contains("PLAYER") && two.GUID.toUpperCase().contains("MOVING")))) {

			// Set the y position to 20000.
			yOne = 20000f;
			yTwo = 20000f;

			// Pack up the parameters.
			Object[] onePosReturn = new Object[2];
			onePosReturn[0] = xOne;
			onePosReturn[1] = yOne;
			one.properties.set(0, onePosReturn);

			Object[] twoPosReturn = new Object[2];
			twoPosReturn[0] = xTwo;
			twoPosReturn[1] = yTwo;

			two.properties.set(0, twoPosReturn);

			params.objectParameters.set(0, one);
			params.objectParameters.set(1, two);
		}
		
		return params;
	}
}
