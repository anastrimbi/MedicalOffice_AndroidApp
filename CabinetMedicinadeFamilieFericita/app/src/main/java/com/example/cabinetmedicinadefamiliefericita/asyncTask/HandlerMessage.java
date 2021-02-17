package com.example.cabinetmedicinadefamiliefericita.asyncTask;

public class HandlerMessage<R> implements Runnable {
    // implementeaza Runnable ca sa putem sa o folosim la nivel de Handler
    // parametrii de intrare: rezultatul R + destinatia din activitatea principala (referinta Callback)
    //este primit din RunnableTask dupa ce a fost primit din thread-ul principal (activitate/fragment)
    private final Callback<R> mainThreadOperation;  // este final, fiindca odata ce am creat un mesaj,
                                                    // nu ii mai permit sa isi schimbe referinta
                                                    // am creat un mesaj cu metoda x din MainActivity, nu o mai schimb
    //este primit din RunnableTask (rezultatul metodei call())
    private final R result;     // odata ce am trimis niste informatii catre mesaj, nu le mai schimb

    public HandlerMessage(Callback<R> mainThreadOperation, R result) {
        this.mainThreadOperation = mainThreadOperation;
        this.result = result;
    }

    // Runnable ne obliga sa implementam metoda run()
    @Override
    public void run() {
        //se trimite rezultatul in activitate/fragment
        mainThreadOperation.runResultOnUiThread(result);
    }
}