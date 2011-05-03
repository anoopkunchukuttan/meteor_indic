/*
 * Carnegie Mellon University
 * Copyright (c) 2004, 2010
 * 
 * This software is distributed under the terms of the GNU Lesser General
 * Public License.  See the included COPYING and COPYING.LESSER files.
 *
 */

package edu.cmu.meteor.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.frenchStemmer;
import org.tartarus.snowball.ext.germanStemmer;
import org.tartarus.snowball.ext.spanishStemmer;

import edu.cmu.meteor.aligner.PartialAlignment;

public class Constants {

	/* Version */

	public static final String VERSION = "1.3";

	/*
	 * Normalizer constants
	 */

	public static final URL DEFAULT_NBP_DIR_URL = ClassLoader
			.getSystemResource("nonbreaking");

	public static final int NBP_NUM_ONLY = 2;
	public static final int NBP_ANY = 1;

	/*
	 * Aligner Constants
	 */

	public static final int MODULE_EXACT = 0;
	public static final int MODULE_STEM = 1;
	public static final int MODULE_SYNONYM = 2;
	public static final int MODULE_PARAPHRASE = 3;

	public static final int MAX_MODULES = 4;

	public static final double DEFAULT_WEIGHT_EXACT = 1.0;
	public static final double DEFAULT_WEIGHT_STEM = 1.0;
	public static final double DEFAULT_WEIGHT_SYNONYM = 1.0;
	public static final double DEFAULT_WEIGHT_PARAPHRASE = 1.0;

	public static final int DEFAULT_BEAM_SIZE = 40;

	public static final URL DEFAULT_SYN_DIR_URL = ClassLoader
			.getSystemResource("synonym");

	public static final URL DEFAULT_FUNCTION_DIR_URL = ClassLoader
			.getSystemResource("function");

	/*
	 * Scorer Constants
	 */

	/* Languages */

	public static final int LANG_EN = 0;
	public static final int LANG_CZ = 1;
	public static final int LANG_FR = 2;
	public static final int LANG_ES = 3;
	public static final int LANG_DE = 4;
	public static final int LANG_AR = 5;
	public static final int LANG_OTHER = 99;

	/* Adequacy task */
	public static final int TASK_ADQ = 0;
	public static final double PARAM_ADQ[][] = {
	//
			{ 0.75, 1.40, 0.45, 0.70 }, // English
			{ 0.0, 0.0, 0.0, 0.0 }, // Czech
			{ 0.0, 0.0, 0.0, 0.0 }, // French
			{ 0.0, 0.0, 0.0, 0.0 }, // Spanish
			{ 0.0, 0.0, 0.0, 0.0 }, // German
			{ 0.00, 0.05, 0.80, 0.50 }, // Arabic
	};
	public static final double WEIGHT_ADQ[][] = {
	//
			{ 1.0, 1.0, 0.6, 0.8 }, // English
			{ 0.0, 0.0, 0.0, 0.0 }, // Czech
			{ 0.0, 0.0, 0.0, 0.0 }, // French
			{ 0.0, 0.0, 0.0, 0.0 }, // Spanish
			{ 0.0, 0.0, 0.0, 0.0 }, // German
			{ 1.0, 0.6, 0.0, 0.0 }, // Arabic
	};

	/* Ranking task */
	public static final int TASK_RANK = 1;
	public static final double PARAM_RANK[][] = {
	//
			{ 0.85, 0.20, 0.60, 0.75 }, // English
			{ 0.95, 0.20, 0.60, 0.80 }, // Czech
			{ 0.90, 1.40, 0.60, 0.65 }, // French
			{ 0.65, 1.30, 0.50, 0.80 }, // Spanish
			{ 0.95, 1.00, 0.55, 0.55 }, // German
			{ 0.0, 0.0, 0.0, 0.50 }, // Arabic
	};
	public static final double WEIGHT_RANK[][] = {
	//
			{ 1.0, 0.6, 0.8, 0.6 }, // English
			{ 1.0, 0.4, 0.0, 0.0 }, // Czech
			{ 1.0, 0.2, 0.4, 0.0 }, // French
			{ 1.0, 0.8, 0.6, 0.0 }, // Spanish
			{ 1.0, 0.8, 0.2, 0.0 }, // German
			{ 0.0, 0.0, 0.0, 0.0 }, // Arabic
	};

	/* HTER task */
	public static final int TASK_HTER = 2;
	public static final double PARAM_HTER[][] = {
	//
			{ 0.40, 1.50, 0.35, 0.55 }, // English
			{ 0.0, 0.0, 0.0, 0.0 }, // Czech
			{ 0.0, 0.0, 0.0, 0.0 }, // French
			{ 0.0, 0.0, 0.0, 0.0 }, // Spanish
			{ 0.0, 0.0, 0.0, 0.0 }, // German
			{ 0.0, 0.0, 0.0, 0.0 }, // Arabic
	};
	public static final double WEIGHT_HTER[][] = {
	//
			{ 1.0, 0.2, 0.6, 0.8 }, // English
			{ 0.0, 0.0, 0.0, 0.0 }, // Czech
			{ 0.0, 0.0, 0.0, 0.0 }, // French
			{ 0.0, 0.0, 0.0, 0.0 }, // Spanish
			{ 0.0, 0.0, 0.0, 0.0 }, // German
			{ 0.0, 0.0, 0.0, 0.0 }, // Arabic
	};

	public static final int TASK_DEFAULT = TASK_RANK;

	/* Language-independent task */
	public static final int TASK_LI = 99;
	public static final double PARAM_I[] = { 0.75, 1.40, 0.70, 0.50 };
	public static final double WEIGHT_I[] = { 1.0, 0.0, 0.0, 0.0 };

	// Cannot be used to set task, only used when options are specified manually
	public static final int TASK_CUSTOM = -1;

	/* Normalization */
	public static final int NO_NORMALIZE = 0;
	public static final int NORMALIZE_LC_ONLY = 1;
	public static final int NORMALIZE_KEEP_PUNCT = 2;
	public static final int NORMALIZE_NO_PUNCT = 3;

	/*
	 * Methods to look up constants
	 */

	public static int getDefaultTask(int langID) {
		// Other languages always get language-independent task
		if (langID == LANG_OTHER)
			return TASK_LI;
		return TASK_DEFAULT;
	}

	public static String getLocation() {
		File codeDir = new File(Constants.class.getProtectionDomain()
				.getCodeSource().getLocation().getFile());
		// Class is either in JAR file or build directory
		return codeDir.getParent();
	}

	public static URL getDefaultParaFileURL(int langID) {
		String shortLang = getLanguageShortName(langID);
		String fileName = "/data/paraphrase-" + shortLang + ".gz";
		try {
			return new File(getLocation() + fileName).toURI().toURL();
		} catch (MalformedURLException ex) {
			throw new RuntimeException();
		}
	}

	public static String normLanguageName(String language) {
		String lang = language.toLowerCase();
		if (lang.equals("english") || lang.equals("en"))
			return "english";
		if (lang.equals("czech") || lang.equals("cz") || lang.equals("cs"))
			return "czech";
		if (lang.equals("french") || lang.equals("fr"))
			return "french";
		if (lang.equals("german") || lang.equals("de"))
			return "german";
		if (lang.equals("spanish") || lang.equals("es"))
			return "spanish";
		if (lang.equals("arabic") || lang.equals("ar"))
			return "arabic";

		// Not listed
		return "other";
	}

	public static int getLanguageID(String language) {
		if (language.equals("english"))
			return LANG_EN;
		if (language.equals("czech"))
			return LANG_CZ;
		if (language.equals("french"))
			return LANG_FR;
		if (language.equals("spanish"))
			return LANG_ES;
		if (language.equals("german"))
			return LANG_DE;
		if (language.equals("arabic"))
			return LANG_AR;

		// Not found
		return LANG_OTHER;
	}

	public static String getLanguageName(int langID) {
		if (langID == LANG_EN)
			return "english";
		if (langID == LANG_CZ)
			return "czech";
		if (langID == LANG_FR)
			return "french";
		if (langID == LANG_ES)
			return "spanish";
		if (langID == LANG_DE)
			return "german";
		if (langID == LANG_AR)
			return "arabic";

		// Not found
		return "other";
	}

	public static String getLanguageShortName(int langID) {
		if (langID == LANG_EN)
			return "en";
		if (langID == LANG_CZ)
			return "cz";
		if (langID == LANG_FR)
			return "fr";
		if (langID == LANG_ES)
			return "es";
		if (langID == LANG_DE)
			return "de";
		if (langID == LANG_AR)
			return "ar";

		// Not found
		return "xx";
	}

	public static String getNormName(int norm) {
		if (norm == NO_NORMALIZE)
			return "no_norm";
		if (norm == NORMALIZE_LC_ONLY)
			return "lc_only";
		if (norm == NORMALIZE_KEEP_PUNCT)
			return "norm";
		if (norm == NORMALIZE_NO_PUNCT)
			return "norm_nopunct";

		// Not found
		return "other";
	}

	public static int getModuleID(String modName) {
		String mod = modName.toLowerCase();
		if (mod.equals("exact"))
			return MODULE_EXACT;
		if (mod.equals("stem"))
			return MODULE_STEM;
		if (mod.equals("synonym"))
			return MODULE_SYNONYM;
		if (mod.equals("paraphrase"))
			return MODULE_PARAPHRASE;

		// Not found
		System.err.println("Module \"" + modName
				+ "\" not found, using \"exact\"");
		return MODULE_EXACT;
	}

	public static String getModuleName(int module) {
		if (module == MODULE_EXACT)
			return "exact";
		if (module == MODULE_STEM)
			return "stem";
		if (module == MODULE_SYNONYM)
			return "synonym";
		if (module == MODULE_PARAPHRASE)
			return "paraphrase";

		// Not found
		return "other";
	}

	public static String getModuleShortName(int module) {
		if (module == MODULE_EXACT)
			return "ex";
		if (module == MODULE_STEM)
			return "st";
		if (module == MODULE_SYNONYM)
			return "sy";
		if (module == MODULE_PARAPHRASE)
			return "pa";

		// Not found
		return "other";
	}

	public static String getModuleListString(ArrayList<Integer> mods) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mods.size(); i++) {
			sb.append(getModuleShortName(mods.get(i)));
			if (i < mods.size() - 1)
				sb.append("_");
		}
		return sb.toString();
	}

	public static int getTaskID(String taskName) {
		String task = taskName.toLowerCase();
		if (task.equals("default"))
			return TASK_DEFAULT;
		if (task.equals("adq"))
			return TASK_ADQ;
		if (task.equals("rank"))
			return TASK_RANK;
		if (task.equals("hter"))
			return TASK_HTER;
		if (task.equals("li"))
			return TASK_LI;

		// Other
		return TASK_CUSTOM;
	}

	public static String getTaskName(int task) {
		if (task == TASK_ADQ)
			return "adq";
		if (task == TASK_RANK)
			return "rank";
		if (task == TASK_HTER)
			return "hter";
		if (task == TASK_LI)
			return "li";

		// Other
		return "custom";
	}

	public static String getTaskDescription(String task) {
		return getTaskDescription(getTaskID(task));
	}

	public static String getTaskDescription(int task) {
		if (task == TASK_ADQ)
			return "Adequacy";
		if (task == TASK_RANK)
			return "Ranking";
		if (task == TASK_HTER)
			return "HTER";
		if (task == TASK_LI)
			return "Language-Independent";

		// Other
		return "Custom";
	}

	public static ArrayList<Double> getParameters(int langID, int taskID) {

		// Get task
		double[] TASK_PARAM;
		if (langID == LANG_OTHER || taskID == TASK_LI) {
			TASK_PARAM = PARAM_I;
		} else {
			if (taskID == TASK_RANK)
				TASK_PARAM = PARAM_RANK[langID];
			else if (taskID == TASK_HTER)
				TASK_PARAM = PARAM_HTER[langID];
			else
				// Assume TASK_ADQ
				TASK_PARAM = PARAM_ADQ[langID];
		}

		// Copy parameters
		ArrayList<Double> parameters = new ArrayList<Double>();
		for (double param : TASK_PARAM)
			parameters.add(param);

		return parameters;
	}

	public static ArrayList<Double> getModuleWeights(String language,
			String task) {
		return getModuleWeights(getLanguageID(language), getTaskID(task));
	}

	public static ArrayList<Double> getModuleWeights(int langID, int taskID) {

		// Get task
		double[] TASK_WEIGHT;
		if (langID == LANG_OTHER || taskID == TASK_LI) {
			TASK_WEIGHT = WEIGHT_I;
		} else {
			if (taskID == TASK_RANK)
				TASK_WEIGHT = WEIGHT_RANK[langID];
			else if (taskID == TASK_HTER)
				TASK_WEIGHT = WEIGHT_HTER[langID];
			else
				// Assume TASK_ADQ
				TASK_WEIGHT = WEIGHT_ADQ[langID];
		}

		// Copy weights
		ArrayList<Double> weights = new ArrayList<Double>();
		for (double weight : TASK_WEIGHT)
			weights.add(weight);

		return weights;
	}

	public static String getWeightListString(ArrayList<Double> weights) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < weights.size(); i++) {
			sb.append(weights.get(i));
			if (i < weights.size() - 1)
				sb.append("_");
		}
		return sb.toString();
	}

	public static SnowballStemmer newStemmer(String language) {
		if (language.equals("english"))
			return new englishStemmer();
		if (language.equals("french"))
			return new frenchStemmer();
		if (language.equals("german"))
			return new germanStemmer();
		if (language.equals("spanish"))
			return new spanishStemmer();

		// Not found
		return new englishStemmer();
	}

	public static ArrayList<Integer> getModules(int langID, int taskID) {
		ArrayList<Integer> modules = new ArrayList<Integer>();
		if (taskID == TASK_LI) {
			modules.add(MODULE_EXACT);
		} else if (langID == LANG_EN) {
			modules.add(MODULE_EXACT);
			modules.add(MODULE_STEM);
			modules.add(MODULE_SYNONYM);
			modules.add(MODULE_PARAPHRASE);
		} else if (langID == LANG_FR) {
			modules.add(MODULE_EXACT);
			modules.add(MODULE_STEM);
			modules.add(MODULE_PARAPHRASE);
		} else if (langID == LANG_ES) {
			modules.add(MODULE_EXACT);
			modules.add(MODULE_STEM);
			modules.add(MODULE_PARAPHRASE);
		} else if (langID == LANG_DE) {
			modules.add(MODULE_EXACT);
			modules.add(MODULE_STEM);
			modules.add(MODULE_PARAPHRASE);
		} else if (langID == LANG_CZ) {
			modules.add(MODULE_EXACT);
			modules.add(MODULE_PARAPHRASE);
		} else if (langID == LANG_AR) {
			modules.add(MODULE_EXACT);
			modules.add(MODULE_PARAPHRASE);
		} else {
			modules.add(MODULE_EXACT);
		}
		return modules;
	}

	// Compare partial alignments: consider total only
	// Use with exact, stem, paraphrase weights 1 0.5 0.5 0.5
	// Use with full paraphrase table
	public static Comparator<PartialAlignment> PARTIAL_COMPARE_TOTAL = new Comparator<PartialAlignment>() {
		public int compare(PartialAlignment x, PartialAlignment y) {
			// More matches always wins
			int matchDiff = (y.matches1 + y.matches2)
					- (x.matches1 + x.matches2);
			if (matchDiff > 0) {
				return 1;
			}
			if (matchDiff < 0) {
				return -1;
			}
			// Otherwise fewer chunks wins
			int chunkDiff = x.chunks - y.chunks;
			if (chunkDiff != 0)
				return chunkDiff;
			// Finally shortest distance wins
			return x.distance - y.distance;
		}
	};

	// Compare partial alignments: consider total, then all matches
	// Use with exact, stem, paraphrase weights 1 1 1 0
	// Use with filtered paraphrase table
	public static Comparator<PartialAlignment> PARTIAL_COMPARE_TOTAL_ALL = new Comparator<PartialAlignment>() {
		public int compare(PartialAlignment x, PartialAlignment y) {
			// 2 - Words covered by exact matches (more)
			int matchDiff = (y.matches1 + y.matches2)
					- (x.matches1 + x.matches2);
			if (matchDiff > 0) {
				return 1;
			}
			if (matchDiff < 0) {
				return -1;
			}
			// 3 - Total words covered (more)
			int allMatchDiff = (y.allMatches1 + y.allMatches2)
					- (x.allMatches1 + x.allMatches2);
			if (allMatchDiff > 0) {
				return 1;
			}
			if (allMatchDiff < 0) {
				return -1;
			}
			// 4 - Number of chunks (less)
			int chunkDiff = x.chunks - y.chunks;
			if (chunkDiff != 0)
				return chunkDiff;
			// 5 - Absolute match distance (less)
			return x.distance - y.distance;
		}
	};
}
