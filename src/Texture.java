import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {

	public static BufferedImage pipe;
	public static BufferedImage background;
	public static BufferedImage ground;
	public static int BACKGROUND_SCALE = 2;
	public static double PIPE_SCALE = 0.75;
	
	public Texture(){
		load();
	}

	void load(){
		try{
			pipe = ImageIO.read(getClass().getResource("Pipe.png"));			
			background =  ImageIO.read(getClass().getResource("Background.png"));
			ground =  ImageIO.read(getClass().getResource("Ground.png"));
		}catch(Exception e) { e.printStackTrace();}
	}
}
