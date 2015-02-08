package fvs.taxe.controller;

import Util.InterruptableSequenceAction;
import Util.Tuple;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import fvs.taxe.actor.ObstacleActor;
import fvs.taxe.actor.TrainActor;
import gameLogic.GameState;
import gameLogic.GameStateListener;
import gameLogic.Player;
import gameLogic.TurnListener;
import gameLogic.map.CollisionStation;
import gameLogic.map.IPositionable;
import gameLogic.map.Position;
import gameLogic.map.Station;
import gameLogic.obstacle.Obstacle;
import gameLogic.resource.Resource;
import gameLogic.resource.Train;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static org.hamcrest.CoreMatchers.instanceOf;

public class TrainMoveController {
	private static final float JUNCTION_FAILURE_CHANCE = 1f;
	private Context context;
	private Train train;
	private InterruptableSequenceAction action;

	public TrainMoveController(final Context context, final Train train) {
		this.context = context;
		this.train = train;

		context.getGameLogic().getPlayerManager().subscribeTurnChanged(new TurnListener() {
			// only set back the interrupt so the train can move after the turn has changed (players turn ended)
			@Override
			public void changed() {
				action.setInterrupt(false);
			}
		});
		
		addMoveActions();
	}

	// an action for the train to run before it starts moving across the screen
	private RunnableAction beforeAction() {
		return new RunnableAction() {
			public void run() {
				train.getActor().setVisible(true);
				train.setPosition(new Position(-1, -1));
			}
		};
	}

	// this action will run every time the train reaches a station within a route
	private RunnableAction perStationAction(final Station station) {
		return new RunnableAction() {
			public void run() {
				train.addHistory(station.getName(), context.getGameLogic().getPlayerManager().getTurnNumber());
				System.out.println("Added to history: passed " + station.getName() + " on turn "
						+ context.getGameLogic().getPlayerManager().getTurnNumber());
				// train.setPosition(station.getLocation());
					
				junctionFailure(station);
				collisions(station);
				obstacleCollision(station);
			}

		};
	}

	private void junctionFailure(Station station) {
		if (station instanceof CollisionStation){
			boolean junctionFailed = MathUtils.randomBoolean(JUNCTION_FAILURE_CHANCE);
			if (junctionFailed && station != train.getRoute().get(0)) {
				action.setInterrupt(true);
				context.getTopBarController().displayObstacleMessage("Junction failed, " + train.getName() + " stopped!", Color.YELLOW);
			}
		}
	}

	// an action for the train to run after it has moved the whole route
	private RunnableAction afterAction() {
		return new RunnableAction() {
			public void run() {
				ArrayList<String> completedGoals = context.getGameLogic().getGoalManager().trainArrived(train, train.getPlayer());
				for(String message : completedGoals) {
					context.getTopBarController().displayFlashMessage(message, Color.WHITE, 2);
				}
				System.out.println(train.getFinalDestination().getLocation().getX() + "," + train.getFinalDestination().getLocation().getY());
				train.setPosition(train.getFinalDestination().getLocation());
				train.getActor().setVisible(false);
				train.setFinalDestination(null);
			}
		};
	}

	public void addMoveActions() {
		action = new InterruptableSequenceAction();
		IPositionable current = train.getPosition();
		action.addAction(beforeAction());

		for (final Station station : train.getRoute()) {
			IPositionable next = station.getLocation();
			float duration = getDistance(current, next) / train.getSpeed();
			action.addAction(moveTo(next.getX() - TrainActor.width / 2, next.getY() - TrainActor.height / 2, duration));
			
			action.addAction(perStationAction(station));
			current = next;
		}

		action.addAction(afterAction());

		// remove previous actions to be cautious
		train.getActor().clearActions();
		train.getActor().addAction(action);
	}

	private float getDistance(IPositionable a, IPositionable b) {
		return Vector2.dst(a.getX(), a.getY(), b.getX(), b.getY());
	}

	private void collisions(Station station) {
		//test for train collisions at Junction point
		if(!(station instanceof CollisionStation)) {
			return;
		}
		List<Train> trainsToDestroy = collidedTrains();

		if(trainsToDestroy.size() > 0) {
			for(Train trainToDestroy : trainsToDestroy) {
				trainToDestroy.getActor().remove();
				trainToDestroy.getPlayer().removeResource(trainToDestroy);
			}

			context.getTopBarController().displayFlashMessage("Two trains collided at a Junction.  They were both destroyed.", Color.BLACK, Color.RED, 4);
		}
	}

	private void obstacleCollision(Station station) {
		// works out if the station has an obstacle active there, whether to destroy the train
		if (station.hasObstacle() && MathUtils.randomBoolean(station.getObstacle().getDestructionChance())){
			train.getActor().remove();
			train.getPlayer().removeResource(train);
			context.getTopBarController().displayFlashMessage("Your train was hit by a natural disaster...", Color.RED, 4);
		}
	}

	private List<Train> collidedTrains() {
		List<Train> trainsToDestroy = new ArrayList<Train>();

		for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
			for(Resource resource : player.getResources()) {
				if(resource instanceof Train) {
					Train otherTrain = (Train) resource;
					if(otherTrain.getActor() == null) continue;
					if(otherTrain == train) continue;

					if(train.getActor().getBounds().overlaps(otherTrain.getActor().getBounds())) {
						//destroy trains that have crashed and burned
						trainsToDestroy.add(train);
						trainsToDestroy.add(otherTrain);
					}
				}
			}
		}

		return trainsToDestroy;
	}
}
