package EA;
import java.util.ArrayList;
import java.util.List;

public class Statistics {

    private List<Double> avg, max, std;
    private List<String> best;

    public Statistics() {
        avg = new ArrayList<Double>();
        max = new ArrayList<Double>();
        std = new ArrayList<Double>();
        best = new ArrayList<String>();
    }

    public void updateStatistics(List<Genotype> population) {
        double avg = 0;
        double max = 0;
        String best = null;
        for (Genotype genotype : population) {
            double sum = genotype.sum();
            if(max < sum) {
                best = genotype.toPhenotype();
                max = sum;
            }
            avg += sum;
        }

        this.max.add(max);
        this.avg.add(avg / population.size());
        this.std.add(Selection.standard_diviasion(population));
        this.best.add(best);
    }

    public double getMax(int iteration) {
        return max.get(iteration);
    }

    public double getAvg(int iteration) {
        return avg.get(iteration);
    }

    public double getStd(int iteration) {
        return std.get(iteration);
    }

    public String getBest(int iteration) {
        return best.get(iteration);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Statistics: best: "+ this.best.get(best.size()-1)+"\n");
        sb.append("var average = " + avg+"\n");
        sb.append("var bestGenome = " + max+"\n");
        sb.append("var std = " + std);
        return sb.toString();
    }
}
