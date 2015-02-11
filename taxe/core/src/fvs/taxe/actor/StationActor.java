package fvs.taxe.actor;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import gameLogic.map.IPositionable;

public class StationActor extends Image {
    private static int width = 20;
    private static int height = 20;

    public StationActor(IPositionable location) {
        super(new Texture(Gdx.files.internal("station_dot.png")));
        
        setSize(width, height);
        setPosition(location.getX() - width / 2, location.getY() - height / 2);
    }

	public void selected() {
		setSize(width+10, height +10);
		setColor(Color.YELLOW);
		
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		    	setSize(width, height);
				setColor(Color.WHITE);
		    }
		}, 2);
	}
    
    
}
