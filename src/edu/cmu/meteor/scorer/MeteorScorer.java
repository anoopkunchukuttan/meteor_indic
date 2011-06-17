/*
 * Carnegie Mellon University
 * Copyright (c) 2004, 2010
 * 
 * This software is distributed under the terms of the GNU Lesser General
 * Public License.  See the included COPYING and COPYING.LESSER files.
 * 
 */

package edu.cmu.meteor.scorer;

import java.util.ArrayList;

import edu.cmu.meteor.aligner.Aligner;
import edu.cmu.meteor.aligner.Alignment;
import edu.cmu.meteor.util.Constants;
import edu.cmu.meteor.util.Normalizer;

/**
 * Entry point class which oversees Meteor scoring. Instantiate with either the
 * default configuration (no args) or an existing MeteorConfiguration. Call the
 * getMeteorStats() methods to obtain MeteorStats objects which include a score
 * field
 * 
 */
public class MeteorScorer {

	private Aligner aligner;
	private String language;
	private int langID;

	private boolean normalize;
	private boolean keepPunctuation;
	private boolean lowerCase;

	// Parameters
	private double alpha;
	private double beta;
	private double gamma;
	private double delta;

	// Weights
	private ArrayList<Double> moduleWeights;

	/**
	 * Use default configuration
	 * 
	 */
	public MeteorScorer() {
		MeteorConfiguration config = new MeteorConfiguration();
		loadConfiguration(config);
	}

	/**
	 * Use a custom configuration
	 * 
	 * @param config
	 */
	public MeteorScorer(MeteorConfiguration config) {
		loadConfiguration(config);
	}

	/**
	 * Create a new scorer that shares thread-safe resources with an existing
	 * scorer
	 * 
	 * @param scorer
	 */
	public MeteorScorer(MeteorScorer scorer) {
		language = scorer.language;
		langID = scorer.langID;
		normalize = scorer.normalize;
		keepPunctuation = scorer.keepPunctuation;
		lowerCase = scorer.lowerCase;
		alpha = scorer.alpha;
		beta = scorer.beta;
		gamma = scorer.gamma;
		delta = scorer.delta;
		moduleWeights = new ArrayList<Double>(scorer.moduleWeights);
		aligner = new Aligner(scorer.aligner);
	}

	/**
	 * Load configuration (only used by constructor)
	 * 
	 * @param config
	 */
	private void loadConfiguration(MeteorConfiguration config) {
		language = config.getLanguage();
		langID = config.getLangID();
		setNormalize(config.getNormalization());
		ArrayList<Double> parameters = config.getParameters();
		alpha = parameters.get(0);
		beta = parameters.get(1);
		gamma = parameters.get(2);
		delta = parameters.get(3);
		moduleWeights = config.getModuleWeights();
		aligner = new Aligner(language, config.getModules(), config
				.getModuleWeights(), config.getBeamSize(), config
				.getSynDirURL(), config.getParaDirURL(),
		// Best alignments for evaluation
				Constants.PARTIAL_COMPARE_TOTAL);

		// Best weights for evaluation
		ArrayList<Integer> modules = config.getModules();
		ArrayList<Double> setWeights = new ArrayList<Double>();
		for (int module : modules) {
			if (module == Constants.MODULE_EXACT)
				setWeights.add(1.0);
			else if (module == Constants.MODULE_STEM)
				setWeights.add(0.5);
			else if (module == Constants.MODULE_SYNONYM)
				setWeights.add(0.5);
			else
				setWeights.add(0.5);
		}
		aligner.updateModuleWeights(setWeights);
	}

	/**
	 * Set normalization type
	 * 
	 * @param normtype
	 */
	private void setNormalize(int normtype) {
		if (normtype == Constants.NORMALIZE_LC_ONLY) {
			this.normalize = false;
			this.keepPunctuation = true;
			this.lowerCase = true;
		} else if (normtype == Constants.NORMALIZE_KEEP_PUNCT) {
			this.normalize = true;
			this.keepPunctuation = true;
			this.lowerCase = true;

		} else if (normtype == Constants.NORMALIZE_NO_PUNCT) {
			this.normalize = true;
			this.keepPunctuation = false;
			this.lowerCase = true;
		} else {
			// Assume NO_NORMALIZE
			this.normalize = false;
			this.keepPunctuation = true;
			this.lowerCase = false;
		}
	}

	/**
	 * Update module weights without reloading resources. This is used for
	 * tuning and is likely not needed for most applications
	 * 
	 * @param moduleWeights
	 */
	public void updateModuleWeights(ArrayList<Double> moduleWeights) {
		aligner.updateModuleWeights(moduleWeights);
	}

	/**
	 * Get the Meteor sufficient statistics for a test / reference pair
	 * 
	 * @param test
	 * @param reference
	 * @return
	 */
	public MeteorStats getMeteorStats(String test, String reference) {
		// Normalize both
		if (normalize) {
			test = Normalizer.normalizeLine(test, langID, keepPunctuation);
			reference = Normalizer.normalizeLine(reference, langID,
					keepPunctuation);
		}
		// Lowercase both
		if (lowerCase) {
			test = test.toLowerCase();
			reference = reference.toLowerCase();
		}
		// Score
		Alignment alignment = aligner.align(test, reference);
		return getMeteorStats(alignment);
	}

	/**
	 * Get the Meteor sufficient statistics for a test give a list of references
	 * 
	 * @param test
	 * @param references
	 * @return
	 */
	public MeteorStats getMeteorStats(String test, ArrayList<String> references) {
		// Normalize test
		if (normalize)
			test = Normalizer.normalizeLine(test, langID, keepPunctuation);
		if (lowerCase)
			test = test.toLowerCase();
		MeteorStats stats = new MeteorStats();
		stats.score = -1;
		// Score each reference
		for (String reference : references) {
			// Normalize reference
			if (normalize)
				reference = Normalizer.normalizeLine(reference, langID,
						keepPunctuation);
			if (lowerCase)
				reference = reference.toLowerCase();
			Alignment alignment = aligner.align(test, reference);
			MeteorStats curStats = getMeteorStats(alignment);
			if (curStats.score > stats.score)
				stats = curStats;
		}
		return stats;
	}

	/**
	 * Get the Meteor sufficient statistics for an alignment
	 * 
	 * @param alignment
	 * @return
	 */
	public MeteorStats getMeteorStats(Alignment alignment) {
		MeteorStats stats = new MeteorStats();

		// Copy alignment stats
		stats.testLength = alignment.words1.size();
		stats.referenceLength = alignment.words2.size();

		stats.testFunctionWords = alignment.line1FunctionWords;
		stats.referenceFunctionWords = alignment.line2FunctionWords;

		stats.testStageMatchesContent = new ArrayList<Integer>(
				alignment.moduleContentMatches1);
		stats.referenceStageMatchesContent = new ArrayList<Integer>(
				alignment.moduleContentMatches2);

		stats.testStageMatchesFunction = new ArrayList<Integer>(
				alignment.moduleFunctionMatches1);
		stats.referenceStageMatchesFunction = new ArrayList<Integer>(
				alignment.moduleFunctionMatches2);

		stats.chunks = alignment.numChunks;

		// Total matches
		// Important: sum from stage matches instead of taking total from
		// alignment as alignment totals are WEIGHTED and totals here are
		// UNWEIGHTED
		for (int i = 0; i < stats.testStageMatchesContent.size(); i++) {
			stats.testTotalMatches += stats.testStageMatchesContent.get(i);
			stats.testTotalMatches += stats.testStageMatchesFunction.get(i);
			stats.referenceTotalMatches += stats.referenceStageMatchesContent
					.get(i);
			stats.referenceTotalMatches += stats.referenceStageMatchesFunction
					.get(i);
		}

		// Meteor score is required to pick best reference
		computeMetrics(stats);

		// Keep underlying alignment
		stats.alignment = alignment;

		return stats;
	}

	/**
	 * Get the Meteor score given sufficient statistics
	 * 
	 * @param stats
	 */
	public void computeMetrics(MeteorStats stats) {

		stats.testWeightedMatches = 0;
		stats.referenceWeightedMatches = 0;

		stats.testWeightedLength = (delta * (stats.testLength - stats.testFunctionWords))
				+ ((1.0 - delta) * (stats.testFunctionWords));
		stats.referenceWeightedLength = (delta * (stats.referenceLength - stats.referenceFunctionWords))
				+ ((1.0 - delta) * (stats.referenceFunctionWords));

		// Apply module weights and delta to test and reference matches
		// (Content)
		for (int i = 0; i < moduleWeights.size(); i++)
			stats.testWeightedMatches += stats.testStageMatchesContent.get(i)
					* moduleWeights.get(i) * delta;
		for (int i = 0; i < moduleWeights.size(); i++)
			stats.referenceWeightedMatches += stats.referenceStageMatchesContent
					.get(i)
					* moduleWeights.get(i) * delta;

		// Apply module weights and delta to test and reference matches
		// (Function)
		for (int i = 0; i < moduleWeights.size(); i++)
			stats.testWeightedMatches += stats.testStageMatchesFunction.get(i)
					* moduleWeights.get(i) * (1.0 - delta);
		for (int i = 0; i < moduleWeights.size(); i++)
			stats.referenceWeightedMatches += stats.referenceStageMatchesFunction
					.get(i)
					* moduleWeights.get(i) * (1.0 - delta);

		// Precision = test matches / test length
		stats.precision = stats.testWeightedMatches / stats.testWeightedLength;
		// Recall = ref matches / ref length
		stats.recall = stats.referenceWeightedMatches
				/ stats.referenceWeightedLength;
		// F1 = 2pr / (p + r) [not part of final score]
		stats.f1 = (2 * stats.precision * stats.recall)
				/ (stats.precision + stats.recall);
		// Fmean = 1 / alpha-weighted average of p and r
		stats.fMean = 1.0 / (((1.0 - alpha) / stats.precision) + (alpha / stats.recall));
		// Fragmentation
		double frag;
		// Case if test = ref
		if (stats.testTotalMatches == stats.testLength
				&& stats.referenceTotalMatches == stats.referenceLength
				&& stats.chunks == 1)
			frag = 0;
		else
			frag = ((double) stats.chunks)
					/ (((double) (stats.testTotalMatches + stats.referenceTotalMatches)) / 2);
		// Fragmentation penalty
		stats.fragPenalty = gamma * Math.pow(frag, beta);
		// Score
		double score = stats.fMean * (1.0 - stats.fragPenalty);

		// Catch division by zero
		if (Double.isNaN(score))
			stats.score = 0;
		else
			// score >= 0.0
			stats.score = Math.max(score, 0.0);
	}
}
