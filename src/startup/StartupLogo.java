package startup;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import Objects.Drawable;

public class StartupLogo extends Drawable{
	StartupLogo thisshit = this;
	Timer timer = new Timer();
	float alpha = 0;
	public StartupLogo() {
		Camera.addToDraw(thisshit, 9);
		
		for (int i = 0; i < 100; i++) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					alpha = alpha + 0.01f;
				}
			}, i * 10);
		}
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				thisshit.delete();
			}
			
		}, 3000);
	}
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		try {
			g2.drawImage(ImageIO.read(new File("media/images/misc/startuplogo.png")), 0, 0, 1920, 1080, 0, 0, 1920, 1080, null);
		} catch (IOException e) {}
	}
	public void delete() {
		for (int i = 0; i < 100; i++) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					alpha -= 0.01f;
				}
			}, i * 10);
		}
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Camera.removeToDraw(9);
				thisshit = null;
			}
			
		}, 1001);
	}

}
