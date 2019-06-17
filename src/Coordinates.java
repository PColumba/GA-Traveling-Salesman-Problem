

public class Coordinates {
	
	private int x;
	private int y;
	
	Coordinates(int x, int y){
		this.x = x;
		this.y = y; 
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}

	private static double getDistance(Coordinates p1, Coordinates p2){
		return Math.sqrt( Math.pow( (double) (p1.getX() - p2.getX()), 2.0) + Math.pow( (double) (p1.getY() - p2.getY()), 2.0) );
	}
	
	public static double getCumulativeDistance(int[] order, Coordinates[] points) {
		
		if(order.length != points.length)
			throw new LengthMismatchException();
		
		double distance = 0;
		
		for (int i = 0; i < (order.length - 1); i++){
			distance += getDistance(points[order[i]],points[order[i+1]]);
		}
		
		return distance;
	}
	
	@Override
	public String toString(){
		return this.getX() + ", " + this.getY();
	}

}