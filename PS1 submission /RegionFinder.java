import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Spring 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Spring 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder {
	private static final int maxColorDiff = 20;                // how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50;                 // how many points in a region to be worth considering

	private static final int radius = 1;

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;            // a region is a list of points
	// so the identified regions are in a list of lists of points


	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */

	public void findRegions(Color targetColor) {
		// TODO: YOUR CODE HERE
		regions= new ArrayList<ArrayList<Point>>();

		BufferedImage visited = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y <= image.getHeight()-1; y++) {
			for (int x = 0; x <= image.getWidth()-1; x++) {
				Color c1 = new Color(image.getRGB(x, y));
				if (visited.getRGB(x, y) == 0 && colorMatch(c1, targetColor)) {
					visited.setRGB(x, y, 1);
					ArrayList<Point> toVisit = new ArrayList<>();
					ArrayList<Point> new_region = new ArrayList<>();
					toVisit.add(new Point(x, y));

					while (!toVisit.isEmpty()) {
						Point p = toVisit.remove(0);
						new_region.add(new Point(p.x, p.y));


//						visited.setRGB(p.x, p.y, 1);
						for (int ny = Math.max(0, p.y - radius);
							 ny <= Math.min(image.getHeight()-1, p.y + radius);
							 ny++) {
							for (int nx = Math.max(0, p.x - radius);
								 nx <= Math.min(image.getWidth()-1, p.x + radius);
								 nx++) {
								// Add all the neighbors to the running toVisit
								Color c = new Color(image.getRGB(nx, ny));
								if (visited.getRGB(nx, ny) == 0 && colorMatch(c, targetColor)) {
									Point np = new Point(nx, ny);
									toVisit.add(np);

									visited.setRGB(nx, ny, 1);
								}
							}
						}
					}

					if (new_region.size() >= minRegion) {
						regions.add(new_region);


					}
				}

			}
		}
	}


	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		// TODO: YOUR CODE HERE
		int red= Math.abs(c1.getRed()-c2.getRed());
		int green= Math.abs(c1.getGreen()-c2.getGreen());
		int blue= Math.abs(c1.getBlue()-c2.getBlue());

		if (red <= maxColorDiff && blue<= maxColorDiff && green<=maxColorDiff){
			return true;
		}
		return false;
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		// TODO: YOUR CODE HERE
//		int largestSize = 0;
		ArrayList<Point> largestRegion = regions.get(0);
		for(ArrayList<Point> list: regions ){
			if (list.size() > largestRegion.size()){
				largestRegion = list;
			}
		}
		return largestRegion;
	}

	/**
	 * Sets recoloredImage to be a copy of image,
	 * but with each region a uniform random color,
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		// TODO: YOUR CODE HERE

		for (ArrayList<Point> r: regions){
			int randomColor= (int) (Math.random()*16777216);
			for (Point p: r){
				recoloredImage.setRGB(p.x,p.y, randomColor);
			}
		}
	}
}
