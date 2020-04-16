package search;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            SearchEngine searchEngine = new SearchEngine();
        }
        else if (args[0].equals("--data")){
            SearchEngine searchEngine = new SearchEngine(args[1]);
        }
    }
}

class SearchEngine {
    private String[] data;
    private Scanner scanner = new Scanner(System.in);
    private boolean isWork = true;
    private Map<String, List<Integer>> invertedIndex = new HashMap<>();

    public String[] getData() {
        return data;
    }

    public Map<String, List<Integer>> getInvertedIndex() {
        return invertedIndex;
    }

    SearchEngine() {
        putData();
        putInverted();
        work();
    }

    SearchEngine(String fileName) {
        importData(fileName);
        putInverted();
        work();
    }

    private void putInverted() {
        for (int i = 0; i < data.length; i++) {
            String[] temp = data[i].split("\\s+");
            for (int j = 0; j < temp.length; j++) {
                List<Integer> temp2 = invertedIndex.get(temp[j].toLowerCase());
                if (temp2 != null) {
                    temp2.add(i);
                    invertedIndex.put(temp[j].toLowerCase(), temp2);
                } else {
                    temp2 = new ArrayList<>();
                    temp2.add(i);
                    invertedIndex.put(temp[j].toLowerCase(), temp2);
                }
            }
        }
    }

    private void importData(String fileName) {
        ArrayList<String> list = new ArrayList<>();
        File file = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                list.add(reader.readLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.data = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            this.data[i] = list.get(i);
        }
    }

    private void work() {
        while (isWork) {
            System.out.printf("\n=== Menu ===\n" +
                    "1. Find a person\n" +
                    "2. Print all people\n" +
                    "0. Exit\n");
            String numberMenu = this.scanner.nextLine();
            System.out.println();
            switch (numberMenu) {
                case "1" : findInData(); break;
                case "2" : print(); break;
                case "0" : exit(); break;
                default:
                    System.out.println("Incorrect option! Try again.");
            }
        }
    }

    private void putData() {
        System.out.println("Enter the number of people:");
        int number = this.scanner.nextInt();
        this.scanner.nextLine();
        this.data = new String[number];
        System.out.println("Enter all people:");
        for (int i = 0; i < number; i++) {
            data[i] = scanner.nextLine();
        }
    }

    private void findInData() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String strategy = scanner.nextLine();
        SearchMethod searchMethod = null;
        switch (strategy) {
            case "ALL": {
                searchMethod = new SearchALL(this); break;
            }
            case "ANY": {
                searchMethod = new SearchANY(this); break;
            }
            case "NONE": {
                searchMethod = new SearchNONE(this); break;
            }
            default: {
                System.out.println("Unknown algorithm type " + strategy);
            }
        }
        System.out.println("\nEnter a name or email to search all suitable people.");
        String request = this.scanner.nextLine();
        if (searchMethod != null) {
            SearchContext scx = new SearchContext();
            scx.setStrategy(searchMethod);
            scx.findAndPrint(request);
        }

        /*
        *
        Search words contain
        *
        int count = 0;
        System.out.println("Enter a name or email to search all suitable people.");
        String request = this.scanner.nextLine();
        for (Map.Entry<String, List<Integer>> entry : invertedIndex.entrySet()) {
            if (request.toLowerCase().equals(entry.getKey())) {
                System.out.printf("%d person found:\n", entry.getValue().size());
                for (Integer temp : entry.getValue()) {
                    System.out.println(data[temp]);
                    count++;
                }
                break;
            }
        }*/
        /*
        *
        Search any contain
        *
        for (int i = 0; i < this.data.length; i++) {
            if (this.data[i].toLowerCase().contains(request.toLowerCase())) {
                System.out.println(this.data[i]);
                count++;
            }
        }*/
        /*if (count == 0) {
            System.out.println("No matching people found.");
        }*/
    }

    private void print() {
        System.out.println("=== List of people ===");
        for (int i = 0; i < this.data.length; i++) {
            System.out.println(this.data[i]);
        }
    }

    private void exit() {
        System.out.println("Bye!");
        scanner.close();
        isWork = false;
    }
}


