
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class TSP {
	
	private static final double DEFAULT_TOURNAMENT_FACTOR = 0.3;
	private static final double DEFAULT_MUTATION_RATE = 0.001;
	private static final int DEFAULT_POPULATION_SIZE = 100;
	private static final int DEFAULT_GENERATIONS = 1000;
	private static final int DEFAULT_THREAD_COUNT = 8;
	private static final int DEFAULT_NUMBER_OF_CITIES = 10;
	private static final int RANDOM_LIMIT = 100;
	
	private static double tournamentFactor = DEFAULT_TOURNAMENT_FACTOR;
	private static double mutationRate = DEFAULT_MUTATION_RATE;
	private static int populationSize = DEFAULT_POPULATION_SIZE;
	private static int generations = DEFAULT_GENERATIONS;
	private static int threadCount = DEFAULT_THREAD_COUNT;
	private static int numberOfCities = DEFAULT_NUMBER_OF_CITIES;
	private static String fileName = "";

	public static void main(String[] args){
		
		if(args.length > 0){
			try{
				for(int i = 0; i < args.length; i++){
					if(args[i].startsWith("tf="))
						tournamentFactor = Double.valueOf(args[i].substring(3));
					else if(args[i].startsWith("mr="))
						mutationRate = Double.valueOf(args[i].substring(3));
					else if(args[i].startsWith("ps="))
						populationSize = Integer.valueOf(args[i].substring(3));
					else if(args[i].startsWith("gen="))
						generations = Integer.valueOf(args[i].substring(4));
					else if(args[i].startsWith("threads="))
						threadCount = Integer.valueOf(args[i].substring(8));
					else if(args[i].startsWith("points="))
						numberOfCities = Integer.valueOf(args[i].substring(7));
					else if(args[i].equals("-help"))
						printUsage();
					else
						fileName = args[i];
				}
			} catch(NumberFormatException e){
				System.out.println("The arguments are badly formatted, please see usage below");
				printUsage();
			}
		}
		
		long start, executionTime;
		Population[] initialPopulations = new Population[threadCount];
		Population[] evolvedPopulations = new Population[threadCount];
		EvoTask[] tasks = new EvoTask[threadCount];
		List<Future<Population>> futures = new ArrayList<>();
		Coordinates[] cities = new Coordinates[numberOfCities];
		Random r = new Random();
		Printer printer = new Printer();
		
		//Read from file or Randomize cities
		if(fileName.length() > 0){
			try{
				cities = CitiesFromFile.readFrom(fileName);
			} catch(Exception e){
				System.out.println("There was a problem reading the file, make sure the file exists and is well formatted");
				System.out.println(e.getMessage());
				System.exit(0);
			}
		}else{		
			for(int  i = 0; i < cities.length; i++)
				cities[i] = new Coordinates(r.nextInt(RANDOM_LIMIT), r.nextInt(RANDOM_LIMIT)); 
		}
		//Print cities
		printer.printTitle("Cities coordinates");
		for(int  i = 0; i < cities.length; i++)
			System.out.println(cities[i].toString());
		
		//Print aglorithm parameters
		printer.printTitle("Algotithm parameters");
		System.out.println("Tournament Factor: " + tournamentFactor + System.lineSeparator() + 
			"Mutation Rate: " + mutationRate + System.lineSeparator() +
			"Population Size: " + populationSize + System.lineSeparator() +
			"Generations: " + generations + System.lineSeparator() +
			"Threads: " + threadCount);
		
		//Create initial populations
		for(int  i = 0; i < threadCount; i++)
			initialPopulations[i] = new Population(populationSize, cities.length, cities);
		
		//Create evolution tasks
		for(int i = 0; i < threadCount; i++)
			tasks[i] = new EvoTask(initialPopulations[i],mutationRate,tournamentFactor, generations);
		
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		
		printer.printTitle("Initial populations best distance");
		System.out.printf("Best distance: %.2f" + System.lineSeparator(), bestDistanceFromPopulations(initialPopulations));
		printer.printTitle("Starting evolution");
		
		start = System.nanoTime();
		for(int i = 0; i < threadCount; i++)
			futures.add(executor.submit(tasks[i]));
		
		try{
			for(int i = 0; i < threadCount; i++)
				evolvedPopulations[i] = futures.get(i).get();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		executionTime = System.nanoTime() - start;
		
		printer.printTitle("Finished");
		System.out.println("Populations count : " + threadCount);
		System.out.println("Best distances from evolved populations");
		for(int i = 0; i < threadCount; i++)
			System.out.printf("Population " + i + " Best distance: %.2f" + System.lineSeparator(), 
				evolvedPopulations[i].getFittestFitness());
		System.out.printf("Best distance: %.2f" + System.lineSeparator(),bestDistanceFromPopulations(evolvedPopulations));
		System.out.println("Execution time[ms]: " + executionTime / 1000000);
		executor.shutdown();
		
		printer.printTitle("Starting brute solution");
		start = System.nanoTime();
		double resultBruteSolution = BruteSolution.getMinimumDistance(cities);
		executionTime = System.nanoTime() - start;
		printer.printTitle("Finished");
		System.out.printf("Brute solution minimum distance: %.2f" + System.lineSeparator(), resultBruteSolution);
		System.out.println("Execution time[ms]: " + executionTime / 1000000);
		
	}
	
	private static double bestDistanceFromPopulations(Population[] populations){
		
		double bestAmongPop = populations[0].getFittestFitness();
		for(Population pop : populations){
			double bestInPop = pop.getFittestFitness();
			if(bestInPop < bestAmongPop)
				bestAmongPop = bestInPop;
		}
		return bestAmongPop;
	}
	
	private static void printUsage(){
		
		System.out.println("Usage: java TSP [option=value] [fileSource]" + Printer.repeatString(System.lineSeparator(),2) +
		"Where possible options/values are:" + Printer.repeatString(System.lineSeparator(),2) +
		"tf=double -Tournament factor specification, i.e what fration of population will take part" + Printer.repeatString(System.lineSeparator(),2) + 
		"           in selection of the fittest individual, default 0.3" + Printer.repeatString(System.lineSeparator(),2) +
		"mr=double -Rate of mutation, default 0.001"+ Printer.repeatString(System.lineSeparator(),2) +
		"ps=integer -Population size, default 100"+ Printer.repeatString(System.lineSeparator(),2) +
		"gen=integer -Number of generations, default 1000" + Printer.repeatString(System.lineSeparator(),2) +
		"threads=integer -Number of parallel threads, each evolving a single population, defualt 8"+ Printer.repeatString(System.lineSeparator(),2) +
		"points=integer -Number of cities/points, not relevant if source file is specified, default 10 " + Printer.repeatString(System.lineSeparator(),2) +
		"fileSource - CSV file with \";\" as separator e.g 60;78. Values need to be integers." + 
		"If not specified random locations will be generated" + Printer.repeatString(System.lineSeparator(),2) +
		"TSP -help to print this.");
		System.exit(0);
	}
	
	static class Printer {
		
		private int outputWidth = 50;
		private int contentWidth = outputWidth - 2;
		
		Printer(){}
		
		Printer(int outputWidth){
			this.outputWidth = outputWidth;
		}
		
		private void printTitle(String title){
			
			StringTokenizer strTokenizer = new StringTokenizer(title, " ");
			StringBuilder strBuilder = new StringBuilder();
			int lenDifference = 0;
			printBar();
			
			if(strTokenizer.countTokens() == 1 && (title.length() > outputWidth - 2)){			
				String str = strTokenizer.nextToken();
				for(int i = 0; i < str.length(); i +=outputWidth - 2){
					strBuilder.append("|");
					if((i + outputWidth - 2) > str.length()){
						strBuilder.append(str.substring(i));
						lenDifference = outputWidth - strBuilder.length() % (outputWidth + System.lineSeparator().length());
						strBuilder.append(repeatString(" ",lenDifference - 1));
						strBuilder.append("|");
					}	
					else{
						strBuilder.append(str.substring(i,i + outputWidth - 2));
						strBuilder.append("|");
						strBuilder.append(System.lineSeparator());
					}	
				}	
				System.out.println(strBuilder.toString());
			} else if(title.length() > outputWidth - 2){
					
				strBuilder.append("|");
				
				while(strTokenizer.hasMoreTokens()){
					String token = strTokenizer.nextToken();
					if((strBuilder.length() % outputWidth + token.length()) + 1 >= outputWidth){
						lenDifference = outputWidth - strBuilder.length() % (outputWidth + System.lineSeparator().length());
						strBuilder.append(repeatString(" ",lenDifference - 1));
						strBuilder.append("|");
						strBuilder.append(System.lineSeparator());
						strBuilder.append("|");
						strBuilder.append(token + " ");
					}
					else{
						strBuilder.append(token + " ");
					}
				}
				lenDifference = outputWidth - strBuilder.length() % (outputWidth + System.lineSeparator().length());
				strBuilder.append(repeatString(" ",lenDifference - 1));
				strBuilder.append("|");
				System.out.println(strBuilder.toString());
			} else{
				strBuilder.append("|");
				while(strTokenizer.hasMoreTokens())
					strBuilder.append(strTokenizer.nextToken() + " ");
				String result = strBuilder.toString().trim();
				lenDifference = outputWidth - result.length();
				result += repeatString(" ",lenDifference - 1) + "|";
				System.out.println(result);
			}
			printBar();
		}
		
		private void printBar(){
			
			System.out.print("|");
			for(int i = 0; i < outputWidth - 2; i++)
				System.out.print("-");
			System.out.print("|" + System.lineSeparator());
		}		
		
		private static String repeatString(String element,int times){
			
			StringBuilder strBuilder = new StringBuilder();
			for(int i = 0; i < times; i++)
				strBuilder.append(element);
			return strBuilder.toString();
		}
	}
}