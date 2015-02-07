package fvs.taxe;

import Util.Tuple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fvs.taxe.controller.*;
import fvs.taxe.dialog.DialogEndGame;
import gameLogic.Game;
import gameLogic.GameState;
import gameLogic.GameStateListener;
import gameLogic.TurnListener;
import gameLogic.map.Map;
import gameLogic.obstacle.Obstacle;
import gameLogic.obstacle.ObstacleListener;
import gameLogic.obstacle.ObstacleType;
import gameLogic.obstacle.Rumble;


public class GameScreen extends ScreenAdapter {
    final private TaxeGame game;
    private Stage stage;
    private Texture mapTexture;
    private Game gameLogic;
    private Skin skin;
    private Map map;
    private float timeAnimated = 0;
    public static final int ANIMATION_TIME = 2;
    private Tooltip tooltip;
    private Context context;

    private StationController stationController;
    private TopBarController topBarController;
    private ResourceController resourceController;
    private GoalController goalController;
    private RouteController routeController;
	private ObstacleController obstacleController;
    private ScoreController scoreController;
    
	private Rumble rumble;

    public GameScreen(TaxeGame game) {
        this.game = game;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        gameLogic = Game.getInstance();
        context = new Context(stage, skin, game, gameLogic);
        Gdx.input.setInputProcessor(stage);

        mapTexture = new Texture(Gdx.files.internal("gamemap.png"));
        map = gameLogic.getMap();

        tooltip = new Tooltip(skin);
        stage.addActor(tooltip);

        stationController = new StationController(context, tooltip);
        topBarController = new TopBarController(context);
        resourceController = new ResourceController(context);
        goalController = new GoalController(context);
        routeController = new RouteController(context);
        obstacleController = new ObstacleController(context);
        scoreController = new ScoreController(context);

        context.setRouteController(routeController);
        context.setTopBarController(topBarController);
        
        rumble = new Rumble();
        
        gameLogic.getPlayerManager().subscribeTurnChanged(new TurnListener() {
            @Override
            public void changed() {
                gameLogic.setState(GameState.ANIMATING);
                topBarController.displayFlashMessage("Time is passing...", Color.BLACK);
            }
        });
        gameLogic.subscribeStateChanged(new GameStateListener() {
        	@Override
        	public void changed(GameState state){
        		if(gameLogic.getPlayerManager().getTurnNumber() == gameLogic.TOTAL_TURNS && state == GameState.NORMAL) {
        			DialogEndGame dia = new DialogEndGame(GameScreen.this.game, gameLogic.getPlayerManager(), skin);
        			dia.show(stage);
        		}
        	}
        });
        
        gameLogic.subscribeObstacleChanged(new ObstacleListener() {
			

			@Override
			public void started(Obstacle obstacle) {
				// draw the obstacles, redraw the stations and connections to ensure they overlap correctly
				obstacleController.drawObstacles();
		        stationController.renderConnections(map.getConnections(), Color.GRAY);
		        stationController.renderStations();
				if (obstacle.getType() == ObstacleType.EARTHQUAKE) {
					rumble = new Rumble();
					rumble.rumble(context, 1f, 2f);
				}
			}
			
			@Override
			public void ended(Obstacle obstacle) {
				
			}
		});
    }


    // called every frame
    @Override
    public void render(float delta) {
 
    	
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        
        if (rumble.time > 0){
        	Vector2 mapPosition = rumble.tick(delta);
        	game.batch.begin();
            game.batch.draw(mapTexture, mapPosition.x, mapPosition.y);
            game.batch.end();
        } else {
        	game.batch.begin();
            game.batch.draw(mapTexture, 0, 0);
            game.batch.end();
        }
        

        topBarController.drawBackground();
        
        if(gameLogic.getState() == GameState.ROUTING) {
            routeController.drawRoute(Color.BLACK);
        }

        if(gameLogic.getState() == GameState.ANIMATING) {
            timeAnimated += delta;
            if (timeAnimated >= ANIMATION_TIME) {
                gameLogic.setState(GameState.NORMAL);
                timeAnimated = 0;
            }
        }
        
        if(gameLogic.getState() == GameState.NORMAL || gameLogic.getState() == GameState.PLACING){
        	stationController.displayNumberOfTrainsAtStations();
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        
        game.batch.begin();
        game.fontSmall.draw(game.batch, "Turn " + (gameLogic.getPlayerManager().getTurnNumber() + 1) + "/" + gameLogic.TOTAL_TURNS, (float) TaxeGame.WIDTH - 90.0f, 20.0f);
        game.batch.end();

        resourceController.drawHeaderText();
        goalController.showCurrentPlayerGoals();
        scoreController.drawScoreDetails();
        
        
    }

    @Override
    // Called when GameScreen becomes current screen of the game
    public void show() {
    	stationController.renderConnections(map.getConnections(), Color.GRAY);
        stationController.renderStations();
        topBarController.addEndTurnButton();
        resourceController.drawPlayerResources(gameLogic.getPlayerManager().getCurrentPlayer());
    }

    
    @Override
    public void dispose() {
        mapTexture.dispose();
        stage.dispose();
    }

}