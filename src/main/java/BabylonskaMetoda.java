public class BabylonskaMetoda {
    private final double S;
    private double hodnota;
    private final double presnost;

    public BabylonskaMetoda(double s, double hodnota, double presnost) {
        S = s;
        this.hodnota = hodnota;
        this.presnost = presnost;
    }

    public double calculate() {
        double x2 = 0.5 * (hodnota + (S/hodnota));
        System.out.println(x2 - hodnota);
        System.out.println(presnost);
        int iterations = 0;
        while (Math.abs(x2 - hodnota) >= presnost) {
            hodnota = x2;
            x2 = 0.5 * (hodnota + (S/hodnota));
            iterations++;
        }
        System.out.printf("Vysledok je %.8f dosiahli sme ho po %d iteraciach.", x2, iterations);
        return x2;
    }
}
