package com.example.cabinetmedicinadefamiliefericita.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.cabinetmedicinadefamiliefericita.enums.TesteAnaliza;
import com.example.cabinetmedicinadefamiliefericita.enums.TipConsultatie;

import java.io.Serializable;

@Entity(tableName = "analize", foreignKeys = {
        // parentColumns se refera la entitatea externa (CadruMedical), iar childColumns la entitatea curenta
        @ForeignKey(entity = CadruMedical.class, parentColumns = "id", childColumns = "id_asistent"),
        @ForeignKey(entity = Pacient.class, parentColumns = "id", childColumns = "id_pacient"),
})
public class Analiza implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "id_asistent")
    private long idAsistent;

    @ColumnInfo(name = "id_pacient")
    private long idPacient;

    @ColumnInfo(name = "data_ora")
    private long dataOra;

    @ColumnInfo(name = "test")
    private TesteAnaliza test;

    public Analiza(long id, long idAsistent, long idPacient, long dataOra, TesteAnaliza test) {
        this.id = id;
        this.idAsistent = idAsistent;
        this.idPacient = idPacient;
        this.dataOra = dataOra;
        this.test = test;
    }

    @Ignore
    public Analiza(long idAsistent, long idPacient, long dataOra, TesteAnaliza test) {
        this.idAsistent = idAsistent;
        this.idPacient = idPacient;
        this.dataOra = dataOra;
        this.test = test;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdAsistent() {
        return idAsistent;
    }

    public void setIdAsistent(long idAsistent) {
        this.idAsistent = idAsistent;
    }

    public long getIdPacient() {
        return idPacient;
    }

    public void setIdPacient(long idPacient) {
        this.idPacient = idPacient;
    }

    public long getDataOra() {
        return dataOra;
    }

    public void setDataOra(long dataOra) {
        this.dataOra = dataOra;
    }

    public TesteAnaliza getTest() {
        return test;
    }

    public void setTest(TesteAnaliza test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "Analiza cu id-ul " + id +
                " efectuata de asistentul cu id-ul " + idAsistent +
                " asupra pacientului cu id-ul " + idPacient +
                " la data si ora " + dataOra +
                " a fost supus unui test de " + test +
                ". ";
    }
}
