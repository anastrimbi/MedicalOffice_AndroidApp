package com.example.cabinetmedicinadefamiliefericita.database.service;

import android.content.Context;

import com.example.cabinetmedicinadefamiliefericita.asyncTask.AsyncTaskRunner;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.DatabaseManager;
import com.example.cabinetmedicinadefamiliefericita.database.dao.ClinicaDao;
import com.example.cabinetmedicinadefamiliefericita.database.model.Clinica;

import java.util.concurrent.Callable;

public class ClinicaService {
    private final ClinicaDao clinicaDao;
    private final AsyncTaskRunner taskRunner;

    public ClinicaService(Context context) {
        clinicaDao = DatabaseManager.getInstance(context).getClinicaDao();
        taskRunner = new AsyncTaskRunner();
    }

    public void getOne(Callback<Clinica> callback, final long id) {
        Callable<Clinica> callable = new Callable<Clinica>() {
            @Override
            public Clinica call() {
                return clinicaDao.getOne(id);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insert(Callback<Clinica> callback, final Clinica clinica) {
        Callable<Clinica> callable = new Callable<Clinica>() {
            @Override
            public Clinica call() {
                if (clinica == null) {
                    return null;
                }

                long id = clinicaDao.insert(clinica);

                if (id == -1) {
                    return null;
                }

                clinica.setId(id);
                return clinica;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void update(Callback<Clinica> callback, final Clinica clinica) {
        Callable<Clinica> callable = new Callable<Clinica>() {
            @Override
            public Clinica call() {
                if (clinica == null) {
                    return null;
                }

                int count = clinicaDao.update(clinica);

                if (count < 1) {
                    return null;
                }

                return clinica;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void delete(Callback<Integer> callback, final Clinica clinica) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                if (clinica == null) {
                    return -1;
                }
                return clinicaDao.delete(clinica);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
}
