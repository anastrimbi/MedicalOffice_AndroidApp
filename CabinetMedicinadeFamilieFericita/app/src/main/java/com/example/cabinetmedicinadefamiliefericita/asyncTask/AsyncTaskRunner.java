package com.example.cabinetmedicinadefamiliefericita.asyncTask;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncTaskRunner {
    //Handler-ul este obiectul care 'vegheaza' asupra firului principal de executie (activitate/fragment)
    //AsyncTask pastreaza o referinta catre Handler a.i. sa putem trimite mesaje din alte thread-uri si sa vedem starea main thread-ului
    // Looper.getMainLooper = referinta catre thread-ul principal
    private final Handler handler = new Handler(Looper.getMainLooper());

    // toate thread-urile paralele le execut in Executor
    //Executor este responsabil cu gestionarea thread-urilor, decide momentul oportun de a porni un thread (apelare: metoda start)
    // Executors = clasa utilitara care stie sa creeze obiecte Executor
    // newCachedThreadPool() = metoda statica care calculeaza cate thread-uri pot rula in paralel si creeaza un Executor
    // care stie sa ruleze x thread-uri in paralel, dupa o analiza in memorie (RAM etc)
    private final Executor executor = Executors.newCachedThreadPool();


    // parametrii pe care ii trimite sunt: operatiile pe care le execut asincron + zona din activitatea principala unde primesc rezultatul
    public <R> void executeAsync(Callable<R> asyncOperation, Callback<R> mainThreadOperation) {
        try {
            // deleg executorul sa porneasca un nou thread RunnableTask cu 3 parametri: handler, asyncOperation, mainThreadOperation
            //ii specificam Executor-ului ca dorim sa procesam un Thread de tipul clasei RunnableTask
            executor.execute(new RunnableTask<>(handler, asyncOperation, mainThreadOperation));
        } catch (Exception ex) {
            Log.i("AsyncTaskRunner", "failed call executeAsync " + ex.getMessage());
        }
    }
}