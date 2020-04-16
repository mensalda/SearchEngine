package search;

import java.util.*;

class SearchContext {
    private SearchMethod searchMethod;

    public void setStrategy(SearchMethod searchMethod) {
        this.searchMethod = searchMethod;
    }
    public void findAndPrint(String query) {
        this.searchMethod.printResult(query);
    }
}

abstract class SearchMethod {
    SearchEngine searchEngine;
    String[] data;
    Map<String, List<Integer>> inverted;

    public SearchMethod(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
        this.data = searchEngine.getData();
        this.inverted = searchEngine.getInvertedIndex();
    }

    abstract void printResult(String query);
}

class SearchALL extends SearchMethod {

    public SearchALL(SearchEngine searchEngine) {
        super(searchEngine);
    }

    @Override
    public void printResult(String query) {
        String[] split = query.split("\\s+");
        Set<Integer> index = new TreeSet<Integer>();
        for (int i = 0; i < split.length; i++) {
            if (inverted.containsKey(split[i].toLowerCase())) {
                if (i == 0)
                    index.addAll(inverted.get(split[i].toLowerCase()));
                else
                    index.retainAll(inverted.get(split[i].toLowerCase()));
            }
        }
        for (Integer item : index) {
            System.out.println(data[item]);
        }
    }
}
class SearchANY extends SearchMethod {

    public SearchANY(SearchEngine searchEngine) {
        super(searchEngine);
    }

    @Override
    public void printResult(String query) {
        String[] split = query.split("\\s+");
        Set<Integer> index = new TreeSet<Integer>();
        for (int i = 0; i < split.length; i++) {
            if (inverted.containsKey(split[i].toLowerCase())) {
                index.addAll(inverted.get(split[i].toLowerCase()));
            }
        }
        for (Integer item : index) {
            System.out.println(data[item]);
        }
    }
}
class SearchNONE extends SearchMethod {

    public SearchNONE(SearchEngine searchEngine) {
        super(searchEngine);
    }

    @Override
    public void printResult(String query) {
        String[] split = query.split("\\s+");
        Set<Integer> index = new TreeSet<Integer>();
        for (int i = 0; i < data.length; i++) {
            index.add(i);
        }
        for (int i = 0; i < split.length; i++) {
            if (inverted.containsKey(split[i].toLowerCase())) {
                index.removeAll(inverted.get(split[i].toLowerCase()));
            }
        }
        for (Integer item : index) {
            System.out.println(data[item]);
        }
    }
}
