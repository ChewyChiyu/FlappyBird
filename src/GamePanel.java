import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class GamePanel extends JPanel{

	Dimension gameDim;
	ArrayList<GameObject> sprites = new ArrayList<GameObject>();

	int score;
	
	int currentFPS;
	int MAX_FPS = 60;

	int bg1X, bg2X;

	final int SPACER;
	final int MAX_OBSTACLES = 6;

	final int GRAVITY = 1;
	final int MAX_GRAVITY = 12;

	Thread gameLoop;
	Runnable gameEngine;
	boolean isRunning;

	boolean initialClick;
	boolean releasedSpace = true;

	Bird flappy;

	public GamePanel(Dimension dim){
		gameDim = dim;
		SPACER = (int) (gameDim.width * .75);
		panel();
		keys();
		start();
	}
	synchronized void start(){
		initialVars();
		gameEngine = () -> gameLoop();
		gameLoop = new Thread(gameEngine);
		isRunning = true;
		gameLoop.start();
	}

	void initialVars(){
		//reseting intialClick
		initialClick = false;
		//reseting score
		score = 0;
		//reseting bg1 and bg2 values
		bg1X = 0;
		bg2X = (Texture.background.getWidth() * Texture.BACKGROUND_SCALE) / 2;

		//make pipes
		int xBuffer = (int) (gameDim.width * 1.5);

		for(int index = 0; index < MAX_OBSTACLES; index++){
			makeObstacle(xBuffer);
			xBuffer += SPACER;
		}
		//add ground
		sprites.add(new Ground(0,(int)(gameDim.getHeight()*.85),this));
		sprites.add(new Ground(gameDim.width,(int)(gameDim.getHeight()*.85),this));
		//add bird
		flappy = new Bird((int)(gameDim.getWidth()/2),(int)(gameDim.getHeight()/2),this);
		sprites.add(flappy);
	}

	void makeObstacle(int xBuffer){
		PipeStyle randStyle = PipeStyle.MIDDLE;
		switch((int)(Math.random()*3)){
		case 0:
			randStyle = PipeStyle.BOTTOM;
			break;
		case 1:
			randStyle = PipeStyle.TOP;
			break;
		}

		Pipe top = new Pipe(xBuffer, randStyle.yPosTop,true,this);
		Pipe bottom = new Pipe(xBuffer, randStyle.yPosBottom,false,this);
		sprites.add(top);
		sprites.add(bottom);
		//adding goal to go with pair
		sprites.add(new Goal(xBuffer - top.w + top.w/2,bottom.y - bottom.h -( bottom.y - (top.y + top.h)), Goal.WIDTH , bottom.y - (top.y + top.h) ,this));
	}

	void gameLoop(){ //standard gameLoop
		long previousTime = System.currentTimeMillis();
		long currentTime = previousTime;
		long elapsedTime;
		long totalElapsedTime = 0;
		int frameCount = 0;

		while(isRunning)
		{
			currentTime = System.currentTimeMillis();
			elapsedTime = (currentTime - previousTime); 
			totalElapsedTime += elapsedTime;

			if (totalElapsedTime > 1000)
			{
				currentFPS = frameCount;
				frameCount = 0;
				totalElapsedTime = 0;
			}

			update();

			try
			{
				Thread.sleep(getFpsDelay(MAX_FPS));
			} catch (Exception e) {
				e.printStackTrace();
			}

			previousTime = currentTime;
			frameCount++;

		}

	}
	int getFpsDelay(int desiredFps)
	{
		return 1000 / desiredFps;
	}


	void update(){ //Based off worldTick		
		manageBackground();
		manageObjects();
		repaint();
	}

	void manageObjects(){
		for(int index = 0; index < sprites.size(); index++){
			GameObject obj = sprites.get(index);
			//updating hitboxes
			obj.updateHitbox();
			//checking collisions
			obj.checkForContact(sprites);
		}
		//sort for ZMask
		sortZMask();
		if(initialClick){ //only gravity if firstClick
			//gravity and angle change for bird
			if(flappy.dy < MAX_GRAVITY){
				flappy.dy += GRAVITY;
			}
			//changing bird angle
			flappy.angle += (flappy.angle < Math.PI/2 ) ? (Math.PI / 60) : 0;
			//moving bird
			flappy.y += flappy.dy;
		}
	}

	void sortZMask(){
		Collections.sort(sprites, new Comparator<GameObject>() {
			@Override
			public int compare(GameObject lhs, GameObject rhs) {
				// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
				return lhs.z < rhs.z ? -1 : (lhs.z > rhs.z) ? 0 : 1;
			}
		});
	}

	void manageBackground(){//if bg1X or bg2X is < Texture.background.getWidth() * Texture.BACKGROUND_SCALE / 2, += Texture.background.getWidth() * Texture.BACKGROUND_SCALE / 2

		bg1X--;
		bg2X--;
		if(bg1X < -Texture.background.getWidth() * Texture.BACKGROUND_SCALE/2 ){
			bg1X += Texture.background.getWidth() * Texture.BACKGROUND_SCALE ;
		}
		if(bg2X < -Texture.background.getWidth() * Texture.BACKGROUND_SCALE/2 ){
			bg2X += Texture.background.getWidth() * Texture.BACKGROUND_SCALE ;
		}
		//moving sprites as well
		for(int index = 0; index < sprites.size(); index++){
			GameObject obj = sprites.get(index);
			if((obj instanceof Pipe || obj instanceof Goal) && !initialClick){ //do not move obstacles if not initial click
				continue;
			}
			if(!(obj instanceof Bird)){ //move everything except bird
				obj.x-=4;
			}

			if(obj instanceof Goal){
				if(obj.x < -(Texture.PIPE_SCALE * Texture.pipe.getWidth())/2){ //if past meaning pipe has already been removed pass threshhold
					sprites.remove(index);
					index--;
					makeObstacle((MAX_OBSTACLES * SPACER) );
				}
			}

			if(obj instanceof Pipe){ //removing instance of pipe
				if(obj.x < 0){
					sprites.remove(index);
					index--;
				}
			}

			if(obj instanceof Ground){
				if(obj.x < -gameDim.getWidth()){
					obj.x = (int) (gameDim.getWidth());
				}
			}

		}
	}
	void keys(){
		getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "jump");
		getActionMap().put("jump", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!initialClick){ initialClick = true; }
				if(releasedSpace){
					releasedSpace = false;
					flappy.jump();
				}
			}

		});
		getInputMap().put(KeyStroke.getKeyStroke("released SPACE"), "rjump");
		getActionMap().put("rjump", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				releasedSpace = true;

			}

		});


	}
	void panel(){
		JFrame frame = new JFrame("Flappy Bird");
		frame.add(this);
		frame.setPreferredSize(gameDim);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//draw backGrounds
		g.drawImage(Texture.background, bg1X, 0, (Texture.background.getWidth() * Texture.BACKGROUND_SCALE) /2 ,Texture.background.getHeight() * Texture.BACKGROUND_SCALE, this);
		g.drawImage(Texture.background, bg2X, 0, (Texture.background.getWidth() * Texture.BACKGROUND_SCALE) /2 ,Texture.background.getHeight() * Texture.BACKGROUND_SCALE, this);
		//drawing sprites
		for(int index = 0 ; index < sprites.size(); index++){
			GameObject obj = sprites.get(index);
			obj.draw(g);
		}
		//drawing score
		//score to texture
		String num = ""+score;
		int xBuffer = gameDim.width/2 - (((num.length()+1)/2) * (Texture.numbers[0].getWidth() * Texture.NUM_SCALE));
		int yBuffer = (int) (gameDim.getHeight() / 7);
		final int SPACER = Texture.numbers[0].getWidth() * Texture.NUM_SCALE;
		for(int index = 0; index < num.length(); index++){
			BufferedImage b = Texture.numbers[Integer.parseInt(num.substring(index, index+1))];
			g.drawImage(b, xBuffer, yBuffer, b.getWidth() * Texture.NUM_SCALE, b.getHeight() * Texture.NUM_SCALE, this);
			xBuffer+=SPACER;
		}
	}
}
