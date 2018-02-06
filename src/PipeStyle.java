
public enum PipeStyle {
	TOP((int)(FlappyBirdLauncher.gameDim.getHeight()*0.15),(int)(FlappyBirdLauncher.gameDim.getHeight()*1.10)),
	MIDDLE((int)(FlappyBirdLauncher.gameDim.getHeight()*0.3),(int)(FlappyBirdLauncher.gameDim.getHeight()*1.25)),
	BOTTOM((int)(FlappyBirdLauncher.gameDim.getHeight()*0.45),(int)(FlappyBirdLauncher.gameDim.getHeight()*1.40));
	
	int yPosTop;
	int yPosBottom;
	private PipeStyle(int yT, int yB){
		yPosTop = yT;
		yPosBottom = yB;
	}
}
