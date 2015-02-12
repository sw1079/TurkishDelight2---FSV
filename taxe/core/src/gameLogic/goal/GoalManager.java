package gameLogic.goal;

import gameLogic.Game;
import gameLogic.Player;
import gameLogic.map.CollisionStation;
import gameLogic.map.Map;
import gameLogic.map.Station;
import gameLogic.resource.ResourceManager;
import gameLogic.resource.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Util.Node;
import Util.Tuple;

public class GoalManager {
	public final static int CONFIG_MAX_PLAYER_GOALS = 3;
	private ResourceManager resourceManager;
	private List<GoalListener> listeners;
	
	public GoalManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
		listeners = new ArrayList<GoalListener>();
	}

	public Goal generateRandomGoal(int turn, int extraConstraints) {
		Map map = Game.getInstance().getMap();
		Station origin;
		do {
			origin = map.getRandomStation();
		} while (origin instanceof CollisionStation);
		Station destination;
		do {
			destination = map.getRandomStation();
			// always true, really?
		} while (destination == origin || destination instanceof CollisionStation);
		
		Goal goal = new Goal(origin, destination, turn);
				
		//Check if we need to complicate the Goal with further constraints
		if(extraConstraints > 0)
		{
			//Find the ideal solution to solving this objective
			Node<Station> originNode = new Node<Station>();
			originNode.setData(origin);
			ArrayList<Node<Station>> searchFringe = new ArrayList<Node<Station>>();
			searchFringe.add(originNode);
			List<Station> idealRoute = map.getIdealRoute(destination, searchFringe, map.getStationsList());
			//Generate a set of constraints to add to the goal
			ArrayList<Tuple<String, Object>> availableConstraints = generateExtraConstraints(idealRoute, map.getRouteLength(idealRoute));
			for(int i = 0; i < extraConstraints; i++)
			{
				//Pick one of our available constraints and add it to the goal
				Tuple<String, Object> goalConstraint = availableConstraints.get(new Random().nextInt(availableConstraints.size()));
				availableConstraints.remove(goalConstraint);
				goal.addConstraint(goalConstraint.getFirst(), goalConstraint.getSecond());
			}
		}
		return goal;
	}
	
	private ArrayList<Tuple<String, Object>> generateExtraConstraints(List<Station> idealRoute, float routeLength) {
		ArrayList<Tuple<String, Object>> list =  new ArrayList<Tuple<String, Object>>();
		System.out.println(routeLength);
		//Add a constraint based on number of turns, based on the time taken for a Bullet Train to complete the route of Param routeLength
		list.add(new Tuple<String, Object>("turns", (int)Math.ceil((routeLength / resourceManager.getTrainSpeed("Bullet Train")))));
		//Add a constraint based on the number of trains completing the same goal, with a random value of either 2 or 3
		list.add(new Tuple<String, Object>("trainCount", new Random().nextInt(2) + 2));
		//Add a constraint based on the train type, picking a random train type
		list.add(new Tuple<String, Object>("train", resourceManager.getTrainNames().get(new Random().nextInt(resourceManager.getTrainNames().size()))));
		//If the route is not linear between 2 points, then we can add an exclusion constraint from the idealRoute
		if(idealRoute.size() > 2)
		{
			list.add(new Tuple<String, Object>("exclusion", idealRoute.get(1 + new Random().nextInt(idealRoute.size() - 2))));
		}
		//Add a constraint of the maximum number of journeys a train can make to get between the 2 locations, the length of the ideal route + 1 (since the ideal route contains the origin)
		list.add(new Tuple<String, Object>("locations", idealRoute.size()));
		return list;
	}
	
	public void updatePlayerGoals(Player player)
	{
		player.updateGoals(this);
	}

	public ArrayList<String> trainArrived(Train train, Player player) {
		ArrayList<String> completedString = new ArrayList<String>();
		for(Goal goal:player.getGoals()) {
			//Check if a goal was completed by the train arrival
			if(goal.isComplete(train)) {
				player.completeGoal(goal);
				player.removeResource(train);
				completedString.add("Player " + player.getPlayerNumber() + " completed a goal to " + goal.toString() + "!");
				goalFinished(goal);
			}
		}
		System.out.println("Train arrived to final destination: " + train.getFinalDestination().getName());
		return completedString;
	}

	public void subscribeGoalFinished(GoalListener goalFinishedListener) {
		listeners.add(goalFinishedListener);
	}
	
	public void goalFinished(Goal goal) {
		for (GoalListener listener : listeners){
			listener.finished(goal);
		}
	}
}
