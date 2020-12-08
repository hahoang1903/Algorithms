import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;

public class WordNet {
    private final ST<Integer, String> idsTable;
    private final ST<String, SET<Integer>> nounsTable;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        idsTable = new ST<>();
        nounsTable = new ST<>();

        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] separatedArgs = in.readLine().split(",");
            idsTable.put(Integer.valueOf(separatedArgs[0]), separatedArgs[1]);

            String[] nouns = separatedArgs[1].split(" ");
            for (String noun : nouns) {
                if (nounsTable.contains(noun))
                    nounsTable.get(noun).add(Integer.valueOf(separatedArgs[0]));
                else {
                    SET<Integer> ids = new SET<>();
                    ids.add(Integer.valueOf(separatedArgs[0]));
                    nounsTable.put(noun, ids);
                }
            }
        }

        Digraph wordNet = new Digraph(idsTable.size());

        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] ids = in.readLine().split(",");
            int v = Integer.parseInt(ids[0]);
            for (int i = 1; i < ids.length; i++)
                wordNet.addEdge(v, Integer.parseInt(ids[i]));
        }

        sap = new SAP(wordNet);

        DirectedCycle directedCycle = new DirectedCycle(wordNet);
        if (directedCycle.hasCycle())
            throw new IllegalArgumentException();

        int counter = 0;
        for (int i = 0; i < wordNet.V(); i++) {
            DirectedDFS directedDFS = new DirectedDFS(wordNet, i);

            if (directedDFS.count() == 1) counter++;

            if (counter > 1) throw new IllegalArgumentException();
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsTable.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();

        return nounsTable.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        return sap.length(nounsTable.get(nounA), nounsTable.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        return idsTable.get(sap.ancestor(nounsTable.get(nounA), nounsTable.get(nounB)));
    }
}