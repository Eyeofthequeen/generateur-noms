public class Interval {
    Double max;
    Double min;

    public Interval(Double min, Double max) {
        if (min.equals(max)) {
            this.min = 0.0;
            this.max = 0.0;
        } else {
            this.min = min; // include
            this.max = max; // exclude
        }
    }

    @Override
    public String toString() {
        if (max > 0) {
            return "[" + Math.round(min * 100.0) / 100.0 + "; " + Math.round(max * 100.0) / 100.0 + "]";
        }
        return " - ";
    }
}