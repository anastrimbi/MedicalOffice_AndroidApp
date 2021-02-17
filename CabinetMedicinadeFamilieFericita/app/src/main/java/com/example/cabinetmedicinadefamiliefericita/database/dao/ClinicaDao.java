package com.example.cabinetmedicinadefamiliefericita.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cabinetmedicinadefamiliefericita.database.model.Clinica;

@Dao
public interface ClinicaDao {

    @Query("select * from clinici where id = :id")
    Clinica getOne(long id);

    @Insert
    long insert(Clinica clinica);

    @Update
    int update(Clinica clinica);

    @Delete
    int delete(Clinica clinica);
}
