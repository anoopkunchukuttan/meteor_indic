package in.ac.iitb.cfilt.genstem.helper;

import java.util.Vector;

/**
 * <p>Class	: WordnetSynset
 * <p>Purpose	: This class is 
 */
public class WordnetSynset {
	/**
	 * This field stores
	 */
	private long m_synsetID = 0;
	/**
	 * This field stores
	 */
	private Vector<String> m_words = new Vector<String>();
	/**
	 * This field stores
	 */
	private String m_category = null;

	public WordnetSynset(long synsetID, Vector<String> words, String category) {
		m_synsetID = synsetID;
		m_words = words;
		m_category = category;
	}

	public void setSynsetID(long synsetID) {
		m_synsetID = synsetID;
	}

	public long getSynsetID() {
		return m_synsetID;
	}

	public void setWords(Vector<String> words) {
		m_words = words;
	}

	public Vector<String> getWords() {
		return m_words;
	}

	public void setCategory(String category) {
		m_category = category;
	}

	private String getCategory() {
		return m_category;
	}
	public String toString() {
		return m_synsetID+":"+m_words+":"+m_category;
	}

}
