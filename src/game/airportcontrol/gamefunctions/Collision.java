package game.airportcontrol.gamefunctions;

import game.airportcontrol.moveables.AircraftBase;

import java.awt.Point;
import java.util.ArrayList;

public class Collision {

	private static boolean areColliding(AircraftBase o1, AircraftBase o2) {

		Point center1 = o1.getPosition();
		Point center2 = o2.getPosition();

		int rad1 = Math
				.max(o1.getImage().getWidth(), o1.getImage().getHeight()) / 2;
		int rad2 = Math
				.max(o2.getImage().getWidth(), o2.getImage().getHeight()) / 2;

		return (center1.x - center2.x) * (center1.x - center2.x)
				+ (center1.y - center2.y) * (center1.y - center2.y) < (rad1 + rad2)
				* (rad1 + rad2);

	}

	public static boolean hasCollision(ArrayList<AircraftBase> objects) {

		boolean b = false;

		for (int i = 0; i < objects.size(); i++) {
			for (int j = i + 1; j < objects.size(); j++) {
				b = areColliding(objects.get(i), objects.get(j));
				if (b == true) {
					return true;
				}
			}
		}
		return false;
	}

}
