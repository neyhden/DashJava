package Objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import startup.Camera;

public class Frame extends Drawable{
	BufferedImage img;
	public Frame() {
		Camera.addToDraw(this, 2);
		try {
			img = ImageIO.read(new File("media/images/misc/frame.png"));
		} catch( IOException e) {}
	}
	@Override
	public void draw(Graphics g) {
		g.drawImage(img, 0, 0, 1920, 1080, 0, 0, 1920, 1080, null);
	}
}
