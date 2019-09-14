import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class TestCMS {
	
	public static void main(String args[]) throws IOException {
		int depth = Integer.parseInt(args[0]);
		int width = Integer.parseInt(args[1]);
		int length = Integer.parseInt(args[2]);
		double skew = Double.parseDouble(args[3]);
		Stream st = new Stream(length, skew);
		/*CMS_default def = new CMS_default(depth, width);
		CMS_conservative conv = new CMS_conservative(depth, width);
		CMS_Morris morris = new CMS_Morris(depth, width);
		double error = test_configuration(depth, width, length, skew, st, def);
		System.out.println("Default Maximum error in stream is " + String.valueOf(error));
		error = test_configuration(depth, width, length, skew, st, conv);
		System.out.println("Conv Maximum error in stream is " + String.valueOf(error));
		error = test_configuration(depth, width, length, skew, st, morris);
		System.out.println("Morris Maximum error in stream is " + String.valueOf(error));
		*/
		run_skew_experiment(depth, width, length,  st, "default");
		run_skew_experiment(depth, width, length,  st, "conservative");
		run_skew_experiment(depth, width, length,  st, "morris");
		run_width_experiment(depth, skew, length,  st, "default");
		run_width_experiment(depth, skew, length,  st, "conservative");
		run_width_experiment(depth, skew, length,  st, "morris");
	}

	public static CMS get_counter(String counter_name, int depth, int width) {
		if (counter_name == "default") {
			return new CMS_default(depth, width);
		} else if (counter_name == "conv") {
			return new CMS_conservative(depth, width);
		} else if (counter_name == "morris") {
			return new CMS_Morris(depth, width);
		}
		return new CMS_default(depth, width);
	}

	public static void run_width_experiment(int depth, double skew, int length, Stream st, String counter_name) {
		int [] widths = {200, 2000, 20000, 200000, 2000000, 20000000};
		for (int i = 0; i < widths.length; i++) {
			CMS sketch = get_counter(counter_name, depth, widths[i]);
			double error = test_configuration(depth, widths[i], length, skew, st, sketch);
			System.out.println(counter_name + " Maximum error in stream is Width : "  + 
					String.valueOf(widths[i]) + " is  "+ String.valueOf(error));
		}
	}

	public static void run_skew_experiment(int depth, int width, int length, Stream st, String counter_name) {
		double [] skews = {0, 0.2, 0.5, 0.8, 1, 1.2,1.5, 2,2.5,10 };
		for (int i = 0; i < skews.length; i++) {
			st = new Stream(length, skews[i]);
			CMS sketch = get_counter(counter_name, depth, width);
			double error = test_configuration(depth, width, length, skews[i], st, sketch);
			System.out.println(counter_name + " Maximum error in stream is Skew : "  + 
					String.valueOf(skews[i]) + " is  "+ String.valueOf(error));
		}
	}

	public static double test_configuration(int depth, int width, int length, double skew, Stream st, CMS def) {
		for (int i = 1; i <= length; i++) {
			int item = st.stream[i];
			def.update(item, 1);
		}
		double error = 0;
		for (int i = 1; i < st.universe; i++) {
		    int def_estimate = def.query(i);
		    if (st.get_exact(i) == 0) continue;
		    double current_error = (def_estimate - st.get_exact(i))/(st.get_exact(i) + 0.0);
		    if (current_error > error) {
	                    //System.out.println(" Value: " + String.valueOf(def_estimate) + "  exact " + String.valueOf(st.get_exact(i)));
			    error = current_error;
	                    //System.out.println(" Error %: " + String.valueOf(error));
		    }

		}
		return error;
	}

	private static void run_query(String file, CMS sketch, int percent) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int total = 0;
			int in_error_bound = 0;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) continue;
				String[] parts = line.split(",");
				String query = parts[0];
				int frequency = Integer.parseInt(parts[1]);
				int F1 = Integer.parseInt(parts[2]);
				int item_val = sketch.query(query);
				System.out.println("Query " + query + " value "+ String.valueOf(item_val) + "  frequ " + String.valueOf(frequency) + "  F1 "+ String.valueOf(F1));
				if (item_val < (frequency + (1.0/percent)*F1)) {
					in_error_bound += 1;
				}
				total += 1;
			}
			System.out.println("Out of total " + String.valueOf(total) + " , we got " + String.valueOf(in_error_bound) + " in error bounds ");
		}
	}

	private static void run_updates(String file, CMS sketch, boolean ignore_neg) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) continue;
				String[] parts = line.split(",");
				int freq = Integer.parseInt(parts[2]);
				if ((freq < 0) && ignore_neg) continue;
				//System.out.println("Updating " + parts[1] + "   "  + String.valueOf(freq));
				sketch.update(parts[1], freq);
				//System.out.println("Getting value of  : " + parts[1] + "   "  + String.valueOf(sketch.query(parts[1])));
			}
		}
	}
}
