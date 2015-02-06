package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import fvs.taxe.TaxeGame;

public class ScoreController {
	private Context context;
	
	public ScoreController(Context context) {
        this.context = context;
    }
	
	public void drawScoreDetails()
	{
		TaxeGame game = context.getTaxeGame();
		
		String player1String = "Player " + context.getGameLogic().getPlayerManager().getCurrentPlayer().getPlayerNumber() + ": " + context.getGameLogic().getPlayerManager().getCurrentPlayer().getScore();

        game.batch.begin();
        game.fontSmall.setColor(Color.BLACK);
        game.fontSmall.draw(game.batch, "Scores:", 10.0f, (float) TaxeGame.HEIGHT - 450.0f);
        game.fontSmall.draw(game.batch, player1String, 10.0f, (float) TaxeGame.HEIGHT - 475.0f);
        game.batch.end();
	}

}
