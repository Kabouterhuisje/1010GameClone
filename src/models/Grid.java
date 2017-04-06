package models;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import models.PuzzleBlock;
import observers.GameObserver;

public class Grid {
	
	private Game game;
	private GameObserver gameObserver;
	private static Clip clip;
	private PuzzleBlock[][] puzzleBlocks;
	private char[][] blocks;
	
	public Grid(Game game) {
		this.game = game;
		puzzleBlocks = new PuzzleBlock[10][10];
		blocks = new char[10][10];
	}
	
	public void setGameObserver(GameObserver gameObserver) {
		this.gameObserver = gameObserver;
	}
	
	// Genereer nieuw grid
	public void generate() {		
		for (int y = 0; y < 10; y++) {			
			for (int x = 0; x < 10; x++) {
				blocks[y][x] = '.';
			}
		}
		gameObserver.loadGrid(blocks);
	}
	
	// Grid laden
	public void load(String[] gridLines) {
		
		for (int y = 0; y < 10; y++) {
			// Spaties wegfilteren per lijn met regex
			String filteredStr = gridLines[y].replaceAll("^\\s+|\\s+$", "");
			char[] lineBlocks = filteredStr.toCharArray();
			
			for (int x = 0; x < 10; x++) {
				blocks[x][y] = lineBlocks[x];
			}
		}
		gameObserver.loadGrid(blocks);
	}
	
	@Override
	public String toString() {
		String str = "";
		
		for (int row = 0; row < 10; row++) {			
			for (int column = 0; column < 10; column++) {
				str += blocks[column][row];
			}
			str += "\n";			
		}	
		return str;
	}
	
	public Game getGame() {
		return game;
	}
	
	public char[][] getPuzzleBlocks() {
		return blocks;
	}
	
	public PuzzleBlock[][] getPuzzleBlockz() {
		return puzzleBlocks;
	}
	
	private boolean isNotFree(int x, int y) {
		if (x > blocks.length - 1) { return true; }
		if (y > blocks.length - 1) { return true; }
			
		return blocks[x][y] != '.';
	}
	
	public boolean puzzleBlockDropped(PuzzleBlock puzzleBlock, int x, int y) {
		
		boolean notFree = false;
		// Loop door de grid (i = horizontaal, j = verticaal)
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				int x1 = i * 32 + i;
				int y1 = j * 32 + j;
				// Tile grootte en gap optellen
				int x2 = x1 + 32;
				int y2 = y1 + 32;
				
				if (x >= x1 && x < x2 && y >= y1 && y < y2) {
					
					boolean[][] blocks = puzzleBlock.getBlocks();	
					// Checken of er plaats is
					for (int a = 0; a < 5; a++) {
						for (int b = 0; b < 5; b++) {
							if (blocks[a][b]) {
								if (isNotFree(i + b, j + a)) {
									notFree = true;
									break;
								}
							}						
						}
					}
					// blokje toevoegen aan grid
					if (!notFree) {	
						for (int a = 0; a < 5; a++) {
							for (int b = 0; b < 5; b++) {
								if (blocks[a][b]) {
									this.blocks[i + b][j + a] = puzzleBlock.getLetter();
								}						
							}
						}											
					}
				}
			}
		}
		int rowsTotal = 0;

		// Checken of een horizontale rij vol is, dan verwijderen
		for (int a = 0; a < 10; a++) {
			boolean filled = isRowFilled(a);
			
			// Horizontale rij is vol
			if (filled) {
				clearRow(a);
				rowsTotal++;
			}
		}
		int columnsTotal = 0;
		// Checken of een verticale rij vol is, dan verwijderen
		for (int b = 0; b < 10; b++) {
			boolean filled = isColumnFilled(b);
			
			// Verticale rij is vol
			if (filled) {
				clearColumn(b);
				columnsTotal++;
			}
		}
		// Geluid afspelen bij verwijderen van rij en score updaten
		if (!notFree) {
			int lines = rowsTotal + columnsTotal;
			// Geluidje afspelen
			if (lines > 0) {
				playSound();
			}
			int score = puzzleBlock.getBlocksCount() + 10 * lines;
			game.addScore(score);
		}
		gameObserver.loadGrid(blocks);
		return notFree;
	}
	
	// Check voor horizontale rij
	private boolean isRowFilled(int y) {
		
		for (int x = 0; x < 10; x++) {
			if (!isNotFree(x, y)) {
				return false;
			}
		}
		return true;
	}
	
	// Horizontale rij verwijderen
	private void clearRow(int y) {
		for (int x = 0; x < 10; x++) {
			blocks[x][y] = '.';
		}
	}
	
	// Check voor verticale rij
	private boolean isColumnFilled(int x) {		
		for (int y = 0; y < 10; y++) {
			if (!isNotFree(x, y)) {
				return false;
			}
		}
		return true;
	}
	
	// Verticale rij verwijderen
	private void clearColumn(int x) {
		for (int y = 0; y < 10; y++) {
			blocks[x][y] = '.';
		}
	}
	
	// Checken of de locatie waar je een blokje wilt plaatsen bezet is
	private boolean checkIfOccupied(PuzzleBlock puzzleBlock, int x, int y) {
		
		boolean[][] blocksPuzzle = puzzleBlock.getBlocks();
		
		for (int a = 0; a < 5; a++) {
			for (int b = 0; b < 5; b++) {
				if (blocksPuzzle[a][b]) {
					// Stop wanneer het blokje niet geplaatst kan worden op x en y
					if (isNotFree(x + b, y + a)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// Checken of 1 van de blokjes uit het dock geplaatst kan worden
	public boolean checkPlaces(PuzzleBlock[] puzzleBlocks) {
		
		int checked = 0;
		int count = 0;
		
		for (int i = 0; i < puzzleBlocks.length; i++) {
			if (puzzleBlocks[i] == null) {
				continue;
			}
			
			checked++;	
			boolean foundSpot = false;
			
			for (int j = 0; j < 10; j++) {
				for (int k = 0; k < 10; k++) {
					if (!checkIfOccupied(puzzleBlocks[i], j, k)) {
						foundSpot = true;
						break;
					}
				}
				if (foundSpot) {
					break;
				}
			}
			if (!foundSpot) {
				count++;
			}
		}
		return count < checked;
	}
	
	// Geluidje bij het verwijderen van een volle rij
	private void playSound() {
		File audioFile = new File("resources/destroy.wav");		 
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);			
			AudioFormat format = audioStream.getFormat();			 
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(audioStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}