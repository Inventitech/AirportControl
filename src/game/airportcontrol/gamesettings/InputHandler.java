package game.airportcontrol.gamesettings;

import game.airportcontrol.gamefunctions.Wayfinding;
import game.airportcontrol.moveables.AircraftBase;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Input;

public class InputHandler {
	private static AircraftBase curSelection;
	public static ArrayList<Point> path = new ArrayList<Point>();
	
	public static void inputHandler(Input input) {
		if (input.isMouseButtonDown(0)) {
			recordTrack(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
		} else {
			saveTrack();
		}
	}

	public static void saveTrack() {
		if (path.size() > 0) {
			if (curSelection != null)
				curSelection.setWayPoints(path);
			path = new ArrayList<Point>();
		}
	}

	public static void recordTrack(int x, int y) {
		if (path.isEmpty()) {
			for (AircraftBase curAircraft : Game.airplanes) {
				if (curAircraft.getPosition().distance(new Point(x, y)) < curAircraft.getDiameter()) {
					curSelection = curAircraft;
				}
			}
		}
		if (path.size() > 0 && curSelection != null) {
			if (path.get(path.size() - 1).distance(new Point(x, y)) >= curSelection
					.getRequiredDistanceToWaypoint()
					* curSelection.getSpeed()
					/ 4 / curSelection.getTurningSpeed()) {
				path.add(new Point(x, y));
			}
		} else
			path.add(new Point(x, y));
	}
}
