package com.example.cabinetmedicinadefamiliefericita.database.service;

import android.content.Context;

import com.example.cabinetmedicinadefamiliefericita.asyncTask.AsyncTaskRunner;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.DatabaseManager;
import com.example.cabinetmedicinadefamiliefericita.database.dao.PacientDao;
import com.example.cabinetmedicinadefamiliefericita.database.model.Pacient;

import java.util.List;
import java.util.concurrent.Callable;

public class PacientService {
    private final PacientDao pacientDao;
    private final AsyncTaskRunner taskRunner;

    public PacientService(Context context) {
        pacientDao = DatabaseManager.getInstance(context).getPacientDao();
        taskRunner = new AsyncTaskRunner();
    }

    public void getAll(Callback<List<Pacient>> callback) {
        Callable<List<Pacient>> callable = new Callable<List<Pacient>>() {
            @Override
            public List<Pacient> call() {
                return pacientDao.getAll();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void getNumeById(Callback<String> callback, final long id) {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() {
                return pacientDao.getNumeById(id);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insert(Callback<Pacient> callback, final Pacient pacient) {
        Callable<Pacient> callable = new Callable<Pacient>() {
            @Override
            public Pacient call() {
                if (pacient == null) {
                    return null;
                }

                long id = pacientDao.insert(pacient);

                if (id == -1) {
                    return null;
                }

                pacient.setId(id);
                return pacient;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insertAll(Callback<List<Pacient>> callback, final List<Pacient> listaPacienti) {
        Callable<List<Pacient>> callable = new Callable<List<Pacient>>() {
            @Override
            public List<Pacient> call() {
                if (listaPacienti.isEmpty()) {
                    return null;
                }

                long[] ids = pacientDao.insertAll(listaPacienti);

                for (int i = 0; i < ids.length; i++) {
                    listaPacienti.get(i).setId(ids[i]);
                }

                return listaPacienti;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void update(Callback<Pacient> callback, final Pacient pacient) {
        Callable<Pacient> callable = new Callable<Pacient>() {
            @Override
            public Pacient call() {
                if (pacient == null) {
                    return null;
                }

                int count = pacientDao.update(pacient);

                if (count < 1) {
                    return null;
                }

                return pacient;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void delete(Callback<Integer> callback, final Pacient pacient) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                if (pacient == null) {
                    return -1;
                }
                return pacientDao.delete(pacient);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
}
