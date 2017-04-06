package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import models.Game;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	
	private ScorePanel scorePanel;
	private GamePanel gamePanel;

	public GameFrame(Game game) {
		
		setTitle("1010 Tiles - Dennis Tijbosch");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(400, 700));
		setResizable(false);
		setLayout(new BorderLayout());
		getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 30));
		getRootPane().setBackground(new Color(40,40,40));
		getContentPane().setBackground(new Color(40,40,40));
		scorePanel = new ScorePanel(game);		
		gamePanel = new GamePanel(game);		
		add(scorePanel, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				game.onExitGame();				
			}
		});
	}
}
