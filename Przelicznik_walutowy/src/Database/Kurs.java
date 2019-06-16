/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import com.j256.ormlite.field.DatabaseField;


public class Kurs {
    
    
    
    
    @DatabaseField
    private String nazwaWaluty;
    @DatabaseField
    private String kodWaluty;
    @DatabaseField
    private String kurs_sredni;
    @DatabaseField(id = true)
    private int id;
    
    
    public Kurs() {
        // ORMLite needs a no-arg constructor 
    }
    public Kurs(int id,String nazwaWaluty, String kodWaluty, String kurs_sredni) {
        
        this.nazwaWaluty = nazwaWaluty;
        this.kodWaluty = kodWaluty;
        this.kurs_sredni=kurs_sredni;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

   

    public String getNazwaWaluty() {
        return nazwaWaluty;
    }

    public void setNazwaWaluty(String nazwaWaluty) {
        this.nazwaWaluty = nazwaWaluty;
    }

    public String getKodWaluty() {
        return kodWaluty;
    }

    public void setKodWaluty(String kodWaluty) {
        this.kodWaluty = kodWaluty;
    }

    public String getKurs_sredni() {
        return kurs_sredni;
    }

    public void setKurs_sredni(String kurs_sredni) {
        this.kurs_sredni = kurs_sredni;
    }
   
    
}

