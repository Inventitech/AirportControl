package game.airportcontrol.gamesettings;

import game.airportcontrol.moveables.AircraftBase;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Input;

public class InputHandler {
	private static AircraftBase curSelection;
	static ArrayList<Point> curRecordedPath = new ArrayList<Point>();
	
	public static void inputHandler(Input input) {
		if (input.isMouseButtonDown(0)) {
			recordTrack(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
		} else {
			saveTrack();
		}
	}

	public static void saveTrack() {
		if (curRecordedPath.size() > 0) {
			if (curSelection != null)
				curSelection.setWayPoints(curRecordedPath);
			curRecordedPath = new ArrayList<Point>();
		}
	}

	public static void recordTrack(int x, int y) {
		if (curRecordedPath.isEmpty()) {
			for (AircraftBase curAircraft : Game.airplanes) {
				if (curAircraft.getPosition().distance(new Point(x, y)) < curAircraft.getDiameter()) {
					curSelection = curAircraft;
				}
			}
		}
		if (curRecordedPath.size() > 0 && curSelection != null) {
			if (curRecordedPath.get(curRecordedPath.size() - 1).distance(new Point(x, y)) >= curSelection
					.getRequiredDistanceToWaypoint()
					* curSelection.getSpeed()
					/ 4 / curSelection.getTurningSpeed()) {
				curRecordedPath.add(new Point(x, y));
			}
		} else
			curRecordedPath.add(new Point(x, y));
	}
}
