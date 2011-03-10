package game.airportcontrol.gamefunctions;

import java.awt.Point;

public class Wayfinding {	
	public static double calcAngleInSourceToTarget(Point source, Point target,
			int mapScaling) {
		int dy = (target.y * mapScaling) - source.y;
		int dx = (target.x * mapScaling) - source.x;

		double a;
		if (dx == 0) { // division through 0 undefined
			a = 0;
		} else {
			// calculate new heading through arcus tangens. Note: arctan in
			// ]-pi/2;pi/2[, map to coordinates ]-90;+90[
			a = Math.toDegrees(Math.atan((double) dy / (double) dx));
		}
		// avoid negative angles and map to full range angle [0;360]
		if (dx < 0) {
			a += 180;
		} else if (dy < 0) {
			a += 360;
		}
		
		// override values for processing of limites -->+infinity or -->-infinity
		if (dx == 0) { 
			if (dy > 0) {
				a = 90;
			} else {
				a = 270;
			}
		}

		return a;
	}
	
	private static double scalarEuclid(double[] v1, double[] v2) {
		return v1[0]*v2[0]+v1[1]*v2[1];
	}
	
	private static double lengthVector(double[] v) {
		return Math.sqrt(v[0]*v[0]+v[1]*v[1]);
	}
	
	public static double calcAngleBetweenSourceTarget(Point source1, Point source2, Point target) {
		double[] v1 = new double[2], v2 = new double[2];
		v1[0] = source2.x - source1.x;
		v1[1] = source2.y - source1.y;
		v2[0] = target.x - source2.x;
		v2[1] = target.y - source2.y;
		
		double tmp = scalarEuclid(v1,v2)/(lengthVector(v1)*lengthVector(v2));
		tmp = Math.toDegrees(Math.acos(tmp));
		return tmp;
	}
}
