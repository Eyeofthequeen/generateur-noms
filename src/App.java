public class App {
    public static void main(String[] args) throws Exception {
        String[] names = {"Simon", "Tim", "Emile", "Lin"};
        MarkovNameGenerator nameGenerator = new MarkovNameGenerator(names);

        // Lire la table affiché à l'envers. Les entêtes de gauche représente le caractère avant X et l'entête du haut
        // représente le caractère qui suit X.

        //        | s
        // vide   | [0, 0.1]            Ici on a un vide suivi de s
        nameGenerator.showMatrix();
        
        for (int i = 0; i < 20; i++) {
            String name = nameGenerator.generateRandomName();
            System.out.println(name);
        }
    }
}
