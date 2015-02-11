package gameLogic;

import gameLogic.goal.Goal;
import gameLogic.goal.GoalManager;
import gameLogic.resource.Resource;
import gameLogic.resource.Train;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private PlayerManager pm;
    private List<Resource> resources;
    private List<Goal> goals;
    private Goal easyGoal;
    private Goal mediumGoal;
    private Goal hardGoal;
    private int score;
    private int number;

    public Player(PlayerManager pm, int playerNumber) {
        goals = new ArrayList<Goal>();
        resources = new ArrayList<Resource>();
        this.pm = pm;
        number = playerNumber;
    }
    
    public int getScore() {
    	return score;
    }

    public void addScore(int score) {
    	this.score += score;
    }

    public List<Resource> getResources() {
        return resources;
    }
    
    public List<Resource> getActiveTrains() {
    	List<Resource> activeResources = new ArrayList<Resource>();
    	for (Resource resource: resources) {
    		if (resource instanceof Train) {
    			if(((Train) resource).getPosition() != null) {
    				activeResources.add(resource);
    			}
    		}
    	}
    	return activeResources;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
        changed();
    }

    public void removeResource(Resource resource) {
        resources.remove(resource);
        resource.dispose();
        changed();
    }

    public void addGoal(Goal goal) {
    	int incompleteGoals = 0;
    	for(Goal existingGoal : goals) {
    		if(!existingGoal.getComplete()) {
    			incompleteGoals++;
    		}
    	}
        if (incompleteGoals >= GoalManager.CONFIG_MAX_PLAYER_GOALS) {
            //throw new RuntimeException("Max player goals exceeded");
        	return;
        }

        goals.add(goal);
        changed();
    }
    
    private void updateEasyTierGoal(GoalManager sender)
    {
    	if(easyGoal != null)
    	{
    		if(!easyGoal.getComplete())
    		{
    			//The current Easy Goal is not complete, so bail out of themethod
    			return;
    		}
    	}
    	//Generate a new Easy goal, varying the number of extra criteria
    	easyGoal = sender.generateRandomGoal(getPlayerManager().getTurnNumber(), 0);
    	addGoal(easyGoal);
    }
    
    private void updateMediumTierGoal(GoalManager sender)
    {
    	if(mediumGoal != null)
    	{
    		if(!mediumGoal.getComplete())
    		{
    			//The current Medium Goal is not complete, so bail out of themethod
    			return;
    		}
    	}
    	//Generate a new Medium Goal
    	mediumGoal = sender.generateRandomGoal(getPlayerManager().getTurnNumber(), 1);
    	addGoal(mediumGoal);
    }
    
    private void updateHardTierGoal(GoalManager sender)
    {
    	if(hardGoal != null)
    	{
    		if(!hardGoal.getComplete())
    		{
    			//The current Hard Goal is not complete, so bail out of themethod
    			return;
    		}
    	}
    	//Generate a new Hard Goal
    	hardGoal = sender.generateRandomGoal(getPlayerManager().getTurnNumber(), 2);
    	addGoal(hardGoal);
    }
    
    public void updateGoals(GoalManager sender)
    {
    	for(Goal goal : goals)
    	{
    		if(goal.isFailed())
    		{
    			goal.setComplete();
    		}
    	}
    	updateEasyTierGoal(sender);
    	updateMediumTierGoal(sender);
    	updateHardTierGoal(sender);
    }
    
    public void completeGoal(Goal goal) {
    	addScore(goal.getRewardScore());
    	goal.setComplete();
        changed();
    }

    /**
     * Method is called whenever a property of this player changes, or one of the player's resources changes
     */
    public void changed() {
        pm.playerChanged();
        
    }

    public List<Goal> getGoals() {
        return goals;
    }
    
    public PlayerManager getPlayerManager() {
    	return pm;
    }
    
    public int getPlayerNumber() {
    	return number;
    }
    
    
}
