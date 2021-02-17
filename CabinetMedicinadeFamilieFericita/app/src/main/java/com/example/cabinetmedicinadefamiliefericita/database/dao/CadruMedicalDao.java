package com.example.cabinetmedicinadefamiliefericita.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;

import java.util.List;

@Dao
public interface CadruMedicalDao {
    @Query("select * from cadre_medicale")
    List<CadruMedical> getAll();

    @Query("select * from cadre_medicale WHERE tip IN (:tipCadreMedicale)")
    List<CadruMedical> getAllOfTip(String[] tipCadreMedicale);

    @Query("select nume from cadre_medicale WHERE id = :id")
    String getNumeById(long id);

    @Insert
    long insert(CadruMedical cadruMedical);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<CadruMedical> listaCadreMedicale);

    @Update
    int update(CadruMedical cadruMedical);

    @Delete
    int delete(CadruMedical cadruMedical);
}
