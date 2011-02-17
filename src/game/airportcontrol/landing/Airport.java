package game.airportcontrol.landing;

import game.airportcontrol.moveables.AircraftBase;
import game.airportcontrol.moveables.Airplane;
import game.airportcontrol.moveables.Helicopter;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Airport {

	private Image image;
	private int toCreateAirplanes;
	private int landedAirplanes;
	private ArrayList<LandingDevice> landingDevices;

	public Airport(String iata, int toCreateAirplanes) {
		this.toCreateAirplanes = toCreateAirplanes;
		this.landingDevices = new ArrayList<LandingDevice>();

		if ("muc" == iata) {
			// Runways
			this.landingDevices.add(new Runway(new Point(354, 319), 35, 10, 35,
					60));
			this.landingDevices.add(new Runway(new Point(502, 188), 35, 170,
					20, 60));

			// Helipads
			this.landingDevices.add(new Helipad(new Point(153, 252), 35, 15));
		}

		/* Automated Graphic Loader */
		try {
			this.image = new Image("/data/airports/ap_" + iata + ".jpg");
		} catch (SlickException e) {
			System.out.println("Couldn't find Image for Airport " + iata);
		}

		this.landedAirplanes = 0;
	}

	public void update(ArrayList<AircraftBase> airplanes) {
		for (LandingDevice curLandingDev : landingDevices) {
			ArrayList<AircraftBase> copyOfOriginalAirplanes = (ArrayList<AircraftBase>) airplanes
					.clone();
			for (AircraftBase curAirplane : copyOfOriginalAirplanes) {
				// it's an airplane
				if (curAirplane instanceof Airplane
						&& Runway.class.isInstance(curLandingDev)) {
					/* Landing attempt has been started */
					if (null != curAirplane.getInitiateLanding()
							&& curAirplane.getInitiateLanding() == curLandingDev) {
						// plane has successfully landed
						if (curAirplane.getLandingPrecision() <= 0) {
							airplanes.remove(curAirplane);
							// System.out.println(curAirplane.getAngle());
							// helpful for designing new airports
							landedAirplanes++;
						}
						// plane is within the required radius of the runway's
						// checkpoint
						else if (curLandingDev.getCheckPoint().distance(
								curAirplane.getPosition()) < curLandingDev
								.getRunwayLength()) {
							// plane is approaching the runway in the required
							// angle, with desired precision
							if (Math.abs(curAirplane.getAngle()
									- ((Runway) curLandingDev)
											.getApproachableAngle()) < ((Runway) curLandingDev)
									.getApproachableAnglePrecision()) {
								if (curAirplane.getLandingPrecision() > 0) {
									curAirplane.setLandingPrecision(curAirplane
											.getLandingPrecision() - 1);
									curAirplane.setTransparency(curAirplane
											.getTransparency() - 0.03);
									// System.out.println(curAirplane.getLandingPrecision());
									// helpful for designing new airports
								}
							}
						}
						// plane has failed landing approach
						else {
							curAirplane.setInitiateLanding(null);
						}
					}
					/* Initiate landing attempt */
					else if (curLandingDev.getCheckPoint().distance(
							curAirplane.getPosition()) < curLandingDev
							.getRunwayLength()) {
						curAirplane.setInitiateLanding(curLandingDev);
					}
				}

				// it's a heli
				else if (curAirplane instanceof Helicopter
						&& Helipad.class.isInstance(curLandingDev)) {
					/* Initiate landing attempt */
					/* Landing attempt has been started */
					if (null != curAirplane.getInitiateLanding()
							&& curAirplane.getInitiateLanding() == curLandingDev) {
						// plane has successfully landed
						if (curAirplane.getLandingPrecision() <= 0) {
							airplanes.remove(curAirplane);
							// System.out.println(curAirplane.getAngle()); //
							// helpful for designing new airports
							landedAirplanes++;
						}
						// plane is within the required radius of the runway's
						// checkpoint
						else if (curLandingDev.getCheckPoint().distance(
								curAirplane.getPosition()) < curLandingDev
								.getRunwayLength()) {
							// plane is approaching the runway in the required
							// precision
							if (curAirplane.getLandingPrecision() > 0) {
								curAirplane.setLandingPrecision(curAirplane
										.getLandingPrecision() - 1);
								curAirplane.setTransparency(curAirplane
										.getTransparency() - 0.03);
								// System.out.println(curAirplane.getLandingPrecision());
								// helpful for designing new airports
							}
						}
						// plane has failed landing approach
						else {
							curAirplane.setInitiateLanding(null);
						}
					}
					/* Initiate landing attempt */
					else if (curLandingDev.getCheckPoint().distance(
							curAirplane.getPosition()) < curLandingDev
							.getRunwayLength()) {
						curAirplane.setInitiateLanding(curLandingDev);
					}
				}

			}
		}
	}

	public int getLandedAirplanes() {
		return landedAirplanes;
	}

	public int getToCreateAirplanes() {
		return toCreateAirplanes;
	}

	public Image getImage() {
		return this.image;
	}

}
