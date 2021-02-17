package com.example.cabinetmedicinadefamiliefericita.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.cabinetmedicinadefamiliefericita.enums.Gen;
import com.example.cabinetmedicinadefamiliefericita.enums.TipCadreMedicale;

import java.io.Serializable;

@Entity(tableName = "cadre_medicale")
public class CadruMedical implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "nume")
    private String nume;

    @ColumnInfo(name = "tip")
    private TipCadreMedicale tip;

    @ColumnInfo(name = "gen")
    private Gen gen;

    @ColumnInfo(name = "poza")
    private String poza;

    public CadruMedical(long id, String nume, TipCadreMedicale tip, Gen gen, String poza) {
        this.id = id;
        this.nume = nume;
        this.tip = tip;
        this.gen = gen;
        this.poza = poza;
    }

    @Ignore
    public CadruMedical(String nume, TipCadreMedicale tip, Gen gen, String poza) {
        this.nume = nume;
        this.tip = tip;
        this.gen = gen;
        this.poza = poza;
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

    public TipCadreMedicale getTip() {
        return tip;
    }

    public void setTip(TipCadreMedicale tip) {
        this.tip = tip;
    }

    public Gen getGen() {
        return gen;
    }

    public void setGen(Gen gen) {
        this.gen = gen;
    }

    public String getPoza() {
        return poza;
    }

    public void setPoza(String poza) {
        this.poza = poza;
    }

    @Override
    public String toString() {
        return "Cadrul medical cu id-ul " + id +
                " si numele " + nume +
                ", de gen " + gen +
                " este " + tip +
                ". Poate fi regasit la adresa: " + poza +
                ". ";
    }
}
