package com.example.cabinetmedicinadefamiliefericita.asyncTask;

public interface Callback<R> {
    // Callback : bucata de cod implementata in activitatea principala unde primim rezultatul
    void runResultOnUiThread(R result);
}
