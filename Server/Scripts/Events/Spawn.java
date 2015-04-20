import java.util.Random;

import objectModel.ObjectMap;
import objectModel.Property;
import synchronization.ObjectSynchronizationManager;
import time.TimeManager;
import eventModel.Event;
import eventModel.EventManager;
import eventModel.EventMethod;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import exceptions.NullArgumentException;

public class Spawn extends EventMethod {

	public Spawn() {
		super();
		signature = new String[2];
		signature[0] = "Position";
		signature[1] = "ObjectMap";
	}

	@Override
	public String getName() {
		return "Spawn";
	}

	@Override
	public EventParameter invoke(EventParameter params) {

		// Unpack.
		ObjectParameter character = params.objectParameters.get(0);
		float x = (float) character.properties.get(0)[0];
		float y = (float) character.properties.get(0)[1];
		ObjectMap map = (ObjectMap) character.properties.get(1)[0];

		// Check the time.
		double time = (double) params.additional[0];

		double timeTo = (double) 10.0;
		if (time <= timeTo) {

			// Get a random spawn position.
			Random rand = new Random(System.currentTimeMillis());
			y = 560f;
			x = (float) (rand.nextInt(940) + 30f);
			boolean ready = false;

			Property positions = null;

			try {
				map.get("Position");
			} catch (NullArgumentException e) {
				e.printStackTrace();
			}

			// Get the ObjectSynchronizationManager.
			ObjectSynchronizationManager oManager = ObjectSynchronizationManager
					.getManager();

			// Now get the rest of the objects
			Iterable<String> guids = oManager.getUnlockedIterator();
			while (!ready) {
				ready = true;
				for (String guid : guids) {
					if (guid.contains("Player") && !guid.contains("Shot")) {
						System.out.println(guid);
						float toCheck_x = (float) positions.getParameters(guid)[0];
						if ((x - toCheck_x) >= 15) {
							x = rand.nextInt(940) + 30f;
							ready = false;
							break;
						}
					}
				}
			}

			// Pack up if needed.
			Object[] posToReturn = new Object[2];
			posToReturn[0] = x;
			posToReturn[1] = y;

			character.properties.set(0, posToReturn);
			params.objectParameters.set(0, character);
		} else {
			time -= timeTo;
			// Create another event.
			// Get the event manager.
			EventManager eManager = EventManager.getManager();

			// Create object parameters for the player.
			ObjectParameter pOne = new ObjectParameter();
			pOne.GUID = character.GUID;

			// Create a Spawn event parameter.
			EventParameter param = new EventParameter();
			param.objectParameters.add(pOne);

			param.additional = new Object[1];
			// Add the time with an additional elapsed time to wait before
			// spawning.
			param.additional[0] = time;

			// Add the event to the EventManager's queue.
			eManager.addEvent(new Event("Spawn", param));
		}

		return params;
	}

}
