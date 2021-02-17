package com.example.cabinetmedicinadefamiliefericita.asyncTask;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.Callable;

//aceasta clasa implementeaza Runnable pentru a o considera un Thread paralel cu thread-ul principal
//este de tip template pentru ca nu stim tipul rezultatului pe care operatia asincrona il va returna
// scop: face procesare paralela de la Callable
public class RunnableTask<R> implements Runnable {
    // pe Handler postez mesajul de tip HandlerMessage (primit de la thread-ul principal - activitate/fragment)
    private final Handler handler;

    // referinta la zona de cod/metoda pe care o procesez asincron (primita de la thread-ul principal - activitate/fragment)
    // am nevoie de o variabila Callable (la executie poate fi o clasa care implementeaza Callable) ca sa apelez metoda call()
    private final Callable<R> asyncOperation;

    // Callback nu il pot extrage din variabilele celelalte, deci il pun ca parametru de intrare in constructor
    private final Callback<R> mainThreadOperation;

    public RunnableTask(Handler handler, Callable<R> asyncOperation, Callback<R> mainThreadOperation) {
        this.handler = handler;
        this.asyncOperation = asyncOperation;
        this.mainThreadOperation = mainThreadOperation;
    }

    @Override
    public void run() {
        try {
            // apelam metoda operatiei asincrone din Callable, care se executa pe RunnableTask (thread-ul paralel)
            //in exemplul din seminarul 7, call() realizeaza executia metodei cu acelasi nume din clasa HttpManager
            final R result = asyncOperation.call();
            //rezultatul obtinut mai sus este postat pe handler prin intermediul unui obiect HandlerMessage
            //Scopul acestui apel de post este de a notifica Handler-ul
            //ca dorim sa trimitem rezultatul R spre Callback (bucata de cod referita de mainThreadOperation (care este implementata in MainActivity))
            handler.post(new HandlerMessage<>(mainThreadOperation, result));
        } catch (Exception e) {
            Log.i("RunnableTask", "failed call RunnableTask" + e.getMessage());
        }
    }
}