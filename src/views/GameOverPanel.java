package views;

import helpers.Button;
import helpers.Label;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import models.Game;

@SuppressWarnings("serial")
public class GameOverPanel extends JPanel {
	
	private Label lblGameOver;
	private Label lblHighScore;
	private JPanel pGameOverOptions;
	private Button btnCancel;
	private Button btnReset;
	
	public GameOverPanel(Game game, boolean surrendered) {
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(200, 400));
		setPreferredSize(new Dimension(200, 400));
		
		lblGameOver = new Label(surrendered ? "You surrendered!" : "No moves left!");
		lblGameOver.setForeground(Color.BLACK);
		lblHighScore = new Label(Integer.toString(game.getHighScore()));
		lblHighScore.setForeground(Color.RED);
		
		btnCancel = new Button("");
		btnCancel.setPreferredSize(new Dimension(150, 50));
		btnCancel.setMinimumSize(new Dimension(150, 50));
		btnCancel.setHorizontalAlignment(SwingConstants.CENTER);
		btnCancel.setIcon(new ImageIcon("resources/cancel.png"));
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				game.onCancelGameOverButtonClicked();
			}
		});
		
		btnReset = new Button("");
		btnReset.setPreferredSize(new Dimension(150, 50));
		btnReset.setMinimumSize(new Dimension(150, 50));
		btnReset.setHorizontalAlignment(SwingConstants.CENTER);
		btnReset.setIcon(new ImageIcon("resources/reset.png"));
		btnReset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				game.onResetButtonClicked();
			}
		});
		
		pGameOverOptions = new JPanel();
		pGameOverOptions.setLayout(new BorderLayout());
		pGameOverOptions.setPreferredSize(new Dimension(50,50));
		pGameOverOptions.add(btnCancel, BorderLayout.LINE_END);
		pGameOverOptions.add(btnReset, BorderLayout.LINE_START);
		pGameOverOptions.add(lblGameOver, BorderLayout.PAGE_START);
		pGameOverOptions.add(lblHighScore, BorderLayout.CENTER);
		add(pGameOverOptions, BorderLayout.SOUTH);
		add(Box.createRigidArea(new Dimension(333, 100)));
	}
	
	public void setSurrendered(boolean surrendered) {
		lblGameOver.setText(surrendered ? "You surrendered!" : "No moves left!");
		btnCancel.setEnabled(surrendered);
		revalidate();
	}
	
	public void setHighscore(int highscore) {
		lblHighScore.setText("" + highscore);
		revalidate();
	}
}
