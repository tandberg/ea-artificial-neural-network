package EA;

import java.util.*;
import Flatland.*;

public class EvolutionaryAlgorithm {

    public final static Random random = new Random();

    private final static int POPULATION                 = 200;               // Size of the population
    private final static int SIZE                       = 40;               // Number of bits. 0 and 1 in each individual
    private final static int MAX_ITERATIONS             = 500;

    private final static int OVER_PRODUCTION_CHILDREN   = POPULATION * 2;
    private final static int ELITISM                    = 5;
    private final static int TOURNAMENT_SIZE            = 4;
    private final static double MUTATION_PERCENT        = 0.02;
    private final static double EPSILON                 = 0.2;
    public  final static double CROSSOVER_RATE          = 0.2;

    private static int PARENT_MATE_SELECTION_MECHANISM;
    private static int ADULT_SELECTION;

    private List<Genotype> population;
    private Statistics statistics;

    private static boolean SELECT_USE_CASES             = false;
    private static boolean USE_STATISTICS               = false;

    public EvolutionaryAlgorithm() {

        if(USE_STATISTICS)
            statistics = new Statistics();

        if(SELECT_USE_CASES) {
            selectUsecases();
        } 
        else {
            PARENT_MATE_SELECTION_MECHANISM = 3;
            ADULT_SELECTION = 3;
        }
        
        initializePopulation();
        runLoop();

        System.out.println("Best fitness: " + population.get(0).fitness());

       ((Agent)population.get(0)).printToFile();

        if(USE_STATISTICS)
            System.out.println("\n"+ statistics);
    }

    private void selectUsecases() {
        Scanner sc = new Scanner(System.in);
        int selection;
        do {
            System.out.println("Parent mate selection: \n1. Fitness-proportionate\n2. Sigma-scaling\n3. Tournament selection\n4. Boltzmann selection");
            selection = sc.nextInt();
        } while (!(selection >= 1 && selection <= 4));
        PARENT_MATE_SELECTION_MECHANISM = selection;

        do {
            System.out.println("\nAdult selection: \n1. Full population replacement (A-I)\n2. Over production (A-II)\n3. Generation mixing (A-III)");
            selection = sc.nextInt();
        } while (!(selection >= 1 && selection <= 3));
        ADULT_SELECTION = selection;
    }

    private void runLoop() {
        int iteration = 0;

        if(USE_STATISTICS)
            statistics.updateStatistics(population);
        
        while(MAX_ITERATIONS > (++iteration)) {
            System.out.println(iteration);
            List<Genotype> children = new ArrayList<Genotype>();
            Tuple[] wheel;
            switch (PARENT_MATE_SELECTION_MECHANISM) {
                case 1:
                    wheel = Selection.fitnessProportionate(population);
                    for (int i = 0; i < (ADULT_SELECTION == 2 ? OVER_PRODUCTION_CHILDREN:POPULATION); i++) {
                        Genotype parent1 = Selection.rouletteWheel(wheel, population);
                        Genotype parent2 = Selection.rouletteWheel(wheel, population);
                        Genotype child = newChild(parent1, parent2);
                        child.mutate(MUTATION_PERCENT);
                        children.add(child);
                    }

                    break;
                case 2:
                    wheel = Selection.sigmaScaling(population);
                    for (int i = 0; i < (ADULT_SELECTION == 2 ? OVER_PRODUCTION_CHILDREN:POPULATION); i++) {
                        Genotype parent1 = Selection.rouletteWheel(wheel, population);
                        Genotype parent2 = Selection.rouletteWheel(wheel, population);
                        Genotype child = newChild(parent1, parent2);
                        child.mutate(MUTATION_PERCENT);
                        children.add(child);
                    }
                    break;
                case 3:

                    for (int i = 0; i < (ADULT_SELECTION == 2 ? OVER_PRODUCTION_CHILDREN:POPULATION); i++) {
                        Genotype parent1 = Selection.tournamentSelection(population, TOURNAMENT_SIZE, EPSILON);
                        Genotype parent2 = Selection.tournamentSelection(population, TOURNAMENT_SIZE, EPSILON);
                        Genotype child = newChild(parent1, parent2);
                        child.mutate(MUTATION_PERCENT);
                        children.add(child);
                    }
                    break;
                case 4:
                    wheel = Selection.boltzmannSelection(population, iteration);
                    for (int i = 0; i < (ADULT_SELECTION == 2 ? OVER_PRODUCTION_CHILDREN:POPULATION); i++) {
                        Genotype parent1 = Selection.rouletteWheel(wheel, population);
                        Genotype parent2 = Selection.rouletteWheel(wheel, population);
                        Genotype child = newChild(parent1, parent2);
                        child.mutate(MUTATION_PERCENT);
                        children.add(child);
                    }
                    break;
                default: System.exit(1);
            }

            switch (ADULT_SELECTION) {
                case 1:
                    population = Selection.fullPopulationReplacement(population, children);
                    break;
                case 2:
                    population = Selection.overProduction(population, children);
                    break;
                case 3:
                    population = Selection.generationMixing(population, children, ELITISM);
                    break;
                default: System.exit(2);
            }
            EvolutionaryAlgorithm.evolveChildren(children);

            if(USE_STATISTICS)
                statistics.updateStatistics(population);

        }
    }

    private Genotype newChild(Genotype parent1, Genotype parent2) {
        return new Agent(parent1, parent2, CROSSOVER_RATE);
    }

    private Genotype newRandomChild() {
        return new Agent();
    }

    private void initializePopulation() {
        population = new ArrayList<Genotype>();
        for (int i = 0; i < POPULATION; i++) {
            population.add(newRandomChild());
        }
    }

    public String toString() {
        return "Evolutionary Algorithm, population size: " + population.size();
    }

    public static void evolveChildren(List<Genotype> children) {
        for (Genotype child : children) {
            child.mutate(MUTATION_PERCENT);
        }
    }

    public static void main(String[] args) {
        new EvolutionaryAlgorithm();
    }
}
