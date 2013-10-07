package in.ac.iitb.cfilt.genstem;

import java.io.IOException;
import java.util.HashMap;
import java.io.*;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.tools.JavaCompiler;

public class evaluator {
	
	public static void main(String args[]) throws IOException {
		//BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		String line_sa=null;
		String tok=null;
		String tok1=null;
		String lexeme[]=null;
		String fields[]=null;
		String fields_sa[]=null;
		String token[]=null;
		String token_sa[]=null;
		String temp;
		
		File dir_lang_rooted = new File("/home/lavita/workspace/lavita/rooted/");
		String[] filesass = dir_lang_rooted.list();
		for (String file : filesass) {
		System.out.println("Reading  file " + file);
		UTFReader corpus = new UTFReader("/home/lavita/workspace/lavita/rooted/" + file);//open hindi tagged corpus
		corpus.open();
		File dir_lang_rooted_sa = new File("/home/lavita/workspace/lavita/modified/");	
		String[] filesa = dir_lang_rooted_sa.list();
		UTFWriter corpus1 = new UTFWriter("/home/lavita/workspace/lavita/modified/" + file);
		corpus1.open();
		while ((line = corpus.readLine()) != null) {
			if(!line.contains("ctx_")){
			
			fields=line.split(" ");
			
			for (int i = 0; i < fields.length; i++) {
			    
				token=fields[i].split("#");
				
			    	  
			    	  
				//System.out.println(token[0]+" "+token[1]);
				
				
				corpus1.write(token[0]+" "+token[1]+"\n");
			   }
			}
				
		}
				
		
		corpus1.close();
		corpus.close();
		
	}
		
		
		
	}
	
	}

	




