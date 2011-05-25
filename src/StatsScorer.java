/*
 * Carnegie Mellon University
 * Copyright (c) 2004, 2010
 * 
 * This software is distributed under the terms of the GNU Lesser General
 * Public License.  See the included COPYING and COPYING.LESSER files.
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import edu.cmu.meteor.scorer.MeteorConfiguration;
import edu.cmu.meteor.scorer.MeteorScorer;
import edu.cmu.meteor.scorer.MeteorStats;
import edu.cmu.meteor.util.Constants;

public class StatsScorer {

	public static void main(String[] args) {

		// Usage for -h, --help, or similar
		if (args.length == 1 && args[0].contains("-h")) {
			System.out.println("Meteor Stats Scorer version "
					+ Constants.VERSION);
			System.out.println("Usage: java -cp meteor.jar "
					+ "StatsScorer [options]");
			System.out.println();
			System.out.println("Options:");
			System.out.println("-l language\t\t\tOne of: en cz de es fr ar");
			System.out.println("-t task\t\t\t\tOne of: adq rank hter li");
			System.out
					.println("-p 'alpha beta gamma delta'\t\tCustom parameters (overrides default)");
			System.out
					.println("-w 'weight1 weight2 ...'\tSpecify module weights (overrides default)");
			return;
		}

		Properties props = new Properties();
		// Get input args
		int arg = 0;
		while (arg < args.length) {
			if (args[arg].equals("-l")) {
				props.setProperty("language", args[arg + 1]);
				arg += 2;
			} else if (args[arg].equals("-t")) {
				props.setProperty("task", args[arg + 1]);
				arg += 2;
			} else if (args[arg].equals("-p")) {
				props.setProperty("parameters", args[arg + 1]);
				arg += 2;
			} else if (args[arg].equals("-w")) {
				props.setProperty("moduleWeights", args[arg + 1]);
				arg += 2;
			} else {
				System.err.println("Unknown option \"" + args[arg] + "\"");
				System.exit(1);
			}
		}

		// Create configuration
		MeteorConfiguration config = new MeteorConfiguration();

		String language = props.getProperty("language");
		if (language != null)
			config.setLanguage(language);

		String task = props.getProperty("task");
		if (task != null)
			config.setTask(task);

		String parameters = props.getProperty("parameters");
		if (parameters != null) {
			ArrayList<Double> params = new ArrayList<Double>();
			StringTokenizer tok = new StringTokenizer(parameters);
			while (tok.hasMoreTokens())
				params.add(Double.parseDouble(tok.nextToken()));
			config.setParameters(params);
		}

		String weights = props.getProperty("moduleWeights");
		if (weights != null) {
			ArrayList<Double> w = new ArrayList<Double>();
			StringTokenizer tok = new StringTokenizer(weights);
			while (tok.hasMoreTokens())
				w.add(Double.parseDouble(tok.nextToken()));
			config.setModuleWeights(w);
		}

		// Do not load resources for any modules
		ArrayList<Integer> none = new ArrayList<Integer>();
		config.setModules(none);

		MeteorScorer scorer = new MeteorScorer(config);

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line;

		// Score input lines
		try {
			while ((line = in.readLine()) != null) {
				MeteorStats stats = new MeteorStats(line);
				scorer.computeMetrics(stats);
				System.out.println(stats.score);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}