package game.airportcontrol.gamesettings;

import game.airportcontrol.gamefunctions.Collision;
import game.airportcontrol.landing.Airport;
import game.airportcontrol.moveables.AircraftBase;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {
	static ArrayList<AircraftBase> airplanes = new ArrayList<AircraftBase>();
	Airport airport;

	private Random rand;
	private long colTime = 0; // TODO (MMB) awful hack

	public Game() {
		super(
				"Airport Control by Moritz & Steffen, supported via VoiceChat by Lisa (Seniorsupport Clara and Bjoern)");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		airport = new Airport("muc", 5, 2);
		rand = new Random();

		for (int j = 0; j < 1; j++) {
			airplanes.add(GameSetup.genRandomPlane());
		}

	}
	
	private void addRandomPlane() {
		if(rand.nextDouble()<airport.getAircraftSpawnRatio() && airport.getLandedAirplanes()+airplanes.size() < airport.getToCreateAirplanes())
			airplanes.add(GameSetup.genRandomPlane());
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		addRandomPlane();
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
		if (colTime > 0
				&& System.currentTimeMillis() - colTime > 700
				|| airport.getLandedAirplanes() == airport
						.getToCreateAirplanes()) {
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
						+ airport.getLandedAirplanes() + " / " + airport.getToCreateAirplanes(), 0, 25);
		
		for (AircraftBase curAirplane : airplanes) {
			curAirplane.render(g);
		}

		for (int i = 1; i < InputHandler.curRecordedPath.size(); i++) {
			g.drawLine(InputHandler.curRecordedPath.get(i - 1).x, InputHandler.curRecordedPath.get(i - 1).y, InputHandler.curRecordedPath.get(i).x,
					InputHandler.curRecordedPath.get(i).y);
		}

	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new Game());
			app.setDisplayMode(GameSetup.resWidth, GameSetup.resHeight,
					GameSetup.fullScreenMode);
			app.setMaximumLogicUpdateInterval(GameSetup.UPDATE_INTERVALL_IN_MS);
			app.setMinimumLogicUpdateInterval(GameSetup.UPDATE_INTERVALL_IN_MS);
			// app.setTargetFrameRate(150);
			app.setShowFPS(GameSetup.showFPS);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
