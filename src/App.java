import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
	static String message = "";
	
	public static void main(String[] args) throws Exception {
		ArrayList<String> names = readNames("names.txt");
		MarkovNameGenerator nameGenerator = new MarkovNameGenerator(names);		
		int choix = -1;

		do {
			System.out.println("\nGénérateur aléatoire de noms:");
			System.out.println("\n[Menu]");
			System.out.println("1: Générer des noms aléatoirement");
			System.out.println("2: Ajouter un nom et réinitialiser le générateur");
			System.out.println("3: Lire un fichier de noms (names.txt par défaut)");
			System.out.println("4: Montrer la matrice de probabilités");
			System.out.println("5: Quitter");
			System.out.println("\n" + message);
			System.out.print("\nFaites votre choix et appuyez sur ENTER: ");
			choix = getInt();
			message = "";

			switch (choix) {
			case 1:
				System.out.print("Déterminer le nombre de noms à générer aléatoirement (0 pour annuler): ");
				int namesCount = getInt();
				int exit = 0;
				String error = "";
			
				if(namesCount < exit) {
					error += "Choix invalide! ";
				}

				if(namesCount == exit || error.length() > 0) {
					message = error + "Retour au menu.";
					break;
				}

				for (int i = 0; i < namesCount; i++) {
					String name = nameGenerator.generateRandomName();
					System.out.println(name);
				}
				
				break;

			case 2:
				System.out.print("\nVeuillez entrer un nom à ajouter: ");
				String name = getString();
				names.add(name);
				nameGenerator = new MarkovNameGenerator(names);
				message = "Le nom " + name + " a été ajouté et le générateur a été réinitialisé.";
				break;

			case 3:
				System.out.print("\nVeuillez entrer le nom du fichier contenant la banque de noms: ");
				String fileName = getString();
				
				if (!fileExists(fileName)) {
					message = "Fichier non trouvé.";
					break;
				}
				
				names = readNames(fileName);
				nameGenerator = new MarkovNameGenerator(names);
				message = "Le générateur a été réinitialisé à partir des noms fournis par le fichier: " + fileName;
				break;

			case 4:
				nameGenerator.showMatrix();
				break;

			case 5:
				System.out.println("\nAu revoir!");
				break;

			default:
				message = "Choix invalide!  Veuillez recommencer.";
				break;
			}
		} while (choix != 5);

		System.exit(0);
	}

	public static ArrayList<String> readNames(String fileName) {
		ArrayList<String> names = new ArrayList<String>();
		
		try {
			File namesFile = new File(fileName);
			Scanner namesReader = new Scanner(namesFile);
			while (namesReader.hasNextLine()) {
				String name = namesReader.nextLine();
				names.add(name);
			}
			namesReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return names;
	}
	
	public static boolean fileExists(String fileName) {
		File file = new File(fileName);
		if(file.isFile()) { 
		    return true;
		}
		
		return false;
	}
	
	public static String getString() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();

		return input;
	}

	public static int getInt() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();

		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
