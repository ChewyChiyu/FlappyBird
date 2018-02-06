import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
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

	int currentFPS;
	int MAX_FPS = 60;

	int bg1X, bg2X;

	final int SPACER;
	final int MAX_OBSTACLES = 6;
	
	Thread gameLoop;
	Runnable gameEngine;
	boolean isRunning;

	boolean initialClick;
	
	public GamePanel(Dimension dim){
		gameDim = dim;
		SPACER = (int) (gameDim.width * .75);
		panel();
		keys();
		start();
	}
	synchronized void start(){
		gameEngine = () -> gameLoop();
		gameLoop = new Thread(gameEngine);
		isRunning = true;
		gameLoop.start();
		initialVars();
	}

	void initialVars(){
		//reseting intialClick
		initialClick = false;
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
		sprites.add(new Ground(0,(int)(gameDim.getHeight()*.85)));
		sprites.add(new Ground(gameDim.width,(int)(gameDim.getHeight()*.85)));
	}

	void makeObstacle(int xBuffer){
		PipeStyle randStyle = PipeStyle.MIDDLE;
		switch((int)(Math.random()*2)){
		case 0:
			randStyle = PipeStyle.BOTTOM;
			break;
		case 1:
			randStyle = PipeStyle.TOP;
			break;
		}

		Pipe top = new Pipe(xBuffer, randStyle.yPosTop,true);
		Pipe bottom = new Pipe(xBuffer, randStyle.yPosBottom,false);
		sprites.add(top);
		sprites.add(bottom);
		//adding goal to go with pair
		sprites.add(new Goal(xBuffer - top.w + top.w/2,bottom.y - bottom.h -( bottom.y - (top.y + top.h)), Goal.WIDTH , bottom.y - (top.y + top.h) ));
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
			
		}
		//sort for ZMask
		sortZMask();
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
			obj.x-=1;
			
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
	}

}