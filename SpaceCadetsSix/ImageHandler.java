import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

public class ImageHandler {
	
	private BufferedImage img;
	private BufferedImage greyImg;
	private BufferedImage scaledImg;
	private BufferedImage sobelImg;
	private BufferedImage houghImg1;
	private BufferedImage houghImg2;
	int scaleFactor;
	int[][] gX;
	int[][] gY;
	double[][] sobelResult;
	private int SOBEL_THRESHOLD = 40;
	private int MAX_TEST_SIZE = 60000;
	ArrayList<SobelCoordinate> sobelCoords = new ArrayList<SobelCoordinate>();
	
	public void setImage(String s) {
		
		try {
			img = ImageIO.read(new File(s));
			houghImg2 = ImageIO.read(new File(s));
		} catch (IOException e) {
			e.printStackTrace();
			img = null;
			houghImg2 = null;
		}
		
		setScaleFactor();
		
		gX = new int[img.getWidth()][img.getHeight()];
		gY = new int[img.getWidth()][img.getHeight()];
		sobelResult = new double[img.getWidth()][img.getHeight()];
	}

	public void greyOut() {
		
		greyImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				
				int pixel = img.getRGB(x, y);
				
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				int grey = (red + green + blue)/3;
				Color greyScale = new Color(grey, grey, grey);
				grey = greyScale.getRGB();
				
				greyImg.setRGB(x, y, grey);
			}
		}
	}
	
	public void setScaleFactor() {
		int width = img.getWidth();
		int height = img.getHeight();
		int scaleFactor = 1;
		
		while(width*height > MAX_TEST_SIZE) {
			width /= 2;
			height /= 2;
			scaleFactor *= 2;
		}
		
		this.scaleFactor = scaleFactor;
	}
	
	public void scaleImage() {
		scaledImg = new BufferedImage(img.getWidth()/scaleFactor, img.getHeight()/scaleFactor, BufferedImage.TYPE_BYTE_GRAY);
		for(int x = 0; x < scaledImg.getWidth(); x++) {
			for(int y = 0; y < scaledImg.getHeight(); y++) {
				int rgbReplacement = greyImg.getRGB(x*scaleFactor, y*scaleFactor);
				scaledImg.setRGB(x, y, rgbReplacement);
			}
		}
	}
	
	public void sobel() {
		sobelImg = new BufferedImage(scaledImg.getWidth(), scaledImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		for(int x = 0; x < scaledImg.getWidth(); x++) {
			for(int y = 0; y < scaledImg.getHeight(); y++) {
				float theta = 0;
				if(x == 0 || y == 0 || x == scaledImg.getWidth() - 1 || y == scaledImg.getHeight() - 1) {
					gX[x][y] = 0;
					gX[x][y] = 0;
					sobelResult[x][y] = 0;
					sobelImg.setRGB(x, y, Color.BLACK.getRGB());
				} else {
					int[][] iRGB = new int[3][3];
					for(int i = -1; i < 2; i++) {
						for(int j = -1; j < 2; j++) {
							iRGB[i + 1][j + 1] = scaledImg.getRGB(x + i, y + j) & 0xff;
						}
					}
					gX[x][y] = (iRGB[0][0] + 2*  iRGB[0][1] + iRGB[0][2] - iRGB[2][0] - 2* iRGB[2][1] - iRGB[2][2]);
					gY[x][y] = (iRGB[0][0] + 2*  iRGB[1][0] + iRGB[2][0] - iRGB[0][2] - 2* iRGB[1][2] - iRGB[2][2]);
					sobelResult[x][y] = (int) Math.sqrt(gX[x][y]*gX[x][y] + gY[x][y]*gY[x][y])/6;
					theta = (float) (-Math.atan2(gY[x][y], gX[x][y]));
					
					if(sobelResult[x][y] > SOBEL_THRESHOLD) {
						int sobelGrey = (int) (100 + 154 * ((sobelResult[x][y] - SOBEL_THRESHOLD)/(255 - SOBEL_THRESHOLD)));
						System.out.println(sobelResult[x][y] - SOBEL_THRESHOLD);
						Color sobelWhite = new Color(sobelGrey, sobelGrey, sobelGrey);
						int whiteRGB = sobelWhite.getRGB();
						sobelImg.setRGB(x, y, whiteRGB);
						SobelCoordinate sc = new SobelCoordinate(x, y, theta);
						sobelCoords.add(sc);
					} else {
						sobelImg.setRGB(x, y, Color.BLACK.getRGB());
					}
				}
			}
		}
	}
	
	public void hough() {
		houghImg1 = new BufferedImage(sobelImg.getWidth(), sobelImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		int radiusRange = (int)(Math.sqrt(Math.pow(sobelImg.getWidth(), 2) + Math.pow(sobelImg.getHeight(), 2)));
		
		int[][][] houghSpace = new int[sobelImg.getWidth()][sobelImg.getHeight()][radiusRange];
		
		int width = sobelImg.getWidth();
		int height = sobelImg.getHeight();
		
		Iterator<SobelCoordinate> sobelIterator = sobelCoords.iterator();
		int progressCounter = 0;
		long startMillis = System.currentTimeMillis();
		
		//Increment houghSpace on a voting system (Very Slow!)
		while(sobelIterator.hasNext()) {
			SobelCoordinate tempCoords = sobelIterator.next();
			int x = tempCoords.getX();
			int y = tempCoords.getY();
			for(int x0 = 0; x0 < width; x0++) {
				for(int y0 = 0; y0 < height; y0++) {
					int radius = (int) (Math.sqrt((x-x0)*(x-x0) + (y-y0)*(y-y0)));
					houghSpace[x0][y0][radius]++;
				}
			}
			progressCounter++;
			System.out.println("Progress: " + progressCounter + "/" + sobelCoords.size());
		}
		System.out.println("Time taken: " + ((System.currentTimeMillis() - startMillis)/1000) + "secs");
		
		double[] maxValues = new double[4];
		for(int x = 0; x < sobelImg.getWidth(); x++) {
			for(int y = 0; y < sobelImg.getHeight(); y++) {
				for(int r = 0; r < radiusRange; r++) {
					if(houghSpace[x][y][r] > maxValues[3]) {
						maxValues[0] = x;
						maxValues[1] = y;
						maxValues[2] = r;
						maxValues[3] = houghSpace[x][y][r];
					}
				}
			}
		}
		
		//Plot x against r for y value with maximum within it
		for(int x = 0; x < sobelImg.getWidth(); x++) {
			for(int r = 0; r < radiusRange; r++) {
				int houghRGBInt = (int) (((houghSpace[x][(int) maxValues[1]][r])*255)/maxValues[3]);
				Color houghColor = new Color(houghRGBInt, houghRGBInt, houghRGBInt);
				int houghRGB = houghColor.getRGB();
				houghImg1.setRGB(x, (int)(((r  * houghImg1.getHeight())/radiusRange)), houghRGB);
			}
		}
		
		//Draw Circle
		for(int theta = 0; theta < 1000; theta++) {
			int xToChange = (int)(maxValues[0] * scaleFactor + maxValues[2] * scaleFactor * Math.sin(((double)(theta)/1000)* 2 * Math.PI));
			int yToChange = (int)(maxValues[1] * scaleFactor + maxValues[2] * scaleFactor * Math.cos(((double)(theta)/1000)* 2 * Math.PI));
			if(yToChange < houghImg2.getHeight() && yToChange > 0 && xToChange < houghImg2.getWidth() && xToChange > 0) {
				houghImg2.setRGB(xToChange, yToChange, Color.RED.getRGB());
				houghImg2.setRGB(xToChange, yToChange - 1, Color.RED.getRGB());
				houghImg2.setRGB(xToChange + 1, yToChange, Color.RED.getRGB());
				houghImg2.setRGB(xToChange + 1, yToChange - 1, Color.RED.getRGB());
			}
		}
	}
	
	
	public BufferedImage[] retrieveImages() {
		BufferedImage[] images = {img, greyImg, sobelImg, houghImg1, houghImg2};
		return images;
	}
}
