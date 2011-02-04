package game.airportcontrol.landing;

import java.awt.Point;

public abstract class LandingDevice {

	private Point checkPoint;
	private int landingPrecision;
	private int runwayLength;
	

	/**
	 * @param checkPoint
	 * @param landingPrecision
	 * @param runwayLength
	 */
	public LandingDevice(Point checkPoint, int landingPrecision,
			int runwayLength) {
		this.checkPoint = checkPoint;
		this.landingPrecision = landingPrecision;
		this.runwayLength = runwayLength;
	}

	public void setCheckPoint(Point checkPoint) {
		this.checkPoint = checkPoint;
	}

	public Point getCheckPoint() {
		return checkPoint;
	}

	public int getLandingPrecision() {
		return landingPrecision;
	}

	public int getRunwayLength() {
		return runwayLength;
	}
	
	public void setLandingPrecision(int landingPrecision) {
		this.landingPrecision = landingPrecision;
	}

}
