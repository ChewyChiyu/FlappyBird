import java.awt.Color;
import java.awt.Graphics;

public class Goal extends GameObject{ 
	
	final static int WIDTH = 10;
	
	public Goal(int x, int y, int w, int h, GamePanel p) {
		super(x, y,ZMask.GOAL.zPosition, w, h, p);
		// TODO Auto-generated constructor stub
	}

	@Override
	void contact(GameObject obj) {
		// TODO Auto-generated method stub
	}

	@Override
	void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.RED);
		//g.drawRect(x, y, w, h);
	}

	@Override
	void updateHitbox() {
		// TODO Auto-generated method stub
		hitBox.setBounds(x, y, w, h);
	}

}
