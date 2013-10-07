package in.ac.iitb.cfilt.genstem;

/** ***********************************Trie.java*****************************************
 @author Ankit Bahuguna

 The words in the WordNet are stored in a data type Trie.  In this type of tree,
 each node can have a maximum of 52 children, one for each letter of the alphabet.
 Each node is structured like this.
 class node {
 char letter;
 boolean endsWord;
 node down;
 node right;
 node up;
 }

 Each node stores a char letter, a boolean variable (indicates if the letter stored
 in the node ends a word or not), a pointer to the child of the node, and a pointer
 to the right sibling of the node.  Each node can have a maximum of 52 children, one
 for each letter of the alphabet.  All children are arranged in alphabetical order
 for easy searching.  This Trie stores each word in the forward rather than backward 
 because the searching must be done from the start of the word.

 This document is responsible for reading the file whose name is sent from wordpop.java. 
 Each word in order is inserted into the Trie one at a time.  This class is also responsible
 for searching the Trie to see if a word (or phrase) is in the Trie.  Finally, this class keeps
 track of the amount of each letter stored in the Trie which is saved in amountList[].
 
 /* ************************************* Trie.java *************************************** */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrieFile {
	public class node {
		char letter;
		boolean endsWord;
		node down;
		node right;
		node up;

		public node(char c) { // Constructor 1
			letter = c;
			endsWord = false;
			down = null;
			right = null;
			up = null;
		}

		public node(char c, node r) { // Constructor 2
			letter = c;
			endsWord = false;
			down = null;
			right = r;
			up = null;
		}
	}

	int countfive = 0;
	String prefix = "";
	String myoutput = "";
	int length = 0;
	int lengthString;
	static node root;
	node current1 = null;
	node position;
	public int[] amountList = new int[52];

	public TrieFile() {
		root = position = null;
	}

	/*
	 * insert() input received: a String "str"
	 * 
	 * returns: nothing
	 * 
	 * Action - Insert the String "str" into the tree starting at the end of the
	 * word. each letter is inserted, and based on the other elements in the
	 * list a new child is added to the list, or an existing child is used.
	 */
	/******************************************************************************************/
	public void insert(String str) {
		if (str == null)
			return;
		int index = 0;
		if (root == null) {
			root = new node(' ');
		}
		node temp, tempBack, pred;
		temp = tempBack = pred = root;
		while (index < str.length()) {
			if (temp.down == null) {
				temp.down = new node(str.charAt(index));
				// System.out.println(str.charAt(index));
				// System.out.println(temp.down.letter);
				temp.down.up = temp;
				temp.down.right = new node(' ');
				temp.down.right.down = temp;
				temp = temp.down;
			} else {
				pred = temp;
				temp = temp.down;
				if (str.charAt(index) < temp.letter) {
					temp = new node(str.charAt(index), temp);
					temp.up = pred;
					pred.down = temp;
				} else if (str.charAt(index) > temp.letter) {
					while (temp.letter != ' '
							&& str.charAt(index) > temp.letter) {
						tempBack = temp;
						temp = temp.right;
					}
					if (str.charAt(index) != temp.letter) {
						temp = new node(str.charAt(index), temp);
						temp.up = tempBack.up;
						tempBack.right = temp;
					}
				}
			}
			index++;
		}
		temp.endsWord = true;
	}

	/*******************************************************************************/
	/*
	 * receives: a String str (the name of a file) returns: nothing - builds the
	 * tree function builds a tree by reading the file named fileName one string
	 * at a time, and calls insert() to add that word to the tree.
	 */
	public void setupTrie(String fileName) {
		String word = new String();
		try {
			FileReader read = new FileReader(fileName);
			BufferedReader in = new BufferedReader(read, 50);
			while (in.ready()) {
				word = in.readLine();
				insert(word);
			}
		} catch (Exception e) {

			e.printStackTrace();
			return;
		}
	}

	/**************************************************************************************/
	public String printAllString(node t, String prefix, String lang) {
		// System.out.println("-- Final Print Entered --");

		// int countx = 1;
		// System.out.println("Initial Prefix: " + prefix);

		prefix = prefix + t.letter;

		// System.out.println("Final Prefix: " + prefix);
		node down = t.down;
		// System.out.println("Current node during print all: " + t.letter);
		if (down != null) {
			node current = down;
			while (current != null && current.letter != ' ') {
				// System.out.println("YO");

				if (current.endsWord) {
					String word1 = prefix + current.letter;
					// System.out.println("01: " + word1);

					if (word1.length() <= lengthString + 4) {
						if (countfive < 20) {
							countfive++;
							// System.out.println("myoutput value1:" +
							// myoutput);

							myoutput = myoutput + word1 + "||";
							// System.out.println("myoutput value2:" +
							// myoutput);
						}
					}

				}
				// countx++;
				// String tem;
				// tem = printAllString(current, prefix, lang);
				printAllString(current, prefix, lang);
				// System.out.println("OO: " + tem);
				// System.out.println(countx);
				current = current.right;
			}
		}

		return myoutput;
	}

	/*********************************************************************************/
	public String searchInTrie(String str, String lang) {
		myoutput = "";
		// if(str.startsWith("१")||str.startsWith("२")||str.startsWith("३")||str.startsWith("४")||str.startsWith("५")||str.startsWith("६")||str.startsWith("७")||str.startsWith("८")||str.startsWith("९")||str.startsWith("०"))
		if (str.startsWith("১") || str.startsWith("২") || str.startsWith("৩")
				|| str.startsWith("৪") || str.startsWith("৫")
				|| str.startsWith("৬") || str.startsWith("৭")
				|| str.startsWith("৮") || str.startsWith("৯")
				|| str.startsWith("০") || str.startsWith("1")
				|| str.startsWith("2") || str.startsWith("3")
				|| str.startsWith("4") || str.startsWith("5")
				|| str.startsWith("6") || str.startsWith("7")
				|| str.startsWith("8") || str.startsWith("9")) {
			return str;
		}
		// System.out.println(str);
		node t = root;
		String word = "";
		String wordArray[] = new String[str.length()];
		myoutput = "";
		int count = 0;
		lengthString = str.length();
		length = str.length();
		for (int index = 0; index < str.length(); index++) {
			// If a null character is encountered at the end of the word then
			// decrease length by one. It uses the unicode's byte order mark to
			// signal end of line with 65279.

			if ((int) (str.charAt(index)) == 65279) {

				length--;
				continue;
			}
			// System.out.println(str.charAt(index));
			count++; // Increments count by one
			if (t.down != null) { // left most child of the current node (in
									// first iteration its root) is not null.
				t = t.down;
				// System.out.println("---\nNode Letter: " + t.letter);
				// System.out.println("Node Down: " + t.down.letter);
				// System.out.println("Node Up: " + t.up.letter);
				// System.out.println("Node Right: " + t.right.letter +
				// "\n---");
				// System.out.println("STR: " + str.charAt(index) + "\n---");
				while (str.charAt(index) != t.letter && t.letter != ' ') {
					// This while loop finds the rightmost child of a given node
					// which matches with the current node in consideration
					// System.out.println(str.charAt(index));
					// System.out.println("Node Right (While Loop): "
					// + t.right.letter);
					if (t.right.letter == ' ') {
						// System.out.println(t.right.letter);
						if (t.up.letter == ' ') {
							return str;
						} else {
							if (t.up.down.letter != ' ') {
								// System.out
								// .println("PRINT EXEC: Current Letter:"
								// + t.up.letter
								// + " Prefix Passed: "
								// + prefix.replace(prefix
								// .substring(prefix
								// .length() - 1),
								// ""));
								return printAllString(t.up, prefix.substring(0,
										prefix.length() - 1), lang);
								/* Most Common Return - Ankit Bahuguna */
								// System.out.println(prefix.replace(
								// prefix.substring(prefix.length() - 1),
								// ""));
							}
						}
					} else {
						t = t.right; // Traverses the Trie horizontally till we
										// achieve a node which satisfies the
										// condition for while loop
					}
				} // END OF WHILE LOOP

				/*
				 * Following code (below) is executed when the queried letter is
				 * equal to the t.letter value or, it is executed when the
				 * t.letter is equal to ' '
				 */
				// System.out.println("Prefix Before: " + prefix);
				prefix = prefix + t.letter;
				// Since an equality is achieved by queried letter and the node
				// in Trie. Thus, we add it to 'prefix'. The default value of
				// prefix is a null string.

				// System.out.println("Prefix After: " + prefix);

				// System.out.println("Word Before "+index+": "+word);
				// Similarly we add the current equal letter from the Trie node
				// to 'word'.
				// The default value of 'word' variable is a null string.
				word = word + t.letter;
				// After each iteration based on 'index' wordArray[] stores this
				// 'word'
				wordArray[index] = word;
				// System.out.println("Word After: "+word);
				// System.out.println("Word at Level [" + index + "] : "
				// + wordArray[index]);
				// System.out.println("End Word " + t.endsWord);
				// t.endsWord is a boolean variable marking the end of word. Its
				// true if the word is complete and false otherwise.
				if (t.endsWord) {
					// System.out.println("WORD ENDING MET");
					if (myoutput.equals("")) {
						// System.out.println("FIRST RESULT:");
						if (word.length() <= lengthString + 4) {
							if (countfive < 20) { // Shows the 5 outputs
													// [maximum match] after the
													// end word is encountered
													// to be true.
								countfive++;
								myoutput = " || " + word + " || ";
								// System.out.println(myoutput);
							}

						}
					} else { // If myoutput is already populated from previous
								// iterations
								// System.out.println("SECOND RESULT:");
						if (word.length() < lengthString + 4) {
							if (countfive < 20) {
								countfive++;
								myoutput = myoutput + word + " || ";
								// System.out.println(myoutput);
							}
						}
					}

					if (count == str.length()) {
						return str;
					}
				} else { // Executes when the endsword boolean variable is
							// false. This means traversal down the Trie.
					if (count == length) {
						// System.out.println("I came here 1");

						if (t.down == null) { // End Case ie the length of
												// string matches the total
												// number of iterations
												// ('count')
							// System.out.println("I came here 2");

							return myoutput;
						}
						// Executes when count == length and t.down!=null
						return printAllString(t,
								prefix.substring(0, prefix.length() - 1), lang);
					}
				}
			} else { // Executes when t.down == null
				// System.out.println("I came here 3");
				return myoutput;
			}
		} // END of FOR LOOP -- Checks both queried word character by character
			// with the words stored in Trie.
		// IF index < str.length() condition is violated then execute the lines
		// below.
		position = t;
		return myoutput;
	}

	/****************************************************************************************/
	public String searchInTrie(String str, String lang, String backtrackLevel) {
		node t_temp;
		int subVal = 0;

		myoutput = "";
		// if(str.startsWith("१")||str.startsWith("२")||str.startsWith("३")||str.startsWith("४")||str.startsWith("५")||str.startsWith("६")||str.startsWith("७")||str.startsWith("८")||str.startsWith("९")||str.startsWith("०"))
		if (str.startsWith("১") || str.startsWith("২") || str.startsWith("৩")
				|| str.startsWith("৪") || str.startsWith("৫")
				|| str.startsWith("৬") || str.startsWith("৭")
				|| str.startsWith("৮") || str.startsWith("৯")
				|| str.startsWith("০") || str.startsWith("1")
				|| str.startsWith("2") || str.startsWith("3")
				|| str.startsWith("4") || str.startsWith("5")
				|| str.startsWith("6") || str.startsWith("7")
				|| str.startsWith("8") || str.startsWith("9")) {
			return str;
		}
		// System.out.println(str);
		node t = root;
		String word = "";
		String wordArray[] = new String[str.length()];
		myoutput = "";
		int count = 0;
		lengthString = str.length();
		length = str.length();
		for (int index = 0; index < str.length(); index++) {
			// If a null character is encountered at the end of the word then
			// decrease length by one. It uses the unicode's byte order mark to
			// signal end of line with 65279.

			if ((int) (str.charAt(index)) == 65279) {

				length--;
				continue;
			}
			// System.out.println(str.charAt(index));
			count++; // Increments count by one
			if (t.down != null) { // left most child of the current node (in
									// first iteration its root) is not null.
				t = t.down;
				// System.out.println("---\nNode Letter: " + t.letter);
				// System.out.println("Node Down: " + t.down.letter);
				// System.out.println("Node Up: " + t.up.letter);
				// System.out.println("Node Right: " + t.right.letter +
				// "\n---");
				// System.out.println("STR: " + str.charAt(index) + "\n---");
				while (str.charAt(index) != t.letter && t.letter != ' ') {
					// This while loop finds the rightmost child of a given node
					// which matches with the current node in consideration
					// System.out.println(str.charAt(index));
					// System.out.println("Node Right (While Loop): "
					// + t.right.letter);
					if (t.right.letter == ' ') {
						// System.out.println(t.right.letter);
						if (t.up.letter == ' ') {
							return str;
						} else {
							if (t.up.down.letter != ' ') {
								// System.out.println("Backtrack Level: "
								// + backtrackLevel);
								t_temp = t.up.up;

								// Level of Backtrack
								if (backtrackLevel.equalsIgnoreCase("1")) {
									subVal = 2;
								} else if (backtrackLevel.equalsIgnoreCase("2")) {
									t_temp = t_temp.up;
									subVal = 3;
								} else if (backtrackLevel.equalsIgnoreCase("3")) {
									t_temp = t_temp.up.up;
									subVal = 4;
								} else if (backtrackLevel.equalsIgnoreCase("4")) {
									t_temp = t_temp.up.up.up;
									subVal = 5;
								} else if (backtrackLevel.equalsIgnoreCase("5")) {
									t_temp = t_temp.up.up.up.up;
									subVal = 6;
								} else if (backtrackLevel.equalsIgnoreCase("6")) {
									t_temp = t_temp.up.up.up.up.up;
									subVal = 7;
								}

								// System.out
								// .println("PRINT EXEC: Current Letter:"
								// + t_temp.letter
								// + " Prefix Passed: "
								// + prefix.replace(prefix
								// .substring(prefix
								// .length()
								// - subVal), ""));

								return printAllString(
										t_temp,
										(prefix.substring(0, prefix.length()
												- subVal)), lang);
								/* Most Common Return - Ankit Bahuguna */
								// System.out.println(prefix.replace(
								// prefix.substring(prefix.length() - 1),
								// ""));
							}
						}
					} else {
						t = t.right; // Traverses the Trie horizontally till we
										// achieve a node which satisfies the
										// condition for while loop
					}
				} // END OF WHILE LOOP

				/*
				 * Following code (below) is executed when the queried letter is
				 * equal to the t.letter value or, it is executed when the
				 * t.letter is equal to ' '
				 */
				// System.out.println("Prefix Before: " + prefix);
				prefix = prefix + t.letter;
				// Since an equality is achieved by queried letter and the node
				// in Trie. Thus, we add it to 'prefix'. The default value of
				// prefix is a null string.

				// System.out.println("Prefix After: " + prefix);

				// System.out.println("Word Before "+index+": "+word);
				// Similarly we add the current equal letter from the Trie node
				// to 'word'.
				// The default value of 'word' variable is a null string.
				word = word + t.letter;
				// After each iteration based on 'index' wordArray[] stores this
				// 'word'
				wordArray[index] = word;
				// System.out.println("Word After: "+word);
				// System.out.println("Word at Level [" + index + "] : "
				// + wordArray[index]);
				// System.out.println("End Word " + t.endsWord);
				// t.endsWord is a boolean variable marking the end of word. Its
				// true if the word is complete and false otherwise.
				if (t.endsWord) {
					// System.out.println("WORD ENDING MET");
					if (myoutput.equals("")) {
						// System.out.println("FIRST RESULT:");
						if (word.length() <= lengthString + 4) {
							if (countfive < 20) { // Shows the 5 outputs
													// [maximum match] after the
													// end word is encountered
													// to be true.
								countfive++;
								myoutput = " || " + word + " || ";
							}

						}
					} else { // If myoutput is already populated from previous
								// iterations
								// System.out.println("SECOND RESULT:");
						if (word.length() <= lengthString + 4) {
							if (countfive < 20) {
								countfive++;
								myoutput = myoutput + word + " || ";
							}
						}
					}

					if (count == str.length()) {
						return str;
					}
				} else { // Executes when the endsword boolean variable is
							// false. This means traversal down the Trie.
					if (count == length) {
						// System.out.println("I came here 1");

						if (t.down == null) { // End Case ie the length of
												// string matches the total
												// number of iterations
												// ('count')
												// System.out.println("I came here 2");

							return myoutput;
						}
						// Executes when count == length and t.down!=null
						return printAllString(t,
								prefix.substring(0, prefix.length() - 1), lang);
					}
				}
			} else { // Executes when t.down == null
				// System.out.println("I came here 3");
				return myoutput;
			}
		} // END of FOR LOOP -- Checks both queried word character by character
			// with the words stored in Trie.
		// IF index < str.length() condition is violated then execute the lines
		// below.
		position = t;
		return myoutput;
	}

	/***************************************************************************************/
	public static String rankTrieResults(String trieResult, int lengthInput) {
		// System.out.println("Rank Function Entered for : " + trieResult);
		String trimmedResult = null;
		String finalResult = null;
		String trieResultFinal = "";
		String[] finalResultArray;
		String trieResultFinal1 = "";

		trimmedResult = trieResult.trim();
		trimmedResult = trimmedResult.replace("  ", " ");
		finalResult = trimmedResult.replace("||", " ");
		finalResultArray = finalResult.split(" ");

		// Sort Array with respect to length we use an ArrayList "myList"
		List myList = new ArrayList();
		Collections.addAll(myList, finalResultArray);
		// Sorting Step
		Collections.sort(myList, new MyComparator());

		// Re-conversion of an Array List "myList" into finalResultArraySorted
		// array.
		String[] finalResultArraySorted = new String[myList.size()];
		myList.toArray(finalResultArraySorted);

		for (String root : finalResultArraySorted) {
			root = root.trim();
			// System.out.println("Root and Length: " + root + " : "
			// + root.length());

			if (root.length() <= lengthInput + 2) {
				if (root.length() != 0) {
					// System.out.println("Root and Length: " + root + " : "
					// + root.length());
					trieResultFinal = trieResultFinal + root + " ";
				}

			}
		}
		trieResultFinal1 = trieResultFinal.trim();
		return trieResultFinal1;
	}

	/***************************************************************************************/

	public static void main(String[] args) throws IOException {
		String result;

		String resultValue;
		int lengthInput;

		String lang = null;
		String infword = null;
		String backTrack = null;

		String filename = null;

		// // TRIE MODIFIED TO RUN ON FILES
		lang = "22";
		backTrack = "n";
		String FinalResult;
		String FinalResultOut;
		String[] rootedGold; // infword

		String directoryLoc = "/home/lavita/workspace/Trie/all_rooted_corpus/others/unique_morfessor";
		File folder = new File(directoryLoc);
		File[] listOfFiles;
		String FileName;

		listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				FileName = file.getName();
				System.out.println(FileName);
				if (FileName.equalsIgnoreCase("assamese_rooted_all_unique")) {
					lang = "2";
					filename = "./trie_wn_db/assamese.wordnet";
				} else if (FileName
						.equalsIgnoreCase("english_health_rooted_all_unique")
						|| FileName
								.equalsIgnoreCase("english_tourism_rooted_all_unique")
						|| FileName.equalsIgnoreCase("english_results.txt")) {
					lang = "22";
					filename = "./trie_wn_db/english.wordnet";
				} else if (FileName
						.equalsIgnoreCase("marathi_health_rooted_all_unique")
						|| FileName
								.equalsIgnoreCase("marathi_tourism_rooted_all_unique")
						|| FileName.equalsIgnoreCase("marathi_results.txt")) {
					lang = "11";
					filename = "./trie_wn_db/marathi.wordnet";
				} else if (FileName
						.equalsIgnoreCase("bengali_health_rooted_all_unique")
						|| FileName
								.equalsIgnoreCase("bengali_tourism_rooted_all_unique")) {
					lang = "3";
					filename = "./trie_wn_db/bengali.wordnet";
				} else if (FileName
						.equalsIgnoreCase("konkani_health_rooted_all_unique")
						|| FileName
								.equalsIgnoreCase("konkani_tourism_rooted_all_unique")) {
					lang = "8";
					filename = "./trie_wn_db/konkani.wordnet";
				} else if (FileName
						.equalsIgnoreCase("punjabi_health_rooted_all_unique")
						|| FileName
								.equalsIgnoreCase("punjabi_tourism_rooted_all_unique")) {
					lang = "16";
					filename = "./trie_wn_db/punjabi.wordnet";
				} else if (FileName
						.equalsIgnoreCase("malayalam_rooted_all_unique")) {
					lang = "9";
					filename = "./trie_wn_db/malayalam.wordnet";
				} else if (FileName
						.equalsIgnoreCase("kannada_rooted_all_unique")) {
					lang = "6";
					filename = "./trie_wn_db/kannada.wordnet";
				} else if (FileName
						.equalsIgnoreCase("hindi_health_rooted_all_unique")
						|| FileName
								.equalsIgnoreCase("hindi_tourism_rooted_all_unique")
						|| FileName.equalsIgnoreCase("hindi_results.txt")) {
					lang = "0";
					filename = "./trie_wn_db/hindi.wordnet";
				}

				try { // Open the file that is the first
						// command line parameter
					FileInputStream fstream = new FileInputStream(directoryLoc
							+ "/" + FileName); // Get the object of
												// DataInputStream
					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(in));

					// Create file FileWriter
					FileWriter fstreamWrite = new FileWriter(FileName
							+ "_trie_output", true);
					BufferedWriter out = new BufferedWriter(fstreamWrite);
					String strLine; // Read File Line By Line
					while ((strLine = br.readLine()) != null) { // Print the
																// content on
																// the console
						// System.out.println(strLine);
						rootedGold = strLine.split(" ");
						// rootedGold[0] - Inflected Word
						// rootedGold[1] - Root Word

						try {
							if (backTrack.equalsIgnoreCase("N")) { //
								// System.out.println("EXIT");

								TrieFile t = new TrieFile();
								t.setupTrie(filename);
								infword = rootedGold[0];
								lengthInput = infword.length();

								resultValue = t.searchInTrie(infword, lang);

								result = rankTrieResults(resultValue,
										lengthInput);

								if (result.equals("")) {
									FinalResult = infword;
								} else {
									FinalResult = result;
								}
								FinalResultOut = rootedGold[0] + " "
										+ rootedGold[1] + " " + "["
										+ FinalResult + "]";

								out.write(FinalResultOut + "\n");

							}

							// Close the output stream

						} catch (Exception e) {// Catch exception if any
							System.err.println("Error: " + e.getMessage());
						}
					}
					// Close the input stream
					in.close();
					out.close();
				} catch (Exception e) {// Catchexception if any
					System.err.println("Error: " + e.getMessage());
				}
			}

		}
		// ///////////////////////////////////////////////////////////////////////////////

	}
}
