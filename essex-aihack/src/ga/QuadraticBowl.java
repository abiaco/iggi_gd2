package ga;

/**
 * Created by abiaco on 16/06/15.
 */
public class QuadraticBowl implements Eval2 {
    static double mag2(double[] v) {
        // square of the magnitude of the vector
        double tot = 0;
        for (double x : v) tot += x * x;
        return tot;
    }
    @Override
    public double pointsDiff(double[] a, double[] b) {
        // simple example that evaluates quality as being
        // the minimum squared magnitude of a vector
        return mag2(a) - mag2(b);
    }
}
