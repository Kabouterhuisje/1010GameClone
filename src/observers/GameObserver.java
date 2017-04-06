package observers;

import models.PuzzleBlock;

public interface GameObserver {

	public void loadGrid(char[][] blocks);
	public void loadDock(PuzzleBlock[] puzzleBlocks);
	public void gameOver(boolean surrendered);
	public void cancel();
	public void reset();
}
