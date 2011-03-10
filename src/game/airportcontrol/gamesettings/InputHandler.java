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
				curSelection.setPath(curRecordedPath);
				curSelection.isSelected = false;
			}
			curRecordedPath = new AircraftPath();
		}
	}

	public static void recordTrack(int x, int y) {
		Point curPoint = new Point(x,y);

		if (curRecordedPath.isEmpty()) {
			for (AircraftBase curAircraft : Game.airplanes) {
				if (curAircraft.getPosition().distance(curPoint) < curAircraft.getDiameter()) {
					curSelection = curAircraft;
					curSelection.isSelected = true;
				}
			}
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
		}
	}
}
