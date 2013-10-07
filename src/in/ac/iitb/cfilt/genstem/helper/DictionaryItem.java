package in.ac.iitb.cfilt.genstem.helper;

/**
 * <p>Class	: DictionaryItem
 * <p>Purpose	: This class is 
 */
public class DictionaryItem {
	private String m_gloss = null;
	private String m_gender = null;

	/**
	 * Constructor
	 * <p>
	 * @param gloss
	 * @param gender
	 */
	public DictionaryItem(String gloss, String gender) {
		this.setGloss(gloss);
		this.setGender(gender);
	}

	/**
	 * <p>Method 	: setGloss
	 * <p>Purpose	: 
	 * <p>@param gloss void
	 */
	public void setGloss(String gloss) {
		m_gloss = gloss;
	}

	/**
	 * <p>Method 	: getGloss
	 * <p>Purpose	: 
	 * <p>@return String
	 */
	public String getGloss() {
		return m_gloss;
	}

	/**
	 * <p>Method 	: setGender
	 * <p>Purpose	: 
	 * <p>@param gender void
	 */
	public void setGender(String gender) {
		m_gender = gender;
	}

	/**
	 * <p>Method 	: getGender
	 * <p>Purpose	: 
	 * <p>@return String
	 */
	public String getGender() {
		return m_gender;
	}
	
	public String toString() {
		return m_gloss+":"+m_gender;
	}

}
