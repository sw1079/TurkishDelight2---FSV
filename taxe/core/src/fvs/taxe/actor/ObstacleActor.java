package fvs.taxe.actor;

import gameLogic.map.IPositionable;
import gameLogic.obstacle.Obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

public class ObstacleActor extends Image {

 //   private Rectangle bounds;

	private static int width = 50;
    private static int height = 50;
    // TODO set size correctly (based upon disaster?
	public ObstacleActor(Obstacle obstacle) {
		super(getImage(obstacle));
		
		obstacle.setActor(this);
		setSize(width, height);
		IPositionable position = obstacle.getPosition();
		setPosition(position.getX() - (width/2), position.getY() - (height/2));
	}

	private static Texture getImage(Obstacle obstacle) {
		switch(obstacle.getType()){
		case VOLCANO:
			return new Texture(Gdx.files.internal("volcano.png"));
		case BLIZZARD:
			return new Texture(Gdx.files.internal("blizzard.png"));
		case FLOOD:
			return new Texture(Gdx.files.internal("flood.png"));
		case EARTHQUAKE:
			return new Texture(Gdx.files.internal("earthquake.png"));
		default:
			return null;
		}
	}
}
