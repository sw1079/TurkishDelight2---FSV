package fvs.taxe.dialog;

import gameLogic.goal.Goal;
import gameLogic.map.Station;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GoalClickedListener extends ClickListener {

	private Goal goal;

	public GoalClickedListener(Goal goal) {
		this.goal = goal;
	}

	@Override
	public void clicked(InputEvent event, float x, float y) {
		System.out.println(goal);
		final Station origin = goal.getOrigin();
		final Station dest = goal.getDestination();
	
		origin.getActor().selected();
		dest.getActor().selected();
	}

}
