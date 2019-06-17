
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;


public class Population {
	
	private int individualSize;
	
	private List<int[]> population;
	
	private Coordinates[] cities;
	
	Population(int populationSize, int individualSize, Coordinates[] cities){
		
		this.cities = cities;
		this.individualSize = individualSize;
		population = new ArrayList<int[]>();
		
		for(int i = 0; i < populationSize; i++){			
			int[] individual = new int[individualSize];
			for(int j = 0; j < individualSize; j++)
				individual[j] = j;
			
			shuffle(individual);
			population.add(individual);
		}	
	}
	
	Population(Coordinates[] cities){
		this.population = new ArrayList<int[]>();
		this.cities = cities;
	}
	
	public Coordinates[] getCities(){
		return this.cities;
	}
	
	public int getPopulationSize(){
		return this.population.size();
	}
	
	public int getindividualSize(){
		return this.individualSize;
	}
	
	private void swap(int[] arr, int i, int j){
		
		int aux = arr[i];
		arr[i] = arr[j];
		arr[j] = aux;
		
	}
	
	private void shuffle(int[] arr){
		
		Random r = new Random();
		int j;
		
		for(int i = arr.length; i > 1; i--){
			j = r.nextInt(i);
			swap(arr, (i-1), j);
		}
		
	}

	public void add(int[] individual){
		this.population.add(individual);
	}
	
	public int[] get(int i){
		return this.population.get(i);
	}
	
	@Override
	public String toString(){
		
		StringBuilder sbr = new StringBuilder();
		
		for(int[] item : this.population)
			sbr.append(Arrays.toString(item) + System.lineSeparator());
		
		return sbr.toString();
	}
	
	public int[] getFittest(){
		
		int[] fittest = this.population.get(0);
		for(int[] individual : this.population)
			if(Individuals.calculateFitness(individual, this.cities) > Individuals.calculateFitness(fittest, this.cities))
				fittest = individual;
		return fittest;
	}
	
	public double getFittestFitness(){
		return Coordinates.getCumulativeDistance(getFittest(),this.cities);
	}
	
	public int[] tournamentSelection(double tournamentFactor){
		
		Population newPop = new Population(this.cities);
		Random r = new Random();
		int popSize = this.getPopulationSize();
		int tournamentSize = (int) (popSize * tournamentFactor);
		
		for(int i = 0; i < tournamentSize; i++)
			newPop.add(this.population.get(r.nextInt(popSize)));
		
		return newPop.getFittest();
		
	}
	
	
}