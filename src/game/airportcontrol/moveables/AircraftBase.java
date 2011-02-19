/**
 * @author Moritz Beller
 */
package game.airportcontrol.moveables;

import game.airportcontrol.landing.LandingDevice;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * @author Moritz Beller
 * 
 */
public class AircraftBase {
	protected Image image;
	private double transparency;

	private Point position;
	private double angle;
	private ArrayList<Point> wayPoints;
	private double speed;

	private LandingDevice initiateLanding;
	private int landingPrecision;

	private final int mapScaling = 20;

	public ParticleSystem system;
	private int mode = ParticleSystem.BLEND_COMBINE;

	public AircraftBase(Point position, int angle, double speed) {
		this.position = position;
		this.transparency = 1;
		this.angle = angle;
		this.wayPoints = null;
		this.initiateLanding = null;
		this.landingPrecision = 10;
		this.speed = Math.max(speed, 3.0);
		try {
			system = ParticleIO.loadConfiguredSystem("data/particles/sys.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		system.setBlendingMode(mode);
		for (int i = 2; i < 6; i++) {
			system.getEmitter(i).setEnabled(false);
		}
	}

	public void setWayPoints(ArrayList<Point> wp) {
		this.wayPoints = wp;
	}

	public ArrayList<Point> getWayPoints() {
		return wayPoints;
	}

	public LandingDevice getInitiateLanding() {
		return initiateLanding;
	}

	public void setInitiateLanding(LandingDevice initiateLanding) {
		this.initiateLanding = initiateLanding;
		if (null != initiateLanding) {
			setLandingPrecision(initiateLanding.getLandingPrecision());
		}
		else {
			setLandingPrecision(1);
			setTransparency(1);
		}
	}

	public boolean alignAircraftToPoint(ArrayList<Point> wp) {
		if (wayPoints == null || wayPoints.size() < 1) {
			return false;
		}

		Point p = wp.get(0);
		int dy = (p.y * mapScaling) - position.y;
		int dx = (p.x * mapScaling) - position.x;

		double a;
		if (dx == 0) {
			a = 0;
		}
		else {
			a = (Math.atan((double) dy / (double) dx)) / (2 * Math.PI) * 360;
		}
		if (dx < 0) {
			a += 180;
		}

		setAngle(a);
		return true;
	}

	public void update(int width, int height, int delta) {
		alignAircraftToPoint(wayPoints);
		double ddx, ddy;
		ddx = Math.cos(((angle) / 360) * 2 * Math.PI) * speed;
		ddy = Math.sin(((angle) / 360) * 2 * Math.PI) * speed;

		int idx, idy;

		idx = (int) ddx;
		idy = (int) ddy;

		int x, y;

		x = position.x + idx;
		y = position.y + idy;

		Point targetPosition = new Point(x, y);

		if (wayPoints != null && wayPoints.size() > 0) { // not nice
			if (targetPosition.distance(new Point(wayPoints.get(0).x
					* mapScaling, wayPoints.get(0).y * mapScaling)) < 5 * mapScaling) {
				wayPoints.remove(0);
			}
		}

		// SIDE-BOUNCE_BACK
		if (x > width * mapScaling) {
			angle = 180 + angle; // 180
		}
		else if (y > height * mapScaling) {
			angle = 180 + angle; // 270
		}
		else if (x < 0) {
			angle = 180 + angle; // 0
		}
		else if (y < 0) {
			angle = 180 + angle;// 90
		}
		system.update(delta); // particle effect
		setPosition(targetPosition);
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public Image getImage() {
		return image;
	}

	public Point getPosition() {
		Point p = new Point((position.x / mapScaling),
				(position.y / mapScaling));
		return p;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public int getLandingPrecision() {
		return landingPrecision;
	}

	public void setLandingPrecision(int landingPrecision) {
		this.landingPrecision = landingPrecision;
	}

	public double getTransparency() {
		return transparency;
	}

	public void setTransparency(double transparency) {
		this.transparency = transparency;
	}

	public void drawCollision() {
		for (int i = 2; i < 6; i++) {
			system.getEmitter(i).setEnabled(true);
		}
	}

}
