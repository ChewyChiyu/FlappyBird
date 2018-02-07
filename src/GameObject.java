import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class GameObject {
	
	int x, y, z, w, h;
	
	Rectangle hitBox;
	GamePanel panel;
	
	boolean inContact = false;
	
	
	
	public GameObject(int x, int y, int z, int w, int h, GamePanel p){
		this.x  = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.h = h;
		panel = p;
		hitBox = new Rectangle(x,y,w,h);
	}
	
		
	
	//checking for a contact between the hitboxes of two gameObjects
	void checkForContact(ArrayList<GameObject> obj){
		if(inContact) { return; }
		for(int index = 0; index < obj.size(); index++){
			GameObject o = obj.get(index);
			if(o.equals(this)){ continue; }
			if(o.hitBox.intersects(hitBox)){
				contact(o);
			}
		}
	}
	
	
	abstract void updateHitbox();

	abstract void contact(GameObject obj);
	abstract void draw(Graphics g);
	
}
