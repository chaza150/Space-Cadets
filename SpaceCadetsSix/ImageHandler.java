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
	private int SOBEL_THRESHOLD = 50; //50 = Default
	private int MAX_TEST_SIZE = 60000;
	private int CIRCLE_THRESHOLD = 95; //95 = Default
	private double DISTANCE_THRESHOLD = 0.5;
	ArrayList<SobelCoordinate> sobelCoords = new ArrayList<SobelCoordinate>();
	ArrayList<Circle> circles = new ArrayList<Circle>();
	
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
		scaledImg = new BufferedImage(img.getWidth()/scaleFactor, img.getHeight()/scaleFactor, BufferedImage.TYPE_INT_ARGB);
		for(int x = 0; x < scaledImg.getWidth(); x++) {
			for(int y = 0; y < scaledImg.getHeight(); y++) {
				int[] sum = new int[3];
				int[] average = new int[3];
				for(int i = 0; i < scaleFactor; i++) {
					for(int j = 0; j < scaleFactor; j++) {
						int pixelRGB = img.getRGB(x * scaleFactor + i, y * scaleFactor + j);
						sum[0] += (pixelRGB >> 16) & 0xff;
						sum[1] += (pixelRGB >> 8) & 0xff;
						sum[2] += (pixelRGB) & 0xff;
					}
				}
				average[0] = sum[0]/(scaleFactor*scaleFactor);
				average[1] = sum[1]/(scaleFactor*scaleFactor);
				average[2] = sum[2]/(scaleFactor*scaleFactor);
				Color scaledColor = new Color(average[0], average[1], average[2]);
				int rgbReplacement = scaledColor.getRGB();
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
					int[][][] iRGB = new int[3][3][3];
					for(int i = -1; i < 2; i++) {
						for(int j = -1; j < 2; j++) {
							iRGB[i + 1][j + 1][0] = (scaledImg.getRGB(x + i, y + j) >> 16) & 0xff;
							iRGB[i + 1][j + 1][1] = (scaledImg.getRGB(x + i, y + j) >> 8) & 0xff;
							iRGB[i + 1][j + 1][2] = scaledImg.getRGB(x + i, y + j) & 0xff;
						}
					}
					int[] gXRGB = new int[3];
					int[] gYRGB = new int[3];
					for(int a = 0; a < 3; a++) {
						gXRGB[a] = (iRGB[0][0][a] + 2*  iRGB[0][1][a] + iRGB[0][2][a] - iRGB[2][0][a] - 2* iRGB[2][1][a] - iRGB[2][2][a])/4;
						gYRGB[a] = (iRGB[0][0][a] + 2*  iRGB[1][0][a] + iRGB[2][0][a] - iRGB[0][2][a] - 2* iRGB[1][2][a] - iRGB[2][2][a])/4;
					}
					
					gX[x][y] = (int) (Math.sqrt(gXRGB[0]*gXRGB[0] + gXRGB[1]*gXRGB[1] + gXRGB[2]*gXRGB[2])/1.74);
					gY[x][y] = (int) (Math.sqrt(gYRGB[0]*gYRGB[0] + gYRGB[1]*gYRGB[1] + gYRGB[2]*gYRGB[2])/1.74);
					sobelResult[x][y] = (int) (Math.sqrt(gX[x][y]*gX[x][y] + gY[x][y]*gY[x][y])/1.42);
					theta = (float) (-Math.atan2(gY[x][y], gX[x][y]));
					
					if(sobelResult[x][y] > SOBEL_THRESHOLD) {
						int sobelGrey = (int) (100 + 154 * ((sobelResult[x][y] - SOBEL_THRESHOLD)/(255 - SOBEL_THRESHOLD)));
						System.out.println(sobelResult[x][y] - SOBEL_THRESHOLD);
						Color sobelWhite = new Color(sobelGrey, sobelGrey, sobelGrey);
						int whiteRGB = sobelWhite.getRGB();
						sobelImg.setRGB(x, y, whiteRGB);
						SobelCoordinate sc = new SobelCoordinate(x, y);
						sobelCoords.add(sc);
					} else {
						sobelImg.setRGB(x, y, Color.BLACK.getRGB());
					}
				}
			}
		}
	}
	
	public void hough() {
		long startMillis = System.currentTimeMillis();
		houghImg1 = new BufferedImage(sobelImg.getWidth(), sobelImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		int radiusRange = (int)(Math.sqrt(Math.pow(sobelImg.getWidth(), 2) + Math.pow(sobelImg.getHeight(), 2)));
		
		int[][][] houghSpace = new int[sobelImg.getWidth()][sobelImg.getHeight()][radiusRange];
		
		int width = sobelImg.getWidth();
		int height = sobelImg.getHeight();
		
		Iterator<SobelCoordinate> sobelIterator = sobelCoords.iterator();
		int progressCounter = 0;
		
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
		
		boolean addingCircles = true;
		
		while(addingCircles) {
			int[] maxValues = new int[4];
			addingCircles = false;
			
			for(int x = 0; x < sobelImg.getWidth(); x++) {
				for(int y = 0; y < sobelImg.getHeight(); y++) {
					for(int r = 0; r < radiusRange; r++) {
						
						if(houghSpace[x][y][r] > CIRCLE_THRESHOLD && houghSpace[x][y][r] > maxValues[3]) {
							
							boolean validCircle = true;
							
							for(Circle c : circles) {
								int distance = (c.getX()-x)*(c.getX()-x) + (c.getY()-y)*(c.getY()-y);
								if(distance < (c.getRadius()*c.getRadius() + 10)) {
									validCircle = false;
								}
							}
							if(validCircle) {
								maxValues[0] = x;
								maxValues[1] = y;
								maxValues[2] = r;
								maxValues[3] = houghSpace[x][y][r];
								addingCircles = true;
							}
						}
					}
				}
			}
			if (addingCircles)
			circles.add(new Circle(maxValues[0], maxValues[1], maxValues[2], maxValues[3]));
		}

		//Plot x against r for y value with maximum within it
		if(circles.size() != 0) {
			for(int x = 0; x < sobelImg.getWidth(); x++) {
				for(int r = 0; r < radiusRange; r++) {
					int houghRGBInt = (int) ((houghSpace[x][circles.get(0).getY()][r]*255)/circles.get(0).getVotes());
					Color houghColor = new Color(houghRGBInt, houghRGBInt, houghRGBInt);
					int houghRGB = houghColor.getRGB();
					houghImg1.setRGB(x, (int)(((r  * houghImg1.getHeight())/radiusRange)), houghRGB);
				}
			}
		}
		
		//Draw Circles
		for(Circle c : circles) {
			for(int theta = 0; theta < 1000; theta++) {
				int xToChange = (int)(c.getX() * scaleFactor + c.getRadius() * scaleFactor * Math.sin(((double)(theta)/1000)* 2 * Math.PI));
				int yToChange = (int)(c.getY() * scaleFactor + c.getRadius() * scaleFactor * Math.cos(((double)(theta)/1000)* 2 * Math.PI));
				if(yToChange < houghImg2.getHeight() && yToChange > 0 && xToChange < houghImg2.getWidth() && xToChange > 0) {
					int circleStrengthColor = (int)(255 * (double)(c.getVotes()-circles.get(circles.size()-1).getVotes()) /(double)(circles.get(0).getVotes() - circles.get(circles.size()-1).getVotes()));
					Color circleColor = new Color(255 - circleStrengthColor, circleStrengthColor, 0);
					houghImg2.setRGB(xToChange, yToChange, circleColor.getRGB());
					houghImg2.setRGB(xToChange, yToChange - 1, circleColor.getRGB());
					houghImg2.setRGB(xToChange - 1, yToChange, circleColor.getRGB());
					houghImg2.setRGB(xToChange - 1, yToChange - 1, circleColor.getRGB());
				}
			}
		}
		System.out.println("Time taken: " + ((System.currentTimeMillis() - startMillis)) + " milliseconds");
	}
	
	
	public BufferedImage[] retrieveImages() {
		BufferedImage[] images = {img, scaledImg, sobelImg, houghImg1, houghImg2};
		return images;
	}
}
