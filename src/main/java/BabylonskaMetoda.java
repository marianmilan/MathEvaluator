import java.util.Scanner;

public class BabylonskaMetoda {
    private Scanner scanner;
    private double S = 50;
    private double hodnota = 5;
    private double presnost = 1e-8;

    public BabylonskaMetoda(Scanner scanner) {
        this.scanner = scanner;
        init();
        calculate();
    }

    public double calculate() {
        double x2 = 0.5 * (hodnota + (S/hodnota));
        int iterations = 0;
        while (Math.abs(x2 - hodnota) >= presnost) {
            hodnota = x2;
            x2 = 0.5 * (hodnota + (S/hodnota));
            iterations++;
        }
        System.out.printf("Vysledok je %.8f dosiahli sme ho po %d iteraciach.", x2, iterations);
        return x2;
    }

    private void init() {
        byte choice;
        do {
            System.out.println("Pouzit ukazku [1] alebo zadat vlastne hodnoty [2]: ");
            choice = Byte.parseByte(scanner.nextLine());
        } while (choice != 1 && choice != 2);

        if (choice == 1) return;

        System.out.println("Zadajte hodnotu cisla S: ");
        this.S = Double.parseDouble(scanner.nextLine());
        System.out.println("Zadajte pociatocnu hodnotu: ");
        this.hodnota = Double.parseDouble(scanner.nextLine());
        System.out.println("Zadajte presnost vo formate 1eX: ");
        this.presnost = Double.parseDouble(scanner.nextLine());
    }
}
