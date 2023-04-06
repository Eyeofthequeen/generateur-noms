import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkovNameGenerator {
    Character TOTALCHAR = '%';
    String[] names;

    public MarkovNameGenerator(String[] names) {
        this.names = names;
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
    
    public HashMap<Character, HashMap<Character, Interval>> generateMatrixWithInterval() {
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
}
