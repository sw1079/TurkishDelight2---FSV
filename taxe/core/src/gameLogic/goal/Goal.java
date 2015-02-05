package gameLogic.goal;

import Util.Tuple;
import gameLogic.map.Station;
import gameLogic.resource.Train;
import fvs.taxe.TaxeGame;

public class Goal {
	private int rewardScore;
	private Station origin;
	private Station destination;
	private int turnIssued;
	private boolean complete = false;
	//constraints
	private String trainName = null;
	
	public Goal(Station origin, Station destination, int turn) {
		this.origin = origin;
		this.destination = destination;
		this.turnIssued = turn;
		setRewardScore();
	}
	
	public void addConstraint(String name, String value) {
		if(name.equals("train")) {
			trainName = value;
		} else {
			throw new RuntimeException(name + " is not a valid goal constraint");
		}
	}
	
	private void setRewardScore() {
		int distX, distY;
		if (origin.getLocation().getX() < destination.getLocation().getX()){
			distX = destination.getLocation().getX() - origin.getLocation().getX();
		} else {
			distX = origin.getLocation().getX() - destination.getLocation().getX();
		}
		if (origin.getLocation().getY() < destination.getLocation().getY()){
			distY = destination.getLocation().getY() - origin.getLocation().getY();
		} else {
			distY = origin.getLocation().getY() - destination.getLocation().getY();
		}
		
		double dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY,2));
		double maxDist = Math.sqrt(Math.pow(TaxeGame.HEIGHT,2) + Math.pow(TaxeGame.WIDTH,2));
		rewardScore = Math.round((float) (30 * (dist/maxDist)));
	}

	public int getRewardScore() {
		return rewardScore;
	}

	public boolean isComplete(Train train) {
		boolean passedOrigin = false;
		for(Tuple<String, Integer> history: train.getHistory()) {
			if(history.getFirst().equals(origin.getName()) && history.getSecond() >= turnIssued) {
				passedOrigin = true;
			}
		}
		if(train.getFinalDestination() == destination && passedOrigin) {
			if(trainName == null || trainName.equals(train.getName())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public String toString() {
		String trainString = "train";
		if(trainName != null) {
			trainString = trainName;
		}
		return "Send a " + trainString + " from " + origin.getName() + " to " + destination.getName() + " - " + rewardScore + " points";
	}

	public void setComplete() {
		complete = true;
	}

	public boolean getComplete() {
		return complete;
	}
}