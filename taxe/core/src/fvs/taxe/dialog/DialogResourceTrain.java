package fvs.taxe.dialog;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fvs.taxe.Button;
import fvs.taxe.controller.Context;
import gameLogic.GameState;
import gameLogic.resource.Train;

public class DialogResourceTrain extends Dialog {
    private List<ResourceDialogClickListener> clickListeners = new ArrayList<ResourceDialogClickListener>();
	private Context context;

    public DialogResourceTrain(Context context, Train train, Skin skin, boolean trainPlaced) {
        super(train.toString(), skin);
        this.context = context;
        context.getGameLogic().setState(GameState.WAITING);
        text("What do you want to do with this train?");

        button("Cancel", "CLOSE");
        button("Delete", "DELETE");

        if (!trainPlaced) {
            button("Place at a station", "PLACE");
        } else if(!train.isMoving()) {
            button("Choose a route", "ROUTE");
        }
    }

    @Override
    public Dialog show (Stage stage) {
        show(stage, null);
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
        return this;
    }

    @Override
    public void hide () {
        hide(null);
    }

    private void clicked(Button button) {
        for(ResourceDialogClickListener listener : clickListeners) {
            listener.clicked(button);
        }
    }

    public void subscribeClick(ResourceDialogClickListener listener) {
        clickListeners.add(listener);
    }

    @Override
    protected void result(Object obj) {
    	context.getGameLogic().setState(GameState.NORMAL);
        if (obj == "CLOSE") {
            this.remove();
        } else if (obj == "DELETE") {
            clicked(Button.TRAIN_DROP);
        } else if(obj == "PLACE") {
            clicked(Button.TRAIN_PLACE);
        } else if(obj == "ROUTE") {
            clicked(Button.TRAIN_ROUTE);
        }
        
    }
}
