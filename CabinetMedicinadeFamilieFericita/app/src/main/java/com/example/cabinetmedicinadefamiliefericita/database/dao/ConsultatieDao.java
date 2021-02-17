package com.example.cabinetmedicinadefamiliefericita.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cabinetmedicinadefamiliefericita.database.model.Consultatie;

import java.util.List;

@Dao
public interface ConsultatieDao {

    @Query("select * from consultatii")
    List<Consultatie> getAll();

    @Query("select count(*) from consultatii WHERE tip = :tipConsultatie AND id_medic = :idMedic")
    int countByTip(String tipConsultatie, long idMedic);

    @Insert
    long insert(Consultatie consultatie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Consultatie> listaConsultatii);

    @Update
    int update(Consultatie consultatie);

    @Delete
    int delete(Consultatie consultatie);
}
