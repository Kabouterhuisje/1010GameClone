package models;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import observers.GameObserver;
import models.PuzzleBlock;

public class Dock {
	
	private Game game;
	private GameObserver gameObserver;
	
	private String[] possiblePuzzleBlocks = getAllowedBlocks();
	private char[] possibleLetters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S'};
	private PuzzleBlock[] puzzleBlocks;
	private int puzzleBlockCount;
	
	public Dock(Game game) {
		this.game = game;
		
		puzzleBlocks = new PuzzleBlock[3];
		puzzleBlockCount = 0;
	}
	
	// Config.txt inladen in string array
		public String[] getAllowedBlocks() {
			String[] arr = null;
			List<String> items = new ArrayList<String>();
			
			try {
				FileInputStream fstream = new FileInputStream("resources/config.txt");
				DataInputStream data_input = new DataInputStream(fstream);
				@SuppressWarnings("resource")
				BufferedReader buffer = new BufferedReader(new InputStreamReader(data_input));
				String line;
				
				while((line = buffer.readLine()) != null) {
					line = line.trim();
					if ((line.length() != 0))
					{
						items.add(line + " \n");
					}
				}
			} catch(IOException e) {
				
			}
			arr = (String[])items.toArray(new String[items.size()]);
			return arr;
		}
	
	public void setGameObserver(GameObserver gameObserver) {
		this.gameObserver = gameObserver;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setPuzzleBlocks(PuzzleBlock[] puzzleBlocks) {
		this.puzzleBlocks = puzzleBlocks;
	}
	
	public PuzzleBlock[] getPuzzleBlocks() {
		return puzzleBlocks;
	}
	
	private char fetchLetterById(int id) {
		return possibleLetters[id];
	}
	
	@Override
	public String toString() {
		String str = "";
		for (PuzzleBlock puzzleBlock : puzzleBlocks) {
			if (puzzleBlock == null) {
				continue;
			}				
			str += puzzleBlock.getId() + " ";
		}		
		str += "\n";	
		return str;
	}
	
	public void loadPuzzleBlocks(int[] blocks) {
		
		for (int i = 0; i < 3; i++) {
			int a = blocks[i];
			
			if (a == 0) {
				continue;
			}
			String str = possiblePuzzleBlocks[a];			
			puzzleBlocks[i] = new PuzzleBlock(str, fetchLetterById(a), a);
		}		
		puzzleBlockCount = 3;
		gameObserver.loadDock(puzzleBlocks);
	}
	
	public void generatePuzzleBlocks() {
			
		for (int i = 0; i < 3; i++) {
			int id = (int) (Math.random() * (possiblePuzzleBlocks.length - 1));
			String str = possiblePuzzleBlocks[id];		
			puzzleBlocks[i] = new PuzzleBlock(str, fetchLetterById(id), id);
		}		
		puzzleBlockCount = 3;
		gameObserver.loadDock(puzzleBlocks);
	}
	
	public void onPuzzleBlockPlaced(int placedPuzzleBlockId) {
		puzzleBlocks[placedPuzzleBlockId] = null;
		puzzleBlockCount--;
		
		// Nieuwe blokjes genereren
		if (puzzleBlockCount < 1) {		
			generatePuzzleBlocks();
		}
		
		gameObserver.loadDock(puzzleBlocks);
	}
	
	
}
