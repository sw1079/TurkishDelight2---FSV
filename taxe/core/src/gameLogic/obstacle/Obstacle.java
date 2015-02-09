package gameLogic.obstacle;

import com.badlogic.gdx.utils.Disposable;

import fvs.taxe.actor.ObstacleActor;
import gameLogic.map.IPositionable;
import gameLogic.map.Station;

public class Obstacle implements Disposable {
	private Station station;						// station the obstacle is connected to
	private ObstacleType type;						
	private Boolean active;							// whether the obstacle is currently active
	private int time;								// number of turns obstacle has left
	private IPositionable position;					// position of obstacle
	private ObstacleActor actor;					// corresponding actor for obstacle, for displaying it
	
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
	
	public float getDestructionChance() {
		switch(this.type){
		case BLIZZARD:
			return 1f;
		case EARTHQUAKE:
			return 1f;
		case FLOOD:
			return 1f;
		case VOLCANO:
			return 1f;
		default:
			return 0f;
		}
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
		// get the duration of the obstacle, based upon the type
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
	
	public IPositionable getPosition() {
		return this.position;
	}
	
	public void setActor(ObstacleActor actor){
		this.actor = actor;
	}
	
	public ObstacleActor getActor(){
		return this.actor;
	}
	
	@Override
	public void dispose() {
		// TODO
		
	}

	

	

}
