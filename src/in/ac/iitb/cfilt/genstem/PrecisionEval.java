package in.ac.iitb.cfilt.genstem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class PrecisionEval {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String directoryLoc = "/home/lavita/workspace/Trie/result_files/result_morfessor";
		File folder = new File(directoryLoc);
		File[] listOfFiles;
		String FileName;
		float ResultCount;
		float outputCount;
		float pValue;
		String[] rootedResultGold;
		String RootWord;
		String[] RootWordArray;
		int startVal, startBraceVal;
		int endVal, endBraceVal;
		String ResultExtract;
		String ResultExtractSpace1;
		String ResultExtractSpace;
		String FinalResult;
		String[] outputList;

		listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				FileName = file.getName();
				System.out.println(FileName);
				try {
					// Open the file that is the first
					// command line parameter
					FileInputStream fstream = new FileInputStream(directoryLoc
							+ "/" + FileName);
					// Get the object of DataInputStream
					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(in));

					// Create file
					FileWriter fstreamWrite = new FileWriter(
							"Precision_Calculation_morfessor", true);
					BufferedWriter out = new BufferedWriter(fstreamWrite);
					String strLine;
					// Read File Line By Line
					ResultCount = 0;
					outputCount = 0;
					while ((strLine = br.readLine()) != null) {
						// Print the content on the console
						// System.out.println(strLine);
						ResultCount++;
						rootedResultGold = strLine.split(" ");
						RootWord = rootedResultGold[1];

						startVal = strLine.indexOf("[");
						endVal = strLine.indexOf("]");

						ResultExtract = strLine.substring(startVal + 1, endVal);
						ResultExtractSpace1 = ResultExtract.replace("||", " ");
						ResultExtractSpace = ResultExtractSpace1.replace("  ",
								" ");
						FinalResult = ResultExtractSpace.trim();
						outputList = FinalResult.split(" ");
						// System.out.println(FinalResult);

						if (RootWord.indexOf("}") != -1) {
							startBraceVal = RootWord.indexOf("{");
							endBraceVal = RootWord.indexOf("}");
							RootWord = RootWord.substring(startBraceVal + 1,
									endBraceVal);
							// System.out.println("RootWord 1 : " + RootWord);

							RootWordArray = RootWord.split(",");
							for (String wordOutput : outputList) {
								for (String RootWordArrayOutput : RootWordArray) {

									if (wordOutput
											.equalsIgnoreCase(RootWordArrayOutput)) {
										outputCount++;
									}
								}

							}

						} else {
							for (String wordOutput : outputList) {
								// System.out.println("RootWord 2 : " +
								// RootWord);
								if (wordOutput.equalsIgnoreCase(RootWord)) {
									outputCount++;
								}
							}
						}
					}
					pValue = (outputCount / ResultCount) * 100;
					try {
						out.write("\n" + "File Name: " + "\n" + FileName + "\n"
								+ "CorrectOutput: " + outputCount + "\n"
								+ "Total Words in Document: " + ResultCount
								+ "\n" + "Precision (p) Value: " + pValue
								+ "\n");
						// Close the output stream

					} catch (Exception e) {// Catch exception if any
						System.err.println("Error 1: " + e.getMessage());
					}

					// Close the input stream
					in.close();
					out.close();
				} catch (Exception e) {// Catch exception if any
					System.err.println("Error 2: " + e.getMessage());
				}

			}
		}

	}
}
