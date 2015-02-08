package gameLogic.map;

import fvs.taxe.actor.CollisionStationActor;

public class CollisionStation extends Station {

	private CollisionStationActor actor;
	public CollisionStation(String name, IPositionable location) {
		super(name, location);
	}
	
	public void setActor(CollisionStationActor actor){
		this.actor = actor;
	}
	
}
