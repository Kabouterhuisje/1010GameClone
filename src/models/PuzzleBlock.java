package models;

public class PuzzleBlock {
	
	private String pattern;
	private boolean[][] blocks;
	private int blocksCount;
	private char letter;
	private int id;
	
	// Blokje in dock
	public PuzzleBlock(String pattern, char letter, int id) {
		blocks = new boolean[5][5];
		this.pattern = pattern;
		this.letter = letter;
		this.id = id;
		char[] splittedPattern = pattern.toCharArray();
		int i = 0;
		for (int y = 0; y < blocks.length; y++) {
			for (int x = 0; x < blocks.length; x++) {
				boolean notDot = splittedPattern[i] != '.';
				blocks[y][x] = notDot;
				if (notDot) {
					blocksCount++;
				}
				i++;
			}
		}
	}
	
	public char getLetter() {
		return letter;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean[][] getBlocks() {
		return blocks;
	}
	
	public int getBlocksCount() {
		return blocksCount;
	}
	
	public String toString() {
		return pattern;
	}
}
