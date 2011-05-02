/*
 * Carnegie Mellon University
 * Copyright (c) 2004, 2010
 * 
 * This software is distributed under the terms of the GNU Lesser General
 * Public License.  See the included COPYING and COPYING.LESSER files.
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import edu.cmu.meteor.scorer.MeteorConfiguration;
import edu.cmu.meteor.scorer.MeteorScorer;
import edu.cmu.meteor.scorer.MeteorStats;
import edu.cmu.meteor.util.Constants;
import edu.cmu.meteor.util.Normalizer;
import edu.cmu.meteor.util.SGMData;

public class Meteor {

	public static void main(String[] args) {

		// Usage
		if (args.length < 2) {
			printUsage();
			return;
		}

		// Files
		String testFile = args[0];
		String refFile = args[1];

		// Use command line options to create props, configuration
		Properties props = createPropertiesFromArgs(args, 2);
		MeteorConfiguration config = createConfiguration(props);

		// Print settings
		Boolean ssOut = Boolean.parseBoolean(props.getProperty("ssOut"));
		Boolean sgml = Boolean.parseBoolean(props.getProperty("sgml"));
		Boolean nBest = Boolean.parseBoolean(props.getProperty("nBest"));
		Boolean oracle = Boolean.parseBoolean(props.getProperty("oracle"));

		String format = sgml ? "SGML" : "plaintext";
		if (!oracle && !ssOut) {
			System.out.println("Meteor version: " + Constants.VERSION);
			System.out.println();
			System.out.println("Language:       "
					+ config.getLanguage().substring(0, 1).toUpperCase()
					+ config.getLanguage().substring(1));
			System.out.println("Format:         " + format);
			System.out.println("Task:           " + config.getTaskDesc());
			System.out.println("Modules:        " + config.getModulesString());
			System.out.println("Weights:        "
					+ config.getModuleWeightsString());
			System.out.println("Parameters:     "
					+ config.getParametersString());
			System.out.println();
		}

		// Module / Weight check
		if (config.getModuleWeights().size() < config.getModules().size()) {
			System.err.println("Warning: More modules than weights specified "
					+ "- modules with no weights will not be counted.");
		}

		// SGML / NBest check
		if (sgml && nBest) {
			System.err.println("Warning: nBest incompatible with SGML - using "
					+ "SGML only");
		}

		MeteorScorer scorer = new MeteorScorer(config);

		if (sgml) {
			try {
				scoreSGML(scorer, props, config, testFile, refFile);
			} catch (IOException ex) {
				System.err.println("Error: Could not score SGML files:");
				ex.printStackTrace();
				System.exit(1);
			}
		} else
			try {
				if (nBest) {
					scoreNBest(scorer, props, config, testFile, refFile);
				} else {
					scorePlaintext(scorer, props, config, testFile, refFile);
				}
			} catch (IOException ex) {
				System.err.println("Error: Could not score text files:");
				ex.printStackTrace();
				System.exit(1);
			}
	}

	private static int getRefCount(Properties props) {
		String refCountString = props.getProperty("refCount");
		if (refCountString == null)
			return 1;
		else
			return Integer.parseInt(refCountString);
	}

	/**
	 * Input is in plaintext format, output is in simple format
	 */

	private static void scorePlaintext(MeteorScorer scorer, Properties props,
			MeteorConfiguration config, String testFile, String refFile)
			throws IOException {

		ArrayList<String> lines1 = new ArrayList<String>();
		ArrayList<String> lines2 = new ArrayList<String>();
		ArrayList<ArrayList<String>> lines2mref = new ArrayList<ArrayList<String>>();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(testFile), "UTF-8"));
		String line;
		while ((line = in.readLine()) != null)
			lines1.add(line);
		in.close();

		int refCount = getRefCount(props);

		in = new BufferedReader(new InputStreamReader(new FileInputStream(
				refFile), "UTF-8"));
		if (refCount == 1)
			while ((line = in.readLine()) != null)
				lines2.add(line);
		else {
			while ((line = in.readLine()) != null) {
				ArrayList<String> refs = new ArrayList<String>();
				refs.add(line);
				for (int refNum = 1; refNum < refCount; refNum++)
					refs.add(in.readLine());
				lines2mref.add(refs);
			}
		}
		in.close();
		if ((refCount == 1 && lines1.size() != lines2.size())
				|| (refCount > 1 && lines1.size() != lines2mref.size())) {
			System.err.println("Error: test and reference not same length");
			return;
		}

		MeteorStats aggStats = new MeteorStats();

		// Write alignments?
		Boolean writeAlignments = Boolean.parseBoolean(props
				.getProperty("writeAlignments"));
		PrintWriter out = null;
		if (writeAlignments) {
			String filePrefix = props.getProperty("filePrefix");
			if (filePrefix == null)
				filePrefix = "meteor";
			out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
					filePrefix + "-align.out"), "UTF-8"));
		}

		Boolean ssOut = Boolean.parseBoolean(props.getProperty("ssOut"));
		Boolean vOut = Boolean.parseBoolean(props.getProperty("vOut"));
		for (int i = 0; i < lines1.size(); i++) {
			MeteorStats stats;
			if (refCount == 1) {
				stats = scorer.getMeteorStats(lines1.get(i), lines2.get(i));
			} else
				stats = scorer.getMeteorStats(lines1.get(i), lines2mref.get(i));
			if (ssOut) {
				System.out.println(stats.toString());
			} else if (vOut) {
				System.out.println("Segment " + (i + 1) + " score:\t"
						+ stats.precision + "\t" + stats.recall + "\t"
						+ stats.fragPenalty + "\t" + stats.score);
			} else {
				System.out.println("Segment " + (i + 1) + " score:\t"
						+ stats.score);
			}
			if (writeAlignments) {
				out.println(stats.alignment.toString("Alignment\t" + (i + 1)
						+ "\t" + stats.precision + "\t" + stats.recall + "\t"
						+ stats.fragPenalty + "\t" + stats.score));
			}
			aggStats.addStats(stats);
		}

		if (writeAlignments) {
			out.close();
		}

		if (!ssOut) {
			scorer.computeMetrics(aggStats);
			printVerboseStats(aggStats, config);
		}
	}

	/**
	 * Input is in nBest format, output will be in nBest score format
	 */

	private static void scoreNBest(final MeteorScorer scorer, Properties props,
			final MeteorConfiguration config, String testFile, String refFile)
			throws IOException {

		// Info for normalization since it will be done outside of the scorer
		int langID = config.getLangID();
		Boolean norm = Boolean.parseBoolean(props.getProperty("norm"));
		Boolean noPunct = Boolean.parseBoolean(props.getProperty("noPunct"));
		Boolean lower = Boolean.parseBoolean(props.getProperty("lower"));

		int refCount = getRefCount(props);
		final Boolean oracle = Boolean
				.parseBoolean(props.getProperty("oracle"));
		final Boolean ssOut = Boolean.parseBoolean(props.getProperty("ssOut"));
		final Boolean vOut = Boolean.parseBoolean(props.getProperty("vOut"));

		// Number of jobs to run simultaneously
		int jobs = 1;
		String jobString = props.getProperty("jobs");
		if (jobString != null)
			jobs = Integer.parseInt(jobString);

		final MeteorWorker.Request.Completion nextSegment = new MeteorWorker.Request.Completion();
		final MeteorWorker.Request.Completion doneScoring = new MeteorWorker.Request.Completion();
		final MeteorWorker.Request.Completion errorScoring = new MeteorWorker.Request.Completion();

		final ArrayBlockingQueue<MeteorWorker.Request> toScore = new ArrayBlockingQueue<MeteorWorker.Request>(
				jobs * 2);
		final ArrayBlockingQueue<MeteorWorker.Request.Completion> toOutput = new ArrayBlockingQueue<MeteorWorker.Request.Completion>(
				jobs * 2);

		ArrayList<Thread> scorers = new ArrayList<Thread>(jobs);
		for (int i = 0; i < jobs; i++) {
			Thread worker = new Thread(new MeteorWorker(scorer, toScore));
			worker.start();
			scorers.add(worker);
		}

		Thread printer = new Thread(new Runnable() {
			public void run() {
				MeteorStats bestAgg = new MeteorStats();
				MeteorStats firstAgg = new MeteorStats();
				String oracleTrans = "";
				MeteorWorker.Request.Completion task = null;
				int curSeg = 0;
				while (task != doneScoring) {
					int segNum = 0;
					MeteorStats best = null;
					MeteorStats first = null;
					while (true) {
						try {
							task = toOutput.take();
						} catch (java.lang.InterruptedException e) {
							return;
						}
						if (task == nextSegment)
							break;
						if (task == doneScoring)
							break;
						if (task == errorScoring)
							return;
						MeteorStats stats;
						try {
							stats = task.get();
						} catch (java.lang.InterruptedException e) {
							return;
						}
						if (first == null) {
							oracleTrans = task.test;
							best = stats;
							first = stats;
						} else if (stats.score > best.score) {
							oracleTrans = task.test;
							best = stats;
						}
						if (ssOut) {
							System.out.println(stats.toString());
						} else if (vOut) {
							System.out.println("Segment " + curSeg
									+ " translation " + (segNum + 1)
									+ " score:\t" + stats.precision + "\t"
									+ stats.recall + "\t" + stats.fragPenalty
									+ "\t" + stats.score);
						} else if (!oracle)
							System.out.println("Segment " + curSeg
									+ " translation " + (segNum + 1)
									+ " score:\t" + stats.score);
						segNum++;
					}
					if (first != null) {
						bestAgg.addStats(best);
						firstAgg.addStats(first);
					}
					if (task == nextSegment) {
						if (oracle) {
							System.out.println(oracleTrans);
						} else {
							System.out.println();
						}
					}
					curSeg++;
				}
				if (!oracle && !ssOut) {
					scorer.computeMetrics(bestAgg);
					scorer.computeMetrics(firstAgg);
					printVerboseStats(bestAgg, config,
							"Best-choice translation statistics\n");
					printVerboseStats(firstAgg, config,
							"\nFirst-sentence translation statistics\n");
				}
			}
		});
		printer.start();

		// Reading thread
		BufferedReader testRead = new BufferedReader(new InputStreamReader(
				new FileInputStream(testFile), "UTF-8"));
		BufferedReader refRead = new BufferedReader(new InputStreamReader(
				new FileInputStream(refFile), "UTF-8"));
		try {
			String line;
			while ((line = testRead.readLine()) != null) {
				int segCount = Integer.parseInt(line);

				// Read references
				ArrayList<String> refs = new ArrayList<String>(refCount);
				for (int refNum = 0; refNum < refCount; refNum++) {
					String ref = refRead.readLine();
					if (ref == null) {
						System.err.println("Error: too few references");
						toOutput.put(doneScoring);
						System.exit(1);
					}
					// Normalize refs as they are read
					if (norm)
						ref = Normalizer.normalizeLine(ref, langID, !noPunct);
					if (lower)
						ref = ref.toLowerCase();
					refs.add(ref);
				}

				for (int segNum = 0; segNum < segCount; segNum++) {
					String seg = testRead.readLine();
					if (seg == null) {
						System.err.println("Error: too few segments");
						toOutput.put(doneScoring);
						System.exit(1);
					}
					// Normalize segments as they are read
					if (norm)
						seg = Normalizer.normalizeLine(seg, langID, !noPunct);
					if (lower)
						seg = seg.toLowerCase();
					MeteorWorker.Request request = new MeteorWorker.Request(
							seg, refs);
					toScore.put(request);
					toOutput.put(request.getOut());
				}
				toOutput.put(nextSegment);
			}
			toOutput.put(doneScoring);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			try {
				toOutput.put(doneScoring);
			} catch (InterruptedException ex2) {
				ex.printStackTrace();
				System.exit(1);
			}
			System.err.println("Error: Interrupted");
			System.exit(1);
		}

		try {
			printer.join();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			System.exit(1);
		}

		try {
			for (int i = 0; i < jobs; i++) {
				toScore.put(MeteorWorker.DONE);
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			System.exit(1);
		}

		if (refRead.readLine() != null) {
			System.err.println("Error: too many references");
			System.exit(1);
		}

		testRead.close();
		refRead.close();
	}

	// Used for multithreaded nBest scoring
	static class MeteorWorker implements Runnable {

		public static class Request {
			public static class Completion {
				private Semaphore wait;
				private String test;
				private MeteorStats result;

				public Completion() {
					wait = new Semaphore(0);
				}

				public Completion(String test) {
					this.test = test;
					wait = new Semaphore(0);
				}

				public void finish(MeteorStats value) {
					result = value;
					wait.release();
				}

				// Only call once to avoid deadlock
				public MeteorStats get() throws InterruptedException {
					wait.acquire();
					return result;
				}
			}

			protected String test;
			protected ArrayList<String> references;

			private Completion out;

			// Null request
			protected Request() {
			}

			public Request(String seg, ArrayList<String> refs) {
				test = seg;
				references = refs;
				out = new Completion(test);
			}

			public Completion getOut() {
				return out;
			}
		}

		public static final Request DONE = new Request();

		private MeteorScorer scorer;
		private BlockingQueue<Request> requests;

		public MeteorWorker(MeteorScorer scorer, BlockingQueue<Request> queue) {
			this.scorer = new MeteorScorer(scorer);
			requests = queue;
		}

		public void run() {
			Request req;
			try {
				while ((req = requests.take()) != DONE) {
					req.getOut().finish(
							scorer.getMeteorStats(req.test, req.references));
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}

	/**
	 * Input is in SGML format, output to be in seg, doc, sys score format
	 */

	private static void scoreSGML(MeteorScorer scorer, Properties props,
			MeteorConfiguration config, String testFile, String refFile)
			throws IOException {

		// Gather SGML data
		SGMData data = new SGMData();
		SGMData.populate(data, testFile, false);
		SGMData.populate(data, refFile, true);

		// Print scores (or stats)
		Boolean ssOut = Boolean.parseBoolean(props.getProperty("ssOut"));
		Boolean vOut = Boolean.parseBoolean(props.getProperty("vOut"));

		/*
		 * Score all sets, documents, and segments for a single system against
		 * one or more references. Check for missing documents and segments.
		 * Save lines to be sorted and output. Assume data exists for specified
		 * system.
		 */

		// Output prefix
		String filePrefix = props.getProperty("filePrefix");
		if (filePrefix == null)
			filePrefix = "meteor";

		// Output lines
		ArrayList<String> sysLines = new ArrayList<String>();
		ArrayList<String> docLines = new ArrayList<String>();
		ArrayList<String> segLines = new ArrayList<String>();
		Hashtable<String, MeteorStats> segStats = new Hashtable<String, MeteorStats>();

		// Set keys
		HashSet<String> allSetIDs = new HashSet<String>();
		allSetIDs.addAll(data.testData.keySet());
		allSetIDs.addAll(data.refData.keySet());
		// For each set ...
		Iterator<String> setIDs = allSetIDs.iterator();
		while (setIDs.hasNext()) {
			// *[set]*[sys][doc][seg]
			String setID = setIDs.next();
			// All references
			Hashtable<String, Hashtable<String, Hashtable<String, String>>> refSyss = data.refData
					.get(setID);
			// All systems
			Hashtable<String, Hashtable<String, Hashtable<String, String>>> testSyss = data.testData
					.get(setID);
			// For each system ...
			Iterator<String> sysIDs = testSyss.keySet().iterator();
			while (sysIDs.hasNext()) {
				// [set]*[sys]*[doc][seg]
				String sysID = sysIDs.next();
				// One set, one sys, multiple doc, multiple segs
				MeteorStats sysStats = new MeteorStats();
				//
				// One system (must exist)
				Hashtable<String, Hashtable<String, String>> testDocs = testSyss
						.get(sysID);
				// All references (must exist)
				ArrayList<Hashtable<String, Hashtable<String, String>>> refDocs = new ArrayList<Hashtable<String, Hashtable<String, String>>>();
				ArrayList<String> refDocNames = new ArrayList<String>();
				Iterator<String> refIDs = refSyss.keySet().iterator();
				while (refIDs.hasNext()) {
					String r = refIDs.next();
					refDocs.add(refSyss.get(r));
					refDocNames.add(r);
				}
				// Doc keys
				HashSet<String> allDocIDs = new HashSet<String>();
				allDocIDs.addAll(testDocs.keySet());
				for (Hashtable<String, Hashtable<String, String>> r : refDocs)
					allDocIDs.addAll(r.keySet());
				// For each doc ...
				Iterator<String> docIDs = allDocIDs.iterator();
				while (docIDs.hasNext()) {
					// [set][sys]*[doc]*[seg]
					String docID = docIDs.next();
					// One set, one sys, one doc, multiple segs
					MeteorStats docStats = new MeteorStats();
					//
					// One test
					Hashtable<String, String> testSegs = testDocs.get(docID);
					if (testSegs == null) {
						System.err.println("Warning: no data for system ["
								+ sysID + "] on document [" + setID + "]["
								+ docID + "] - skipping this document");
						continue;
					}
					// All references (that exist)
					ArrayList<Hashtable<String, String>> refSegs = new ArrayList<Hashtable<String, String>>();
					ArrayList<String> refSegNames = new ArrayList<String>();
					for (int i = 0; i < refDocs.size(); i++) {
						String r = refDocNames.get(i);
						Hashtable<String, String> refSegsElement = refDocs.get(
								i).get(docID);
						if (refSegsElement == null) {
							System.err
									.println("Warning: no data for reference ["
											+ r + "] on document [" + setID
											+ "][" + docID + "]");
							continue;
						}
						refSegNames.add(r);
						refSegs.add(refSegsElement);
					}
					// Require at least one reference
					if (refSegs.size() == 0) {
						System.err
								.println("Warning: no usable references for document ["
										+ setID
										+ "]["
										+ docID
										+ "] - skipping this document");
						continue;
					}
					// Seg keys
					HashSet<String> allSegIDs = new HashSet<String>();
					allSegIDs.addAll(testSegs.keySet());
					for (Hashtable<String, String> r : refSegs)
						allSegIDs.addAll(r.keySet());
					// For each seg
					Iterator<String> segIDs = allSegIDs.iterator();
					while (segIDs.hasNext()) {
						// [set][sys][doc]*[seg]*
						String segID = segIDs.next();
						// One test
						String testSeg = testSegs.get(segID);
						if (testSeg == null) {
							System.err.println("Warning: no data for system ["
									+ sysID + "] on segment [" + setID + "]["
									+ docID + "][" + segID
									+ "] - scoring empty segment");
							testSeg = "";
						}
						// All references
						ArrayList<String> refSeg = new ArrayList<String>();
						for (int i = 0; i < refSegs.size(); i++) {
							String refSegName = refSegNames.get(i);
							String refSegText = refSegs.get(i).get(segID);
							if (refSegText == null) {
								System.err
										.println("Warning: no data for reference ["
												+ refSegName
												+ "] on segment ["
												+ setID
												+ "]["
												+ docID
												+ "]["
												+ segID + "]");
								continue;
							}
							refSeg.add(refSegText);
						}
						// Require at least one reference
						if (refSeg.size() == 0) {
							System.err
									.println("Warning: no usable references for segment ["
											+ setID
											+ "]["
											+ docID
											+ "]["
											+ segID
											+ "] - skipping this segment");
							continue;
						}
						// One system, one set, one doc, one seg
						// Score segment
						MeteorStats stats = scorer.getMeteorStats(testSeg,
								refSeg);
						//
						// Aggreagate statistics
						docStats.addStats(stats);
						sysStats.addStats(stats);
						// Eng segment: store score or SS
						String segName = setID + "\t" + sysID + "\t" + docID
								+ "\t" + segID + "\t";
						String segScore;
						if (ssOut) {
							segScore = stats.toString();
						} else if (vOut) {
							segScore = String.valueOf(stats.precision + "\t"
									+ stats.recall + "\t" + stats.fragPenalty
									+ "\t" + stats.score);
						} else {
							segScore = String.valueOf(stats.score);
						}
						String segLine = segName + segScore;
						segLines.add(segLine);
						// Store alignment and stats using seg key
						segStats.put(segLine, stats);
					}
					// End document: store score or SS
					scorer.computeMetrics(docStats);
					String docName = setID + "\t" + sysID + "\t" + docID + "\t";
					String docScore;
					if (ssOut) {
						docScore = docStats.toString();
					} else if (vOut) {
						docScore = String.valueOf(docStats.precision + "\t"
								+ docStats.recall + "\t" + docStats.fragPenalty
								+ "\t" + docStats.score);
					} else {
						docScore = String.valueOf(docStats.score);
					}
					docLines.add(docName + docScore);
				}
				// End system: store score or SS
				scorer.computeMetrics(sysStats);
				String setName = setID + "\t" + sysID + "\t";
				String setScore;
				if (ssOut) {
					setScore = sysStats.toString();
				} else if (vOut) {
					setScore = String.valueOf(sysStats.precision + "\t"
							+ sysStats.recall + "\t" + sysStats.fragPenalty
							+ "\t" + sysStats.score);
				} else {
					setScore = String.valueOf(sysStats.score);
				}
				sysLines.add(setName + setScore);

				/*
				 * Report system performance
				 */
				if (!ssOut) {
					printVerboseStats(sysStats, config, "[" + setID + "]["
							+ sysID + "] ");
					System.out.println();
				}
			}
		}

		/*
		 * Output score files: system-sys.score system-doc.score
		 * system-seg.score
		 */

		// System
		// Sort
		Collections.sort(sysLines, new Comparator<String>() {
			public int compare(String s1, String s2) {
				// s1 Set Sys
				int scoreDelim1 = s1.lastIndexOf("\t");
				String set1 = s1.substring(0, scoreDelim1);
				// s2 Set Sys
				int scoreDelim2 = s2.lastIndexOf("\t");
				String set2 = s2.substring(0, scoreDelim2);
				return set1.compareTo(set2);
			}
		});
		// Write
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream(filePrefix + "-sys.scr"), "UTF-8"));
		for (String line : sysLines)
			out.println(line);
		out.close();

		// Document
		// Sort
		Collections.sort(docLines, new Comparator<String>() {
			public int compare(String s1, String s2) {
				// s1 Set Sys Doc
				int scoreDelim1 = s1.lastIndexOf("\t");
				String doc1 = s1.substring(0, scoreDelim1);
				// s2 Set Sys Doc
				int scoreDelim2 = s2.lastIndexOf("\t");
				String doc2 = s2.substring(0, scoreDelim2);
				return doc1.compareTo(doc2);
			}
		});
		// Write
		out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
				filePrefix + "-doc.scr"), "UTF-8"));
		for (String line : docLines)
			out.println(line);
		out.close();

		// Segment
		// Sort
		Collections.sort(segLines, new Comparator<String>() {
			public int compare(String s1, String s2) {
				// s1 Set Sys Doc
				int scoreDelim1 = s1.lastIndexOf("\t");
				int segDelim1 = s1.lastIndexOf("\t", scoreDelim1 - 1);
				String doc1 = s1.substring(0, segDelim1);
				// s2 Set Sys Doc
				int scoreDelim2 = s2.lastIndexOf("\t");
				int segDelim2 = s2.lastIndexOf("\t", scoreDelim2 - 1);
				String doc2 = s2.substring(0, segDelim2);
				int diff = doc1.compareTo(doc2);
				// Same set/sys/doc ?
				if (diff != 0)
					return diff;
				// Compare Segs
				String seg1 = s1.substring(segDelim1 + 1, scoreDelim1);
				String seg2 = s2.substring(segDelim2 + 1, scoreDelim2);
				try {
					// Try to compare as ints
					int seg1int = Integer.parseInt(seg1);
					int seg2int = Integer.parseInt(seg2);
					return seg1int - seg2int;
				} catch (Exception ex) {
					// Otherwise compare as strings
					return seg1.compareTo(seg2);
				}
			}
		});
		// Write
		out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
				filePrefix + "-seg.scr"), "UTF-8"));
		for (String line : segLines)
			out.println(line);
		out.close();

		// Write alignments?
		Boolean writeAlignments = Boolean.parseBoolean(props
				.getProperty("writeAlignments"));
		if (writeAlignments) {
			out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
					filePrefix + "-align.out"), "UTF-8"));
			for (int i = 0; i < segLines.size(); i++) {
				String line = segLines.get(i);
				MeteorStats stats = segStats.get(line);
				int idx = -1;
				for (int j = 0; j < 4; j++) {
					idx = line.indexOf('\t', idx + 1);
				}
				String lineNoScore = line.substring(0, idx);
				out.println(stats.alignment.toString("Alignment\t"
						+ lineNoScore + '\t' + stats.precision + "\t"
						+ stats.recall + "\t" + stats.fragPenalty + "\t"
						+ stats.score));
			}
			out.close();
		}
	}

	private static void printVerboseStats(MeteorStats stats,
			MeteorConfiguration config) {
		printVerboseStats(stats, config, "\nSystem level statistics:\n");
	}

	private static void printVerboseStats(MeteorStats stats,
			MeteorConfiguration config, String header) {
		System.out.println(header);
		System.out.println();
		System.out
				.println("           Test Matches                  Reference Matches");
		System.out
				.println("Stage      Content  Function    Total    Content  Function    Total");
		ArrayList<Double> weights = config.getModuleWeights();
		int testContentMatches = 0;
		int testFunctionMatches = 0;
		int referenceContentMatches = 0;
		int referenceFunctionMatches = 0;
		for (int i = 0; i < weights.size(); i++) {
			System.out.println(String.format(
					"%1d          %7d   %7d  %7d    %7d   %7d  %7d", i + 1,
					stats.testStageMatchesContent.get(i),
					stats.testStageMatchesFunction.get(i),
					stats.testStageMatchesContent.get(i)
							+ stats.testStageMatchesFunction.get(i),
					stats.referenceStageMatchesContent.get(i),
					stats.referenceStageMatchesFunction.get(i),
					stats.referenceStageMatchesContent.get(i)
							+ stats.referenceStageMatchesFunction.get(i)));
			testContentMatches += stats.testStageMatchesContent.get(i);
			testFunctionMatches += stats.testStageMatchesFunction.get(i);
			referenceContentMatches += stats.referenceStageMatchesContent
					.get(i);
			referenceFunctionMatches += stats.referenceStageMatchesFunction
					.get(i);
		}
		System.out.println(String.format(
				"Total      %7d   %7d  %7d    %7d   %7d  %7d",
				testContentMatches, testFunctionMatches, testContentMatches
						+ testFunctionMatches, referenceContentMatches,
				referenceFunctionMatches, referenceContentMatches
						+ referenceFunctionMatches));
		System.out.println();
		System.out.println("Test words:             " + stats.testLength);
		System.out.println("Reference words:        " + stats.referenceLength);
		System.out.println("Chunks:                 " + stats.chunks);
		System.out.println("Precision:              " + stats.precision);
		System.out.println("Recall:                 " + stats.recall);
		System.out.println("f1:                     " + stats.f1);
		System.out.println("fMean:                  " + stats.fMean);
		System.out.println("Fragmentation penalty:  " + stats.fragPenalty);
		System.out.println();
		System.out.println("Final score:            " + stats.score);
	}

	public static Properties createPropertiesFromArgs(String[] args,
			int startIndex) {
		Properties props = new Properties();
		int curArg = startIndex;
		while (curArg < args.length) {
			if (args[curArg].equals("-l")) {
				props.setProperty("language", args[curArg + 1]);
				curArg += 2;
			} else if (args[curArg].equals("-t")) {
				props.setProperty("task", args[curArg + 1]);
				curArg += 2;
			} else if (args[curArg].equals("-p")) {
				props.setProperty("parameters", args[curArg + 1]);
				curArg += 2;
			} else if (args[curArg].equals("-m")) {
				props.setProperty("modules", args[curArg + 1]);
				curArg += 2;
			} else if (args[curArg].equals("-w")) {
				props.setProperty("moduleWeights", args[curArg + 1]);
				curArg += 2;
			} else if (args[curArg].equals("-r")) {
				props.setProperty("refCount", args[curArg + 1]);
				curArg += 2;
			} else if (args[curArg].equals("-x")) {
				props.setProperty("beamSize", args[curArg + 1]);
				curArg += 2;
			} else if (args[curArg].equals("-d")) {
				props.setProperty("synDir", args[curArg + 1]);
				curArg += 2;
			} else if (args[curArg].equals("-a")) {
				props.setProperty("paraFile", args[curArg + 1]);
				curArg += 2;
			} else if (args[curArg].equals("-j")) {
				props.setProperty("jobs", args[curArg + 1]);
				curArg += 2;
			} else if (args[curArg].equals("-f")) {
				props.setProperty("filePrefix", args[curArg + 1]);
				curArg += 2;
			} else if (args[curArg].equals("-writeAlignments")) {
				props.setProperty("writeAlignments", "true");
				curArg += 1;
			} else if (args[curArg].equals("-norm")) {
				props.setProperty("norm", "true");
				curArg += 1;
			} else if (args[curArg].equals("-lower")) {
				props.setProperty("lower", "true");
				curArg += 1;
			} else if (args[curArg].equals("-nBest")) {
				props.setProperty("nBest", "true");
				curArg += 1;
			} else if (args[curArg].equals("-oracle")) {
				props.setProperty("oracle", "true");
				curArg += 1;
			} else if (args[curArg].equals("-sgml")) {
				props.setProperty("sgml", "true");
				curArg += 1;
			} else if (args[curArg].equals("-noPunct")) {
				props.setProperty("noPunct", "true");
				curArg += 1;
			} else if (args[curArg].equals("-ssOut")) {
				props.setProperty("ssOut", "true");
				curArg += 1;
			} else if (args[curArg].equals("-vOut")) {
				props.setProperty("vOut", "true");
				curArg += 1;
			} else {
				System.err.println("Error: unknown option \"" + args[curArg]
						+ "\"");
				System.exit(1);
			}
			String params = props.getProperty("parameters");
			if (params != null)
				props.setProperty("task", "custom (" + params + ")");
		}
		return props;
	}

	public static MeteorConfiguration createConfiguration(Properties props) {

		// Default configuration
		MeteorConfiguration config = new MeteorConfiguration();

		// Language
		String language = props.getProperty("language");
		if (language != null)
			config.setLanguage(language);

		// Task
		String task = props.getProperty("task");
		if (task != null)
			config.setTask(task);

		// Parameters
		String parameters = props.getProperty("parameters");
		if (parameters != null) {
			ArrayList<Double> params = new ArrayList<Double>();
			StringTokenizer p = new StringTokenizer(parameters);
			while (p.hasMoreTokens())
				params.add(Double.parseDouble(p.nextToken()));
			config.setParameters(params);
		}

		// Weights
		String weights = props.getProperty("moduleWeights");
		if (weights != null) {
			ArrayList<Double> weightList = new ArrayList<Double>();
			StringTokenizer wtok = new StringTokenizer(weights);
			while (wtok.hasMoreTokens())
				weightList.add(Double.parseDouble(wtok.nextToken()));
			config.setModuleWeights(weightList);
		}

		// Modules
		String modules = props.getProperty("modules");
		if (modules != null) {
			ArrayList<String> modList = new ArrayList<String>();
			StringTokenizer mods = new StringTokenizer(modules);
			while (mods.hasMoreTokens())
				modList.add(mods.nextToken());
			config.setModulesByName(modList);
			// Update weights to match number of modules
			ArrayList<Double> weightList = config.getModuleWeights();
			ArrayList<Double> sizedWeightList = new ArrayList<Double>();
			for (int i = 0; i < modList.size(); i++) {
				if (i < weightList.size())
					sizedWeightList.add(weightList.get(i));
				else
					sizedWeightList.add(0.0);
			}
			config.setModuleWeights(sizedWeightList);
		}

		// Beam size
		String beamSize = props.getProperty("beamSize");
		if (beamSize != null)
			config.setBeamSize(Integer.parseInt(beamSize));

		// Synonym dir
		String synDir = (props.getProperty("synDir"));
		if (synDir != null)
			try {
				// This should not ever throw a malformed url exception
				config.setSynDirURL((new File(synDir)).toURI().toURL());
			} catch (MalformedURLException ex) {
				System.err.println("Error: Synonym directory URL NOT set");
				ex.printStackTrace();
			}

		// Paraphrase file
		String paraFile = (props.getProperty("paraFile"));
		if (paraFile != null)
			try {
				// This should not ever throw a malformed url exception
				config.setParaFileURL((new File(paraFile)).toURI().toURL());
			} catch (MalformedURLException ex) {
				System.err.println("Error: Paraphrase directory URL NOT set");
				ex.printStackTrace();
			}

		// Normalization
		Boolean norm = Boolean.parseBoolean(props.getProperty("norm"));
		Boolean lower = Boolean.parseBoolean(props.getProperty("lower"));
		Boolean noPunct = Boolean.parseBoolean(props.getProperty("noPunct"));
		Boolean nBest = Boolean.parseBoolean(props.getProperty("nBest"));

		if (nBest) {
			// NBest scoring handles its own normalization
			config.setNormalization(Constants.NO_NORMALIZE);
		} else if (norm) {
			if (noPunct)
				config.setNormalization(Constants.NORMALIZE_NO_PUNCT);
			else
				config.setNormalization(Constants.NORMALIZE_KEEP_PUNCT);
		} else if (lower) {
			config.setNormalization(Constants.NORMALIZE_LC_ONLY);
		} else {
			config.setNormalization(Constants.NO_NORMALIZE);
		}

		return config;
	}

	private static void printUsage() {
		System.out.println("Meteor version " + Constants.VERSION);
		System.out.println();
		System.out
				.println("Usage: java -Xmx2G -jar meteor-*.jar <test> <reference> [options]");
		System.out.println();
		System.out.println("Options:");
		System.out
				.println("-l language                     One of: en cz de es fr ar");
		System.out
				.println("-t task                         One of: rank adq hter li");
		System.out
				.println("-p 'alpha beta gamma delta'     Custom parameters (overrides default)");
		System.out
				.println("-m 'module1 module2 ...'        Specify modules (overrides default)");
		System.out
				.println("                                  Any of: exact stem synonym paraphrase");
		System.out
				.println("-w 'weight1 weight2 ...'        Specify module weights (overrides default)");
		System.out
				.println("-r refCount                     Number of references (plaintext only)");
		System.out.println("-x beamSize                     (default 40)");
		System.out
				.println("-d synonymDirectory             (if not default for language)");
		System.out
				.println("-a paraphraseFile               (if not default for language)");
		System.out
				.println("-j jobs                         Number of jobs to run (nBest only)");
		System.out
				.println("-f filePrefix                   Prefix for output files (default 'meteor')");
		System.out
				.println("-norm                           Tokenize / normalize punctuation and lowercase");
		System.out
				.println("                                  (Recommended unless scoring raw output with");
		System.out
				.println("                                   pretokenized references)");
		System.out
				.println("-lower                          Lowercase only (not required if -norm specified)");
		System.out
				.println("-noPunct                        Do not consider punctuation when scoring");
		System.out
				.println("                                  (Not recommended unless special case)");
		System.out
				.println("-sgml                           Input is in SGML format");
		System.out
				.println("-nBest                          Input is in nBest format");
		System.out
				.println("-oracle                         Output oracle translations (nBest only)");
		System.out
				.println("-vOut                           Output verbose scores (P / R / frag / score)");
		System.out
				.println("-ssOut                          Output sufficient statistics instead of scores");
		System.out
				.println("-writeAlignments                Output alignments annotated with Meteor scores");
		System.out
				.println("                                  (written to <prefix>-align.out)");
		System.out.println();
		System.out.println("Sample options for plaintext: -l <lang> -norm");
		System.out.println("Sample options for SGML: -l <lang> -norm -sgml");
		System.out
				.println("Sample options for raw output / pretokenized references: -l <lang> -lower");
		System.out.println();
		System.out.println("See README file for additional information");
	}
}