import java.util.*;


public class HashListAutocomplete implements Autocompletor{
    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap = new HashMap<>();
    private int mySize;
    private Term[] myTerms;

    public HashListAutocomplete(String[] terms, double[] weights){
        if (terms == null || weights == null) {
            throw new NullPointerException("One or more arguments null");
        }

        initialize(terms, weights);
    }



    @Override
    public List<Term> topMatches(String prefix, int k) {
        return null;
    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        myTerms = new Term[terms.length];

        for (int i = 0; i < terms.length; i++) {
            myTerms[i] = new Term(terms[i], weights[i]);
        }

        Arrays.sort(myTerms);

        for (int i = 1; i <= MAX_PREFIX; i++) {
            for (String t : terms) {
                if (t.length() >= i) {

                    myMap.putIfAbsent(t.substring(0, i), new ArrayList<>());

                } else {
                    continue;
                }
            }
        }
        for (String prefix : myMap.keySet()) {
            Term dummy = new Term(prefix, 0);
            Term.PrefixOrder comp = new Term.PrefixOrder(prefix.length());
            for (Term t : myTerms) {
                if (comp.compare(dummy, t) == 0) {
                    myMap.get(prefix).add(t);

                }
            }
            System.out.println(prefix + myMap.get(prefix));
        }

    }

    @Override
    public int sizeInBytes() {
        return 0;
    }


    public static void main(String[] args){
        HashListAutocomplete test = new HashListAutocomplete(new String[]{ "ape", "app", "ban", "bat", "bee", "car", "cat" }, new double[]{6, 4, 2, 3, 5, 7, 1});
    }
}
