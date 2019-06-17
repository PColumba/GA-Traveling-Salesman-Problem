
import java.util.Random;
import java.util.Arrays;

public class Individuals {

	
	public static double calculateFitness(int[] individual, Coordinates[] cities){

		if(individual.length != cities.length)
			throw new LengthMismatchException();
		
		return 1 / (Coordinates.getCumulativeDistance(individual, cities));
		
	}
	
	public static int[] breed(int[] mather, int[] father){
		
		if(mather.length != father.length)
			throw new LengthMismatchException();
		
		Random r = new Random();
		
		int start = r.nextInt(mather.length);
		int end = start + r.nextInt(mather.length - start);
		
		return crossOver(mather, father, start, end);
		
		
	}
	
	public static int[] breed(int[] mather, int[] father, int start, int end){
		
		if(mather.length != father.length)
			throw new LengthMismatchException();
		
		return crossOver(mather, father, start, end);
		
	}
	
	public static int[] mutate(int[] individual){
		
		Random r = new Random();
		int swapPosition1 = r.nextInt(individual.length);
		int swapPosition2 = r.nextInt(individual.length);
		
		while(swapPosition1 == swapPosition2)
			swapPosition2 = r.nextInt(individual.length);
		
		int aux = individual[swapPosition1];
		individual[swapPosition1] = individual[swapPosition2];
		individual[swapPosition2] = aux;
		
		return individual;
		
	}
	
	private static int[] crossOver(int[] a, int[] b, int start, int end) {
		
		if(end >= a.length || end >= b.length)
			throw new ArrayIndexOutOfBoundsException();
		
		int[] result = new int[a.length];
		Arrays.fill(result, -1);
		
		for(int i = start; i <= end; i++)
			result[i] = a[i];
		
		for(int i = 0, j = 0; i < b.length; i++){
			
			if(search(result, b[i], start, end) >= 0)
				continue;
			else if(j >= start && j <= end){
				result[j = end + 1] = b[i];
				j++;
			}
			else{
				result[j] = b[i];
				j++;
			}
		}		
		return result;
	}
	
	private static int search(int[] arr, int key, int start, int end){
		for(int i = start; i <= end; i++)
			if(arr[i] == key)
				return arr[i];		
		return -1;
	}
	

}