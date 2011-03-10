package game.airportcontrol.gamefunctions;

import java.awt.Point;

public class Wayfinding {
	// converts from rad (in pi) to full degree (0-360)
	public static double radToDegree(double rad) {
		return rad*360/(2*Math.PI);
	}
	
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
			a = radToDegree((Math.atan((double) dy / (double) dx)));
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
	
	public static double calcAngleBetweenSourceTarget(Point source1, Point source2, Point target) {
		// TODO (bellemo) needs more tweaking for delta = 0
		double m1, m2;
		m1 = (source2.y - source1.y)/(source2.x - source1.x);
		m2 = (target.y - source2.y)/(target.x - source2.x);
		
		double phi;
		if(m1*m2==-1) {
			phi = 90;
		}
		else {
			phi = radToDegree(Math.atan(Math.abs((m1-m2)/(1+m1*m2))));
		}
		
		return phi;
	}
}
