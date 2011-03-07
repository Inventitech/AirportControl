package game.airportcontrol.gamefunctions;

import java.awt.Point;

public class Wayfinding {
	public static double calcAngleInSourceToTarget(Point source, Point target, int mapScaling) {
		int dy = (target.y * mapScaling) - source.y;
		int dx = (target.x * mapScaling) - source.x;

		double a;
		if (dx == 0) { // division through 0 undefined
			a = 0;
		} else { // calculate new heading through arcus tangens. Note: arctan in
					// [-pi/2;pi/2], map to coordinates [-90;+90]
			a = (Math.atan((double) dy / (double) dx)) / (2 * Math.PI) * 360;
		}
		// avoid negative angles and map to full range angle [0;360]
		if (dx < 0) {
			a += 180;
		} else if (dy < 0) {
			a += 360;
		}
		
		return a;
	}
}
