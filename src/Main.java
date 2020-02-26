import java.sql.SQLException;
import java.util.Scanner;

 public class Main {

     public static void main(String[] args) throws SQLException {

        Scanner lukija = new Scanner(System.in);

        Tietokanta kanta = new Tietokanta();

        while (true) {

            System.out.print("Toiminnot: [1]Luo kanta [2]Lisää paikka [3]Lisää asiakas [4]Lisää paketti"
                    + " [5]Lisää tapahtuma"
                    +"\n           [L]Lopeta [P]Poista kanta [N]Näytä kaikki"
                    + "\n           Valitse toiminto (1-9): ");

            String komento = lukija.nextLine();

            if (komento.equals("L")) {
                break;
            }
            if (komento.equals("1")) {
                kanta.luoKanta();
            }
            if (komento.equals("2")) {
                System.out.print("Anna paikan nimi: ");
                String nimi = lukija.nextLine();
                kanta.luoPaikka(nimi);
            }
            if (komento.equals("3")) {
                System.out.print("Anna asiakkaan nimi: ");
                String nimi = lukija.nextLine();
                kanta.luoAsiakas(nimi);
            }
            if (komento.equals("4")) {
                System.out.print("Anna paketin seurantakoodi: ");
                String koodi = lukija.nextLine();
                System.out.print("Anna asiakkaan nimi: ");
                String nimi = lukija.nextLine();
                kanta.luoPaketti(koodi, nimi);
            }
            if (komento.equals("5")) {
                System.out.print("Anna paketin seurantakoodi: ");
                String koodi = lukija.nextLine();
                System.out.println("Anna tapahtuman paikka: ");
                String paikka = lukija.nextLine();
                System.out.println("Anna tapahtuman kuvaus: ");
                String kuvaus = lukija.nextLine();
                kanta.luoTapahtuma(koodi, paikka, kuvaus);
            }
            if (komento.equals("P")) {
                kanta.poistaKanta();
            }
            if (komento.equals("N")) {
                kanta.naytaKanta();
            }

        }

    }

}