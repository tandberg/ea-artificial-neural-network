package EA;

import java.util.*;
public class EvolutionAlgorithm {

    public final static Random random = new Random();

    private final static int POPULATION                 = 30;               // Number of genomes
    private final static int SIZE                       = 40;               // Number of bits
    private final static int MAX_ITERATIONS             = 100;
    private final static int OVER_PRODUCTION_CHILDREN   = POPULATION * 2;
    private final static int ELITISM                    = 5;
    private final static int TOURNAMENT_SIZE            = 4;
    private final static double MUTATION_PERCENT        = 0.02;
    private final static double EPSILON                 = 0.2;
    public  final static double CROSSOVER_RATE          = 0.2;

    public final static int SYMBOLSET_SIZE              = 20;
    private final static int SEQUENCE_LENGTH            = 199;

    public final static boolean USE_LOCAL               = true;
    public final static boolean USE_RANDOM_TARGET       = true;
    public final static int[] RANDOM_TARGET             = {0,1,1,1,0,0,1,0,1,1,1,0,0,1,1,0,1,1,0,0,0,1,1,1,1,0,1,1,0,0,0,1,0,0,0,0,1,1,1,1};



    private static int PROBLEM_TYPE;
    private static int PARENT_MATE_SELECTION_MECHANISM;
    private static int ADULT_SELECTION;

    private List<Genotype> population;
    private Statistics statistics;

    public EvolutionAlgorithm() {

        statistics = new Statistics();

        selectUsecases();
        initializePopulation();
        runLoop();

        System.out.println("\n"+ statistics);
    }

    private void selectUsecases() {
        Scanner sc = new Scanner(System.in);
        int selection;
        do {
            System.out.println("Select problem: \n1. One-max\n2. Surprising Sequences");
            selection = sc.nextInt();
            System.out.println(selection >= 1 && selection <= 2);
        } while (!(selection >= 1 && selection <= 2));
        PROBLEM_TYPE = selection;

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

    /* TODO: 3 steg med adult selection

        1. Bytte ut alle voksne med barn                                                                                - done
        2. Overproduksjon av unger. Feks har population size pa 10, lager 20 unger og de 10 beste blir igjen            - done
        3. Mixing av generasjoner. Tar vare pa M gamle, disse jobber mot de N nye unga.                                 - done
    */

    /* TODO: Parent mate selection

        1. fitness-proportionate  - done
        2. sigma-scaling
        3. tournament selection
        4. ?????

    */

    private void runLoop() {
        int iteration = 0;
        statistics.updateStatistics(population);
        while(!complete() && MAX_ITERATIONS > (++iteration)) {

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
            EvolutionAlgorithm.evolveChildren(children);


            statistics.updateStatistics(population);
//            System.out.println(population);
            System.out.println("Iteration " + iteration + ": max = " + statistics.getMax(iteration) + ", avg = " + statistics.getAvg(iteration) + ", std = " + statistics.getStd(iteration) + "\tbest: " + statistics.getBest(iteration));
        }
    }

    private Genotype newChild(Genotype parent1, Genotype parent2) {
        switch (PROBLEM_TYPE) {
            case 1:
                return new Genome(parent1, parent2);
            case 2:
                return new SurprisingSequences(parent1, parent2);
        }
        return null;
    }

    private Genotype newRandomChild() {
        switch (PROBLEM_TYPE) {
            case 1:
                return new Genome(SIZE);
            case 2:
                return new SurprisingSequences(SEQUENCE_LENGTH);
        }
        return null;
    }

    private boolean complete() {
        for (Genotype genome : population) {
            if(genome.done(SIZE)) {
                return true;
            }
        }
        return false;
    }

    private void initializePopulation() {
        population = new ArrayList<Genotype>();
        for (int i = 0; i < POPULATION; i++) {
            population.add(newRandomChild());
        }
    }


    public String toString() {
        return "EvolutionAlgorithm";
    }

    public static void evolveChildren(List<Genotype> children) {
        for (Genotype child : children) {
            child.mutate(MUTATION_PERCENT);
        }
    }

    public static void main(String[] args) {
        new EvolutionAlgorithm();
    }
}
