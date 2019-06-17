# GA-Traveling-Salesman-Problem
A genetic algorithm that solves the traveling salesman problem.
You can read about the problem on [wikipedia](https://en.wikipedia.org/wiki/Travelling_salesman_problem).
In brief the problem is defined as follows: Given the list of points find the shortest way that connects all points (each point can be visited only once). Brute solution has O(n!) complexity, which means it's only reasonably practical for small number of points.

### Algorithm overview:
* Each individual is represented as array of unique integers from **0 to n-1** where **n** is the number of points (also reffered to as cities/locations). The ordering of integers represents the route order, e.g individual {2, 0, 1, 3} represents a path 2-> 0-> 1-> 3.
* Fitness function is the inverse of the distance between points.
* Selection of individuals for breeding is done via tournament selection. A subset of the population is created with the size of (tournament_factor x population_size), then the fittest individual is selected from the subset. This is done again to find his breeding mate.
* Crossover is accomplished via copying a part of random length from one individual and then filling the remaining space with the content of his breeding mate. Example: Breeding of {2,0,4,1,3,5} and {4,0,2,3,5,1} with crossover points of 2 and 4 (defining the bounds of the part that is to be extracted from first individual), results with a child {0,2,4,1,3,5}.
* Mutation occurs at a specified mutation rate (defualt = 0.001) and results in swapping places between two points selected at random.

### Implementation details:
* The genetic algorithm will run on several threads each representing a different population.
* The output of genetic algorithm is compared with the results of brute force solution (if it finishes :)).The execution time is also compared. Note: it not always converges.

### How to run:
* Run the jar using: `java -jar TSP_GA.jar [options] [source file]`
* Using the options you can specify algorithm parameters like tournament factor, mutation rate, population size, number of generations and more, use `java -jar TSP_GA.jar -help` to see all options.
* Source file specifies the coordinates of points and should: 
1. Be a CSV file with ; as delimiter, no trailing spaces/newlines allowed
2. Contain two columns with integer values only.
if not specified, random points witthin 100 x 100 square will be created.


