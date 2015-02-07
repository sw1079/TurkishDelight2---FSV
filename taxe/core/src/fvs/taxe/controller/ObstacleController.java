package fvs.taxe.controller;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Group;

import fvs.taxe.actor.ObstacleActor;
import gameLogic.map.Map;
import gameLogic.obstacle.Obstacle;
import gameLogic.obstacle.ObstacleListener;
import gameLogic.obstacle.ObstacleManager;

public class ObstacleController {

	private Context context;
	private ArrayList<ObstacleActor> activeObstacleActors;
	private Group obstacleActors = new Group();
	
	public ObstacleController(Context context) {
		
		// needs to have rendering stuff
		activeObstacleActors = new ArrayList<ObstacleActor>();
		// takes care of the logic when an obstacle has occurred
		this.context = context;
		context.getGameLogic().subscribeObstacleChanged(new ObstacleListener() {
			
			@Override
			public void started(Obstacle obstacle) {
				System.out.println("Obstacle has started of type " + obstacle.getType() + " at " + obstacle.getStation().getName());
				obstacle.start();
				obstacle.getStation().setObstacle(obstacle); // maybe move to station controller?
				ObstacleActor obstacleActor = new ObstacleActor(obstacle);
				obstacle.setActor(obstacleActor);
				activeObstacleActors.add(obstacleActor);
			}
			
			@Override
			public void ended(Obstacle obstacle) {
				System.out.println("Obstacle has ended of type " + obstacle.getType());
				obstacle.getStation().clearObstacle();
				obstacle.end();
				activeObstacleActors.remove(obstacle);
			}
		});
	}
	
	public ArrayList<ObstacleActor> getActiveObstacleActors(){
		return this.activeObstacleActors;
	}
	
	public void drawObstacles(){
		obstacleActors.remove();
		obstacleActors.clear();
		
		for (ObstacleActor obstacle : activeObstacleActors){
			obstacleActors.addActor(obstacle);
		}
		
		context.getStage().addActor(obstacleActors);
	}
	
}
