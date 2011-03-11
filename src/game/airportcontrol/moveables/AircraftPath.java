package game.airportcontrol.moveables;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/* 
 * Class is representing an aircraft's planned flight path.
 * It functions in parts as a wrapper class for ArrayList
 * but has additional ability to draw itself. 
 * 
 * TODO (MMB) add ability to directly fly to first waypoint, once that has been 
 * assigned
 */
public class AircraftPath {
	ArrayList<Point> wayPoints; // TODO (MMB) change visibility to private

	public AircraftPath() {
		wayPoints = new ArrayList<Point>();
	}

	/* Renders the WayPoints of the Aircraft, if there are any */
	public void renderWayPoints(Graphics g) {
		Color prevColor = g.getColor();
		g.setColor(new Color(255, 210, 0, 120));
		g.setLineWidth(5);
		if (wayPoints != null) {
			for (int i = 1; i < wayPoints.size(); i++) {
				g.drawLine(wayPoints.get(i - 1).x, wayPoints.get(i - 1).y,
						wayPoints.get(i).x, wayPoints.get(i).y);
			}
		}
		g.setLineWidth(1);
		g.setColor(prevColor);
	}

	public Point get(int index) {
		return wayPoints.get(index);
	}

	public Point remove(int index) {
		return wayPoints.remove(index);
	}

	public boolean add(Point o) {
		return wayPoints.add(o);
	}

	public boolean isEmpty() {
		return wayPoints.isEmpty();
	}

	public int size() {
		return wayPoints.size();
	}
}