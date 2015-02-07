package fvs.taxe.controller;

import java.util.ArrayList;

import Util.Tuple;

import com.badlogic.gdx.scenes.scene2d.Group;

import fvs.taxe.actor.ObstacleActor;
import gameLogic.map.Map;
import gameLogic.obstacle.Obstacle;
import gameLogic.obstacle.ObstacleListener;
import gameLogic.obstacle.ObstacleManager;

public class ObstacleController {

	private Context context;
	
	public ObstacleController(Context context) {
		// take care of rendering of stations (only rendered on map creation)
		this.context = context;
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
	
	public void drawObstacles(){
		// needs to only be called once, on map creation
		// adds all obstacles to the stage but makes them invisible
		ArrayList<Tuple<Obstacle, Float>> obstaclePairs = context.getGameLogic().getObstacleManager().getObstacles();
		for (Tuple<Obstacle, Float> obstaclePair: obstaclePairs) {
			renderObstacle(obstaclePair.getFirst(), false);
		}
	}

	private ObstacleActor renderObstacle(Obstacle obstacle, boolean visible) {
		ObstacleActor obstacleActor = new ObstacleActor(obstacle);
		obstacleActor.setVisible(visible);
		obstacle.setActor(obstacleActor);
		context.getStage().addActor(obstacleActor);
		return obstacleActor;
	}
	
}
