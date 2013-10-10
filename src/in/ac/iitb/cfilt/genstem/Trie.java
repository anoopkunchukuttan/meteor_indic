package in.ac.iitb.cfilt.genstem;

/**
 * ***********************************Trie.java*********************************
 * ********
 * 
 * @author Ankit Bahuguna
 * 
 *         The words in the WordNet are stored in a data type Trie. In this type
 *         of tree, each node can have a maximum of 52 children, one for each
 *         letter of the alphabet. Each node is structured like this. class node
 *         { char letter; boolean endsWord; node down; node right; node up; }
 * 
 *         Each node stores a char letter, a boolean variable (indicates if the
 *         letter stored in the node ends a word or not), a pointer to the child
 *         of the node, and a pointer to the right sibling of the node. Each
 *         node can have a maximum of 52 children, one for each letter of the
 *         alphabet. All children are arranged in alphabetical order for easy
 *         searching. This Trie stores each word in the forward rather than
 *         backward because the searching must be done from the start of the
 *         word.
 * 
 *         This document is responsible for reading the file whose name is sent
 *         from wordpop.java. Each word in order is inserted into the Trie one
 *         at a time. This class is also responsible for searching the Trie to
 *         see if a word (or phrase) is in the Trie. Finally, this class keeps
 *         track of the amount of each letter stored in the Trie which is saved
 *         in amountList[].
 * 
 *         /* ************************************* Trie.java
 *         ***************************************
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Trie {
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

	int countfive;
	String prefix;
	String myoutput;
	int length ;
	int lengthString;
	static node root;
	node current1;
	node position;
	public int[] amountList;
	static String[] unicodeRange;

	public Trie() {
		countfive = 0;
		prefix = "";
		myoutput = "";
		length = 0;
		current1 = null;
		amountList = new int[52];
		unicodeRange = null;
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
	public void setupTrie(String lang) {
		Properties prop = new Properties();
		String filename = null;
		String word = new String();
		countfive = 0;
		prefix = "";
		myoutput = "";
		length = 0;
		current1 = null;
		amountList = new int[52];
		unicodeRange = null;
		root = position = null;
		try {
			
			prop.load(new FileInputStream(
					"./indicResources/properties/unicode_range.properties"));
			// INDIAN LANGUAGES
			// HINDI
			if (lang.equalsIgnoreCase("hi")) {
				filename = "./indicResources/trie_wn_db/hi.wordnet";
				unicodeRange = prop.getProperty("HINDI").split("-");
			}
			// ASSAMESE
			if (lang.equalsIgnoreCase("as")) {
				filename = "./indicResources/trie_wn_db/as.wordnet";
				unicodeRange = prop.getProperty("ASSAMESE").split("-");
			}
			// BENGALI
			if (lang.equalsIgnoreCase("bn")) {
				filename = "./indicResources/trie_wn_db/bn.wordnet";
				unicodeRange = prop.getProperty("BENGALI").split("-");
			}
			// BODO
			if (lang.equalsIgnoreCase("bD")) {
				filename = "./indicResources/trie_wn_db/bD.wordnet";
				unicodeRange = prop.getProperty("BODO").split("-");
			}
			// GUJARATI
			if (lang.equalsIgnoreCase("gu")) {
				filename = "./indicResources/trie_wn_db/gu.wordnet";
				unicodeRange = prop.getProperty("GUJARATI").split("-");
			}
			// KANNADA
			if (lang.equalsIgnoreCase("kn")) {
				filename = "./indicResources/trie_wn_db/kn.wordnet";
				unicodeRange = prop.getProperty("KANNADA").split("-");
			}
			// KASHMIRI
			if (lang.equalsIgnoreCase("ks")) {
				filename = "./indicResources/trie_wn_db/ks.wordnet";
				unicodeRange = prop.getProperty("KASHMIRI").split("-");
			}
			// KONKANI
			if (lang.equalsIgnoreCase("kK")) {
				filename = "./indicResources/trie_wn_db/kK.wordnet";
				unicodeRange = prop.getProperty("KONKANI").split("-");
			}
			// MALAYALAM
			if (lang.equalsIgnoreCase("ml")) {
				filename = "./indicResources/trie_wn_db/ml.wordnet";
				unicodeRange = prop.getProperty("MALAYALAM").split("-");
			}
			// MANIPURI
			if (lang.equalsIgnoreCase("mP")) {
				filename = "./indicResources/trie_wn_db/mP.wordnet";
				unicodeRange = prop.getProperty("MANIPURI").split("-");
			}
			// MARATHI
			if (lang.equalsIgnoreCase("mr")) {
				filename = "./indicResources/trie_wn_db/mr.wordnet";
				unicodeRange = prop.getProperty("MARATHI").split("-");
			}
			// NEPALI
			if (lang.equalsIgnoreCase("ne")) {
				filename = "./indicResources/trie_wn_db/ne.wordnet";
				unicodeRange = prop.getProperty("NEPALI").split("-");
			}
			// SANSKRIT
			if (lang.equalsIgnoreCase("sa")) {
				filename = "./indicResources/trie_wn_db/sa.wordnet";
				unicodeRange = prop.getProperty("SANSKRIT").split("-");
			}
			// TAMIL
			if (lang.equalsIgnoreCase("ta")) {
				filename = "./indicResources/trie_wn_db/ta.wordnet";
				unicodeRange = prop.getProperty("TAMIL").split("-");
			}
			// TELUGU
			if (lang.equalsIgnoreCase("te")) {
				filename = "./indicResources/trie_wn_db/te.wordnet";
				unicodeRange = prop.getProperty("TELUGU").split("-");
			}
			// PUNJABI
			if (lang.equalsIgnoreCase("pa")) {
				filename = "./indicResources/trie_wn_db/pa.wordnet";
				unicodeRange = prop.getProperty("PUNJABI").split("-");
			}
			// URDU
			if (lang.equalsIgnoreCase("ur")) {
				filename = "./indicResources/trie_wn_db/ur.wordnet";
				unicodeRange = prop.getProperty("URDU").split("-");
			}
			// ODIYA
			if (lang.equalsIgnoreCase("or")) {
				filename = "./indicResources/trie_wn_db/or.wordnet";
				unicodeRange = prop.getProperty("ODIYA").split("-");
			}
			// ///////////////////////////////////
			// EUROPEAN LANGUAGES
			// FRENCH
			if (lang.equalsIgnoreCase("fr")) {
				filename = "./indicResources/trie_wn_db/fr.wordnet";
			}
			// DANISH
			if (lang.equalsIgnoreCase("da")) {
				filename = "./indicResources/trie_wn_db/da.wordnet";
			}
			// HUNGARY
			if (lang.equalsIgnoreCase("hu")) {
				filename = "./indicResources/trie_wn_db/hu.wordnet";
			}
			// ENGLISH
			if (lang.equalsIgnoreCase("en")) {
				filename = "./indicResources/trie_wn_db/en.wordnet";
			}
			// ITALIAN
			if (lang.equalsIgnoreCase("it")) {
				filename = "./indicResources/trie_wn_db/it.wordnet";
			}
			FileReader read = new FileReader(filename);
			BufferedReader in = new BufferedReader(read, 50);
			while (in.ready()) {
				word = in.readLine();
				insert(word);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	/**************************************************************************************/
	public String printAllString(node t, String prefix) {
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
				printAllString(current, prefix);
				// System.out.println("OO: " + tem);
				// System.out.println(countx);
				current = current.right;
			}
		}

		return myoutput;
	}

	/*********************************************************************************/
	public String searchInTrie(String str) {
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
										prefix.length() - 1));
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
						if (word.length() <= lengthString + 1) {
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
						if (word.length() < lengthString + 1) {
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
								prefix.substring(0, prefix.length() - 1));
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
	public String searchInTrie(String str, String backtrackLevel) {
		node t_temp, t_temp2;
		int subVal = 0;
		int subVal2 = 0;

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
												- subVal)));
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

						t_temp2 = t.up;

						// Level of Backtrack
						if (backtrackLevel.equalsIgnoreCase("1")) {
							subVal2 = 2;
						} else if (backtrackLevel.equalsIgnoreCase("2")) {
							t_temp2 = t_temp2.up;
							subVal2 = 3;
						} else if (backtrackLevel.equalsIgnoreCase("3")) {
							t_temp2 = t_temp2.up.up;
							subVal2 = 4;
						} else if (backtrackLevel.equalsIgnoreCase("4")) {
							t_temp2 = t_temp2.up.up.up;
							subVal2 = 5;
						} else if (backtrackLevel.equalsIgnoreCase("5")) {
							t_temp2 = t_temp2.up.up.up.up;
							subVal2 = 6;
						} else if (backtrackLevel.equalsIgnoreCase("6")) {
							t_temp2 = t_temp2.up.up.up.up.up;
							subVal2 = 7;
						}

						// Executes when count == length and t.down!=null
						return printAllString(t_temp2,
								prefix.substring(0, prefix.length() - subVal2));
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

	public String rankTrieResults(String trieResult, int lengthInput) {
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
		ArrayList<String> myList = new ArrayList<String>();
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

			if (getCharLength(root) <= lengthInput) {
				if (root.length() != 0) {
					// System.out.println("Root and Length: " + root + " : "
					// + root.length());
					trieResultFinal = trieResultFinal + root + "  ";
				}
			}
		}
		trieResultFinal1 = trieResultFinal.trim();
		return trieResultFinal1;
	}

	/***************************************************************************************/

	public static void main(String[] args) throws IOException {
		Trie t = new Trie();
		t.setupTrie("hi");
		System.out.println(t.getRoot("बताओ", "n", "0"));
		
		System.out.println(t.getStem("लड़कियाँ", "n", "0"));


	}

	public int getCharLength(String word) {
		int len = 0;
		for (int i = 0; i < word.length(); i++) {
			int value = (int) word.charAt(i);
			if ((Integer.valueOf(unicodeRange[0], 16) < value && value < Integer
					.valueOf(unicodeRange[1], 16))
					|| (Integer.valueOf(unicodeRange[2], 16) < value && value < Integer
							.valueOf(unicodeRange[3], 16)))
				len++;
		}
		return len;
	}
	public String getStem(String infword, String backTrack,String backtrackLevel ){
		//Getting the morphological base form 
				HashMap<Integer, List<String>> lScore = new HashMap<Integer, List<String>>();
				String stem = "";
				String result;
				String result1;
				String resultValue;
				int lengthInput;
				countfive = 0;
				prefix = "";
				myoutput = "";
				length = 0;


				try {
					lengthInput = getCharLength(infword);
					// Backtrack = NO
					if (backTrack.equalsIgnoreCase("N")) {
						resultValue = searchInTrie(infword);
						result = rankTrieResults(resultValue, lengthInput);
						result = result.trim().replace("  ", " ");
						String[] stems = result.split(" ");
						//System.out.println("Result: " + result);
						int score2;
						int score1;
						for (String str : stems) {
							score1 = LDistance.LD(str, infword);

							if (lScore.get(score1) == null) {
								List<String> list = new ArrayList<String>();
								list.add(str);
								lScore.put(score1, list);
							} else {
								lScore.get(score1).add(str);
							}
						}
						for (Integer key : lScore.keySet()) {
							List<String> tempVal = lScore.get(key);

							HashMap<Integer, List<String>> tempMap = new HashMap<Integer, List<String>>();
							for (String st : tempVal) {
								score2 = LDistance.stackDistance(st, infword);

								if (tempMap.get(score2) == null) {
									List<String> list = new ArrayList<String>();
									list.add(st);
									tempMap.put(score2, list);

								} else {
									tempMap.get(score2).add(st);
								}

							}
							for (Integer k1 : tempMap.keySet()) {
								stem = tempMap.get(k1).get(0);
//								System.out.println(key + " : " + k1 + ":"
//										+ tempMap.get(k1));

							}
							break;
						}
					}

					// Backtrack = Yes
					else if (backTrack.equalsIgnoreCase("Y")) {
						resultValue = searchInTrie(infword, backtrackLevel);
						result1 = rankTrieResults(resultValue, lengthInput);
						result1 = result1.trim().replace("  ", " ");
						String[] stems = result1.split(" ");
						//System.out.println("Result: " + result1);
						int score2;
						int score1;
						for (String str : stems) {
							score1 = LDistance.LD(str, infword);

							if (lScore.get(score1) == null) {
								List<String> list = new ArrayList<String>();
								list.add(str);
								lScore.put(score1, list);
							} else {
								lScore.get(score1).add(str);
							}
						}
						for (Integer key : lScore.keySet()) {
							List<String> tempVal = lScore.get(key);

							HashMap<Integer, List<String>> tempMap = new HashMap<Integer, List<String>>();
							for (String st : tempVal) {
								score2 = LDistance.stackDistance(st, infword);

								if (tempMap.get(score2) == null) {
									List<String> list = new ArrayList<String>();
									list.add(st);
									tempMap.put(score2, list);

								} else {
									tempMap.get(score2).add(st);
								}

							}
							for (Integer k1 : tempMap.keySet()) {
								stem = tempMap.get(k1).get(0);
//								System.out.println(key + " : " + k1 + ":"
//										+ tempMap.get(k1));
							}
							break;
						}

					} else {
						System.out.println("EXIT from trie stemming program");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				int longestSub = LDistance.longestSubstr(infword,stem);
				if(longestSub==0){
					return infword;
				}
				else{
					
					stem =infword.substring(0,longestSub);
					System.out.println("Stem="+infword+"::"+stem);
					return stem;
				}
				
	}
	
	public String getRoot(String infword, String backTrack,
			String backtrackLevel) {
		//Getting the morphological base form 
		HashMap<Integer, List<String>> lScore = new HashMap<Integer, List<String>>();
		String root = "";
		String result;
		String result1;
		String resultValue;
		int lengthInput;
		countfive = 0;
		prefix = "";
		myoutput = "";
		length = 0;

		try {
			lengthInput = getCharLength(infword);
			// Backtrack = NO
			if (backTrack.equalsIgnoreCase("N")) {
				resultValue = searchInTrie(infword);
				result = rankTrieResults(resultValue, lengthInput);
				result = result.trim().replace("  ", " ");
				String[] stems = result.split(" ");
				//System.out.println("Result: " + result);
				int score2;
				int score1;
				for (String str : stems) {
					score1 = LDistance.LD(str, infword);

					if (lScore.get(score1) == null) {
						List<String> list = new ArrayList<String>();
						list.add(str);
						lScore.put(score1, list);
					} else {
						lScore.get(score1).add(str);
					}
				}
				for (Integer key : lScore.keySet()) {
					List<String> tempVal = lScore.get(key);

					HashMap<Integer, List<String>> tempMap = new HashMap<Integer, List<String>>();
					for (String st : tempVal) {
						score2 = LDistance.stackDistance(st, infword);

						if (tempMap.get(score2) == null) {
							List<String> list = new ArrayList<String>();
							list.add(st);
							tempMap.put(score2, list);

						} else {
							tempMap.get(score2).add(st);
						}

					}
					for (Integer k1 : tempMap.keySet()) {
						root = tempMap.get(k1).get(0);
//						System.out.println(key + " : " + k1 + ":"
//								+ tempMap.get(k1));

					}
					break;
				}
			}

			// Backtrack = Yes
			else if (backTrack.equalsIgnoreCase("Y")) {
				resultValue = searchInTrie(infword, backtrackLevel);
				result1 = rankTrieResults(resultValue, lengthInput);
				result1 = result1.trim().replace("  ", " ");
				String[] stems = result1.split(" ");
				//System.out.println("Result: " + result1);
				int score2;
				int score1;
				for (String str : stems) {
					score1 = LDistance.LD(str, infword);

					if (lScore.get(score1) == null) {
						List<String> list = new ArrayList<String>();
						list.add(str);
						lScore.put(score1, list);
					} else {
						lScore.get(score1).add(str);
					}
				}
				for (Integer key : lScore.keySet()) {
					List<String> tempVal = lScore.get(key);

					HashMap<Integer, List<String>> tempMap = new HashMap<Integer, List<String>>();
					for (String st : tempVal) {
						score2 = LDistance.stackDistance(st, infword);

						if (tempMap.get(score2) == null) {
							List<String> list = new ArrayList<String>();
							list.add(st);
							tempMap.put(score2, list);

						} else {
							tempMap.get(score2).add(st);
						}

					}
					for (Integer k1 : tempMap.keySet()) {
						root = tempMap.get(k1).get(0);
//						System.out.println(key + " : " + k1 + ":"
//								+ tempMap.get(k1));
					}
					break;
				}

			} else {
				System.out.println("EXIT from trie stemming program");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Root="+infword+"::"+root);
		return root;
	}

}
