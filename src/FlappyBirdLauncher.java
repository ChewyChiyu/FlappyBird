import java.awt.Dimension;

public class FlappyBirdLauncher {
	
	static Dimension gameDim;
	
	public static void main(String[] args){
		new FlappyBirdLauncher();
	}
	
	FlappyBirdLauncher(){
		new Texture(); //loading textures
		gameDim = new Dimension(Texture.background.getWidth() * Texture.BACKGROUND_SCALE / 2 ,Texture.background.getHeight() * Texture.BACKGROUND_SCALE);
		new GamePanel(gameDim);
	}
	
}
