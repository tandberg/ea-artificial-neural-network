package EA;

public abstract class Genotype implements Comparable<Genotype> {

    public abstract double fitness();
    public abstract void mutate(double mutationPercent);
    public abstract String toPhenotype();
    public abstract int[] getArray();
    public abstract boolean done(int size);
    public abstract double sum();


}
