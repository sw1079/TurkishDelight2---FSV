package fvs.taxe.actor;

import gameLogic.map.IPositionable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ConnectionActor extends Image{

	private float connectionWidth;
	private ShapeRenderer shapeRenderer;
	private Color color;
	private IPositionable start;
	private IPositionable end;

	public ConnectionActor(Color color, IPositionable start, IPositionable end, float connectionWidth)  {
		shapeRenderer = new ShapeRenderer();
		this.color = color;
		this.start = start;
		this.end = end;
		this.connectionWidth = connectionWidth;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rectLine(start.getX(), start.getY(), end.getX(), end.getY(), connectionWidth);
        shapeRenderer.end();
        batch.begin();
	}

	public void setConnectionColor(Color color) {
		this.color = color;
	}
	
	public Color getConnectionColor(){
		return this.color;
	}

}
