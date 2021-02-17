package com.example.cabinetmedicinadefamiliefericita.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cabinetmedicinadefamiliefericita.database.dao.AnalizaDao;
import com.example.cabinetmedicinadefamiliefericita.database.dao.CadruMedicalDao;
import com.example.cabinetmedicinadefamiliefericita.database.dao.ClinicaDao;
import com.example.cabinetmedicinadefamiliefericita.database.dao.ConsultatieDao;
import com.example.cabinetmedicinadefamiliefericita.database.dao.PacientDao;
import com.example.cabinetmedicinadefamiliefericita.database.model.Analiza;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.database.model.Clinica;
import com.example.cabinetmedicinadefamiliefericita.database.model.Consultatie;
import com.example.cabinetmedicinadefamiliefericita.database.model.Pacient;
import com.example.cabinetmedicinadefamiliefericita.utils.DatabaseConverters;

@Database(entities = { Clinica.class, CadruMedical.class, Pacient.class, Analiza.class, Consultatie.class }, exportSchema = false, version = 1)
@TypeConverters({DatabaseConverters.class})
public abstract class DatabaseManager extends RoomDatabase {
    private static final String CLINICA_DB = "clinica_db";

    private static DatabaseManager databaseManager;

    public static DatabaseManager getInstance(Context context) {
        if (databaseManager == null) {
            synchronized (DatabaseManager.class) {
                if (databaseManager == null) {
                    databaseManager = Room.databaseBuilder(context, DatabaseManager.class, CLINICA_DB)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return databaseManager;
    }

    public abstract ClinicaDao getClinicaDao();
    public abstract CadruMedicalDao getCadruMedicalDao();
    public abstract PacientDao getPacientDao();
    public abstract AnalizaDao getAnalizaDao();
    public abstract ConsultatieDao getConsultatieDao();
}
