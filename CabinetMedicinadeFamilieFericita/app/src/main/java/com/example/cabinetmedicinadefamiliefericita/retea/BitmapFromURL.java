package com.example.cabinetmedicinadefamiliefericita.retea;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

public class BitmapFromURL implements Callable<Bitmap> {
        //clase utilitare pentru conexiunea la retea

        // face parte din pachetul java.net
        // obiective: 1. se construieste printr-un constructor care primeste ca parametru de intrare un URL sub forma de string
        //            2. verifica daca URL e valid (trimite un ping sa vada daca exista), verifica formatul http:// sau https://
        //            3. deschide o conexiune de tipul HttpURLConnection
    private URL url;
    private HttpURLConnection connection;   // legatura dintre aplicatia noastra mobila si locatia de la URL

        //clase utilitare pentru preluarea informatiilor din retea
    private InputStream inputStream;                // TOATA informatia pe care vrem sa o citim (tot json-ul)

        // adresa de unde preiau informatiile
    private final String urlAddress;

        // constructorul cu parametrul string URL
    public BitmapFromURL(String urlAddress) {
        this.urlAddress = urlAddress;
    }


        // metoda call() face conexiune + preluare
    @Override
    public Bitmap call() {
        try {
            return getBitmapResultFromHttp();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                // toate conexiunile externe/de stream (clase utilitare de tip .net) trebuie inchise (evit memory leak)
            closeConnections();
        }
        return null;
    }


        // metoda extrasa asa: selectez continutul metodei, click dreapta -> Refactor -> Extract Method (CTRL + ALT + M)
    private Bitmap getBitmapResultFromHttp() throws IOException {
            //obiectul url verifica string-ul primit daca e valid. Prin el se obtine o conexiune
            // clasa URL arunca exceptie MalformedURLException
        url = new URL(urlAddress);
            //conexiunea realizata de url
        connection = (HttpURLConnection) url.openConnection();

            //toata informatia se preia prin intermediul conexiunii
        inputStream = connection.getInputStream();

        Bitmap bmp = BitmapFactory.decodeStream(inputStream);

        return bmp;
    }


    private void closeConnections() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
    }
}
