
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TutorialBackground implements Background {

    protected static int TILE_WIDTH = 50;
    protected static int TILE_HEIGHT = 50;

    private Image wood;
    private Image stone;
    private Image path;
    private Image water;
    private Image grass;
    private int maxCols = 0;
    private int maxRows = 0;
	/* private PortalSprite exitSprite; */

	private int map[][] = new int[][] { 
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		{1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,3,3,3,3,3,2,2},
		{1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,3,3,3,3,3,2,2},
		{1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,3,3,3,3,3,2,2},
		{1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,3,3,3,3,3,2,2},
		{1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,3,3,3,3,3,2,2},
		{1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,3,3,3,3,3,4,4},
		{1,0,0,0,0,1,1,1,1,1,0,0,1,0,0,1,3,3,3,3,3,4,4},
		{1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,3,3,3,3,3,2,2},
		{1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,3,3,3,3,3,2,2},
		{1,1,1,1,1,1,1,1,1,1,0,0,1,0,0,1,3,3,3,3,3,2,2},
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,2,2},
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,2,2},
		{1,0,0,0,0,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2},
		{1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2},
		{1,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,2,2},
		{1,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,2,2},
		{1,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2},
		{1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2},
		{1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2},
		{1,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,2,2},
		{1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2},
		{1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2},
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2}
	};
    
    public TutorialBackground() {
    	try {
    		this.wood = ImageIO.read(new File("res/backgrounds/map-tiles/wood_tile.jpg"));
    		this.stone = ImageIO.read(new File("res/backgrounds/map-tiles/stone_tile.jpg"));
    		this.water = ImageIO.read(new File("res/backgrounds/map-tiles/water_tile.jpg"));
    		this.path = ImageIO.read(new File("res/backgrounds/map-tiles/lightstonepath.png"));    		
    		this.grass = ImageIO.read(new File("res/backgrounds/map-tiles/grass.jpg"));    		
    	}
    	catch (IOException e) {
    		//System.out.println(e.toString());
    	}
    	maxRows = map.length - 1;
    	maxCols = map[0].length - 1;
    	
		/*
		 * exitSprite = new PortalSprite(this.getMinX() + TutorialBackground.TILE_WIDTH
		 * * 1, this.getMaxY() - TutorialBackground.TILE_HEIGHT * 3, 50, true);
		 */
    	    	
    }
	
	public Tile getTile(int col, int row) {
		
		Image image = null;
		
		if (row < 0 || row > maxRows || col < 0 || col > maxCols) {
			image = null;
		}
		else if (map[row][col] == 0) {
			image = path;
		}
		else if (map[row][col] == 1) {
			image = stone;
		}
		else if (map[row][col] == 2) {
			image = water;
		}
		else if (map[row][col] == 3) {
			image = grass;
		}
		else if (map[row][col] == 4) {
			image = wood;
		}
		else {
			image = null;
		}
			
		int x = (col * TILE_WIDTH);
		int y = (row * TILE_HEIGHT);
		
		Tile newTile = new Tile(image, x, y, TILE_WIDTH, TILE_HEIGHT, false);
		
		return newTile;
	}
	
	public int getCol(double x) {
		//which col is x sitting at?
		int col = 0;
		if (TILE_WIDTH != 0) {
			col = (int) (x / TILE_WIDTH);
			if (x < 0) {
				return col - 1;
			}
			else {
				return col;
			}
		}
		else {
			return 0;
		}
	}
	
	public int getRow(double y) {
		//which row is y sitting at?
		int row = 0;
		
		if (TILE_HEIGHT != 0) {
			row = (int) (y / TILE_HEIGHT);
			if (y < 0) {
				return row - 1;
			}
			else {
				return row;
			}
		}
		else {
			return 0;
		}
	}
	
	@Override
	public double getShiftX() {
		return 0;
	}

	@Override
	public double getShiftY() {
		return 0;
	}

	@Override
	public void setShiftX(double shiftX) {
		//ignore
	}

	@Override
	public void setShiftY(double shiftY) {
		//ignore
	}
	
	@Override
	public void update(Universe universe, long actual_delta_time) {
		//ignore
	}
	
	// the following method provides a convenient way to add barriers corresponding to certain types of background tiles
	// it means that this data does not need to be duplicated elsewhere, which is best practice
	public ArrayList<DisplayableSprite> getBarriers() {
		ArrayList<DisplayableSprite> barriers = new ArrayList<DisplayableSprite>();
		for (int col = 0; col < map[0].length; col++) {
			for (int row = 0; row < map.length; row++) {
				if (map[row][col] == 1) {
					barriers.add(new BarrierSprite(col * TILE_WIDTH, row * TILE_HEIGHT, (col + 1) * TILE_WIDTH, (row + 1) * TILE_HEIGHT, false));
				}
			}
		}
		return barriers;
	}

	// Similar to providing the barriers in the background, this method provides other sprites that are located within the background.
	// Even though sprites are drawn separately from backgrounds, the (initial) location of the sprite is tightly bound to the design
	// of the background, and thus again it is best practice to instantiate it here
	/*
	 * public PortalSprite getExit() { return this.exitSprite; }
	 */


	// the following four methods are added so that a client of this background can know the bounds.
	// note that these are not part of the interface, so they do not override, and they are only
	// added to this class as there is a specific need
	public double getMinX() {
		return 0;
	}

	public double getMaxX() {
		return (map[0].length + 1) * TILE_WIDTH; 
	}

	public double getMinY() {
		return 0;
	}

	public double getMaxY() {
		return (map.length + 1) * TILE_HEIGHT; 
	}
	
		
}