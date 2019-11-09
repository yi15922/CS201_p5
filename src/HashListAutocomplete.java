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

        //System.out.println(Arrays.asList(myTerms).toString());
        for (String prefix : myMap.keySet()) {
            Term dummy = new Term(prefix, 0);
            //System.out.println(dummy.toString());
            Term.PrefixOrder comp = new Term.PrefixOrder(prefix.length());
            int first = BinarySearchAutocomplete.firstIndexOf(myTerms, dummy, comp);
            int last = BinarySearchAutocomplete.lastIndexOf(myTerms, dummy, comp);
            //System.out.println(first + " " + last);

            if (first == -1 || prefix.length() == 0) {               // prefix not found
                myMap.put(prefix, new ArrayList<>());
            }

            myMap.get(prefix).addAll(Arrays.asList(myTerms).subList(first, last + 1));

            //System.out.println(prefix + myMap.get(prefix).toString());


        }

    }

    @Override
    public int sizeInBytes() {
        return 0;
    }


    public static void main(String[] args){
        HashListAutocomplete test = new HashListAutocomplete(new String[]{ "ape", "apsadfp", "ban", "bat", "bee", "car", "cat" }, new double[]{6, 4, 2, 3, 5, 7, 1});
    }
}
