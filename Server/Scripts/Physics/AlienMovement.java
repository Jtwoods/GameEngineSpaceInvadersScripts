import java.util.Random;

import exceptions.NullArgumentException;
import actionModel.ActionMethod;
import actionModel.ActionParameter;
import objectModel.ObjectMap;
import objectModel.Property;
import synchronization.ObjectSynchronizationManager;

public class AlienMovement extends ActionMethod {

	public AlienMovement() {
		super();
		signature = new String[3];
		signature[0] = "MovingRight";
		signature[1] = "Count";
		signature[2] = "ObjectMap";
	}

	@Override
	public String getName() {
		return "AlienMovement";
	}

	@Override
	public ActionParameter invoke(ActionParameter params) {
		boolean right = (boolean) params.properties.get(0)[0];
		int count = (int) params.properties.get(1)[0];
		ObjectMap map = (ObjectMap) params.properties.get(2)[0];

		// Get the ObjectSynchronizationManager.
		ObjectSynchronizationManager oManager = ObjectSynchronizationManager
				.getManager();

		// Now get the rest of the objects
		Iterable<String> guids = oManager.getUnlockedIterator();

		Random rand = new Random(System.currentTimeMillis());

		boolean changed = false;

		Property positions = null;

		try {
			positions = map.get("Position");
		} catch (NullArgumentException e) {
			e.printStackTrace();
		}

		if (count >= 40) {

			// This will record the max or min value depending on the movement
			// direction.
			float max_min_x = 0;
			if (!right)
				max_min_x = Float.MAX_VALUE;
			float max_y = 0;
			int num_alive = 0;

			// We need to figure out which way the aliens should move.
			for (String guid : guids) {

				if (guid.contains("Alien") && !guid.contains("Shot")
						&& !guid.contains("Movement")) {

					float x = (float) positions.getParameters(guid)[0];
					float y = (float) positions.getParameters(guid)[1];

					// If the alien is alive get the min or max value for x.
					if (y < 20000f) {

						if (right) {
							if (x > max_min_x) {
								max_min_x = x;
							}
						} else {
							if (x < max_min_x) {
								max_min_x = x;
							}
						}
						num_alive++;
					}

				}
			}

			// If there are some aliens alive.
			if (num_alive > 0) {

				// Use the max or min value for x to determine if the aliens
				// should change direction.
				if (right && max_min_x >= 950) {
					right = false;
					changed = true;
				} else if (!right && max_min_x <= 50) {
					right = true;
					changed = true;
				}

				// Now get the iterator again.
				guids = oManager.getUnlockedIterator();

				// Move all the aliens in the appropriate direction.
				for (String guid : guids) {
					if (guid.contains("Alien") && !guid.contains("Shot")
							&& !guid.contains("Movement")) {

						float x = (float) positions.getParameters(guid)[0];
						float y = (float) positions.getParameters(guid)[1];

						if (changed)
							y += 20f;
						else {
							if (right) {
								x += 20f;
							} else {
								x -= 20f;
							}
						}

						positions.addParameter(guid, 0, x);
						positions.addParameter(guid, 1, y);
					}
				}

			}

			count = 0;
		} else if (Math.abs(count - 20f) <= 0.1f) {
			// We will try to make a shot here.
			guids = oManager.getUnlockedIterator();

			float shot_x = 0.0f;
			float shot_y = 0.0f;

			for (String guid : guids) {
				if (guid.contains("Alien") && !guid.contains("Shot")
						&& !guid.contains("Movement")) {
					float x = (float) positions.getParameters(guid)[0];
					float y = (float) positions.getParameters(guid)[1];

					// If the alien is alive and there is a random boolean get the value for x.
					if (y < 20000f && rand.nextInt(11) + 1 == 6) {
							shot_x = x;
							break;
						
					}
				}
			}

			if (shot_x > 0.0f) {
				// Now we find the lowest most alien in the selected column.
				for (String guid : guids) {
					if (guid.contains("Alien") && !guid.contains("Shot")
							&& !guid.contains("Movement")) {
						float x = (float) positions.getParameters(guid)[0];
						float y = (float) positions.getParameters(guid)[1];

						// If the alien is alive get the min or max value for x.
						if (Math.abs(x - shot_x) <= 0.1f) {
							if (y < 20000f && y > shot_y) {
								shot_y = y;
							}
						}
					}
				}

				if (shot_y > 0.0f) {
					// Now we have a position to start our shot.
					// We need to get a shot from the object map.
					guids = oManager.getUnlockedIterator();

					for (String guid : guids) {
						if (guid.contains("AlienShot")) {
							float y = (float) positions.getParameter(guid, 1);
							if (y > 1000f) {
								positions.addParameter(guid, 0, shot_x);
								positions.addParameter(guid, 1, shot_y + 15);
								break;
							}
						}
					}
				}
			}
			count++;
		} else {
			count++;
		}

		Object[] r = new Object[1];
		r[0] = right;
		params.properties.set(0, r);
		Object[] c = new Object[1];
		c[0] = count;
		params.properties.set(1, c);
		Object[] m = new Object[1];
		m[0] = map;
		params.properties.set(2, m);

		return params;
	}

}
