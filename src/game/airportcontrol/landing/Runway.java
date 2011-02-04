package game.airportcontrol.landing;


import java.awt.Point;

public class Runway extends LandingDevice {
	private double approachableAngle;
	private double approachableAnglePrecision;
	public Runway(Point checkPoint, int landingPrecision, double approchableAngle, double approchableAnglePrecision, int runwayLength){
		super(checkPoint, landingPrecision, runwayLength);
		
		this.approachableAngle = approchableAngle;
		this.approachableAnglePrecision = approchableAnglePrecision;
	}

	public double getApproachableAngle() {
		return approachableAngle;
	}

	public double getApproachableAnglePrecision() {
		return approachableAnglePrecision;
	}
	
	
	
}
