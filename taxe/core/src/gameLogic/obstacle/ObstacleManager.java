package gameLogic.obstacle;

import gameLogic.map.Map;
import gameLogic.map.Station;

import java.util.ArrayList;

import Util.Tuple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class ObstacleManager {

	private static final float DEFAULT_OBSTACLE_PROBABILITY = 0.001f;
	private ArrayList<Tuple<Obstacle,Float>> obstacles; 				// obstacle and the probability of obstacle occurring		
	private Map map;
	
	public ObstacleManager(Map map) {
		initialise(map);
	}

	private void initialise(Map map) {
		// parse all of the obstacles from obstacles.json
		this.map = map;

		JsonReader jsonReader = new JsonReader();
		JsonValue jsonVal = jsonReader.parse(Gdx.files.local("obstacles.json"));

		obstacles = new ArrayList<Tuple<Obstacle, Float>>();
		for(JsonValue jObstacle = jsonVal.getChild("obstacles"); jObstacle != null; jObstacle = jObstacle.next()) {
			String typeName = "";
			String stationName = "";
			float probability = DEFAULT_OBSTACLE_PROBABILITY;
			for(JsonValue val  = jObstacle.child; val != null; val = val.next()) {
				if(val.name.equalsIgnoreCase("type")) {
					typeName = val.asString();
				} else if (val.name.equalsIgnoreCase("station")) {
					stationName = val.asString();
				} else {
					probability = val.asFloat();
				}
			}
			Obstacle obstacle = createObstacle(typeName, stationName);
			if (obstacle != null){
				obstacles.add(new Tuple<Obstacle, Float>(obstacle, probability));
			}
		}
	}

	private Obstacle createObstacle(String typeName, String stationName) {
		// create the obstacle from the strings given in json file
		ObstacleType type = null;
		Station station = null;
		if (typeName.equalsIgnoreCase("volcano")){
			type = ObstacleType.VOLCANO;
		} else if (typeName.equalsIgnoreCase("blizzard")) {
			type = ObstacleType.BLIZZARD;
		} else if (typeName.equalsIgnoreCase("flood")) {
			type = ObstacleType.FLOOD;
		} else if (typeName.equalsIgnoreCase("earthquake")) {
			type = ObstacleType.EARTHQUAKE;
		} 
		
		System.out.println(type + " , " + typeName);
		
		station = map.getStationByName(stationName);
		
		if (type != null && station != null){
			return new Obstacle(type, station);
		} else {
			return null;
		}
	}

	public ArrayList<Tuple<Obstacle, Float>> getObstacles() {
		return this.obstacles;
	}

}
