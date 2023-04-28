import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MarkovNameGenerator {
    private Character TOTALCHAR = '%';
    private ArrayList<String> names;
    private HashMap<Character, HashMap<Character, Interval>> matrix;


    public MarkovNameGenerator(ArrayList<String> names) {
        this.names = names;
        this.matrix = generateMatrixWithInterval();
    }

    private List<Character> extractUniqueCharacters() {
        List<Character> uniqueCharacters = new ArrayList<>();
        uniqueCharacters.add(' '); // Représente l'absence de char
        for (String name : names) {
            name = name.toLowerCase(); // Insensible à la case
            for (char letter : name.toCharArray()) {
                // Ignorer les caractères qui ne sont pas des lettres et éviter les doublons
                if (Character.isLetter(letter) && !uniqueCharacters.contains(letter)) {
                    uniqueCharacters.add(letter);
                }
            }
        }
        return uniqueCharacters;
    }

    private HashMap<Character, HashMap<Character, Integer>> generateMatrixWithCount() {
        HashMap<Character, HashMap<Character, Integer>> matrix = new HashMap<>();
        List<Character> keys = extractUniqueCharacters();

        for (Character key: keys) {
            // Initialiser une matrice avec les chars uniques de la banque de noms
            matrix.put(key, new HashMap<>());
            for (Character k: keys) {
                matrix.get(key).put(k, 0);
            }
            matrix.get(key).put(TOTALCHAR, 0); // total
        }

        for (String name : names) {
            Character previousChar = ' ';
            name += previousChar; // Le dernier char est un vide
            for (Character letter: name.toLowerCase().toCharArray()) {
                matrix.get(previousChar).put(letter, matrix.get(previousChar).get(letter) + 1);
                matrix.get(previousChar).put(TOTALCHAR, matrix.get(previousChar).get(TOTALCHAR) + 1);
                previousChar = letter;
            }
        }

        return matrix;
    }
    
    private HashMap<Character, HashMap<Character, Interval>> generateMatrixWithInterval() {
        HashMap<Character, HashMap<Character, Integer>> origin = generateMatrixWithCount();
        HashMap<Character, HashMap<Character, Interval>> matrix = new HashMap<>();

        for (Map.Entry<Character, HashMap<Character, Integer>> fistEntry : origin.entrySet()) {
            Double prob = 1.0 / Double.valueOf(fistEntry.getValue().get(TOTALCHAR));
            Double min = 0.0, max = 0.0;
            matrix.put(fistEntry.getKey(), new HashMap<>());
            for (Map.Entry<Character, Integer> secondEntry : fistEntry.getValue().entrySet()) {
                if (secondEntry.getKey().equals(TOTALCHAR)) { continue; }

                max += prob * secondEntry.getValue();
                matrix.get(fistEntry.getKey()).put(secondEntry.getKey(), new Interval(min, max));
                min = max;
            }
        }

        return matrix;
    }

    public void regenerateMatrix(ArrayList<String> names) {
        this.names = names;
        matrix = generateMatrixWithInterval();
    }

    public void showMatrix() {
        String formatChar = "%-12c|";
        String formatString = "%-12s|";
        HashMap<Character, HashMap<Character, Interval>> matrix = generateMatrixWithInterval();

        var test = matrix.get(' ').keySet();
        for (Character item : test) {
            if (item.equals(' ')) {
                System.out.format(formatChar, item);
                System.out.format(formatString, "vide");
                continue;
            }
            System.out.format(formatChar, item);
        }
        System.out.println("");

        for (Map.Entry<Character, HashMap<Character, Interval>> firstEntry : matrix.entrySet()) {
            var leftHeader = firstEntry.getKey().toString();
            if (firstEntry.getKey().equals(' ')) {
                leftHeader = "vide";
            }
            System.out.format(formatString, leftHeader);
            
            for (Map.Entry<Character, Interval> secondEntry : firstEntry.getValue().entrySet()) {
                System.out.format(formatString, secondEntry.getValue().toString());
            }
            System.out.println("");
        }
    }
    
    public String generateRandomName() {
    	String randomName = "";
        Random random = new Random();
        char current = ' ';
        boolean endOfName = false;
        
        while (!endOfName) {
            HashMap<Character, Interval> possibleNext = matrix.get(current);
            double index = 0.0 + (1.0 - 0.0) * random.nextDouble();
            for (Map.Entry<Character, Interval> entry : possibleNext.entrySet()) {
                char next = entry.getKey();
                Interval interval = entry.getValue();
                if (index >= interval.getMin() && index < interval.getMax()) {
                	if (next == ' ') {
                		endOfName = true;
                		break;
                	}
                    current = next;
                    randomName += current;
                    break;
                }
            }
        }
        return randomName;
    }
}
