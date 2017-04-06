package controllers;

import models.Game;
import views.GameFrame;

public class GameController {
	
	private Game game;
	
	public GameController() {
		game = new Game();
		
		new GameFrame(game);
		
		game.loadGame();
	}
	
}
