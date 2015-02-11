package fvs.taxe.dialog;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import fvs.taxe.controller.Context;
import gameLogic.goal.Goal;
import gameLogic.map.Station;

public class GoalClickedListener extends ClickListener {

	private Goal goal;
	private Context context;

	public GoalClickedListener(Context context, Goal goal) {
		this.goal = goal;
		this.context = context;
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
