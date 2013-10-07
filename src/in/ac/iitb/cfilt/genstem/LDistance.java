package in.ac.iitb.cfilt.genstem;


public class LDistance {

  //****************************
  // Get minimum of three values
  //****************************

  private static int Minimum (int a, int b, int c) {
  int mi;

    mi = a;
    if (b < mi) {
      mi = b;
    }
    if (c < mi) {
      mi = c;
    }
    return mi;

  }

  //*****************************
  // Compute Levenshtein distance
  //*****************************

  public static int LD (String s, String t) {
  int d[][]; // matrix
  int n; // length of s
  int m; // length of t
  int i; // iterates through s
  int j; // iterates through t
  char s_i; // ith character of s
  char t_j; // jth character of t
  int cost; // cost

    // Step 1

    n = s.length ();
    m = t.length ();
    if (n == 0) {
      return m;
    }
    if (m == 0) {
      return n;
    }
    d = new int[n+1][m+1];

    // Step 2

    for (i = 0; i <= n; i++) {
      d[i][0] = i;
    }

    for (j = 0; j <= m; j++) {
      d[0][j] = j;
    }

    // Step 3

    for (i = 1; i <= n; i++) {

      s_i = s.charAt (i - 1);

      // Step 4

      for (j = 1; j <= m; j++) {

        t_j = t.charAt (j - 1);

        // Step 5

        if (s_i == t_j) {
          cost = 0;
        }
        else {
          cost = 1;
        }

        // Step 6

        d[i][j] = Minimum (d[i-1][j]+1, d[i][j-1]+1, d[i-1][j-1] + cost);

      }

    }

    // Step 7

    return d[n][m];

  }
  public static int computeLevenshteinDistance(CharSequence str1,

          CharSequence str2) {
  int[][] distance = new int[str1.length() + 1][str2.length() + 1];

  for (int i = 0; i <= str1.length(); i++)
          distance[i][0] = i;
  for (int j = 1; j <= str2.length(); j++)
          distance[0][j] = j;

//  for (int i = 1; i <= str1.length(); i++)
//          for (int j = 1; j <= str2.length(); j++)
//                  distance[i][j] = Minimum(
//                                  distance[i - 1][j] + 1,
//                                  distance[i][j - 1] + 1,
//                                  distance[i - 1][j - 1]
//                                                  + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
//                                                                  : 1));
  for (int i = 1; i <= str1.length(); i++)
      for (int j = 1; j <= str2.length(); j++)
              distance[i][j] = Minimum(
                              distance[i - 1][j] + 1,
                              distance[i][j - 1] + 1,
                              distance[i - 1][j - 1]
                                              + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
                                                              : Math.abs((int)str1.charAt(i - 1)-(int)str2.charAt(j - 1))));
  

  return distance[str1.length()][str2.length()];
}
  public static int longestSubstr(String first, String second) {
	    if (first == null || second == null || first.length() == 0 || second.length() == 0) {
	        return 0;
	    }

	    int maxLen = 0;
	    int fl = first.length();
	    int sl = second.length();
	    int[][] table = new int[fl][sl];

	    for (int i = 0; i < fl; i++) {
	        for (int j = 0; j < sl; j++) {
	            if (first.charAt(i) == second.charAt(j)) {
	                if (i == 0 || j == 0) {
	                    table[i][j] = 1;
	                }
	                else {
	                    table[i][j] = table[i - 1][j - 1] + 1;
	                }
	                if (table[i][j] > maxLen) {
	                    maxLen = table[i][j];
	                }
	            }
	        }
	    }
	    return maxLen;
	}
  public static int stackDistance(String s, String t)
  {
	  int lcs = longestSubstr(s,t);
	  int delPenalty = Math.abs(s.length()-lcs);
	  int insPenalty = Math.abs(t.length()-lcs);
	  //System.out.println(delPenalty+insPenalty);
	  return (delPenalty+insPenalty);
  }
 public static void main(String args[]){
	  String str1 = "लड़कियाँ";
	  //String str2 = "लड़की";
	  String str2 = "लड़की";
	  System.out.println(LD(str1, str2));
	  System.out.println(stackDistance(str1,str2));
  }
}
