package com.example.cabinetmedicinadefamiliefericita.retea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

public class HttpManager implements Callable<String> {
        //clase utilitare pentru conexiunea la retea

        // face parte din pachetul java.net
        // obiective: 1. se construieste printr-un constructor care primeste ca parametru de intrare un URL sub forma de string
        //            2. verifica daca URL e valid (trimite un ping sa vada daca exista), verifica formatul http:// sau https://
        //            3. deschide o conexiune de tipul HttpURLConnection
    private URL url;
    private HttpURLConnection connection;   // legatura dintre aplicatia noastra mobila si locatia de la URL

        //clase utilitare pentru preluarea informatiilor din retea
    private InputStream inputStream;                // TOATA informatia pe care vrem sa o citim (tot json-ul)
        // NU citesc totul deodata ca sa evit timeout. Folosesc InputStreamReader (subdiviziune a lui InputStream)
    private InputStreamReader inputStreamReader;
        // folosesc BufferedReader (zona tampon), daca InputStreamReader este prea mare si el
        // prin el aduc informatie cu informatie usor-usor (la cateva milisecunde) a.i. SO sa nu dea timeout, vede ca procesul nu e blocat
    private BufferedReader bufferedReader;


        // adresa de unde preiau informatiile
    private final String urlAddress;

        // constructorul cu parametrul string URL
    public HttpManager(String urlAddress) {
        this.urlAddress = urlAddress;
    }


        // metoda call() face conexiune + preluare
    @Override
    public String call() {
        try {
            return getResultFromHttp();
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
    private String getResultFromHttp() throws IOException {
            //obiectul url verifica string-ul primit daca e valid. Prin el se obtine o conexiune
            // clasa URL arunca exceptie MalformedURLException
        url = new URL(urlAddress);
            //conexiunea realizata de url
        connection = (HttpURLConnection) url.openConnection();

            //toata informatia se preia prin intermediul conexiunii
        inputStream = connection.getInputStream();
            //impart informatia in bucati mai mici
        inputStreamReader = new InputStreamReader(inputStream);
            //fiecare buacta este la randul ei impartita in bucati mai mici si incarcate intr-o zona tampon din memorie
        bufferedReader = new BufferedReader(inputStreamReader);

            // StringBuilder permite concatenarea mai multor string-uri prin metoda append(string)
        StringBuilder result = new StringBuilder();
        String line;
            //citesc linie cu linie informatiile din zona tampon (bufferedReader)
        while ((line = bufferedReader.readLine()) != null) {    // cat timp mai am de citit linii din bufferedReader
            //concatenez linia citita intr-un StringBuilder
            result.append(line);
        }
        return result.toString();
    }


    private void closeConnections() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
    }
}
