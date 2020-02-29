import java.sql.*;
import java.io.*;
import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter;

public class Tietokanta {
    
    Connection db = DriverManager.getConnection("jdbc:sqlite:testi.db");
    Statement s = db.createStatement();
    
    public Tietokanta() throws SQLException {
    
    }
    public void luoKanta() throws SQLException {
        s.execute("CREATE TABLE Paikat (id INTEGER PRIMARY KEY, nimi STRING)");
        s.execute("CREATE TABLE Paketit (id INTEGER PRIMARY KEY, seurantakoodi STRING, asiakas_id INTEGER)");
        s.execute("CREATE TABLE Asiakkaat (id INTEGER PRIMARY KEY, nimi STRING)");
        s.execute("CREATE TABLE Tapahtumat (id INTEGER PRIMARY KEY, paketti_id INTEGER, "
                + "paikka_id INTEGER, kuvaus STRING, lisaysaika STRING )");
        System.out.println("Tietokanta luotu");
    }
    public void naytaKanta() throws SQLException {
        ResultSet paikat = s.executeQuery("SELECT * FROM Paikat");
        System.out.println("Paikat:");
        while (paikat.next()) {
            System.out.println(paikat.getInt("id")+" "+paikat.getString("nimi"));
        }
        System.out.println("Asiakkaat:");
        ResultSet asiakkaat = s.executeQuery("SELECT * FROM Asiakkaat");
        while (asiakkaat.next()) {
            System.out.println(asiakkaat.getInt("id")+" "+asiakkaat.getString("nimi"));
        }
        System.out.println("Paketit:");
        ResultSet paketit = s.executeQuery("SELECT * FROM Paketit");
        while (paketit.next()) {
            System.out.println(paketit.getInt("id")+" "+paketit.getString("seurantakoodi")+" "+paketit.getString("asiakas_id"));
        }
        System.out.println("Tapahtumat:");
        ResultSet tapahtumat = s.executeQuery("SELECT * FROM Tapahtumat");
        while (tapahtumat.next()) {
            System.out.println(tapahtumat.getInt("id")+" "+tapahtumat.getString("paketti_id")
            +" "+tapahtumat.getString("paikka_id")
            +" "+tapahtumat.getString("kuvaus")+" "+tapahtumat.getString("lisaysaika"));
        }
    }
    public String naytaAika() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.M.yyyy HH:mm");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }
    public void poistaKanta() throws SQLException {
        s.close();
        db.close();
        new File("testi.db").delete();
        System.out.println("Tietokanta poistettu");
    }
    public void luoPaikka(String nimi) throws SQLException {
        if (onkoOlemassa(nimi, "Paikat")) {
            System.out.println("VIRHE: Paikka on jo olemassa");
        } else {
            talletaKantaan(nimi, "Paikat");
            System.out.println("Paikka lisätty");
        }   
    }
    public void luoAsiakas(String nimi) throws SQLException {
        if (onkoOlemassa(nimi, "Asiakkaat")) {
            System.out.println("VIRHE: Asiakas on jo olemassa");
        } else {
            talletaKantaan(nimi, "Asiakkaat");
            System.out.println("Asiakas lisätty");
        }   
    }
    public void talletaKantaan(String nimi, String kanta) throws SQLException {
        PreparedStatement talleta = db.prepareStatement("INSERT INTO "+kanta+"(nimi) VALUES (?)");
        talleta.setString(1,nimi);
        talleta.executeUpdate();
    }
    public boolean onkoOlemassa (String nimi, String kanta) throws SQLException {
        PreparedStatement kysy = db.prepareStatement("SELECT nimi FROM "+kanta+" WHERE nimi=?");
        kysy.setString(1,nimi);
        ResultSet r = kysy.executeQuery();
        return r.next();
    }
    public void luoPaketti(String koodi, String nimi) throws SQLException {
        PreparedStatement kysy = db.prepareStatement("SELECT id FROM Asiakkaat WHERE nimi=?");
        kysy.setString(1,nimi);
        ResultSet r = kysy.executeQuery();
        int asiakas_id = r.getInt("id");
        
        PreparedStatement talleta = db.prepareStatement("INSERT INTO Paketit(seurantakoodi,asiakas_id) VALUES (?,?)");
        talleta.setString(1,koodi);
        talleta.setInt(2,asiakas_id);
        talleta.executeUpdate();
        System.out.println("Paketti lisätty");
    }
    public void luoTapahtuma(String seurantakoodi, String paikka, String kuvaus) throws SQLException {
        PreparedStatement kysykoodi = db.prepareStatement("SELECT id FROM Paketit WHERE seurantakoodi=?");
        kysykoodi.setString(1,seurantakoodi);
        ResultSet eka = kysykoodi.executeQuery();
        int paketti_id = eka.getInt("id");
        
        PreparedStatement kysypaikka = db.prepareStatement("SELECT id FROM Paikat WHERE nimi=?");
        kysypaikka.setString(1,paikka);
        ResultSet toka = kysypaikka.executeQuery();
        int paikka_id = toka.getInt("id");
        
        PreparedStatement talleta = db.prepareStatement("INSERT INTO Tapahtumat(paketti_id,paikka_id, "
                + "kuvaus, lisaysaika) VALUES (?,?,?,?)");
        talleta.setInt(1, paketti_id);
        talleta.setInt(2, paikka_id);
        talleta.setString(3, kuvaus);
        talleta.setString(4, naytaAika());
        talleta.executeUpdate();
    }
    public void haePaketti(String koodi) throws SQLException {
        ResultSet tapahtumat = s.executeQuery("SELECT T.lisaysaika, P.nimi, T.kuvaus "
                + "FROM Tapahtumat T, Paikat P, Paketit PT WHERE T.paketti_id = P.id "
                + "AND T.paikka_id = PT.id AND PT.seurantakoodi =" +koodi);
        while (tapahtumat.next()) {
            System.out.println(tapahtumat.getString("lisaysaika")+" "+tapahtumat.getString("nimi")
                    +" "+tapahtumat.getString("kuvaus"));
        }
    }
} 
