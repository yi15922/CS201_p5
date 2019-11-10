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
        if (k < 0) {
            throw new IllegalArgumentException("Illegal value of k:"+k);
        }

        List<Term> ret;

        if (prefix.equals("")) {
            ret = Arrays.asList(myTerms);
            Collections.sort(ret, Comparator.comparing(Term::getWeight).reversed());

        } else if (myMap.containsKey(prefix)) {
            ret = myMap.get(prefix);
        } else {
            return new ArrayList<>();
        }
        return ret.subList(0, Math.min(k, ret.size()));
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
                if (t.equals("")) {
                    myMap.putIfAbsent("", new ArrayList<>());
                }
                if (t.length() >= i) {
                    myMap.putIfAbsent(t.substring(0, i), new ArrayList<>());
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
                continue;
            }

            List<Term> temp = new ArrayList<>(Arrays.asList(myTerms).subList(first, last + 1));
            Collections.sort(temp, Comparator.comparing(Term::getWeight).reversed());

            myMap.get(prefix).addAll(temp);

            System.out.println(prefix + myMap.get(prefix).toString());


        }

    }

    @Override
    public int sizeInBytes() {
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
                System.out.println("Adding all strings in set: " + t.toString() + " " + t.getWord().length());
                mySize += BYTES_PER_CHAR * t.getWord().length();
                //System.out.println(mySize);

            }



        }
        //System.out.println(mySize);
        return mySize;
    }


    public static void main(String[] args){
        HashListAutocomplete test = new HashListAutocomplete(new String[]{"a", "b", "c", "d"}, new double[]{2, 1, 4, 3});
        System.out.println(test.topMatches("", 10));
        System.out.println(test.sizeInBytes());
    }
}
