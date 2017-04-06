package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import models.PuzzleBlock;
import models.Game;
import observers.GameObserver;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements GameObserver, MouseListener, MouseMotionListener {
	
	private Game game;
	private GameOverPanel gameOverPanel;
	private PuzzleBlock[] puzzleBlocks;
	private int selectedPuzzleBlockId;
	private char[][] blocks;
	private int x, y;
	
	public GamePanel(Game game) {
		
		setLayout(new GridLayout(10, 10, 2, 2));
		setOpaque(false);
		this.game = game;
		puzzleBlocks = new PuzzleBlock[3];
		blocks = new char[10][10];
		selectedPuzzleBlockId = -1;
		gameOverPanel = new GameOverPanel(game, true);
		addMouseListener(this);
		addMouseMotionListener(this);		
		game.setGameObserver(this);		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Tekenen van het grid met tiles
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				int x1 = x * 32 + x * 2;
				int y1 = y * 32 + y * 2;
				drawTile(blocks[x][y] == '.' ? new Color(96, 96, 96) : getColorByChar(blocks[x][y]), x1, y1, true, g);
			}
		}
		// Tekenen van de blokjes in het dock
		int x = 0;
		for (int i = 0; i < puzzleBlocks.length; i++) {	
			if (puzzleBlocks[i] != null) {
				drawPuzzleBlock(puzzleBlocks[i], x, 400, g);
			}			
			x += 120;
		}
	}
	
	public void clear() {
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				blocks[x][y] = '.';
			}
		}
	}
	
	private void drawTile(Color color, int x, int y, boolean grid, Graphics g) {		
		g.setColor(color);
		g.fillRect(x, y, grid ? 32 : 20, grid ? 32 : 20);
	}
	
	// Tekenen van blokje
	private void drawPuzzleBlock(PuzzleBlock puzzleBlock, int x, int y, Graphics g) {
		
		boolean[][] blocks = puzzleBlock.getBlocks();
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				if (blocks[j][i]) {
					boolean isSelected;
					if (selectedPuzzleBlockId > -1) {
						isSelected = puzzleBlock == puzzleBlocks[selectedPuzzleBlockId];
					}
					else {
						isSelected = false;
					}
					int x1 = (isSelected ? this.x : x) + i * (isSelected ? 32 : 20) + i * 2;
					int y1 = (isSelected ? this.y : y) + j * (isSelected ? 32 : 20) + j * 2;
					drawTile(getColorByChar(puzzleBlock.getLetter()), x1, y1, isSelected, g);				
				}
			}
		}
	}
	
	// Random kleur voor de blokjes genereren
	private Color getColorByChar(char letter) {
		
		Color color = Color.WHITE;
		
		switch(letter) {
		case 'A':
			color = new Color(122, 130, 207);
			break;
		case 'B': case 'C':
			color = new Color(244, 199, 62);
			break;
		case 'D': case 'E':
			color = new Color(219, 140, 69);
			break;
		case 'F': case 'G': case 'H': case 'I':
			color = new Color(105, 219, 126);
			break;
		case 'J': case 'K':
			color = new Color(211, 82, 122);
			break;
		case 'L':
			color = new Color(157, 235, 81);
			break;
		case 'M': case 'N':
			color = new Color(199, 82, 79);
			break;
		case 'O': case 'P': case 'Q': case 'R':
			color = new Color(104, 193, 224);
			break;
		case 'S':
			color = new Color(97, 229, 172);
			break;
		default:
			color = new Color(255, 255, 255);
		}
		return color;
	}

	@Override
	public void loadGrid(char[][] blocks) {
		this.blocks = blocks;
		repaint();
	}

	@Override
	public void loadDock(PuzzleBlock[] puzzleBlocks) {
		this.puzzleBlocks = puzzleBlocks;
		repaint();		
	}
	
	@Override
	public void mouseClicked(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent me) {		
		int i = -1;
		int y = 400;
		int puzzleBlockWidthPlusGap = 113;
		
		if (me.getY() > y && me.getY() < y + puzzleBlockWidthPlusGap) {
			for (int a = 0; a < 3; a++) {
				if (puzzleBlocks[a] == null) {
					continue;
				}
				
				int xmin = a * puzzleBlockWidthPlusGap;
				int xmax = xmin + puzzleBlockWidthPlusGap;
				
				if (me.getX() > xmin && me.getX() < xmax) {					
					i = a;
					break;
				}
			}
		}
		
		if (i < 0 || i > 2) { 
			return; 
		}
		this.selectedPuzzleBlockId = i;
	}

	@Override
	public void mouseReleased(MouseEvent me) {	
		if (selectedPuzzleBlockId < 0) {
			return;
		}
		if (x > 0 && y > 0 && x < 338 && y < 338) {
			game.puzzleBlockDropped(puzzleBlocks[selectedPuzzleBlockId], selectedPuzzleBlockId, x, y);
		}
		this.selectedPuzzleBlockId = -1;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		if (selectedPuzzleBlockId == -1) {
			return;
		}
		x = me.getX();
		y = me.getY();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameOver(boolean surrendered) {
		gameOverPanel.setVisible(true);
		gameOverPanel.setSurrendered(surrendered);
		gameOverPanel.setHighscore(game.getHighScore());
		gameOverPanel.revalidate();
		add(gameOverPanel, BorderLayout.CENTER);
		repaint();
		revalidate();
	}

	@Override
	public void cancel() {
		gameOverPanel.setVisible(false);
		remove(gameOverPanel);
		revalidate();
	}

	@Override
	public void reset() {
		gameOverPanel.setVisible(false);
		remove(gameOverPanel);
		revalidate();
	}
	
}
