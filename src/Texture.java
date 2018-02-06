import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {

	public static BufferedImage pipe;
	public static BufferedImage background;
	public static BufferedImage ground;
	public static BufferedImage bird;
	
	public static int BACKGROUND_SCALE = 2;
	public static double PIPE_SCALE = 0.75;
	public static double BIRD_SCALE = 0.15;
	
	public Texture(){
		load();
	}

	void load(){
		try{
			pipe = ImageIO.read(getClass().getResource("Pipe.png"));			
			background =  ImageIO.read(getClass().getResource("Background.png"));
			ground =  ImageIO.read(getClass().getResource("Ground.png"));
			bird =  ImageIO.read(getClass().getResource("Bird.png"));
		}catch(Exception e) { e.printStackTrace();}
	}
}
