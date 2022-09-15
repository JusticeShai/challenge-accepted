import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;

public class App {
    /**
     * @param args
     */
    public static void main(String[] args) {
        
        System.out.println("**********Results***********");
        List<String> lines = new ArrayList<>();
        lines = readFile(args[0]);
        Map<String, Integer> map = new HashMap<>();
        map = parseData(lines);
        for(String line: lines){
            updateHelper(line, map);
        }
        map = sortByValue(map);
        printRanking(map);

        // System.out.println("**********Tests One***********");
        // testParseData();
        // System.out.println("**********Tests Two***********");
        // testUpdateHelper();
        // System.out.println("**********Tests Three***********");
        // testSortByValue();
    }
    
    //Reads the file and returns a list of lines
    public static List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            lines = stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    //create a tournament table map  with score as 0s
    public static Map<String, Integer> parseData(List<String> lines) {
        Map<String, Integer> map = new HashMap<>();
        for (String line : lines) {
            String[] split = line.split(",");
            String[] team1 = split[0].split(" [0-9]");
            String[] team2 = split[1].split(" [0-9]");
            if (!map.containsKey(team1[0])) {
                map.put(team1[0].trim(), 0);    
            }
            if (!map.containsKey(team2[0])) {
                 map.put(team2[0].trim(), 0);
            }
        }

        return map;
    }
    
    public static void updateHelper(String line, Map<String, Integer> map) {
        //create map from file contents
        Pattern pattern = Pattern.compile("(.*?) (\\d+), (.*?) (\\d+)");
        Matcher matcher = pattern.matcher(line);
        int team1Score = 0;
        int team2Score = 0;
        String team1Name = "";
        String team2Name = "";
        if (matcher.find()) {
            team1Name = matcher.group(1);
            team1Score = Integer.parseInt(matcher.group(2));
            team2Name = matcher.group(3);
            team2Score = Integer.parseInt(matcher.group(4));
            if(team1Score > team2Score) {
                map.put(team1Name, map.get(team1Name)+3);
            } else if (team1Score < team2Score) {
                map.put(team2Name, map.get(team2Name)+3);
            } else {
                map.put(team1Name, map.get(team1Name)+1);
                map.put(team2Name, map.get(team2Name)+1);
            }
        }
    }
    
    //sort the league results by name and points
    public static Map<String, Integer> sortByValue(Map<String, Integer> map) {
        //turn map to list
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
        //create a new LinkedHashMap to simplify moving values around
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

        //sort list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if(o2.getValue() == o1.getValue()) {
                    return (o1.getKey()).compareTo(o2.getKey());
                }
                return (o2.getValue()).compareTo(o1.getValue());
                
            }
        });

        //turns sorted list to map
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    //print the league results
    public static void printRanking(Map<String, Integer> map) {
        int rank = 1;
        int previousScore = 0;
        int position = 1;
        int count2 = 1;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (previousScore == entry.getValue()) {
                count2++;
                rank --;
                if(count2 > 2){
                    rank--;
                }
                 
                System.out.println(rank + ". " + entry.getKey() + ", " + entry.getValue() + " pts");
                position++;
                rank += 2;
            } else {
                position++;

                if(position > rank){
                    rank = position-1;
                }
                
                System.out.println(rank + ". " + entry.getKey() + ", " + entry.getValue() + " pts");
                rank++;
            }
            previousScore = entry.getValue();
            
        }
    }


    //Tests!!
    /*
     * write unit test for the following methods
     * parseData
     * updateHelper
     * sortByValue
     * printRanking
     */
    public static void testParseData() {
        List<String> lines = new ArrayList<>();
        lines.add("Team1 1, Team2 2");
        lines.add("Team3 3, Team4 4");
        Map<String, Integer> map = new HashMap<>();
        map = parseData(lines);
        //traverse map and print out
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public static void testUpdateHelper() {
        List<String> lines = new ArrayList<>();
        lines.add("Team1 1, Team2 2");
        lines.add("Team3 3, Team4 4");
        Map<String, Integer> map = new HashMap<>();
        map = parseData(lines);
        //traverse map and print out
        for(String line: lines){
            updateHelper(line, map);
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public static void testSortByValue() {
        List<String> lines = new ArrayList<>();
        lines.add("Team1 1, Team2 2");
        lines.add("Team3 3, Team4 4");
        Map<String, Integer> map = new HashMap<>();
        map = parseData(lines);
        //traverse map and print out
        for(String line: lines){
            updateHelper(line, map);
        }
        map = sortByValue(map);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public static void testPrintRanking() {
        List<String> lines = new ArrayList<>();
        lines.add("Team1 1, Team2 2");
        lines.add("Team3 3, Team4 4");
        Map<String, Integer> map = new HashMap<>();
        map = parseData(lines);
        //traverse map and print out
        for(String line: lines){
            updateHelper(line, map);
        }
        map = sortByValue(map);
        printRanking(map);
    }
   
}
