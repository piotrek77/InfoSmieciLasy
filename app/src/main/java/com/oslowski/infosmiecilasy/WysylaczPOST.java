package com.oslowski.infosmiecilasy;

import android.graphics.Bitmap;

import java.util.concurrent.Callable;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
/**
 * Created by Piotrek on 2017-04-18.
 */

public class WysylaczPOST implements Callable<StringBuilder> {


    private Bitmap bitmapa;


    public WysylaczPOST(Bitmap bitmapa)
    {
        this.bitmapa = bitmapa;
    }




    private void wyslij()
    {

    }

    @Override
    public StringBuilder call() throws Exception {
        wyslij();
        return null;
    }
}
