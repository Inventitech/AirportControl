package game.airportcontrol.gamesettings;

import game.airportcontrol.gamefunctions.Collision;
import game.airportcontrol.landing.Airport;
import game.airportcontrol.moveables.AircraftBase;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {
	static ArrayList<AircraftBase> airplanes = new ArrayList<AircraftBase>();
	Airport airport;

	static ArrayList<Point> path = new ArrayList<Point>();
	static int c = 0;
	static AircraftBase pathTo = null;

	private long colTime = 0; // TODO (MMB) awful hack

	public Game() {
		super(
				"Airport Control by Moritz & Steffen, supported via VoiceChat by Lisa (Seniorsupport Clara and Bjoern)");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		airport = new Airport("muc");

		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 8; j++) {
				airplanes.add(GameSetup.genRandomPlane());
			}
		}

	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		for (AircraftBase curAirplane : airplanes) {
			curAirplane.update(GameSetup.resWidth, GameSetup.resHeight, delta);
		}

		airport.update(airplanes);

		InputHandler.inputHandler(container.getInput());

		if (GameSetup.doCollisionChecks) {
			if (Collision.hasCollision(airplanes)) {
				// TODO (MMB) Add collision effect, end game awful use of
				// particles
				collision();

			}
		}

		// Game over?
		if (colTime > 0 && System.currentTimeMillis() - colTime > 700) {
			container.exit();
		}
	}

	/* awful hack */
	public void collision() {
		if (colTime == 0) {
			colTime = System.currentTimeMillis();
		}
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {

		airport.getImage().draw(0, 0, container.getWidth(),
				container.getHeight());
		g.drawString(
				System.getProperty("user.name") + ", "
						+ airport.getLandedAirplanes(), 0, 25);
		Image img;
		int ax = 0;
		int ay = 0;
		double an = 0;
		for (AircraftBase curAirplane : airplanes) {
			img = curAirplane.getImage().copy();
			img.setCenterOfRotation((int) (0.5 * img.getWidth() * 1), // TODO
																		// ADD
																		// scale
																		// factor
																		// instead
																		// of 1
					(int) (0.5 * img.getHeight() * 1));
			img.rotate((float) curAirplane.getAngle());
			Color color = new Color(1f, 1f, 1f,
					(float) curAirplane.getTransparency());
			img.draw(curAirplane.getPosition().x
					- curAirplane.getImage().getWidth() / 2,
					curAirplane.getPosition().y
							- curAirplane.getImage().getWidth() / 2, color);

			if (curAirplane.getWayPoints() != null) {
				for (int i = 1; i < curAirplane.getWayPoints().size(); i++) {
					g.drawLine(curAirplane.getWayPoints().get(i - 1).x,
							curAirplane.getWayPoints().get(i - 1).y,
							curAirplane.getWayPoints().get(i).x, curAirplane
									.getWayPoints().get(i).y);
				}
			}
			ax = curAirplane.getPosition().x;
			ay = curAirplane.getPosition().y;
			an = curAirplane.getAngle();

			curAirplane.system.setPosition(ax, ay);
			g.rotate(ax, ay, (int) an);
			g.translate(1, 1);
			curAirplane.system.render();
			g.resetTransform();
		}

		for (int i = 1; i < path.size(); i++) {
			g.drawLine(path.get(i - 1).x, path.get(i - 1).y, path.get(i).x,
					path.get(i).y);
		}

	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new Game());
			app.setDisplayMode(GameSetup.resWidth, GameSetup.resHeight,
					GameSetup.fullScreenMode);
			app.setMaximumLogicUpdateInterval(20);
			app.setMinimumLogicUpdateInterval(20);
			// app.setTargetFrameRate(150);
			app.setShowFPS(GameSetup.showFPS);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}