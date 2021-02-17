package com.example.cabinetmedicinadefamiliefericita.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cabinetmedicinadefamiliefericita.database.model.Pacient;

import java.util.List;

@Dao
public interface PacientDao {

    @Query("select * from pacienti")
    List<Pacient> getAll();

    @Query("select nume from pacienti WHERE id = :id")
    String getNumeById(long id);

    @Insert
    long insert(Pacient pacient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Pacient> listaPacienti);

    @Update
    int update(Pacient pacient);

    @Delete
    int delete(Pacient pacient);
}
