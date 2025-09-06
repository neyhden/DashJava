package Objects;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Background extends Drawable{
	int offsetX = 0;
	int offsetY = 0;
	Image bg;
	public Background(String image) {
		try {
			bg = ImageIO.read(new File("media/images/backgrounds/" + image + ".png"));
		} catch (IOException e) {
			System.exit(1);
		}
	}
	@Override
	public void draw(Graphics g) {
		g.drawImage(bg, 560, 140, 1360, 940, 200, 200, 1000, 1000, null);
	}
}
