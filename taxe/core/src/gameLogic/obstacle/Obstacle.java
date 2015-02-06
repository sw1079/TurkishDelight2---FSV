package gameLogic.obstacle;

import com.badlogic.gdx.utils.Disposable;

import gameLogic.map.IPositionable;
import gameLogic.map.Station;

public class Obstacle implements Disposable {
	// name? 
	// image of obstacle - need ObstacleActor?
	private IPositionable position;
	private Station station;
	private ObstacleType type;
	private Boolean active;
	private int time;
	
	public Obstacle(ObstacleType type, Station station) {
		initialise(type, station);
	}

	private void initialise(ObstacleType type, Station station) {
		// set up attributes
		this.type = type;
		this.station = station;
		this.position = station.getLocation();
		this.active = false;
	}
	
	public Station getStation() {
		return this.station;
	}
	
	public ObstacleType getType() {
		return this.type;
	}
	
	public boolean isActive(){
		return this.active;
	}
	
	public void start() {
		// start the obstacle
		this.active = true;
		this.time = getDuration();
	}

	public void end() {
		// end the obstacle
		this.active = false;
	}

	public boolean decreaseTimeLeft() {
		// returns true if time left, false if no time left
		if (time > 0){
			this.time -= 1;
			return true;
		} else {
			return false;
		}
	}
	
	private int getDuration() {
		if (type == ObstacleType.BLIZZARD){
			return 4;
		} else if (type == ObstacleType.EARTHQUAKE) {
			return 5;
		} else if (type == ObstacleType.FLOOD) {
			return 3;
		} else if (type == ObstacleType.VOLCANO) {
			return 2;
		} else {
			return -1; // invalid obstacle type!
		}
	}
	
	@Override
	public void dispose() {
		// TODO
	}

	

}
