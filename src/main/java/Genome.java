import java.util.Arrays;

public class Genome extends Genotype {

    int[] array;

    public Genome(int size) {
        array = new int[size];
        randomize();
    }

    private void randomize() {
        for (int i = 0; i < array.length; i++) {
            array[i] = EvolutionAlgorithm.random.nextInt(2);
        }
    }

    public Genome(int[] array) {
        this(array.length);
        this.array = Arrays.copyOf(array, array.length);
    }

    public Genome(Genotype parent1, Genotype parent2) {

        if(EvolutionAlgorithm.random.nextDouble() >= EvolutionAlgorithm.CROSSOVER_RATE) {

            if(EvolutionAlgorithm.random.nextBoolean()) {
                this.array = Arrays.copyOf(parent1.getArray(), parent1.getArray().length);
            } else {
                this.array = Arrays.copyOf(parent2.getArray(), parent2.getArray().length);
            }
            return;
        }

        int[] parent1_arr = parent1.getArray();
        int[] parent2_arr = parent2.getArray();
        int[] child_arr = new int[parent1_arr.length];

        for (int i = 0; i < child_arr.length; i++) {
            if(EvolutionAlgorithm.random.nextBoolean()) {
                child_arr[i] = parent1_arr[i];
            } else {
                child_arr[i] = parent2_arr[i];
            }
        }
        this.array = child_arr;
    }

    public int[] getArray() {
        return array;
    }

    @Override
    public boolean done(int size) {
        return sum() == size;
    }

    private void flip(int i) {
        array[i] = array[i] == 1 ? 0 : 1;
    }

    public double sum() {
        double sum = 0;
        if(EvolutionAlgorithm.USE_RANDOM_TARGET) {
            for (int i = 0; i < array.length; i++) {
                if(array[i] == EvolutionAlgorithm.RANDOM_TARGET[i]) {
                    sum += 1;
                }
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                sum += array[i];
            }
        }
        return sum;
    }

    public double fitness() {

        return Math.exp(- (array.length - sum()));
    }

    public void mutate(double mutationPercent) {
        for (int i = 0; i < array.length; i++) {
            double random = EvolutionAlgorithm.random.nextDouble();
            if(random <= mutationPercent) {
                flip(i);
            }
        }
    }

    @Override
    public String toPhenotype() {
        int sum = 0;
        for (int i : array) {
            sum += i;
        }
        return "Phenotype: " + sum + " string: " + Arrays.toString(array);
    }

    public String toString() {
        return toPhenotype();
//        return fitness() + ": " +Arrays.toString(array) +"\n";
    }

    @Override
    public int compareTo(Genotype genome) {
        return fitness() > genome.fitness() ? -1 : 1;
//        return fitness() - genome.fitness();
    }
}
