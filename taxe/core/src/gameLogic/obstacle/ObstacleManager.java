package gameLogic.obstacle;

import fvs.taxe.controller.Context;
import gameLogic.Game;
import gameLogic.map.Map;
import gameLogic.map.Station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;

import Util.Tuple;

public class ObstacleManager {

	private ArrayList<Tuple<Obstacle,Float>> obstacles; 				// probability of obstacle occurring
	private HashMap<String,Station> stations;				
	
	public ObstacleManager(Map map) {
		initialise(map);
	}

	private void initialise(Map map) {
		// set up an array that contains all of the obstacles
		// parse json file in future?
		
		this.stations = new HashMap<String, Station>();
		List<Station> mapStations = map.getStations();
		for (Station station: mapStations){
			this.stations.put(station.getName(), station);
		}
		
		// temporary creation of obstacles - will be a json file eventually (hopefully!)
		obstacles = new ArrayList<Tuple<Obstacle,Float>>();
		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.BLIZZARD, stations.get("Rome")), 1f));
		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.BLIZZARD, stations.get("London")), 1f));
		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.BLIZZARD, stations.get("Berlin")), 1f));
		
		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.FLOOD, stations.get("Vienna")), 1f));
		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.FLOOD, stations.get("Sofia")), 1f));
		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.FLOOD, stations.get("London")), 1f));

		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.EARTHQUAKE, stations.get("Kiev")), 1f));
		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.EARTHQUAKE, stations.get("York")), 1f));
		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.EARTHQUAKE, stations.get("Monaco")), 1f));

		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.VOLCANO, stations.get("Madrid")), 1f));
		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.VOLCANO, stations.get("Frankfurt")), 1f));
		obstacles.add(new Tuple<Obstacle,Float>(new Obstacle(ObstacleType.VOLCANO, stations.get("Geneva")), 1f));
		
		
	}

	public ArrayList<Tuple<Obstacle, Float>> getObstacles() {
		return this.obstacles;
	}

}
