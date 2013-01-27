package edu.cmu.meteor.aligner;

import org.tartarus.snowball.SnowballStemmer;

/**
 * 
 * SNOWBALL STEMMERS ARE NOT THREADSAFE. KEEP THIS IN MIND WHEN USING THIS
 * CLASS.
 * 
 */
public class SnowballStemmerWrapper implements Stemmer {

	private SnowballStemmer stemmer;

	public SnowballStemmerWrapper(SnowballStemmer stemmer) {
		this.stemmer = stemmer;
	}

	/**
	 * 
	 * THIS METHOD IS NOT THREADSAFE BECAUSE SNOWBALL STEMMERS ARE NOT
	 * THREADSAFE. USE WITH CAUTION.
	 */
	public String stem(String word) {
		stemmer.setCurrent(word);
		stemmer.stem();
		return stemmer.getCurrent();
	}
}
