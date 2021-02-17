package com.example.cabinetmedicinadefamiliefericita.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cabinetmedicinadefamiliefericita.database.model.Analiza;
import com.example.cabinetmedicinadefamiliefericita.database.model.Pacient;

import java.util.List;

@Dao
public interface AnalizaDao {

    @Query("select * from analize")
    List<Analiza> getAll();

    @Query("select count(*) from analize WHERE test = :testAnaliza AND id_asistent = :idAsistent")
    int countByTest(String testAnaliza, long idAsistent);

    @Insert
    long insert(Analiza analiza);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Analiza> listaAnaliza);

    @Update
    int update(Analiza analiza);

    @Delete
    int delete(Analiza analiza);
}
