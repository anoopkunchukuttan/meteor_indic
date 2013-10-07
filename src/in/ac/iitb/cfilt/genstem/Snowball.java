package in.ac.iitb.cfilt.genstem;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Snowball {

	public static void main(String args[]) throws Exception {
		FileWriter fstream = new FileWriter(
				"/home/lavita/workspace/Trie/snowball_health_edited");
		BufferedWriter out = new BufferedWriter(fstream);
		String line = null;
		String line1 = null;
		String line2 = null;
		String wordnet_word = null;
		String corpus_word = null;

		String field[] = null;

		UTFReader lexicon = new UTFReader(
				"/home/lavita/workspace/Trie/english results/snowball_health");
		;
		lexicon.open();
		while ((line = lexicon.readLine()) != null) {
			// Traverses Corpus
			field = line.split(" -> ");
			corpus_word = field[0];
			if(!corpus_word.contains("_")&& !corpus_word.contains("-")){
			System.out.println(corpus_word);
			System.out.println(corpus_word);
			out.write(corpus_word);
			out.write("\n");
		}
		}
		out.close();
		lexicon.close();
	}
}
