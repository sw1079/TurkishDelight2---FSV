package fvs.taxe.dialog;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fvs.taxe.controller.Context;
import gameLogic.GameState;
import gameLogic.map.Station;
import gameLogic.resource.Resource;
import gameLogic.resource.Train;

public class DialogStationMultitrain extends Dialog {

	private Context context;
	private boolean isTrain = false;

	public DialogStationMultitrain(Station station, Skin skin, Context context) {
		super(station.getName(), skin);

		this.context = context;

		System.out.println("hello");
		
		text("Choose which train you would like");

		//int i = 0;
		//Train train = null;
		
		List<Resource> activeTrains = context.getGameLogic().getPlayerManager().getCurrentPlayer().getActiveTrains();
		List<Train> localTrains = new ArrayList<Train>();
		for (Resource resource : activeTrains) {
			if(((Train) resource).getPosition() == station.getLocation()) {
				localTrains.add((Train) resource);
			}
		}
		
		System.out.println(localTrains);
		if (localTrains.size() == 0) {
			hide();
			context.getTopBarController().displayFlashMessage("No Player " + context.getGameLogic().getPlayerManager().getCurrentPlayer().getPlayerNumber() + " trains at this station", Color.RED);
		} else if (localTrains.size() == 1) {
			hide();
			result(localTrains.get(0));
			context.getGameLogic().setState(GameState.WAITING);
		} else {
			for (Resource resource : localTrains){
				String destination = "";
				if(((Train) resource).getFinalDestination() != null) {
					destination = " to " + ((Train) resource).getFinalDestination().getName();
				}
				button(((Train) resource).getName() + destination + " (Player " + ((Train) resource).getPlayer().getPlayerNumber() + ")", ((Train) resource));
				getButtonTable().row();
				isTrain = true;
			}
			context.getGameLogic().setState(GameState.WAITING);
		}

		button("Cancel","CANCEL");
	}

	@Override
	public Dialog show(Stage stage) {
		show(stage, null);
		setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
		return this;
	}

	@Override
	public void hide() {
		hide(null);
	}

	@Override
	protected void result(Object obj) {
		context.getGameLogic().setState(GameState.NORMAL);
		if(obj == "CANCEL"){
			this.remove();
		} else {
			//Simulate click on train
			TrainClicked clicker = new TrainClicked(context, (Train) obj);
			clicker.clicked(null, 0, 0);
		}
		
	}

	public boolean getIsTrain() {
		return isTrain;
	}
}
