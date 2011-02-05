package game.airportcontrol.moveables;

import java.awt.Point;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Helicopter extends AircraftBase {

	/**
	 * @param position
	 * @param angle
	 * @param speed
	 */
	public Helicopter(Point position, int angle, double speed) {
		super(position, angle, speed);
		try {
			image = new Image("data/aircraft/heli_test.png");
		} catch (SlickException e) {
			System.out.println("Error orcurred during loading picture.");
			e.printStackTrace();
		}

	}

}
