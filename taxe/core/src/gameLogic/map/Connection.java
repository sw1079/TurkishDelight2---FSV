package gameLogic.map;

import fvs.taxe.actor.ConnectionActor;

public class Connection {
	private Station station1;
	private Station station2;
	private ConnectionActor actor;
	
	public Connection(Station station1, Station station2) {
		this.station1 = station1;
		this.station2 = station2;
	}
	
	public Station getStation1() {
		return this.station1;
	}

	public Station getStation2() {
		return this.station2;
	}
	
	public void setActor(ConnectionActor actor){
		this.actor = actor;
	}
	
	public ConnectionActor getActor(){
		return this.actor;
	}
}
