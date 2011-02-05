package game.airportcontrol.gamesettings;

import game.airportcontrol.moveables.Airplane;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class GameSetup {
	/* constants for basic game setup */
	public static final int resWidth = 800;
	public static final int resHeight = 500;

	public static final boolean fullScreenMode = false;

	public static final boolean showFPS = true;

	public static final boolean doCollisionChecks = true;

	/* some methods used for game */
	public static Airplane genRandomPlane() {
		Random rnd = new Random();
		int x = rnd.nextInt(GameSetup.resWidth * 20);
		int y = rnd.nextInt(GameSetup.resHeight * 20);
		int s = rnd.nextInt(4);

		Point p;

		if (s == 0) {
			p = new Point(x, 0);
		}
		else if (s == 1) {
			p = new Point(0, y);
		}
		else if (s == 2) {
			p = new Point(x, 500 * 20);
		}
		else {
			p = new Point(800 * 20, y);
		}

		ArrayList<Point> pts = new ArrayList<Point>();
		pts.add(new Point(rnd.nextInt(400) + 200, rnd.nextInt(200) + 100));
		Airplane ap = new Airplane(p, rnd.nextInt(360), rnd.nextInt(20) + 10);
		ap.setWayPoints(pts);

		return ap;
	}
}
