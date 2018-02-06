import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Bird extends GameObject{
	
	int dy = 0; // jump impulse factor
	
	double angle = 0;
	
	boolean isJumping = false;
	
	public Bird(int x, int y) {
		super(x, y, ZMask.BIRD.zPosition,(int) (Texture.bird.getWidth() * Texture.BIRD_SCALE), (int)(Texture.bird.getHeight() * Texture.BIRD_SCALE));
	}

	void jump(){
		if(!isJumping){ //jump only once
			dy = 0;
			final int JUMP_INDEX = 9;
			final int JUMP_IMPULSE = 3;
			final int ANGLE_SHIFT_INDEX = 30;
			isJumping = true;
			Thread impulse = new Thread(new Runnable(){

				@Override
				public void run() {
					for(int index = 0; index < JUMP_INDEX; index++){
						if(index < JUMP_INDEX/2) // half of parabola of jump
						dy -= JUMP_IMPULSE;
						try{ Thread.sleep(1); }catch(Exception e) { }
					}
					isJumping = false; // not jumping anymore
				}
			});	
			Thread shiftAngle = new Thread(new Runnable(){
				@Override
				public void run() {
					for(int index = 0; index < ANGLE_SHIFT_INDEX; index++){
						angle -= (angle > -Math.PI/3) ? Math.PI/60 : 0;
						try{ Thread.sleep(2); } catch(Exception e) { }
					}
				}
				
			});
			impulse.start();
			shiftAngle.start();
		}
	}
	
	@Override
	void contact(GameObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void draw(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.blue);
		AffineTransform old = g2d.getTransform();
		g2d.translate(x - w/2, y - h/2);
		g2d.rotate(angle);
		g2d.drawImage(Texture.bird, -w/2, -h/2, w, h, panel);
		g2d.setTransform(old);
		g2d.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height); //hitboxes
	}

	@Override
	void updateHitbox() {
		// TODO Auto-generated method stub
		hitBox.setBounds(x-w, y-h, w, h);
	}

}
