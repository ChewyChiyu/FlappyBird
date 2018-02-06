import java.awt.Color;
import java.awt.Graphics;

public class Goal extends GameObject{
	
	final static int WIDTH = 10;
	
	public Goal(int x, int y, int w, int h) {
		super(x, y,ZMask.GOAL.zPosition, w, h);
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
		g.drawRect(x, y, w, h);
	}

}
