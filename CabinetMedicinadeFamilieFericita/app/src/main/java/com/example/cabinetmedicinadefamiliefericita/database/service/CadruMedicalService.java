package com.example.cabinetmedicinadefamiliefericita.database.service;

import android.content.Context;

import com.example.cabinetmedicinadefamiliefericita.asyncTask.AsyncTaskRunner;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.DatabaseManager;
import com.example.cabinetmedicinadefamiliefericita.database.dao.CadruMedicalDao;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.enums.TipCadreMedicale;

import java.util.List;
import java.util.concurrent.Callable;

public class CadruMedicalService {
    private final CadruMedicalDao cadruMedicalDao;
    private final AsyncTaskRunner taskRunner;

    public CadruMedicalService(Context context) {
        cadruMedicalDao = DatabaseManager.getInstance(context).getCadruMedicalDao();
        taskRunner = new AsyncTaskRunner();
    }

    public void getAll(Callback<List<CadruMedical>> callback) {
        Callable<List<CadruMedical>> callable = new Callable<List<CadruMedical>>() {
            @Override
            public List<CadruMedical> call() {
                return cadruMedicalDao.getAll();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void getNumeById(Callback<String> callback, final long id) {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() {
                return cadruMedicalDao.getNumeById(id);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void getAllMedici(Callback<List<CadruMedical>> callback) {
        Callable<List<CadruMedical>> callable = new Callable<List<CadruMedical>>() {
            @Override
            public List<CadruMedical> call() {
                String[] cadreTipMedici = { TipCadreMedicale.MedicPrimar.toString(), TipCadreMedicale.MedicSpecialist.toString() };
                return cadruMedicalDao.getAllOfTip(cadreTipMedici);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void getAllAsistenti(Callback<List<CadruMedical>> callback) {
        Callable<List<CadruMedical>> callable = new Callable<List<CadruMedical>>() {
            @Override
            public List<CadruMedical> call() {
                String[] cadreTipMedici = { TipCadreMedicale.Asistent.toString() };
                return cadruMedicalDao.getAllOfTip(cadreTipMedici);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insert(Callback<CadruMedical> callback, final CadruMedical cadruMedical) {
        Callable<CadruMedical> callable = new Callable<CadruMedical>() {
            @Override
            public CadruMedical call() {
                if (cadruMedical == null) {
                    return null;
                }

                long id = cadruMedicalDao.insert(cadruMedical);

                if (id == -1) {
                    return null;
                }

                cadruMedical.setId(id);
                return cadruMedical;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insertAll(Callback<List<CadruMedical>> callback, final List<CadruMedical> listaCadreMedicale) {
        Callable<List<CadruMedical>> callable = new Callable<List<CadruMedical>>() {
            @Override
            public List<CadruMedical> call() {
                if (listaCadreMedicale.isEmpty()) {
                    return null;
                }

                long[] ids = cadruMedicalDao.insertAll(listaCadreMedicale);

                for (int i = 0; i < ids.length; i++) {
                    listaCadreMedicale.get(i).setId(ids[i]);
                }

                return listaCadreMedicale;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void update(Callback<CadruMedical> callback, final CadruMedical cadruMedical) {
        Callable<CadruMedical> callable = new Callable<CadruMedical>() {
            @Override
            public CadruMedical call() {
                if (cadruMedical == null) {
                    return null;
                }

                int count = cadruMedicalDao.update(cadruMedical);

                if (count < 1) {
                    return null;
                }

                return cadruMedical;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void delete(Callback<Integer> callback, final CadruMedical cadruMedical) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                if (cadruMedical == null) {
                    return -1;
                }
                return cadruMedicalDao.delete(cadruMedical);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
}
