package com.example.cabinetmedicinadefamiliefericita.database.service;

import android.content.Context;

import com.example.cabinetmedicinadefamiliefericita.asyncTask.AsyncTaskRunner;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.DatabaseManager;
import com.example.cabinetmedicinadefamiliefericita.database.dao.AnalizaDao;
import com.example.cabinetmedicinadefamiliefericita.database.model.Analiza;
import com.example.cabinetmedicinadefamiliefericita.enums.TesteAnaliza;
import com.example.cabinetmedicinadefamiliefericita.enums.TipConsultatie;

import java.util.List;
import java.util.concurrent.Callable;

public class AnalizaService {
    private final AnalizaDao analizaDao;
    private final AsyncTaskRunner taskRunner;

    public AnalizaService(Context context) {
        analizaDao = DatabaseManager.getInstance(context).getAnalizaDao();
        taskRunner = new AsyncTaskRunner();
    }

    public void getAll(Callback<List<Analiza>> callback) {
        Callable<List<Analiza>> callable = new Callable<List<Analiza>>() {
            @Override
            public List<Analiza> call() {
                return analizaDao.getAll();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void countByTest(Callback<Integer> callback, final TesteAnaliza testAnaliza, final long idAsistent) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                return analizaDao.countByTest(testAnaliza.name(), idAsistent);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insert(Callback<Analiza> callback, final Analiza analiza) {
        Callable<Analiza> callable = new Callable<Analiza>() {
            @Override
            public Analiza call() {
                if (analiza == null) {
                    return null;
                }

                long id = analizaDao.insert(analiza);

                if (id == -1) {
                    return null;
                }

                analiza.setId(id);
                return analiza;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insertAll(Callback<List<Analiza>> callback, final List<Analiza> listaAnalize) {
        Callable<List<Analiza>> callable = new Callable<List<Analiza>>() {
            @Override
            public List<Analiza> call() {
                if (listaAnalize.isEmpty()) {
                    return null;
                }

                long[] ids = analizaDao.insertAll(listaAnalize);

                for (int i = 0; i < ids.length; i++) {
                    listaAnalize.get(i).setId(ids[i]);
                }

                return listaAnalize;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void update(Callback<Analiza> callback, final Analiza analiza) {
        Callable<Analiza> callable = new Callable<Analiza>() {
            @Override
            public Analiza call() {
                if (analiza == null) {
                    return null;
                }

                int count = analizaDao.update(analiza);

                if (count < 1) {
                    return null;
                }

                return analiza;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void delete(Callback<Integer> callback, final Analiza analiza) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                if (analiza == null) {
                    return -1;
                }
                return analizaDao.delete(analiza);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
}
