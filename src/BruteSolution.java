import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class BruteSolution {
	
	private static final int[] EMPTY_ARRAY = new int[0];

	private static double minDistance;
	
	private static Coordinates[] locations;
	
	private BruteSolution(){};
	
	private static int[] merge(int[] a, int[] b){
		int newLength = a.length + b.length;
		int[] result = new int[newLength];
		if(a.length == 0 && b.length == 0){
			return EMPTY_ARRAY;
		}
		if(a.length == 0){
			for(int i = 0; i < result.length; i++)
				result[i] = b[i];
			return result;
		}
		if(b.length == 0){
			for(int i = 0; i < result.length; i++)
				result[i] = a[i];
			return result;
		}
		for(int i = 0; i < result.length; i++)
			result[i] = (i < a.length) ? a[i] : b[i - a.length]; 
		return result;
	}
	
	private static int[] apend(int[] a, int b){	
		int[] result = new int[a.length + 1];
		if(a.length == 0){
			result[0] = b;
			return result;
		} 
		for(int i = 0; i < a.length; i++)
			result[i] = a[i];
		result[result.length - 1] = b;
		return result;
	}

	private static void permutationEntry(int[] initialArray){
		permutation(EMPTY_ARRAY, initialArray);
	}

	private static void permutation(int[] subArray, int[] initialArray) {
		int n = initialArray.length;
		if (n == 0){
			double sum = Coordinates.getCumulativeDistance(subArray, locations);
			if(sum < minDistance)
				minDistance = sum;
		}
		else {
			for (int i = 0; i < n; i++)
				permutation(apend(subArray,initialArray[i]), merge(Arrays.copyOfRange(initialArray,0,i),Arrays.copyOfRange(initialArray,i+1,n)));
		}
	}
	
	public static double getMinimumDistance(Coordinates[] locations){
		BruteSolution.locations = locations;
		int[] initialArray = new int[locations.length];
		for(int i = 0; i < initialArray.length; i++)
			initialArray[i] = i;
		minDistance = Coordinates.getCumulativeDistance(initialArray,locations);
		permutationEntry(initialArray);
		return minDistance;
	}	
	
}