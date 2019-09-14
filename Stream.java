/*
 * Author: Gaurav Arora
 * Inspiration from including parameter for universe from: https://www.cs.rutgers.edu/~muthu/teststab.c
 * 
 */

public class Stream {
    // We used the universse size as used by original author of Count-Min Sketch.
    public int universe = 1048575;
    Hash hash = new Hash();
    int [] exact = new int[universe+1];
    public int [] stream;
    int length;

    public Stream (int len, double skew) {
	length = len;
        stream = new int[length + 1];
	for (int i = 1; i <length; i++) {
		double rand = StdRandom.uniform();
		long zipf_item =  inverseCdfFast(rand,skew, length);
		int stream_item = hash.h_basic(zipf_item, universe);
		exact[stream_item] += 1;
		stream[i] = stream_item;
		//System.out.println("Stream item " + String.valueOf(i) + " is " + String.valueOf(stream_item));
	}

    }

    public int get_exact(int item) {
	    return exact[item];
    }

    // Function taken from https://medium.com/@jasoncrease/zipf-54912d5651cc
    public static long inverseCdfFast(final double p, final double s, final double N) {
        if (p > 1d || p < 0d)
            throw new IllegalArgumentException("p must be between 0 and 1");

        final double tolerance = 0.01d;
        double x = N / 2f;

        final double pD = p * (12 * (Math.pow(N, -s + 1) - 1) / (1 - s) + 6 + 6 * Math.pow(N, -s) + s - s * Math.pow(N, -s - 1));

        while (true) {
            final double m = Math.pow(x, -s - 2);   // x ^ ( -s - 2)
            final double mx = m * x;                // x ^ ( -s - 1)
            final double mxx = mx * x;              // x ^ ( -s)
            final double mxxx = mxx * x;            // x ^ ( -s + 1)

            final double a = 12 * (mxxx - 1) / (1 - s) + 6 + 6 * mxx + s - (s * mx) - pD;
            final double b = 12 * mxx - (6 * s * mx) + (m * s * (s + 1));
            final double newx = Math.max(1, x - a / b);
            if (Math.abs(newx - x) <= tolerance) {
                return Math.round(newx);
            }
            x = newx;
        }
    }
	
}
