import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Pipe extends GameObject{
	
	boolean isUpsideDown;
	
	final BufferedImage img;
	
	public Pipe(int x, int y, boolean isUpsideDown){
		super(x,y,ZMask.PIPE.zPosition,(int)(Texture.pipe.getWidth() * Texture.PIPE_SCALE),(int)(Texture.pipe.getHeight()* Texture.PIPE_SCALE));
		this.isUpsideDown = isUpsideDown;
		img = (isUpsideDown) ? Texture.pipe : Texture.pipe;
	}

	@Override
	void contact(GameObject obj) {
		
	}

	@Override
	void draw(Graphics g) { //draw rotated 180 if isUpsideDown
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		AffineTransform old = g2d.getTransform();
		g2d.translate(x - w/2, y - h/2);
		g2d.rotate( (isUpsideDown) ? Math.PI : 0);
		g.drawImage(img, -w/2, -h/2, w, h, panel);
		g.drawRect(-hitBox.width/2, -hitBox.height/2, hitBox.width, hitBox.height); //hitboxes
		g2d.setTransform(old);
	}
	
}
