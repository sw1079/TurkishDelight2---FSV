package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fvs.taxe.TaxeGame;
import gameLogic.GameState;
import gameLogic.GameStateListener;
import gameLogic.map.Station;
import gameLogic.obstacle.Obstacle;
import gameLogic.obstacle.ObstacleListener;
import gameLogic.obstacle.ObstacleType;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;


public class TopBarController {
    public final static int CONTROLS_HEIGHT = 40;

    private Context context;
    private Color controlsColor = Color.LIGHT_GRAY;
    private TextButton endTurnButton;
    private Label flashMessage;
    private Label obstacleLabel;

	private Color obstacleColor = Color.LIGHT_GRAY;
    
    public TopBarController(final Context context) {
        this.context = context;
        
        context.getGameLogic().subscribeObstacleChanged(new ObstacleListener(){

			@Override
			public void started(Obstacle obstacle) {
				ObstacleType type = obstacle.getType();						     
				Color color = null;
				switch(type){
				case BLIZZARD:
					color = Color.WHITE;
					break;
				case FLOOD:
					color = Color.valueOf("1079c1");
					break;
				case VOLCANO:
					color = Color.valueOf("ec182c");
					break;
				case EARTHQUAKE:
					color = Color.valueOf("7a370a");
					break;
				}				
				displayObstacleMessage(obstacle.getType().toString() + " in " + obstacle.getStation().getName(), color);
			}

			@Override
			public void ended(Obstacle obstacle) {
				ObstacleType type = obstacle.getType();
			}		        	
        });
        createFlashActor();
        createObstacleLabel();
    }

    public void displayObstacleMessage(String message, Color color) {
    	obstacleLabel.clearActions();
		obstacleLabel.setText(message);
		obstacleLabel.setColor(Color.BLACK);
		obstacleColor = color;
		obstacleLabel.pack();
		obstacleLabel.addAction(sequence(delay(2f),fadeOut(0.25f), run(new Runnable() {
			public void run() {
				obstacleLabel.setText("");
				obstacleColor = Color.LIGHT_GRAY;
			}
		})));
	}

	private void createObstacleLabel() {
    	obstacleLabel = new Label("", context.getSkin());
    	obstacleLabel.setColor(Color.BLACK);
    	obstacleLabel.setPosition(10,TaxeGame.HEIGHT - 34);
    	context.getStage().addActor(obstacleLabel);
	}

	private void createFlashActor() {
        flashMessage = new Label("", context.getSkin());
        flashMessage.setPosition(450, TaxeGame.HEIGHT - 24);
        flashMessage.setAlignment(0);
        context.getStage().addActor(flashMessage);
    }
 
    public void displayFlashMessage(String message, Color color) {
        displayFlashMessage(message, color, 2f);
    }

    public void displayFlashMessage(String message, Color color, float time) {
        flashMessage.setText(message);
        flashMessage.setColor(color);
        flashMessage.addAction(sequence(delay(time), fadeOut(0.25f)));
    }
    
    public void displayFlashMessage(String message, Color backgroundColor, Color textColor, float time) {
    	flashMessage.clearActions();
    	obstacleColor = backgroundColor;
    	controlsColor = backgroundColor;
    	flashMessage.setText(message);
        flashMessage.setColor(textColor);
        flashMessage.addAction(sequence(delay(time), fadeOut(0.25f), run(new Runnable() {
			public void run() {
				controlsColor = Color.LIGHT_GRAY;
				System.out.println("top bar finished and word" + obstacleLabel.getText());
				if (obstacleLabel.getActions().size == 0){
					obstacleColor = Color.LIGHT_GRAY;
				}
			}
		})));
    }

    public void drawBackground() {
        TaxeGame game = context.getTaxeGame();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        game.shapeRenderer.setColor(controlsColor);
        game.shapeRenderer.rect(0, TaxeGame.HEIGHT - CONTROLS_HEIGHT, TaxeGame.WIDTH, CONTROLS_HEIGHT);
       
        game.shapeRenderer.setColor(obstacleColor);
        game.shapeRenderer.rect(0, TaxeGame.HEIGHT - CONTROLS_HEIGHT, obstacleLabel.getWidth()+20, CONTROLS_HEIGHT);
        
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(0, TaxeGame.HEIGHT - CONTROLS_HEIGHT, TaxeGame.WIDTH, 1);
        
        game.shapeRenderer.end();
    }

    public void addEndTurnButton() {
        endTurnButton = new TextButton("End Turn", context.getSkin());
        endTurnButton.setPosition(TaxeGame.WIDTH - 100.0f, TaxeGame.HEIGHT - 33.0f);
        endTurnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                context.getGameLogic().getPlayerManager().turnOver();
            }
        });

        context.getGameLogic().subscribeStateChanged(new GameStateListener() {
            @Override
            public void changed(GameState state) {
                if(state == GameState.NORMAL) {
                    endTurnButton.setVisible(true);
                } else {
                    endTurnButton.setVisible(false);
                }
            }
        });

        context.getStage().addActor(endTurnButton);
        }
    }
    

