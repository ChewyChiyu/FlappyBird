import java.awt.Graphics;

public class Ground extends GameObject{

	public Ground(int x, int y) {
		super(x, y, ZMask.GROUND.zPosition, Texture.ground.getWidth() * Texture.BACKGROUND_SCALE, Texture.ground.getHeight() * Texture.BACKGROUND_SCALE);
	}

	@Override
	void contact(GameObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(Texture.ground, x, y, Texture.ground.getWidth() * Texture.BACKGROUND_SCALE, Texture.ground.getHeight() * Texture.BACKGROUND_SCALE, panel);
	}

}
