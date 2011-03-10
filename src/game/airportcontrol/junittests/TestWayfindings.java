package game.airportcontrol.junittests;

import static org.junit.Assert.assertEquals;
import game.airportcontrol.gamefunctions.Wayfinding;

import java.awt.Point;

import org.junit.Test;

public class TestWayfindings {

	@Test
	public void testCalcAngleInSourceToTarget() {
		// coordinate system: origo in upper left-hand corner
		// 360 degree-system: 0 degrees to right hand side, parallel to bottom of screen
		// test coordinate system alignments
		// test straight line to the right: angle should be zero
		double actualAngle = Wayfinding.calcAngleInSourceToTarget(new Point(1,1), new Point(3,1), 1);
		assertEquals(0, actualAngle,0);
		// line to bottom of screen
		actualAngle = Wayfinding.calcAngleInSourceToTarget(new Point(1,1), new Point(1,2), 1);
		assertEquals(90, actualAngle,0);
		// test line to top of screen
		actualAngle = Wayfinding.calcAngleInSourceToTarget(new Point(1,1), new Point(1,0), 1);
		assertEquals(270, actualAngle,0);
		// test 180 degree curve 
		actualAngle = Wayfinding.calcAngleInSourceToTarget(new Point(1,1), new Point(0,1), 1);
		assertEquals(180, actualAngle,0);
		
		// angle measured on paper with set square 
		actualAngle = Wayfinding.calcAngleInSourceToTarget(new Point(1,1), new Point(7,5), 1);
		assertEquals(34, actualAngle, 0.5);
		// check upper third square (transition of squares)
		actualAngle = Wayfinding.calcAngleInSourceToTarget(new Point(1,1), new Point(0,0), 1);
		assertEquals(225, actualAngle, 0);
	}
	
	@Test public void testAngleBetweenSourceTarget() {
		//double actualAngle = Wayfinding.calcAngleBetweenSourceTarget(new Point(0,0), new Point(1,0), new Point(1,1));
		//assertEquals(90, actualAngle, 0);
		
		double actualAngle = Wayfinding.calcAngleBetweenSourceTarget(new Point(1,1), new Point(2,2), new Point(3,3));
		assertEquals(0, actualAngle, 0);
		
		actualAngle = Wayfinding.calcAngleBetweenSourceTarget(new Point(1,1), new Point(2,1), new Point(3,2));
		assertEquals(45, actualAngle, 0);
	}

}
