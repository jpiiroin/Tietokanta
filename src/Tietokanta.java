import java.sql.*;
import java.io.*;

public class Tietokanta {
    
    Connection db = DriverManager.getConnection("jdbc:sqlite:testi.db");
    Statement s = db.createStatement();
    
    public Tietokanta() throws SQLException {
    
    }
    public void luoKanta() throws SQLException {
        
        s.execute("CREATE TABLE Paikat (id INTEGER PRIMARY KEY, nimi TEXT)");
        s.execute("CREATE TABLE Paketit (id INTEGER PRIMARY KEY, seurantakoodi STRING, asiakas_id INTEGER)");
        s.execute("CREATE TABLE Asiakkaat (id INTEGER PRIMARY KEY, nimi TEXT)");
        s.execute("CREATE TABLE Tapahtumat (id INTEGER PRIMARY KEY, paketti_id INTEGER, paikka_id INTEGER, kuvaus TEXT, lisaysaika DATETIME )");
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
    }
    public void poistaKanta() throws SQLException {
        s.close();
        db.close();
        new File("testi.db").delete();
    }
    public void luoPaikka(String nimi) throws SQLException {
        
        PreparedStatement kysy = db.prepareStatement("SELECT nimi FROM Paikat WHERE nimi=?");
        kysy.setString(1,nimi);
        ResultSet r = kysy.executeQuery();
        if (r.next()) {
            System.out.println("Paikka on jo olemassa");
        } else {
            PreparedStatement talleta = db.prepareStatement("INSERT INTO Paikat(nimi) VALUES (?)");
            talleta.setString(1,nimi);
            talleta.executeUpdate();
            System.out.println("Paikka lisätty");
        }
    }
    public void luoAsiakas(String nimi) throws SQLException {
        
        PreparedStatement kysy = db.prepareStatement("SELECT nimi FROM Asiakkaat WHERE nimi=?");
        kysy.setString(1,nimi);
        ResultSet r = kysy.executeQuery();
        if (r.next()) {
            System.out.println("Asiakas on jo olemassa");
        } else {
            PreparedStatement talleta = db.prepareStatement("INSERT INTO Asiakkaat(nimi) VALUES (?)");
            talleta.setString(1,nimi);
            talleta.executeUpdate();
            System.out.println("Asiakas lisätty");
        }
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
} 
