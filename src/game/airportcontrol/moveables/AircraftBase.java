/**
 * @author Moritz Beller
 */
package game.airportcontrol.moveables;

import game.airportcontrol.gamefunctions.Wayfinding;
import game.airportcontrol.gamesettings.GameSetup;
import game.airportcontrol.landing.LandingDevice;

import java.awt.Point;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * @author Moritz Beller
 * 
 *         The aircraft angle is in [0;360] always It should be noted that the
 *         nose of the aircraft when pointing to the right hand side denotes the
 *         0, angle. A clockwise rotation from this direction onward increases
 *         the angle
 * 
 *         All angles and distances are relative to middle of aircraft which is
 *         defined to be centre of picture.
 */
public abstract class AircraftBase {
	protected Image image;
	private double transparency;

	private Point position;
	private double curAngle;
	private ArrayList<Point> wayPoints;
	private double speed;

	private enum turningDirections {
		LEFT_TURN, RIGHT_TURN, STRAIGHT
	};

	private turningDirections turningDirection;

	private double turningSpeed;
	private double requiredDistanceToWaypoint;

	private LandingDevice initiateLanding;
	private int landingPrecision;

	private final int mapScaling = 20;

	public ParticleSystem system;
	protected int mode = ParticleSystem.BLEND_COMBINE;

	public AircraftBase(Point position, int angle, double speed,
			double turningSpeed, double requiredDistanceToWaypoint) {
		this.position = position;
		this.transparency = 1;
		this.curAngle = angle;
		this.wayPoints = null;
		this.initiateLanding = null;
		this.landingPrecision = 10;
		this.speed = Math.max(speed, 3.0);
		this.turningSpeed = turningSpeed;
		this.requiredDistanceToWaypoint = requiredDistanceToWaypoint;
		this.turningDirection = turningDirections.STRAIGHT;
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
		} else {
			setLandingPrecision(1);
			setTransparency(1);
		}
	}

	public boolean alignAircraftToPoint(ArrayList<Point> wp) {
		if (wayPoints == null || wayPoints.size() < 1) {
			return false;
		}

		Point p = wp.get(0);
		double a = Wayfinding.calcAngleInSourceToTarget(this.position, p,
				this.mapScaling);

		if (this.turningDirection == turningDirections.STRAIGHT) {
			// no turn has been initiated on this waypoint before, i.e. aircraft
			// is flying in a straight line atm
			if (Math.abs(a - curAngle) > this.turningSpeed) {
				// decide which is closest: a right-turn or a left-turn
				if ((a > curAngle && a < curAngle + 180)
						|| (a < (curAngle + 180) % 360 && curAngle >= 180))
					this.turningDirection = turningDirections.RIGHT_TURN;
				else
					this.turningDirection = turningDirections.LEFT_TURN;
			}
		}

		if (this.turningDirection != turningDirections.STRAIGHT) {
			// perform the actual smooth curving
			if (this.turningDirection == turningDirections.LEFT_TURN) {
				curAngle = (curAngle - this.turningSpeed) % 360;
				if (curAngle < 0)
					curAngle += 360;
			} else
				curAngle = (curAngle + this.turningSpeed) % 360;

		}

		if (Math.abs(a - curAngle) <= this.turningSpeed) {
			setAngle(a);
			this.turningDirection = turningDirections.STRAIGHT;
		} else {
			setAngle(curAngle);
		}
		return true;
	}

	public void update(int width, int height, int delta) {
		alignAircraftToPoint(wayPoints);
		double ddx, ddy;
		ddx = Math.cos(((curAngle) / 360) * 2 * Math.PI) * speed;
		ddy = Math.sin(((curAngle) / 360) * 2 * Math.PI) * speed;

		int idx, idy;

		idx = (int) ddx;
		idy = (int) ddy;

		int x, y;

		x = position.x + idx;
		y = position.y + idy;

		Point targetPosition = new Point(x, y);

		if (wayPoints != null && wayPoints.size() > 0) { // not nice
			if (targetPosition.distance(new Point(wayPoints.get(0).x
					* mapScaling, wayPoints.get(0).y * mapScaling)) < requiredDistanceToWaypoint
					* mapScaling) {
				wayPoints.remove(0);
				this.turningDirection = turningDirections.STRAIGHT;
			}
		}

		// wall bounce back
		if (x > width * mapScaling) {
			curAngle = 180 + curAngle; // 180
		} else if (y > height * mapScaling) {
			curAngle = 180 + curAngle; // 270
		} else if (x < 0) {
			curAngle = 180 + curAngle; // 0
		} else if (y < 0) {
			curAngle = 180 + curAngle;// 90
		}

		curAngle = curAngle % 360;

		system.update(delta); // particle effect
		setPosition(targetPosition);
	}

	public double getAngle() {
		return curAngle;
	}

	public void setAngle(double angle) {
		this.curAngle = angle;
	}

	public Image getImage() {
		return image;
	}

	public Point getPosition() {
		Point p = new Point((position.x / mapScaling),
				(position.y / mapScaling));
		return p;
	}

	public double getSpeed() {
		return this.speed;
	}

	public double getTurningSpeed() {
		return this.turningSpeed;
	}

	public double getRequiredDistanceToWaypoint() {
		return requiredDistanceToWaypoint;
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

	public double getDiameter() {
		double max = (image.getHeight() > image.getWidth() ? image.getHeight()
				: image.getWidth());
		return max / 2 * Math.PI;
	}

	public void setTransparency(double transparency) {
		this.transparency = transparency;
	}

	public void enableCollisionEffect() {
		for (int i = 2; i < 6; i++) {
			system.getEmitter(i).setEnabled(true);
		}
	}

	// renders the WayPoints of the Aircraft, if there are any
	private void renderWayPoints(Graphics g) {
		if (this.getWayPoints() != null) {
			for (int i = 1; i < this.getWayPoints().size(); i++) {
				g.drawLine(this.getWayPoints().get(i - 1).x, this
						.getWayPoints().get(i - 1).y, this.getWayPoints()
						.get(i).x, this.getWayPoints().get(i).y);
			}
		}
	}

	public void render(Graphics g) throws SlickException {
		Image img;
		int ax = 0;
		int ay = 0;
		double an = 0;

		img = this.getImage().copy();
		img.setCenterOfRotation((int) (0.5 * img.getWidth() * 1), // TODO
																	// ADD
																	// scale
																	// factor
																	// instead
																	// of 1
				(int) (0.5 * img.getHeight() * 1));
		img.rotate((float) this.getAngle());
		Color color = new Color(1f, 1f, 1f, (float) this.getTransparency());
		img.draw(this.getPosition().x - this.getImage().getWidth() / 2,
				this.getPosition().y - this.getImage().getWidth() / 2, color);

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);

		if (GameSetup.VERBOSE)
			g.drawString("curAngle: " + nf.format(this.getAngle()), 0, 40);

		renderWayPoints(g);

		ax = this.getPosition().x;
		ay = this.getPosition().y;
		an = this.getAngle();

		this.system.setPosition(ax, ay);
		g.rotate(ax, ay, (int) an);
		g.translate(1, 1);
		this.system.render();
		g.resetTransform();
	}
}
