package com.oslowski.infosmiecilasy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.net.URL;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by Piotrek on 2017-04-15.
 */

public class Wysylacz implements Callable<StringBuilder> {

    private final String USER_AGENT = "Mozilla/5.0";
    private Double lat;
    private Double lng;
    private String android_id;
    private String nazwaZdjecia;
    private String sieczka;

    public Wysylacz(Double lat, Double lng, String android_id, String nazwaZdjecia, String sieczka)
    {
        this.lat = lat;
        this.lng = lng;
        this.android_id = android_id;
        this.nazwaZdjecia = nazwaZdjecia;
        this.sieczka = sieczka;
    }



    private StringBuilder sendGet() throws Exception {

        String url = "http://31.14.136.242:5000/dodaj?lat="+lat+"&lng="+lng+"&android_id="+android_id+"&nazwaZdjecia="+nazwaZdjecia+"&sieczka="+sieczka;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        //System.out.println(response.toString());
        StringBuilder wynik = new StringBuilder();
        wynik.append(response);
        return wynik;
    }


    private StringBuilder wyslij() throws Exception {
        if ((lat!=0) && (lng!=0))
        {
            return sendGet();
        }
        return new StringBuilder();
    }

    @Override
    public StringBuilder call() throws Exception {

        StringBuilder wynik =  wyslij();


        return wynik;
    }
}
