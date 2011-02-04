package game.airportcontrol.moveables;

import game.airportcontrol.landing.LandingDevice;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Airplane {
	private Image image;
	private double transparency;
	
	private int typeOfAirplane;
	
	private Point position; 
	private double angle;
	private ArrayList<Point> wayPoints;
	private double speed;
	
	private LandingDevice initiateLanding; 
	private int landingPrecision;

	private final int mapScaling = 20;
	
	public Airplane(Point position, int typeOfAirplane, int angle, double speed) {
		this.position = position;
		this.transparency = 1;
		this.angle = angle;
		this.typeOfAirplane = typeOfAirplane;
		this.wayPoints = null;
		this.initiateLanding = null;
		this.landingPrecision = 10;
		this.speed = Math.max(speed,3.0);
		switch(typeOfAirplane) {
		case 2:
			try {
				image = new Image("data/planes/heli_test.png");
			} catch (SlickException e) {
				System.out.println("Error orcurred during loading picture.");
				e.printStackTrace();
			}
			break;
		case 1:
		default:
			try {
				image = new Image("data/planes/plane_test.png");
			} catch (SlickException e) {
				System.out.println("Error orcurred during loading picture.");
				e.printStackTrace();
			}
			break;
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
		if(null != initiateLanding) {
			setLandingPrecision(initiateLanding.getLandingPrecision());
		}
		else {
			setLandingPrecision(1);
			setTransparency(1);
		}
	}

	public boolean alignAirplaneToPoint(ArrayList<Point> wp) {
		if(wayPoints == null || wayPoints.size() < 1) {
			return false;
		}

		Point p = wp.get(0);
		int dy = (p.y*mapScaling)-position.y;
		int dx = (p.x*mapScaling)-position.x;

		
		double a;
		if(dx == 0) {
			a = 0;
		}
		else {
			a = (Math.atan((double)dy/(double)dx))/(2*Math.PI)*360;
		}
		if(dx < 0) {
			a += 180;
		}
		
		setAngle(a);
		return true;
	}

	
	public void update(int width, int height) {
		boolean haspath = alignAirplaneToPoint(wayPoints);
		//angle++;
		double ddx, ddy;
		ddx = Math.cos(((angle)/360)*2*Math.PI)*speed;
		ddy = Math.sin(((angle)/360)*2*Math.PI)*speed;
		
		int idx,idy;
		
		idx = (int)ddx;
		idy = (int)ddy;

//		if(idx == 0 && !haspath){
//			if (Math.abs(ddx) > 0.1*mapScaling){
//				if (ddy>0) angle = 90;
//				else angle = 270;
//			}
//		}
//		if(idy == 0 && !haspath){
//			if (Math.abs(ddy) > 0.1*mapScaling){
//				if (ddx>0) angle = 0;
//				else angle = 180;
//			}
//		}

		
		int x,y;
		
		x =	position.x+idx;
		y = position.y+idy;
				
		Point targetPosition = new Point(x,y);
		
		if(wayPoints != null && wayPoints.size() > 0) { // not nice
			if(targetPosition.distance(new Point(wayPoints.get(0).x*mapScaling,wayPoints.get(0).y*mapScaling)) < 5*mapScaling) {
				wayPoints.remove(0);
			}
		}
		
		//SIDE-BOUNCE_BACK
		if (x > width*mapScaling){
			angle=180+angle; //180
		}else if (y > height*mapScaling){
			angle=180+angle; //270
		}else if (x < 0){
			angle=180+angle; //0
		}else if (y < 0){
			angle=180+angle;//90
		}
		
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
		Point p = new Point((position.x/mapScaling),(position.y/mapScaling));
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

	public int getTypeOfAirplane() {
		return typeOfAirplane;
	}
	
	
}
