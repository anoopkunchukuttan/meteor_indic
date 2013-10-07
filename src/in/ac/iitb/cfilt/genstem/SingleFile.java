package in.ac.iitb.cfilt.genstem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class SingleFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String directoryLoc = "/home/lavita/workspace/lavita/modified/";
		File folder = new File(directoryLoc);
		File[] listOfFiles;
		String FileName;

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
							"english_tourism_rooted_all.txt", true);
					BufferedWriter out = new BufferedWriter(fstreamWrite);
					String strLine;
					// Read File Line By Line
					while ((strLine = br.readLine()) != null) {
						// Print the content on the console
//						System.out.println(strLine);
						try {

							out.write(strLine + "\n");
							// Close the output stream

						} catch (Exception e) {// Catch exception if any
							System.err.println("Error: " + e.getMessage());
						}
					}
					// Close the input stream
					in.close();
					out.close();
				} catch (Exception e) {// Catch exception if any
					System.err.println("Error: " + e.getMessage());
				}

			}
		}

	}
}
