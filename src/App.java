public class App {
    public static void main(String[] args) throws Exception {
        String[] names = {"Simon", "Tim", "Emile", "Lin"};
        MarkovNameGenerator nameGenerator = new MarkovNameGenerator();

        var test = nameGenerator.generateMatrixWithInterval(names);
    }
}
