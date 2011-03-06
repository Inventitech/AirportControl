package game.airportcontrol.moveables;

import java.awt.Point;
import java.io.IOException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleIO;

public class Helicopter extends AircraftBase {

	/**
	 * @param position
	 * @param angle
	 * @param speed
	 */
	public Helicopter(Point position, int angle, double speed) {
		super(position, angle, speed, 5, 3.4);
		try {
			image = new Image("data/aircraft/heli_test.png");
		} catch (SlickException e) {
			System.out.println("Error orcurred during loading picture.");
			e.printStackTrace();
		}
		try {
			system = ParticleIO.loadConfiguredSystem("data/particles/sys.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		system.setBlendingMode(mode);
		for (int i = 0; i < 6; i++) {
			system.getEmitter(i).setEnabled(false);
		}
	}

}
