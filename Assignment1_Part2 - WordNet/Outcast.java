public class Outcast {
    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDistance = 0;
        String outcast = null;
        for (String nounA : nouns) {
            int distance = 0;
            for (String nounB : nouns)
                if (!nounA.equals(nounB))
                    distance += wordNet.distance(nounA, nounB);

            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = nounA;
            }
        }
        
        return outcast;
    }
}