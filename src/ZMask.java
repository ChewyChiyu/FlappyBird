
public enum ZMask {
	PIPE(2), GROUND(3), GOAL(1);
	
	int zPosition;
	private ZMask(int z){
		zPosition = z;
	}
}
