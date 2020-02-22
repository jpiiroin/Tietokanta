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
        ResultSet r = s.executeQuery("SELECT * FROM Paikat");
        while (r.next()) {
            System.out.println(r.getInt("id")+" "+r.getString("nimi"));
        }  
    }
    public void poistaKanta() throws SQLException {
        s.close();
        db.close();
        boolean result = new File("D:\\NetBeansProjects\\Tietokanta\\testi.db").delete();
    }
    public void luoPaikka(String nimi) throws SQLException {
        PreparedStatement p = db.prepareStatement("INSERT INTO Paikat(nimi) VALUES (?)");
        p.setString(1,nimi);
        p.executeUpdate();
        System.out.println("Paikka lisätty");
    }
    public void luoAsiakas(String nimi) throws SQLException {
        PreparedStatement p = db.prepareStatement("INSERT INTO Asiakkaat(nimi) VALUES (?)");
        p.setString(1,nimi);
        p.executeUpdate();
        System.out.println("Asiakas lisätty");
    }
} 
