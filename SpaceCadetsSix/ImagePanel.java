import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{

	int height;
	int width;
	BufferedImage[] images;
	
	public ImagePanel(int width, int height, String imageFile) {
		this.height = height;
		this.width = width;
		ImageHandler ih = new ImageHandler();
		ih.setImage(imageFile);
		ih.greyOut();
		ih.scaleImage();
		ih.sobel();
		ih.hough();
		images = ih.retrieveImages();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(images[0], 0, 0, this);
		g.drawImage(images[1], 600, 0, this);
		g.drawImage(images[2], 0, images[0].getHeight(), this);
		g.drawImage(images[3], 600, images[0].getHeight(), this);
		g.drawImage(images[4], 1200, 0, this);
	}
	
}
