import java.util.Random;
import java.util.concurrent.Callable;

public class EvoTask implements Callable<Population> {

	private Population generation;
	private double mutationRate;
	private double tournamentFactor;
	private int generationsCount;
	
	
	EvoTask(Population generation, double mutationRate, double tournamentFactor, int generationsCount){
		
		this.generation = generation;
		this.mutationRate = mutationRate;
		this.tournamentFactor = tournamentFactor;
		this.generationsCount = generationsCount;

	}
	
	public Population call() throws Exception {

		Random r = new Random();
		
		for(int i = 0; i < generationsCount; i++){
			
			Population nextGeneration = new Population(generation.getCities());
		
			for(int j = 0; j < generation.getPopulationSize(); j++){
				
				int[] child = Individuals.breed(generation.tournamentSelection(tournamentFactor), generation.tournamentSelection(tournamentFactor));
				
				if(r.nextDouble() < mutationRate)
					nextGeneration.add(Individuals.mutate(child));
				else
					nextGeneration.add(child);
			}
			
			generation = nextGeneration;
		}
		
		return generation;
	}




}