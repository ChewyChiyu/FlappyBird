import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class GameObject {
	
	int x, y, z, w, h;
	
	Rectangle hitBox;
	GamePanel panel;
	
	boolean inContact = false;
	
	
	
	public GameObject(int x, int y, int z, int w, int h){
		this.x  = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.h = h;
		hitBox = new Rectangle(x,y,w,h);
	}
	
	
	public
	
	
	//checking for a contact between the hitboxes of two gameObjects
	void checkForContact(ArrayList<GameObject> obj){
		if(inContact) { return; }
		for(int index = 0; index < obj.size(); index++){
			GameObject o = obj.get(index);
			if(o.hitBox.intersects(hitBox)){
				inContact = true;
				o.inContact = true;
				contact(o);
			}
		}
	}
	
	
	void updateHitbox(){

		hitBox.setBounds(x, y, w, h);
	}

	abstract void contact(GameObject obj);
	abstract void draw(Graphics g);
	
}
