package views;

import helpers.Button;
import helpers.Label;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import models.Game;
import observers.ScoreObserver;

@SuppressWarnings("serial")
public class ScorePanel extends JPanel implements ScoreObserver {
	
	private static BufferedImage imgThrophy;	
	private Label lblScore;
	private Label lblHighScore;
	private Button btnSurrender;
	private Button btnCheat;
	private Button btnTest;
	
	public ScorePanel(Game game) {	
		
		setPreferredSize(new Dimension(400, 110));
		setOpaque(false);
		
		try {
			imgThrophy = ImageIO.read(new File("resources/beker.png"));
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
	
		btnCheat = (Button) add(new Button(""));
		btnCheat.setPreferredSize(new Dimension(90,50));
		btnCheat.setIcon(new ImageIcon("resources/cheatButton.png"));
		btnCheat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				game.onCheatButtonClicked();
			}
		});
		
		lblScore = (Label) add(new Label("0"));
		lblScore.setForeground(Color.RED);
		add(Box.createRigidArea(new Dimension(50, 60)));
		lblHighScore = (Label) add(new Label("0"));
		lblHighScore.setForeground(Color.RED);
		
		btnSurrender = (Button) add(new Button("Surrender"));
		btnSurrender.setPreferredSize(new Dimension(76,50));
		btnSurrender.setIcon(new ImageIcon("resources/surrenderButton.png"));
		btnSurrender.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				game.onSurrenderButtonClicked();
				setButtonsEnabled(false);
			}
		});
		game.setScoreObserver(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(imgThrophy, 137, 10, 50, 50, null);
	}

	@Override
	public void scoreChanged(int score) {
		lblScore.setText("" + score);
	}

	@Override
	public void highScoreChanged(int highscore) {
		lblHighScore.setText("" + highscore);		
	}
	
	public void setButtonsEnabled(boolean enabled) {
		btnCheat.setEnabled(enabled);
		btnSurrender.setEnabled(enabled);
	}

	@Override
	public void gameOver() {
		setButtonsEnabled(true);		
	}

}
