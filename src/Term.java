
/*************************************************************************
 * @author Kevin Wayne
 *
 * Description: A term and its weight.
 * 
 * @author Owen Astrachan, revised for Java 8-11, toString added
 * 
 *************************************************************************/

import java.util.Comparator;

public class Term implements Comparable<Term> {

	private String myWord;
	private double myWeight;

	/**
	 * The constructor for the Term class. Should set the values of word and
	 * weight to the inputs, and throw the exceptions listed below
	 * 
	 * @param word
	 *            The word this term consists of
	 * @param weight
	 *            The weight of this word in the Autocomplete algorithm
	 * @throws NullPointerException
	 *             if word is null
	 * @throws IllegalArgumentException
	 *             if weight is negative
	 */
	public Term(String word, double weight) {
		if (word == null) {
			throw new NullPointerException("Null word");
		}

		if (weight < 0) {
			throw new IllegalArgumentException("Negative weight " + weight);
		}

		myWord = word;
		myWeight = weight;
	}
	
	/**
	 * Default compare is by word, no weight involved
	 * @return this.getWord().compareTo(that.getWord())
	 */
	@Override
	public int compareTo(Term that) {
		return myWord.compareTo(that.myWord);
	}

	/**
	 * Getter for Term's word
	 * @return this Term's word
	 */
	public String getWord() {
		return myWord;
	}

	/**
	 * Getter for Term's weight
	 * @return this Term's weight
	 */
	public double getWeight() {
		return myWeight;
	}

	/**
	 * @return (word,weight) for this Term
	 */
	@Override
	public String toString() {
		return String.format("(%2.1f,%s)", myWeight, myWord);
	}
	
	/**
	 * Use default compareTo, so only word for equality, not weight
	 * @return true if this.getWord().equal(o.getWord())
	 */
	@Override
	public boolean equals(Object o) {
		Term other = (Term) o;
		return this.compareTo(other) == 0;
	}

	/**
	 * A Comparator for comparing Terms using a set number of the letters they
	 * start with. 
	 * This Comparator required for assignment.
	 *
	 */
	public static class PrefixOrder implements Comparator<Term> {
		private final int myPrefixSize;

		public PrefixOrder(int r) {
			this.myPrefixSize = r;
		}

		/**
		 * Compares v and w lexicographically using only their first r letters.
		 * If the first r letters are the same, then v and w should be
		 * considered equal. This method should take O(r) to run, and be
		 * independent of the length of v and w's length. You can access the
		 * Strings to compare using v.word and w.word.
		 * 
		 * @param v/w
		 *            - Two Terms whose words are being compared
		 */
		public int compare(Term v, Term w) {
			int numChars = 0;

			// Sets numChars to be the least number of characters that need to be compared.

			if (v.getWord().length() < this.myPrefixSize || w.getWord().length() < this.myPrefixSize) {
				numChars = Math.min(v.getWord().length(), w.getWord().length());
			} else {
				numChars = this.myPrefixSize;
			}


			// Compare the first numChars characters and return if difference is found
			for (int i = 0; i < numChars; i++) {
				if (v.getWord().charAt(i) > w.getWord().charAt(i)) {
					return 1;
				} else if (v.getWord().charAt(i) < w.getWord().charAt(i)) {
					return -1;
				}
			}

			// The below only runs if the first numChars characters are all equal


			// If the first numChar characters are equal and numChar is equal to myPrefixSize
			// return 0 because all compared chars are equal.
			if (numChars == this.myPrefixSize) {
				return 0;
			}

			// If the first numChar characters are equal and more than numChar characters are
			// asked to be compared, the longer word is greater
			if (numChars < this.myPrefixSize) {
				if (v.getWord().length() > w.getWord().length()) {
					return 1;
				} else if (v.getWord().length() < w.getWord().length()){
					return -1;
				}
			}

			return 0;
		}
	
	}
}
