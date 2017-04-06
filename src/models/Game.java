package models;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import models.PuzzleBlock;
import observers.GameObserver;
import observers.ScoreObserver;

public class Game {

	private ScoreObserver scoreObserver;
	private GameObserver gameObserver;
	private int score;
	private int highScore;
	private Grid grid;
	private Dock dock;
	private Color color;
	
	public Game() {		
		grid = new Grid(this);	
		dock = new Dock(this);
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public Dock getDock() {
		return dock;
	}
	
	public void setScoreObserver(ScoreObserver scoreObserver) {
		this.scoreObserver = scoreObserver;
	}
	
	public void setGameObserver(GameObserver gameObserver) {
		this.gameObserver = gameObserver;
		dock.setGameObserver(gameObserver);
		grid.setGameObserver(gameObserver);
	}
	
	public void setScore(int score) {
		this.score = score;
		scoreObserver.scoreChanged(score);
	}
	
	public void setHighScore(int highscore) {
		this.highScore = highscore;
		scoreObserver.highScoreChanged(highscore);
	}	
	
	public int getHighScore() {
		return highScore;
	}
	
	public void addScore(int score) {
		int newscore = this.score + score;
		setScore(newscore);
	}
	
	public void puzzleBlockDropped(PuzzleBlock selectedPuzzleBlock, int selectedPuzzleBlockId, int x, int y) {
		
		boolean notFree = grid.puzzleBlockDropped(selectedPuzzleBlock, x, y);
		if (!notFree) {
			dock.onPuzzleBlockPlaced(selectedPuzzleBlockId);
		}
		boolean enoughPlace = grid.checkPlaces(dock.getPuzzleBlocks());
		// Verloren
		if (!enoughPlace) {
			gameObserver.gameOver(false);
		}
	}
	
	private int toInteger(String str) {
		int integer = -1;
		
		try {
			integer = Integer.parseInt(str);
		}
		catch(NumberFormatException e) {
			return -1;
		}
		return integer;
	}
	
	// Laden van een game doormiddel van een tekst file
	public boolean loadGame() {
		String[] lines = new String[13];
		
		boolean wrongGrid = false;
		
		try {
			// laad tekst file in
			BufferedReader br = new BufferedReader(new FileReader("resources/savegame.txt"));
			
			// Voeg alle regels toe aan array
			int i = 0;
			String line;
			while((line = br.readLine()) != null) {
				line = line.replace("\n", "").trim();
				
				// Checken of elke rij in de savegame bestaat uit 0-9 en A-S
				if (!line.matches("[0-9A-S. ]+") || line.equals(" ")) {
					wrongGrid = true;
					break;
				}
				lines[i] = line;
				i++;
				
				if (i > lines.length - 1) {
					break;
				}
			}
			
			br.close();
		} catch (IOException e) {
			wrongGrid = true;
		}
		
		// Zet savegame inhoud in nieuwe string array
		String[] gridLines = Arrays.copyOfRange(lines, 0, 10);
		
		boolean noBlocks = false;
		int[] blocks = new int[3];
		boolean noScores = false;
		int score = -1;
		int highscore = -1;
		
		if (!wrongGrid) {
			String[] savegameBlocks = lines[10].split(" ");
	
			for (int i = 0; i < savegameBlocks.length; i++) {
				
				int a = -1;
				try {
					a = Integer.parseInt(savegameBlocks[i]);
				}
				catch (Exception e) {
					noBlocks = true;
				}
				
				if (a > 0) {
					blocks[i] = a;
				}
			}
			
			//Scores laden vanuit savegame
			if (!noBlocks) {				
				score = toInteger(lines[11].trim());
				highscore = toInteger(lines[12].trim());
				
				// Checken of er een score aanwezig is
				if (score < 0 || highscore < 0) {
					noScores = true;
				}
			}
		}
		
		// Checken op savegame
		if (noBlocks || wrongGrid || noScores) {
			System.out.println("Savegame niet gevonden.. Een nieuw spel start nu!");
			dock.generatePuzzleBlocks();
			grid.generate();
		}
		else {
			dock.loadPuzzleBlocks(blocks);
			grid.load(gridLines);
			setScore(score);
			setHighScore(highscore);
		}
		return true;
	}
	
	// Opslaan van een game bij het afsluiten van het spel
	public void onExitGame() {
		String str = grid.toString();
		str += dock.toString();
		str += score + "\n" + highScore;
		// Kijken of tekst file kan worden ingeladen
		try {
			FileWriter fw = new FileWriter("resources/savegame.txt"); 
			fw.write(str);
			fw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	// Surrenderen van een game
	public void onSurrenderButtonClicked() {
		if (score > highScore) {
			setHighScore(score);
		}
		gameObserver.gameOver(true);		
	}
	
	// Genereren van nieuwe blokjes in het dock en score resetten (EXTRA 1)
	public void onCheatButtonClicked() {
		setScore(0);
		dock.generatePuzzleBlocks();
	}
	
	// Cancel het surrenderen
	public void onCancelGameOverButtonClicked() {
		gameObserver.cancel();
		scoreObserver.gameOver();
	}
	
	// Start een nieuwe game
	public void onResetButtonClicked() {
		dock.generatePuzzleBlocks();
		grid.generate();
		setScore(0);
		gameObserver.reset();		
		scoreObserver.gameOver();
	}
}
