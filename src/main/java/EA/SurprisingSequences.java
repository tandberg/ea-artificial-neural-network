package EA;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SurprisingSequences extends Genotype {

    private int[] array;

    public SurprisingSequences(int length) {
        array = new int[length];
        randomize();
    }

    public SurprisingSequences(Genotype parent1, Genotype parent2) {
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

    private int fails() {
        Map<String, Integer> map = new HashMap<String, Integer>();

        for (int i = 0; i < array.length; i++) {
            for (int j = i; j < array.length; j++) {
                if(i != j) {
                    String now = array[i] + "-" + (j-i) + "-" + array[j];
                    if(map.containsKey(now)) {
                        map.put(now, map.get(now) + 1);
                    } else {
                        map.put(now, 0);
                    }
                }
            }
        }

        int fails = 0;
        for (Integer integer : map.values()) {
            fails += integer;
        }

        return fails;
    }

    private int localFails() {
        Map<String, Integer> map = new HashMap<String, Integer>();

        for (int i = 0; i < array.length-1; i++) {
                    String now = array[i] + "-" + 0 + "-" + array[i+1];
                    if(map.containsKey(now)) {
                        map.put(now, map.get(now) + 1);
                    } else {
                        map.put(now, 0);
                    }
        }

        int fails = 0;
        for (Integer integer : map.values()) {
            fails += integer;
        }

        return fails;
    }

    @Override
    public double fitness() {
        return 1.0 / (1 + (EvolutionAlgorithm.USE_LOCAL ? localFails() : fails()));
    }

    public void flip(int i) {
        array[i] = EvolutionAlgorithm.random.nextInt(EvolutionAlgorithm.SYMBOLSET_SIZE);
    }

    @Override
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
        StringBuilder sb = new StringBuilder();
        for (int i : array) {
            sb.append(i + " ");
        }
        return sb.toString();
    }

    @Override
    public int[] getArray() {
        return array;
    }

    @Override
    public boolean done(int size) {
        return (EvolutionAlgorithm.USE_LOCAL ? localFails() : fails()) == 0;
    }

    @Override
    public double sum() {
        return fitness();
    }

    @Override
    public int compareTo(Genotype genotype) {
        return fitness() > genotype.fitness() ? -1 : 1;
    }

    public String toString() {
        return Arrays.toString(array);
    }

    private void randomize() {
        for (int i = 0; i < array.length; i++) {
            array[i] = EvolutionAlgorithm.random.nextInt(EvolutionAlgorithm.SYMBOLSET_SIZE);
        }
    }

    public static void main(String[] args) {
//        Genotype geno = new SurprisingSequences(4, 10);
//        System.out.println(geno);
//        System.out.println(geno.toPhenotype());

//        geno.mutate(0.2);
//        System.out.println(geno);
//        System.out.println(geno.toPhenotype());


    }
}
