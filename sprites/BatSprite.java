import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class BatSprite implements DisplayableSprite {

	private static Image image;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;	
	

	private final double RANGE = 200;
	private final double VELOCITY = 160;

	public BatSprite(double centerX, double centerY, double height, double width) {
		this(centerX, centerY);
		
		this.height = height;
		this.width = width;
	}

	
	public BatSprite(double centerX, double centerY) {

		this.centerX = centerX;
		this.centerY = centerY;
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/Bat.png"));
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

	public void update(Universe universe, long actual_delta_time) {
		
		double velocityX = 0;
		double velocityY = 0;
		
		// check for main overlap
		boolean moving = checkPlayerInRange(universe);
		//determine xy coordinates setup, use theorem de pythagore for direction
		//also determine attack pattern. more like terraria/metroid or stardew?
		//also determine image.
		
		
		if (moving) { //following is wrong
			
			double mainx = 0;
			double mainy = 0;
			for (DisplayableSprite sprite: universe.getSprites()) {
				if (sprite instanceof SimpleSprite) {
					mainx = sprite.getCenterX();
					mainy = sprite.getCenterY();
				}
			}
			double dx = mainx - this.centerX;
			double dy = mainy - this.centerY;
			double angle = Math.atan(dy/dx);
			
			
			//double angle = checkAngle(universe);
			velocityX = (VELOCITY * Math.cos(angle) * 0.001);// * (dx / Math.abs(dx));
			velocityY = (VELOCITY * Math.sin(angle) * 0.001);// * (dy / Math.abs(dy));
			double deltaX = actual_delta_time * velocityX;
			double deltaY = actual_delta_time * velocityY;
			
//			System.out.println(actual_delta_time);
//			System.out.println(angle);
//			System.out.println(dx);
//			System.out.println(dy);
			if (dx < 0) { // this is what fixed it
				deltaX = 0 - deltaX;
				deltaY = 0 - deltaY;
			}
			//System.out.println(String.format("delta time: %12d, angle: %5.2f; dx: %5.2f; dy: %5.2f; moveX %5.2f, moveY %5.2f", actual_delta_time, angle * 57.2958, dx, dy, deltaX, deltaY));
		
			boolean collidingWithPinball = checkCollisionWithPlayer(universe.getSprites(), deltaX, deltaY);
			boolean collidingWithBat = checkCollisionWithBat(universe.getSprites(), deltaX, deltaY);
			
			if ((collidingWithPinball == false) && (collidingWithBat == false)) {
				this.centerX += deltaX;
			}
			//only move if there is no collision with pinball in any dimension and no collision with barrier in Y dimension 
			if ((collidingWithPinball == false) && (collidingWithBat == false)) {
				this.centerY += deltaY;
			}
			
			if ((collidingWithPinball == false) && (collidingWithBat == true)) {
				this.centerX += deltaX;
			}
			
			if ((collidingWithPinball == false) && (collidingWithBat == true)) {
				this.centerY -= deltaY;
			}
			
//			this.centerX += deltaX;
//			this.centerY += deltaY;
//			System.out.printf("%f, %f", deltaX, deltaY);
//			System.out.println();
		}
		

	}


	private double checkAngle(Universe universe) {
		double mainx = 0;
		double mainy = 0;
		for (DisplayableSprite sprite: universe.getSprites()) {
			if (sprite instanceof SimpleSprite) {
				mainx = sprite.getCenterX();
				mainy = sprite.getCenterY();
			}
		}
		double dx = mainx - this.centerX;
		double dy = mainy - this.centerY;
		double angle = Math.atan(dy/dx);
		return angle;
	}


	private boolean checkPlayerInRange(Universe universe) {
		boolean inRange = false;
		for (DisplayableSprite sprite: universe.getSprites()) {
			if (sprite instanceof SimpleSprite) {
				if (CollisionDetection.overlaps(this.getMinX() - RANGE, this.getMinY() - RANGE, 
						this.getMaxX()  + RANGE, this.getMaxY() + RANGE, 
						sprite.getMinX(),sprite.getMinY(), 
						sprite.getMaxX(), sprite.getMaxY())) {
					inRange = true;
				}
			}
		}
		return inRange;
	}

	private boolean checkCollisionWithPlayer(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
		//deltaX and deltaY represent the potential change in position
		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof SimpleSprite) {
				if (CollisionDetection.pixelBasedOverlaps(this, sprite, deltaX, deltaY)) {
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
			if ((sprite instanceof BatSprite) && (sprite != this)) {
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
