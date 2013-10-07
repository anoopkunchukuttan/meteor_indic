package edu.cmu.meteor.aligner;

import in.ac.iitb.cfilt.genstem.Trie;

public class IndicStemmer implements Stemmer{

	/**
	 * @param args
	 */
	String lang;
	Trie t;
	public IndicStemmer(String lang) {
		t = new Trie();
		t.setupTrie(lang);
	}

	/**
	 * 
	 * THIS METHOD IS A SYNCHRONIZED WRAPPER FOR NON-THREADSAFE SNOWBALL
	 * STEMMERS.
	 */
	public synchronized String stem(String word) {
		return t.getStem(word, "n", "0");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
