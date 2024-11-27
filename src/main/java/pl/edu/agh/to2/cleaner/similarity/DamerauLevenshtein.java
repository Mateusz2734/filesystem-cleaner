package pl.edu.agh.to2.cleaner.similarity;

public class DamerauLevenshtein {
    static int distance(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();

        if (len1 == 0 || len2 == 0) {
            return Math.max(len1, len2);
        }

        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1, // Deletion
                                dp[i][j - 1] + 1 // Insertion
                        ),
                        dp[i - 1][j - 1] + cost // Substitution
                );

                if (i > 1 && j > 1 && str1.charAt(i - 1) == str2.charAt(j - 2) && str1.charAt(i - 2) == str2.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + cost); // Transposition
                }
            }
        }

        return dp[len1][len2];
    }

    public static double similarity(String str1, String str2) {
        if (str1.isEmpty() && str2.isEmpty()) {
            return 1.0;
        }

        return 1.0 - ((double) distance(str1, str2) / Math.max(str1.length(), str2.length()));
    }
}