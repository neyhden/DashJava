package startup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Objects.Drawable;
import Objects.Frame;
import Objects.Menu;

public class Camera extends JPanel{
	private static final long serialVersionUID = 1L;
	private static Drawable[] toDraw = new Drawable[10];
	private static Timer timer = new Timer();
	private Camera thisshit = this;
	private Menu menu;
	
	public Camera() {
		startCamera();
		new StartupLogo();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				new Frame();
				menu = new Menu();
				thisshit.addMouseListener(menu);
				thisshit.addKeyListener(menu);
				thisshit.setFocusable(true);
				thisshit.requestFocus();
			}
		}, 3000);
		
	}
	private void startCamera() {
		
		JFrame frame = new JFrame("Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		frame.getContentPane().add(this);
		this.setBackground(Color.BLACK);

		if (gd.isFullScreenSupported()) {
			frame.setUndecorated(true);
			gd.setFullScreenWindow(frame);
		} else {
			frame.setUndecorated(false);
			frame.setPreferredSize(new Dimension(1920, 1080));
			frame.pack();
			frame.setVisible(true);
		}
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				repaint();
			}
		}, 0, 5);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		if (toDraw == null) {
			return;
		}
		for (Drawable d : toDraw) {
			if (d == null) {
				continue;
			}
			d.draw(g);
		}
		
	}
	public static void addToDraw(Drawable d, int i) {
		toDraw[i] = d;
		return;
	}
	public static void removeToDraw(int i) {
		toDraw[i] = null;
		return;
	}
}
