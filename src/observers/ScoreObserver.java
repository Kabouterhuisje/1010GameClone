package observers;

public interface ScoreObserver {
	
	public void scoreChanged(int score);
	public void highScoreChanged(int highscore);
	public void gameOver();
}
