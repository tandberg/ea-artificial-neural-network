package EA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Selection {


    public static List<Genotype> fullPopulationReplacement(List<Genotype> adults, List<Genotype> children) {
        adults = children;
        return adults;
    }

    public static List<Genotype> overProduction(List<Genotype> adults, List<Genotype> children) {
        if(adults.size() >= children.size()) {
            throw new IllegalArgumentException("too few children");
        }

        Collections.sort(children);
        ArrayList<Genotype> newPopulation = new ArrayList<Genotype>();
        for (int i = 0; i < adults.size(); i++) {
            newPopulation.add(children.get(i));
        }

        adults = newPopulation;
        return adults;
    }

    public static List<Genotype> generationMixing(List<Genotype> adults, List<Genotype> children, int elistism) {
        ArrayList<Genotype> newPopulation = new ArrayList<Genotype>();

        Collections.sort(adults);
        Collections.sort(children);

        for (int i = 0; i < elistism; i++) {
            newPopulation.add(adults.get(i));
        }

        for (int i = 0; i < adults.size() - elistism; i++) {
            newPopulation.add(children.get(i));
        }

        adults = newPopulation;
        return adults;
    }


    public static Genotype tournamentSelection(List<Genotype> population, int k, double epsilon) {

        List<Genotype> tournament_members = new ArrayList<Genotype>();
        for (int j = 0; j < k; j++) {
            tournament_members.add(population.get(EvolutionaryAlgorithm.random.nextInt(population.size())));
        }
        Collections.sort(tournament_members);

        if(EvolutionaryAlgorithm.random.nextDouble() <= 1 - epsilon) {
            return tournament_members.get(0);
        } else {
            return tournament_members.get(EvolutionaryAlgorithm.random.nextInt(tournament_members.size()));
        }

    }

    public static Tuple[] boltzmannSelection(List<Genotype> population, int generation) {
        double[] probabilities = new double[population.size()];
        double sum = 0;
        double T = 1.0/generation;
        double denominator = Math.exp(avg(population)/T);
        for (int i = 0; i < probabilities.length; i++) {
            double numerator = Math.exp(population.get(i).fitness() / T);
            probabilities[i] = numerator/denominator;
            sum += probabilities[i];
        }

        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }

        Tuple[] wheel = new Tuple[probabilities.length];
        double temp = 0.0;
        for (int i = 0; i < wheel.length; i++) {
            wheel[i] = new Tuple(temp, probabilities[i]+temp);
            temp += probabilities[i];
        }

        return wheel;
    }

    public static Tuple[] sigmaScaling(List<Genotype> population) {

        double sum = 0;
        double[] probabilities = new double[population.size()];

        for(int i = 0; i < probabilities.length; i++) {

            probabilities[i] = (1 + ((population.get(i).fitness() - avg(population)) / (2 * standard_diviasion(population))));
            sum += probabilities[i];
        }



        for(int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }

        Tuple[] wheel = new Tuple[probabilities.length];
        double temp = 0.0;
        for (int i = 0; i < wheel.length; i++) {
            wheel[i] = new Tuple(temp, probabilities[i]+temp);
            temp += probabilities[i];
        }

        return wheel;
    }

    public static Tuple[] fitnessProportionate(List<Genotype> population) {
        double sum = sum(population);
        double[] probabilities = new double[population.size()];
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = population.get(i).fitness() / sum;
        }

        Tuple[] wheel = new Tuple[probabilities.length];
        double temp = 0.0;
        for (int i = 0; i < wheel.length; i++) {
            wheel[i] = new Tuple(temp, probabilities[i]+temp);
            temp += probabilities[i];
        }

        return wheel;
    }






    // Helper methods
    public static Genotype rouletteWheel(Tuple[] wheel, List<Genotype> population) {
        double random = EvolutionaryAlgorithm.random.nextDouble();

        for (int i = 0; i < wheel.length; i++) {
            if(random > wheel[i].from && random <= wheel[i].to) {
                return population.get(i);
            }
        }

        return population.get(0);
    }

    public static double avg(List<Genotype> population) {
        double avg = 0;
        for (Genotype genotype : population) {
            avg += genotype.fitness();
        }
        return avg / population.size();
    }

    public static double standard_diviasion(List<Genotype> population) {
        double variance = 0;
        double avg = avg(population);

        for (Genotype genotype : population) {
            variance += Math.pow(genotype.fitness() - avg, 2);
        }

        if(variance == 0) return 0.5;
        return Math.sqrt(variance/population.size());
    }

    private static double sum(List<Genotype> population) {
        double sum = 0;
        for (Genotype genome : population) {
            sum += genome.fitness();
        }
        return sum;
    }
}
