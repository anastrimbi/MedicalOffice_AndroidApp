package com.example.cabinetmedicinadefamiliefericita.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.cabinetmedicinadefamiliefericita.enums.TipConsultatie;

import java.io.Serializable;

@Entity(tableName = "consultatii", foreignKeys = {
        // parentColumns se refera la entitatea externa (CadruMedical), iar childColumns la entitatea curenta
        @ForeignKey(entity = CadruMedical.class, parentColumns = "id", childColumns = "id_medic"),
        @ForeignKey(entity = Pacient.class, parentColumns = "id", childColumns = "id_pacient"),
})
public class Consultatie implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "id_medic")
    private long idMedic;

    @ColumnInfo(name = "id_pacient")
    private long idPacient;

    @ColumnInfo(name = "data_ora")
    private long dataOra;

    @ColumnInfo(name = "tip")
    private TipConsultatie tip;

    public Consultatie(long id, long idMedic, long idPacient, long dataOra, TipConsultatie tip) {
        this.id = id;
        this.idMedic = idMedic;
        this.idPacient = idPacient;
        this.dataOra = dataOra;
        this.tip = tip;
    }

    @Ignore
    public Consultatie(long idMedic, long idPacient, long dataOra, TipConsultatie tip) {
        this.idMedic = idMedic;
        this.idPacient = idPacient;
        this.dataOra = dataOra;
        this.tip = tip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdMedic() {
        return idMedic;
    }

    public void setIdMedic(long idMedic) {
        this.idMedic = idMedic;
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

    public TipConsultatie getTip() {
        return tip;
    }

    public void setTip(TipConsultatie tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "Consultatia cu id-ul " + id +
                ", efectuata de medicul cu id-ul " + idMedic +
                " asupra pacinetului cu id-ul " + idPacient +
                ", la data si ora: " + dataOra +
                ". Consultatia este de tip " + tip +
                ". ";
    }
}
