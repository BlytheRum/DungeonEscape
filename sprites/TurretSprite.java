import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TurretSprite implements DisplayableSprite {

	private static final int WIDTH = 120;
	private static final int HEIGHT = 120;
	
	private static Image[] rotatedImages = new Image[360];
	private AudioPlayer thrustSound = new AudioPlayer();
	private AudioPlayer bulletSound = new AudioPlayer();
	
	private double reloadTime = 0;
	private Image rotatedImage;

	private double ACCELERATION = 400;          			//PIXELS PER SECOND PER SECOND 
	private double ROTATION_SPEED = 180;	//degrees per second	
	private double currentAngle = 0;
	private int currentImageAngle = 0;
	
	private static Image image;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = WIDTH;
	private double height = HEIGHT;
	private boolean dispose = false;	
	private double velocityX = 0;
	private double velocityY = 0;
	
	private DisplayableSprite bullet = null;
	
	private final double RANGE = 200;
	private final double VELOCITY = 215;
	
	
	public TurretSprite(double centerX, double centerY, double height, double width) {
		this(centerX, centerY);
		
		this.height = height;
		this.width = width;
	}
	
	public TurretSprite(double centerX, double centerY) {

		this.centerX = centerX;
		this.centerY = centerY;		

		Image image = null;
		try {
			image = ImageIO.read(new File("res/TurretSprite.png"));
		}
		catch (IOException e) {
			System.out.print(e.toString());
		}

		if (image != null) {
			for (int i = 0; i < 360; i++) {
				rotatedImages[i] = ImageRotator.rotate(image, i);			
			}
		}
	}

	public Image getImage() {
		return rotatedImages[(int)currentAngle];
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

	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}
	
	public void update(Universe universe, long actual_delta_time) {
		
		//find player. if player in range, create new bullet sprite
		boolean shooting = checkPlayerInRange(universe);
		
		if (shooting) {
			reloadTime -= actual_delta_time;
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
			
			//update image here
			currentAngle = angle;

			velocityX = (VELOCITY * Math.cos(angle) * 0.001);
			velocityY = (VELOCITY * Math.sin(angle) * 0.001);
			
			if (dx < 0) { 
				velocityX = 0 - velocityX;
				velocityY = 0 - velocityY;
			}
			
//			currentAngle ¡
			System.out.println(String.format("delta time: %12d, angle: %5.2f; dx: %5.2f; dy: %5.2f; moveX %5.2f, moveY %5.2f", actual_delta_time, angle * 57.2958, dx, dy, velocityX, velocityY));

			
			if (reloadTime <= 0) {
				bullet = new BulletSprite(this.centerX, this.centerY, velocityX, velocityY);
				universe.getSprites().add(bullet);
				reloadTime = 1000;
				//play sound? see spaceshipSprite
			}
			
		}
		

		
		
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
	
	

}













