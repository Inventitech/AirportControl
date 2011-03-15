package game.airportcontrol.gamesettings;

import game.airportcontrol.gamefunctions.Wayfinding;
import game.airportcontrol.moveables.AircraftBase;
import game.airportcontrol.moveables.AircraftPath;

import java.awt.Point;

import org.newdawn.slick.Input;

public class InputHandler {
	private static AircraftBase curSelection;
	static AircraftPath curRecordedPath = new AircraftPath();
	
	public static void inputHandler(Input input) {
		if (input.isMouseButtonDown(0)) {
			recordTrack(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
		} else {
			saveTrack();
		}
	}

	public static void saveTrack() {
		if (curRecordedPath.size() > 0) {
			if (curSelection != null) {
				curSelection.isSelected = false;
				curSelection = null;
			}
		}
	}
	
	public static void selectAircraft(Point curPoint) {
		for (AircraftBase curAircraft : Game.airplanes) {
			if (curAircraft.getPosition().distance(curPoint) < curAircraft
					.getDiameter()) {
				curSelection = curAircraft;
				curSelection.isSelected = true;
			}
		}
	}

	public static void recordTrack(int x, int y) {
		Point curPoint = new Point(x,y);
		
		// If there is no AirCraft selected, start recording a new path.
		// This means that upon selection, the aircraft's current path
		// will be overridden
		// otherwise, continue recording current path
		if(curSelection != null)  {
			curRecordedPath = curSelection.getPath();
		}
		else {
			curRecordedPath = new AircraftPath();
			selectAircraft(curPoint);
		}
		
		if (curSelection != null) {
			int lastIndex = curRecordedPath.size() - 1;
			if (lastIndex > 0) {
				if (Wayfinding.calcAngleBetweenSourceTarget(
						curRecordedPath.get(lastIndex - 1),
						curRecordedPath.get(lastIndex), curPoint) < 60) {
					if(curRecordedPath.get(lastIndex).distance(curPoint) >= curSelection
						.getRequiredDistanceToWaypoint()*curSelection.getSpeed()/curSelection.getTurningSpeed()/11)
						curRecordedPath.add(curPoint);
				}

			} else if (lastIndex == 0) {
				if (curRecordedPath.get(lastIndex).distance(curPoint) >= curSelection
						.getRequiredDistanceToWaypoint()) {
					curRecordedPath.add(curPoint);
				}
			} else {
				curRecordedPath.add(curPoint);
			}
			curSelection.setPath(curRecordedPath);
		}
	}
}
