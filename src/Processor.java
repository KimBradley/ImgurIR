/**
 * @file Processor.java
 * @author Kim Bradley
 */


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.ArrayList;

import Processing.DocVector;
import Processing.Stemmer;
import Processing.TokenizedDoc;

/**
 * This class implements data processing through information retrieval
 * techniques. It includes operators for tokenizing, word stemming, and word
 * frequency calculations.
 */
public class Processor {

	/**
	 * Construct a processor
	 */
	public Processor() {
	}

	/**
	 * Tokenize the words in the given string, removing non letter and number
	 * characters, and storing them in a vector.
	 * 
	 * @param rawStr the raw string to tokenize
	 * @return the vector of tokenized words
	 */
	public Vector tokenize(String rawStr) {
		System.out.println("--TOKENIZE--");
		rawStr = rawStr.replaceAll("[^\\w\\s]", " ");
		Vector wordVector = new TokenizedDoc(rawStr, "",
				"src/Processing/stopwords.txt").getTokens();
		// testing purposes
		for (int i = 0; i < wordVector.size(); i++)
			System.out.println(wordVector.get(i));
		return wordVector;
	}

	/**
	 * Stem the words in the given string, removing non letter and number
	 * characters.
	 * 
	 * @param rawStr the raw string to stem
	 * @return the stemmed string
	 */
	public String stem(String rawStr) {
		System.out.println("--STEM--");
		rawStr = rawStr.replaceAll("[^\\w\\s]", " ");
		char[] w = new char[501];
		String stemmed = "";
		Stemmer s = new Stemmer();
		int i = 0;
		while (i < rawStr.length() - 2) {
			int ch = rawStr.charAt(i);
			i++;
			if (Character.isLetter((char) ch)) {
				int j = 0;
				while (true) {
					ch = Character.toLowerCase((char) ch);
					w[j] = (char) ch;
					if (j < 500)
						j++;
					ch = rawStr.charAt(i);
					i++;
					if (!Character.isLetter((char) ch)) {
						for (int c = 0; c < j; c++)
							s.add(w[c]);
						s.stem();
						stemmed += (s.toString() + " ");
						break;
					}
				}
			}
			if (ch < 0)
				break;
		}
		// testing purposes
		System.out.println(stemmed);
		return stemmed;
	}

	/**
	 * Calculate the frequencies and normalized frequencies of the words in the
	 * given string.
	 * 
	 * @param rawStr the raw string to calculate
	 * @return the vector containing the stemmed words, their frequencies, and
	 *         their normalized frequencies
	 */
	public Vector frequencies(String rawStr) {
		System.out.println("--FREQUENCIES--");
		Vector wordVector = this.tokenize(rawStr);
		DocVector documentVector = new DocVector(wordVector, "");
		Vector cdv = documentVector.getVector();
		// testing purposes
		for (int i = 0; i < cdv.size(); i++)
			System.out.println(cdv.get(i));
		return cdv;
	}

	/**
	 * Main method for testing above processing methods.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Processor proc = new Processor();
		proc.tokenize("the quick brown-fox jumped over the lazy dog.");
		proc.stem("the quick brown-fox jumped over the lazy dog.");
		proc.frequencies("the quick brown-fox jumped jump jumping over the lazy dog.");
	}

}
