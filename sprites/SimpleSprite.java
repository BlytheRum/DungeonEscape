import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SimpleSprite implements DisplayableSprite {

	private static Image image;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;	
	
	private int health = 100;

	private final double VELOCITY = 200;

	public SimpleSprite(double centerX, double centerY, double height, double width) {
		this(centerX, centerY);
		
		this.height = height;
		this.width = width;
	}

	
	public SimpleSprite(double centerX, double centerY) {

		this.centerX = centerX;
		this.centerY = centerY;
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/KnightResting_Left.png"));
			}
			catch (IOException e) {
				//System.out.println(e.toString());
			}		
		}		
	}

	public Image getImage() {
		return image;
	}
	
	//DISPLAYABLE
	
	public boolean getVisible() {
		return true;
	}
	
	public double getMinX() {
		return centerX - (width / 2);
	}

	public double getMaxX() {
		return centerX + (width / 2);
	}

	public double getMinY() {
		return centerY - (height / 2);
	}

	public double getMaxY() {
		return centerY + (height / 2);
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getCenterX() {
		return centerX;
	};

	public double getCenterY() {
		return centerY;
	};
	
	
	public boolean getDispose() {
		return dispose;
	}
	
	public int getHealth() {
		return health;
	}


	public void setHealth(int health) {
		this.health = health;
	}

	public void update(Universe universe, long actual_delta_time) {
		
		double velocityX = 0;
		double velocityY = 0;
		
		KeyboardInput keyboard = KeyboardInput.getKeyboard();

		//LEFT	
		if (keyboard.keyDown(37)) {
			velocityX = -VELOCITY;
		}
		//UP
		if (keyboard.keyDown(38)) {
			velocityY = -VELOCITY;			
		}
		// RIGHT
		if (keyboard.keyDown(39)) {
			velocityX += VELOCITY;
		}
		// DOWN
		if (keyboard.keyDown(40)) {
			velocityY += VELOCITY;			
		}

		double deltaX = actual_delta_time * 0.001 * velocityX;
        
		
		double deltaY = actual_delta_time * 0.001 * velocityY;
    	

    	
		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
		boolean collidingBarrierY = checkCollisionWithBarrier(universe.getSprites(), 0, deltaY);
		boolean collidingWithBat = checkCollisionWithBat(universe.getSprites(), deltaX, deltaY);
		
		System.out.println(collidingBarrierX);
		

		//only move if there is no collision with pinball in any dimension and no collision with barrier in X dimension 
		if ((collidingBarrierX == false)) {
			this.centerX += deltaX;
		}
		//only move if there is no collision with pinball in any dimension and no collision with barrier in Y dimension 
		if ((collidingBarrierY == false)) {
			this.centerY += deltaY;
		}
		
		if ((collidingWithBat == true)) {
			this.setHealth(this.getHealth() - 1);
		}
	}

	private boolean checkCollisionWithBarrier(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {

		//deltaX and deltaY represent the potential change in position
		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof BarrierSprite) {
				if (CollisionDetection.overlaps(this.getMinX() + deltaX, this.getMinY() + deltaY, 
						this.getMaxX()  + deltaX, this.getMaxY() + deltaY, 
						sprite.getMinX(),sprite.getMinY(), 
						sprite.getMaxX(), sprite.getMaxY())) {
					colliding = true;
					break;					
				}
			}
		}		
		return colliding;		
	}
	
	private boolean checkCollisionWithBat(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
		//deltaX and deltaY represent the potential change in position
		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if ((sprite instanceof BatSprite)) {
				if (CollisionDetection.pixelBasedOverlaps(this, sprite, deltaX, deltaY)) {
					colliding = true;
					break;					
				}
			}
		}		
		return colliding;		
	}
	
	


	@Override
	public void setDispose(boolean dispose) {
		this.dispose = true;
	}


	

}
