import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class TestCMS {
	
	public static void main(String args[]) throws IOException {
		int depth = Integer.parseInt(args[0]);
		int width = Integer.parseInt(args[1]);
		int length = Integer.parseInt(args[2]);
		int skew = Integer.parseInt(args[3]);

	        System.out.println(" DEFAULT: Runing Updates");
		CMS_default def = new CMS_default(depth, width);
		run_updates(args[2], def, false);
		run_query(args[3], def, 100);
	        System.out.println(" Conservative: Runing Updates");
		CMS_conservative conv = new CMS_conservative(depth, width);
		run_updates(args[2], conv, false);
		run_query(args[3], conv, 100);
	        System.out.println(" DEFAULT: Runing Morris");
		CMS_Morris morris = new CMS_Morris(depth, width);
		run_updates(args[2], morris,  true);
		run_query(args[3], morris, 100);
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
