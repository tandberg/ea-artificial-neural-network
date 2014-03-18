public class Tuple {
    public double from, to;
    public Tuple(double a, double b) {
        this.from = a;
        this.to = b;
    }
    public String toString() {
        return "("+from+","+to+")";
    }
}
