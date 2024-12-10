import java.util.ArrayList;

public class ShellUniverse implements Universe {

	private boolean complete = false;	
	private DisplayableSprite player1 = null;
	private DisplayableSprite bat = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private ArrayList<Background> backgrounds = new ArrayList<Background>();
	private ArrayList<DisplayableSprite> disposalList = new ArrayList<DisplayableSprite>();
	private Background stoneBackground = null;	
	private Background tutorialBackground = null;

	
	public ShellUniverse () {
		stoneBackground = new StoneBackground();
		tutorialBackground = new TutorialBackground();
		ArrayList<DisplayableSprite> barriers = ((TutorialBackground)tutorialBackground).getBarriers();

		backgrounds = new ArrayList<Background>();
		backgrounds.add(stoneBackground);
		backgrounds.add(tutorialBackground);		
		this.setXCenter(0);
		this.setYCenter(0);

		bat = new BatSprite(150, 100, TutorialBackground.TILE_HEIGHT * 0.9, TutorialBackground.TILE_HEIGHT * 0.9);

		player1 = new SimpleSprite(145, 200, TutorialBackground.TILE_HEIGHT * 0.9, TutorialBackground.TILE_HEIGHT * 0.9);
		
		
		sprites.addAll(barriers);
		sprites.add(player1);
		sprites.add(bat);
			
	}

	public double getScale() {
		return 1;
	}

	public double getXCenter() {
		return this.player1.getCenterX();
	}

	public double getYCenter() {
		return this.player1.getCenterY();
	} 

	public void setXCenter(double xCenter) {
	}

	public void setYCenter(double yCenter) {
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		complete = true;
	}

	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}	

	
	public ArrayList<DisplayableSprite> getSprites() {
		return sprites;
	}

	public boolean centerOnPlayer() {
		return true;
	}		

	public void update(Animation animation, long actual_delta_time) {

		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, actual_delta_time);
    	} 
		
		disposeSprites();
		
	}
	
    protected void disposeSprites() {
        
    	//collect a list of sprites to dispose
    	//this is done in a temporary list to avoid a concurrent modification exception
		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
    		if (sprite.getDispose() == true) {
    			disposalList.add(sprite);
    		}
    	}
		
		//go through the list of sprites to dispose
		//note that the sprites are being removed from the original list
		for (int i = 0; i < disposalList.size(); i++) {
			DisplayableSprite sprite = disposalList.get(i);
			sprites.remove(sprite);
    	}
		
		//clear disposal list if necessary
    	if (disposalList.size() > 0) {
    		disposalList.clear();
    	}
    }


	public String toString() {
		return "ShellUniverse";
	}

}
