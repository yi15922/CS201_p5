import java.util.*;



// This is a temporary new branch to test another approach




/**
 * Implements Autocompleter and uses a Map to store all possibe search results
 * Slow to initialize, but very fast a returning search results. Takes up more memory.
 * @author Yi Chen
 */
public class HashListAutocomplete implements Autocompletor{
    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap = new HashMap<>();
    private int mySize;


    /**
     * Given arrays of words and weights, initialize myTerms to a HashMap
     * containing all possible prefixes as keys and all possible search results
     * as values.
     *
     * @param terms
     *            - A list of words to form terms from
     * @param weights
     *            - A corresponding list of weights, such that terms[i] has
     *            weight[i].
     * @return a BinarySearchAutocomplete with a map of all possible search results.
     * @throws a NullPointerException if either argument passed in is null
     */
    public HashListAutocomplete(String[] terms, double[] weights){
        if (terms == null || weights == null) {
            throw new NullPointerException("One or more arguments null");
        }

        initialize(terms, weights);
    }


    /**
     * Gets and returns the top matches of a given prefix
     * @param prefix a String specifying the prefix to search for
     * @param k int number of top matches to return
     * @return a List of top matches sorted in descending order by weight
     */
    @Override
    public List<Term> topMatches(String prefix, int k) {
        if (k < 0) {
            throw new IllegalArgumentException("Illegal value of k:"+k);
        }

        List<Term> ret;

        if (myMap.containsKey(prefix)) {
            ret = myMap.get(prefix);
        } else {
            return new ArrayList<>();
        }
        return ret.subList(0, Math.min(k, ret.size()));
    }


    /**
     * Initializes the object by populating the map with search results.
     *
     * @param terms is array of Strings for words in each Term
     * @param weights is corresponding weight for word in terms
     */
    @Override
    public void initialize(String[] terms, double[] weights) {
        //System.out.println(Arrays.asList(terms).toString());

        for (int i = 0; i < terms.length; i++) {
            for (int j = 0; j <= MAX_PREFIX; j++) {

                if (terms[i].length() >= j) {
                    myMap.putIfAbsent(terms[i].substring(0, j), new ArrayList<>());
                    myMap.get(terms[i].substring(0, j)).add(new Term(terms[i], weights[i]));
                    Collections.sort(myMap.get(terms[i].substring(0, j)), Comparator.comparing(Term::getWeight).reversed());
                }
            }
        }
        //System.out.println(myMap.toString());


        //System.out.println(Arrays.asList(myTerms).toString());

    }

    /**
     * Calculates the size of this object
     * @return an int showing the size of this object in bytes.
     */
    @Override
    public int sizeInBytes() {
        if (myMap.size() == 1) {
            return BYTES_PER_DOUBLE * myMap.size();
        }
        if (mySize == 0) {
            Set<Term> theSet = new HashSet<>();
            for(String t : myMap.keySet()) {
                //System.out.println("Adding all lengths of keys: " + t.length());
                mySize += BYTES_PER_CHAR * t.length();
                theSet.addAll(myMap.get(t));
                //System.out.println(mySize);

            }
            mySize += BYTES_PER_DOUBLE * theSet.size();
            //System.out.println("Adding all doubles in the set: " + theSet.size());
            //System.out.println(mySize);

            for (Term t : theSet) {
                //System.out.println("Adding all strings in set: " + t.toString() + " " + t.getWord().length());
                mySize += BYTES_PER_CHAR * t.getWord().length();
                //System.out.println(mySize);

            }



        }
        //System.out.println(mySize);
        return mySize;
    }

    public static void main(String[] args){
        HashListAutocomplete test = new HashListAutocomplete(new String[]{"ape", "app", "ban", "bat", "bee", "car", "cat", ""}, new double[]{6, 4, 2, 3, 5, 7, 1, 10});
        //System.out.println(test.topMatches("", 10));
        //System.out.println(test.sizeInBytes());
    }
}
