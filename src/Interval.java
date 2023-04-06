public class Interval {
    Double max;
    Double min;

    public Interval(Double min, Double max) {
        this.min = min; // include
        this.max = max; // exclude
    }
}