package fvs.taxe.controller;

import java.util.ArrayList;

import Util.Tuple;

import com.badlogic.gdx.math.MathUtils;

import gameLogic.Game;
import gameLogic.TurnListener;
import gameLogic.map.Map;
import gameLogic.obstacle.Obstacle;
import gameLogic.obstacle.ObstacleListener;
import gameLogic.obstacle.ObstacleManager;

public class ObstacleController {

	Context context;
	ObstacleManager obstacleManager;
	Map map;
	
	public ObstacleController(Context context) {
		// takes care of the logic when an obstacle has occurred
		this.context = context;
		this.map = context.getGameLogic().getMap();
		context.getGameLogic().subscribeObstacleChanged(new ObstacleListener() {
			
			@Override
			public void started(Obstacle obstacle) {
				System.out.println("Obstacle has started of type " + obstacle.getType() + " at " + obstacle.getStation().getName());
				obstacle.start();
				obstacle.getStation().setObstacle(obstacle); // maybe move to station controller?
			}
			
			@Override
			public void ended(Obstacle obstacle) {
				System.out.println("Obstacle has ended of type " + obstacle.getType());
				obstacle.getStation().clearObstacle();
				obstacle.end();
			}
		});
	}
	
	
}
