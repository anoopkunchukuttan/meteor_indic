package in.ac.iitb.cfilt.genstem;

import java.io.File;
import java.io.IOException;

public class golddata {

	public static void main(String args[]) throws IOException {
		// BufferedReader stdin = new BufferedReader(new
		// InputStreamReader(System.in));
		String line = null;
		String line_sa = null;
		String inflected_word = null;
		String word2 = null;
		String field[] = null;
		String word[] = null;
		String word1[] = null;
		String token[] = null;

		File dir_lang_rooted = new File("/home/lavita/workspace/gold data/");
		String[] filesass = dir_lang_rooted.list();
		for (String file : filesass) {
			System.out.println("Reading  file " + file);
			UTFReader corpus = new UTFReader(
					"/home/lavita/workspace/gold data/" + file);// open
																// hindi
																// tagged
																// corpus
			corpus.open();
			File dir_lang_rooted_sa = new File(
					"/home/lavita/workspace/gold data_modified/");
			String[] filesa = dir_lang_rooted_sa.list();
			UTFWriter corpus1 = new UTFWriter(
					"/home/lavita/workspace/gold data_modified/" + file);
			corpus1.open();
			while ((line = corpus.readLine()) != null) {
				if (line.contains("\t")) {
					field = line.split("	");
					inflected_word = field[0];
					// System.out.println(inflected_word);
					// word = field[1].split("1");
					if (field[1].contains("LAVITA ")) {
						word = field[1].split("LAVITA ");
						// System.out.println(word[0]);
						if (word[0].contains(" ")) {
							word1 = word[0].split(" ");
							// System.out.println(word1[1]);
							String str = "";
							if (word1.length > 1) {
								for (int i = 0; i < word1.length; i++) {

									token = word1[i].split("_");
									// System.out.println(word1[i]);
									str = str + token[0];
									// System.out.println(token[0]);

									// corpus1.write(token[0] + " " + token[1] +
									// "\n");
								}
							} else {
								token = word[0].split("_");
								str = str + token[0];
							}
							System.out.println(inflected_word + " " + str);
							corpus1.write(inflected_word + " " + str + "\n");
						}
					}
				}
			}
			corpus1.close();
			corpus.close();

		}

	}

}
