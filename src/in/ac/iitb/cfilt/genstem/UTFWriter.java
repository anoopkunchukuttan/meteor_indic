package in.ac.iitb.cfilt.genstem;

/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : UTFWriter.java
 *
 * Created On: 02-Jun-07
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Class	: UTFWriter
 * Purpose	: This class handles all file output operations.
 *            
 */
public class UTFWriter {
	
	/**
	 * This field stores a handle to the output file
	 */
	private File objFile = null;
	
	/**
	 * This field stores a boolean value to indicate whether
	 * to open the file in append mode or not.
	 */
	private boolean bAppend = false;

	/**
	 * This field stores a handle to a BufferedWriter
	 */
	private BufferedWriter objBufWriter = null;

	/**
	 * This field stores the file encoding like UTF8, UTF16 etc
	 */
	private String encoding = "UTF8";

	/**
	 * Constructor
	 * @param objFile
	 */
	public UTFWriter(File objFile) {
		this.objFile = objFile;
	}

	/**
	 * Constructor
	 * @param strFileName
	 */
	public UTFWriter(String strFileName) {
		new File(new File(strFileName).getParent()).mkdirs();
		this.objFile = new File(strFileName);
	}

	/**
	 * Constructor
	 * @param strFileName
	 * @param bAppend
	 */
	public UTFWriter(String strFileName, boolean bAppend) {
		new File(new File(strFileName).getParent()).mkdirs();
		this.objFile = new File(strFileName);
		this.bAppend = bAppend;
	}

	/**
	 * Constructor
	 * @param objFile
	 * @param bAppend
	 */
	public UTFWriter(File objFile, boolean bAppend) {
		this.objFile = objFile;
		this.bAppend = bAppend;
	}

	/**
	 * Constructor
	 * @param objFile
	 * @param encoding 
	 */
	public UTFWriter(File objFile, String encoding) {
		this.objFile = objFile;
		this.encoding = encoding;
	}

	/**
	 * Constructor
	 * @param strFileName
	 * @param encoding 
	 */
	public UTFWriter(String strFileName, String encoding) {
		this.objFile = new File(strFileName);
		this.encoding = encoding;
	}

	/**
	 * Constructor
	 * @param strFileName
	 * @param bAppend
	 * @param encoding 
	 */
	public UTFWriter(String strFileName, boolean bAppend, String encoding) {
		this.objFile = new File(strFileName);
		this.bAppend = bAppend;
		this.encoding = encoding;
	}

	/**
	 * Constructor
	 * @param objFile
	 * @param bAppend
	 * @param encoding 
	 */
	public UTFWriter(File objFile, boolean bAppend, String encoding) {
		this.objFile = objFile;
		this.bAppend = bAppend;
		this.encoding = encoding;
	}

	
    /**
     * Method 	: open
     * Purpose	: Opens a handle to the file .
     * @throws IOException 
     */
	public void open() throws IOException {
		this.objBufWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(objFile, bAppend), encoding), 4096);

	}

    /**
     * Method 	: write
     * Purpose	: Writes a String to the file .
	 * @param strLine
     * @throws IOException 
	 */
	public void write (String strLine) throws IOException {
		if(this.objBufWriter != null) {
			this.objBufWriter.write(strLine);
		}

	}

    /**
     * Method 	: write
     * Purpose	: Writes a String to the file and appends a new line character.
	 * @param strLine
     * @throws IOException 
	 */
	public void writeln (String strLine) throws IOException {
		if(this.objBufWriter != null) {
			this.objBufWriter.write(strLine + "\n");
		}

	}

    /**
     * Method 	: write
     * Purpose	: Writes an array of bytes to the file .
	 * @param bytes
     * @throws IOException 
	 */
	public void write (byte[] bytes) throws IOException {
		if(this.objBufWriter != null) {
			this.objBufWriter.write(new String(bytes));
		}		
	}

	/**
     * Method 	: flush
     * Purpose	: Writes an array of bytes to the file .
	 * @throws IOException 
	 * 
	 */
	public void flush () throws IOException {
		if(this.objBufWriter != null) {
			this.objBufWriter.flush();
		}		
	}

    /**
     * Method 	: close
     * Purpose	: Closes the handle to the file .
     * @throws IOException
     */
	public void close() throws IOException {
		if(this.objBufWriter != null) {
			this.objBufWriter.close();
		}
	}

	/**
	 * Method 	: getBufWriter
	 * Purpose	: Returns the objBufWriter.
	 * @return BufferedWriter
	 */
	public BufferedWriter getBufWriter() {
		return objBufWriter;
	}

	/**
	 * Method 	: setBufWriter
	 * Purpose	: Sets the value of objBufWriter.
	 * @param bufWriter
	 */
	public void setBufWriter(BufferedWriter bufWriter) {
		objBufWriter = bufWriter;
	}
}
