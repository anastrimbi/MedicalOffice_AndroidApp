package com.example.cabinetmedicinadefamiliefericita.database.service;

import android.content.Context;

import com.example.cabinetmedicinadefamiliefericita.asyncTask.AsyncTaskRunner;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.DatabaseManager;
import com.example.cabinetmedicinadefamiliefericita.database.dao.ConsultatieDao;
import com.example.cabinetmedicinadefamiliefericita.database.model.Consultatie;
import com.example.cabinetmedicinadefamiliefericita.enums.TipConsultatie;

import java.util.List;
import java.util.concurrent.Callable;

public class ConsultatieService {
    private final ConsultatieDao consultatieDao;
    private final AsyncTaskRunner taskRunner;

    public ConsultatieService(Context context) {
        consultatieDao = DatabaseManager.getInstance(context).getConsultatieDao();
        taskRunner = new AsyncTaskRunner();
    }

    public void getAll(Callback<List<Consultatie>> callback) {
        Callable<List<Consultatie>> callable = new Callable<List<Consultatie>>() {
            @Override
            public List<Consultatie> call() {
                return consultatieDao.getAll();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void countByTip(Callback<Integer> callback, final TipConsultatie tipConsultatie, final long idMedic) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                return consultatieDao.countByTip(tipConsultatie.name(), idMedic);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insert(Callback<Consultatie> callback, final Consultatie consultatie) {
        Callable<Consultatie> callable = new Callable<Consultatie>() {
            @Override
            public Consultatie call() {
                if (consultatie == null) {
                    return null;
                }

                long id = consultatieDao.insert(consultatie);

                if (id == -1) {
                    return null;
                }

                consultatie.setId(id);
                return consultatie;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insertAll(Callback<List<Consultatie>> callback, final List<Consultatie> listaConsultatii) {
        Callable<List<Consultatie>> callable = new Callable<List<Consultatie>>() {
            @Override
            public List<Consultatie> call() {
                if (listaConsultatii.isEmpty()) {
                    return null;
                }

                long[] ids = consultatieDao.insertAll(listaConsultatii);

                for (int i = 0; i < ids.length; i++) {
                    listaConsultatii.get(i).setId(ids[i]);
                }

                return listaConsultatii;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void update(Callback<Consultatie> callback, final Consultatie consultatie) {
        Callable<Consultatie> callable = new Callable<Consultatie>() {
            @Override
            public Consultatie call() {
                if (consultatie == null) {
                    return null;
                }

                int count = consultatieDao.update(consultatie);

                if (count < 1) {
                    return null;
                }

                return consultatie;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void delete(Callback<Integer> callback, final Consultatie consultatie) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                if (consultatie == null) {
                    return -1;
                }
                return consultatieDao.delete(consultatie);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
}
