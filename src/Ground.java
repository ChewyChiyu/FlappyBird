import java.awt.Graphics;

public class Ground extends GameObject{

	public Ground(int x, int y, GamePanel p) {
		super(x, y, ZMask.GROUND.zPosition, Texture.ground.getWidth() * Texture.BACKGROUND_SCALE, Texture.ground.getHeight() * Texture.BACKGROUND_SCALE, p);
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

	@Override
	void updateHitbox() {
		// TODO Auto-generated method stub
		hitBox.setBounds(x, y, w, h);
	}

}
