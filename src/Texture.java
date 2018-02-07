import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {

	public static BufferedImage pipe;
	public static BufferedImage background;
	public static BufferedImage ground;
	public static BufferedImage bird;
	public static BufferedImage[] numbers = new BufferedImage[10];
	
	public static int BACKGROUND_SCALE = 2;
	public static double PIPE_SCALE = 0.75;
	public static double BIRD_SCALE = 0.15;
	public static int NUM_SCALE = 3;
	public Texture(){
		load();
	}

	void load(){
		try{
			pipe = ImageIO.read(getClass().getResource("Pipe.png"));			
			background =  ImageIO.read(getClass().getResource("Background.png"));
			ground =  ImageIO.read(getClass().getResource("Ground.png"));
			bird =  ImageIO.read(getClass().getResource("Bird.png"));
			
			//numbers
			BufferedImage universalSheet = ImageIO.read(getClass().getResource("UniversalSheet.png"));
			numbers[0] = universalSheet.getSubimage(495,59,13,19);
			numbers[1] = universalSheet.getSubimage(135,454,9,19);
			numbers[2] = universalSheet.getSubimage(291,159,13,19);
			numbers[3] = universalSheet.getSubimage(305,159,13,19);
			numbers[4] = universalSheet.getSubimage(319,159,13,19);
			numbers[5] = universalSheet.getSubimage(333,159,13,19);
			numbers[6] = universalSheet.getSubimage(291,183,13,19);
			numbers[7] = universalSheet.getSubimage(305,183,13,19);
			numbers[8] = universalSheet.getSubimage(319,183,13,19);
			numbers[9] = universalSheet.getSubimage(333,183,13,19);

			
		}catch(Exception e) { e.printStackTrace();}
	}
}
