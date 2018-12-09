package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day2-input.file"));

		part1(input);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");

	}

	public static void part1(List<String> lines) {
		int countDoubleLetter = 0;
		int countTripleLetter = 0;
		for (String line : lines) {
			Map<String, Integer> map = new HashMap<>();
			for (int i = 0; i < line.length(); i++) {
				if (null == map.get(line.charAt(i) + "")) {
					map.put(line.charAt(i) + "", new Integer(1));
				} else {
					Integer count = map.get(line.charAt(i) + "");
					map.put(line.charAt(i) + "", count + 1);
				}
			}

			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				if (entry.getValue() == 2) {
					countDoubleLetter++;
					break;
				}
			}
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				if (entry.getValue() == 3) {
					countTripleLetter++;
					break;
				}
			}

		}
		int result1 = countDoubleLetter * countTripleLetter;
		System.out.println(result1);
		Map<String, Integer> map2 = new HashMap<>();

		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.size(); j++) {
				map2.put(i + "!" + j, getLevenshteinDistance(lines.get(i), lines.get(j)));
			}
		}

		int index1 = 0;
		int index2 = 0;
		for (Map.Entry<String, Integer> entry : map2.entrySet()) {
			if (entry.getValue() == 1) {
				index1 = Integer.valueOf(entry.getKey().split("!")[0]);
				index2 = Integer.valueOf(entry.getKey().split("!")[1]);
			}
		}
		for (int i = 0; i < lines.get(index1).length(); i++) {
			if (lines.get(index1).charAt(i) == lines.get(index2).charAt(i)) {
				System.out.print(lines.get(index1).charAt(i));
			}
		}
		System.out.println();
	}

	// copy from the Net, since I don't want to import StringUtils from
	// org.apache.commons.lang3 (not in my classpath)

	public static int getLevenshteinDistance(String s, String t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}

		/*
		 * The difference between this impl. and the previous is that, rather than
		 * creating and retaining a matrix of size s.length()+1 by t.length()+1, we
		 * maintain two single-dimensional arrays of length s.length()+1. The first, d,
		 * is the 'current working' distance array that maintains the newest distance
		 * cost counts as we iterate through the characters of String s. Each time we
		 * increment the index of String t we are comparing, d is copied to p, the
		 * second int[]. Doing so allows us to retain the previous cost counts as
		 * required by the algorithm (taking the minimum of the cost count to the left,
		 * up one, and diagonally up and to the left of the current cost count being
		 * calculated). (Note that the arrays aren't really copied anymore, just
		 * switched...this is clearly much better than cloning an array or doing a
		 * System.arraycopy() each time through the outer loop.)
		 * 
		 * Effectively, the difference between the two implementations is this one does
		 * not cause an out of memory condition when calculating the LD over two very
		 * large strings.
		 */

		int n = s.length(); // length of s
		int m = t.length(); // length of t

		if (n == 0) {
			return m;
		} else if (m == 0) {
			return n;
		}

		if (n > m) {
			// swap the input strings to consume less memory
			String tmp = s;
			s = t;
			t = tmp;
			n = m;
			m = t.length();
		}

		int p[] = new int[n + 1]; // 'previous' cost array, horizontally
		int d[] = new int[n + 1]; // cost array, horizontally
		int _d[]; // placeholder to assist in swapping p and d

		// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t

		char t_j; // jth character of t

		int cost; // cost

		for (i = 0; i <= n; i++) {
			p[i] = i;
		}

		for (j = 1; j <= m; j++) {
			t_j = t.charAt(j - 1);
			d[0] = j;

			for (i = 1; i <= n; i++) {
				cost = s.charAt(i - 1) == t_j ? 0 : 1;
				// minimum of cell to the left+1, to the top+1, diagonally left and up +cost
				d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
			}

			// copy current distance counts to 'previous row' distance counts
			_d = p;
			p = d;
			d = _d;
		}

		// our last action in the above loop was to switch d and p, so p now
		// actually has the most recent cost counts
		return p[n];
	}
}
