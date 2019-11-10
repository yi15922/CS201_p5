import java.util.*;


/**
 * Extends BruteAutocomplete but does not use a PriorityQueue or binary search
 * to find the top matches.
 * Therefore runtime is slower because all possible matches need to be
 * sorted rather than only the specified number of top matches.
 * @author Yi Chen
 */
public class SlowBruteAutocomplete extends BruteAutocomplete{

    /**
     * Create immutable instance with terms constructed from parameter
     *
     * @param terms   words such that terms[k] is part of a word pair 0 <= k < terms.length
     * @param weights weights such that weights[k] corresponds to terms[k]
     * @throws NullPointerException     if either parameter is null
     * @throws IllegalArgumentException if terms.length != weights.length
     * @throws IllegalArgumentException if any elements of weights is negative
     * @throws IllegalArgumentException if any elements of terms is duplicate
     */
    public SlowBruteAutocomplete(String[] terms, double[] weights) {

        super(terms, weights);
    }


    /**
     * Overrides topMatches so that no PriorityQueue or binary search is used.
     * Slower implementation than the original.
     * @param prefix String prefix search query
     * @param k int number of top matches to return
     * @return a List of top matches sorted in descending order by weight
     */
    @Override
    public List<Term> topMatches (String prefix, int k) {


        List<Term> list = new ArrayList<>();
        for (Term t : myTerms) {
            if (t.getWord().startsWith(prefix)) {
                list.add(t);
            }
        }
        if (prefix.equals("")) {
            list = Arrays.asList(myTerms);
        }
        Collections.sort(list, Comparator.comparing(Term::getWeight).reversed());
        return list.subList(0, Math.min(k, list.size()));
    }
}
