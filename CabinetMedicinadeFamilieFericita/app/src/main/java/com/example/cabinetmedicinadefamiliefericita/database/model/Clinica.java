package com.example.cabinetmedicinadefamiliefericita.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "clinici")
public class Clinica implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "nume")
    private String nume;

    @ColumnInfo(name = "descriere")
    private String descriere;

    @ColumnInfo(name = "adresa")
    private String adresa;

    @ColumnInfo(name = "contact")
    private String contact;

    public Clinica(long id, String nume, String descriere, String adresa, String contact) {
        this.id = id;
        this.nume = nume;
        this.descriere = descriere;
        this.adresa = adresa;
        this.contact = contact;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Clinica medicala cu numele " + nume +
                " are urmatoarea descriere: " + descriere +
                ". Adresa sa este: " + adresa +
                ", iar datele de contact sunt: " + contact +
                ". ";
    }
}
