package gameLogic;

import gameLogic.goal.GoalManager;
import gameLogic.map.Map;
import gameLogic.obstacle.Obstacle;
import gameLogic.obstacle.ObstacleListener;
import gameLogic.obstacle.ObstacleManager;
import gameLogic.resource.ResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import Util.Tuple;

import com.badlogic.gdx.math.MathUtils;

public class Game {
	private static Game instance;
	private PlayerManager playerManager;
	private GoalManager goalManager;
	private ResourceManager resourceManager;
	private ObstacleManager obstacleManager;
	private Map map;
	private GameState state;
	private List<GameStateListener> gameStateListeners = new ArrayList<GameStateListener>();
	private List<ObstacleListener> obstacleListeners = new ArrayList<ObstacleListener>();

	private final int CONFIG_PLAYERS = 2;
	public final int TOTAL_TURNS = 30;
	

	private Game() {
		playerManager = new PlayerManager();
		playerManager.createPlayers(CONFIG_PLAYERS);

		resourceManager = new ResourceManager();
		goalManager = new GoalManager(resourceManager);
		map = new Map();
		obstacleManager = new ObstacleManager(map);
		
		state = GameState.NORMAL;

		playerManager.subscribeTurnChanged(new TurnListener() {
			@Override
			public void changed() {
				Player currentPlayer = playerManager.getCurrentPlayer();
				goalManager.addRandomGoalToPlayer(currentPlayer);
				resourceManager.addRandomResourceToPlayer(currentPlayer);
				resourceManager.addRandomResourceToPlayer(currentPlayer);
				calculateObstacles();
				decreaseObstacleTime();
			}
		});
	}

	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
			// initialisePlayers gives them a goal, and the GoalManager requires an instance of game to exist so this
			// method can't be called in the constructor
			instance.initialisePlayers();
		}

		return instance;
	}

	// Only the first player should be given goals and resources during init
	// The second player gets them when turn changes!
	private void initialisePlayers() {
		Player player = playerManager.getAllPlayers().get(0);
		goalManager.addRandomGoalToPlayer(player);
		resourceManager.addRandomResourceToPlayer(player);
		resourceManager.addRandomResourceToPlayer(player);
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public GoalManager getGoalManager() {
		return goalManager;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public Map getMap() {
		return map;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
		stateChanged();
	}

	public void subscribeStateChanged(GameStateListener listener) {
		gameStateListeners.add(listener);
	}

	private void stateChanged() {
		for(GameStateListener listener : gameStateListeners) {
			listener.changed(state);
		}
	}
	
	public ObstacleManager getObstacleManager(){
		return obstacleManager;
	}
	
	public void obstacleStarted(Obstacle obstacle) {
		// called whenever an obstacle starts, notifying all listeners that an obstacle has occured (handled by ... 
		for (ObstacleListener listener : obstacleListeners) {
			listener.started(obstacle);
		}
	}
	
	public void obstacleEnded(Obstacle obstacle) {
		// called whenever an obstacle ends, notifying all listeners that an obstacle has occured (handled by ... 
		for (ObstacleListener listener : obstacleListeners) {
			listener.ended(obstacle);
		}
	}

	public void subscribeObstacleChanged(ObstacleListener listener) {
		obstacleListeners.add(listener);
	}
	
	public void calculateObstacles() {
		// randomly choose one obstacle, then make the obstacle happen with its associated probability
		ArrayList<Tuple<Obstacle, Float>> obstacles = obstacleManager.getObstacles();
		int index = MathUtils.random(obstacles.size()-1);
		
		Tuple<Obstacle, Float> obstacleProbPair = obstacles.get(index);
		boolean obstacleOccured = MathUtils.randomBoolean(obstacleProbPair.getSecond());
		Obstacle obstacle = obstacleProbPair.getFirst();
		
		// if it has occured and isnt already active, start the obstacle
		if(obstacleOccured && !obstacle.isActive()){
			obstacleStarted(obstacle);
		}
	}
	
	public void decreaseObstacleTime() {
		// decreases any active obstacles time left active by 1
		ArrayList<Tuple<Obstacle, Float>> obstacles = obstacleManager.getObstacles();
		for (int i = 0; i< obstacles.size(); i++) {
			Obstacle obstacle = obstacles.get(i).getFirst();
			if (obstacle.isActive()) {
				boolean isTimeLeft = obstacle.decreaseTimeLeft();
				if (!isTimeLeft) {
					// if the time left = 0, then deactivate the obstacle
					obstacleEnded(obstacle);
				}
			}
		}
		
	}
}
