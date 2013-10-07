package in.ac.iitb.cfilt.genstem;

/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : Reader.java
 *
 * Created On: 02-Jun-07
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class	: Reader
 * Purpose	: This class handles all file input operations.
 *            
 */
public class UTFReader {
	
	/**
	 * This field stores a handle to the input file
	 */
	private File objFile = null;
	
	/**
	 * This field stores a handle to a BufferedWriter
	 */
	private BufferedReader objBufReader = null;

	private String encoding = "UTF8";

	/**
	 * Constructor
	 * @param objFile
	 */
	public UTFReader(File objFile) {
		this.objFile = objFile;
	}

	/**
	 * Constructor
	 * @param strFileName
	 */
	public UTFReader(String strFileName) {
		this.objFile = new File(strFileName);
	}

	/**
	 * Constructor
	 * @param strFileName
	 * @param encoding 
	 */
	public UTFReader(String strFileName, String encoding) {
		this.objFile = new File(strFileName);
		this.encoding  = encoding; 
	}

    /**
     * Method 	: open
     * Purpose	: Opens a handle to the file .
     * @throws IOException 
     */
	public void open() throws IOException {
		this.objBufReader = new BufferedReader(
			new InputStreamReader(new FileInputStream(objFile), encoding), 4096);
		
	}

    /**
     * Method 	: readLine
     * Purpose	: Reads a Line from the file .
     * @return 
     * @throws IOException 
	 * 
	 */
	public String readLine () throws IOException{
		if(this.objBufReader != null) {
			return this.objBufReader.readLine();
		}
		return null;		
	}

    /**
     * Method 	: close
     * Purpose	: Closes the handle to the file .
     */
	public void close() {
		try {
			if(this.objBufReader != null) {
				this.objBufReader.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			//closing error can be ignored
		}
	}
}
