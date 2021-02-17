package com.example.cabinetmedicinadefamiliefericita.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.cabinetmedicinadefamiliefericita.enums.Gen;

import java.io.Serializable;

@Entity(tableName = "pacienti")
public class Pacient implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "nume")
    private String nume;

    @ColumnInfo(name = "cnp")
    private long CNP;

    @ColumnInfo(name = "varsta")
    private int varsta;

    @ColumnInfo(name = "gen")
    private Gen gen;

    public Pacient(long id, String nume, long CNP, int varsta, Gen gen) {
        this.id = id;
        this.nume = nume;
        this.CNP = CNP;
        this.varsta = varsta;
        this.gen = gen;
    }

    @Ignore
    public Pacient(String nume, long CNP, int varsta, Gen gen) {
        this.nume = nume;
        this.CNP = CNP;
        this.varsta = varsta;
        this.gen = gen;
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

    public long getCNP() {
        return CNP;
    }

    public void setCNP(long CNP) {
        this.CNP = CNP;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public Gen getGen() {
        return gen;
    }

    public void setGen(Gen gen) {
        this.gen = gen;
    }

    @Override
    public String toString() {
        return "Pacientul cu id-ul " + id +
                " se numeste " + nume +
                ", e de gen " + gen +
                " si are " + varsta + " ani. " +
                "CNP-ul sau este " + CNP +
                ". ";
    }
}
