package com.oslowski.infosmiecilasy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends Activity implements LocationListener {


    private TextView txtLat;
    private TextView txtLong;
    private TextView txtSource;
    private TextView txtDokladnosc;
    private LocationManager locationManager;
    private String provider;
    private static final int CAMERA_REQUEST = 123;
    //private ImageView imageView;

    private static String android_id; //Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    private static double latStatic;
    private static double lngStatic;
    private static float dokladnoscStatic;
    //private static String nazwaZdjecia = "testowanazwazdjecia";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLong = (TextView) findViewById(R.id.txtLong);
        txtSource = (TextView) findViewById(R.id.txtSource);
        txtDokladnosc = (TextView)  findViewById(R.id.txtDokladnosc);
// Initialize locationManager
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, false);


        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //if ( Build.VERSION.SDK_INT >= 23 &&
            //        ContextCompat.checkSelfPermission ( this  , android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
            //        ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    return  ;
            //}
            //return;
        }
        Location location = locationManager.
                getLastKnownLocation(provider);
// Initialize the location
        if (location != null) {
            txtSource.setText("Source = " + provider);
            onLocationChanged(location);
        }

        //imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.zrobZdjecieButton);


        android_id =Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }



    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //return;
        }
        locationManager.requestLocationUpdates
                (provider, 500, 1, this);
    }


    @Override
    public  void onPause()
    {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        float dokladnosc = location.getAccuracy();

        latStatic = lat;
        lngStatic = lng;
        dokladnoscStatic = dokladnosc;
        DecimalFormat formatter = new DecimalFormat("#,###.000");
        String get_value = formatter.format(lat);

        txtLat.setText(formatter.format(lat));
        txtLong.setText(formatter.format(lng));
        txtSource.setText(provider);
        txtDokladnosc.setText(formatter.format(dokladnosc));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        txtSource.setText("Source = " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        txtSource.setText("Source = " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        txtSource.setText("Source = " + provider);
    }


    public void zrobZdjecieOnClick(View view) throws JSONException {
        /*Intent cameraIntent = new Intent
                (android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
        */
        wrzucWspolrzedneNaServer();

    }


    protected void onActivityResult
            (int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            //imageView.setImageBitmap(photo);


            ExecutorService executor = Executors.newFixedThreadPool(1);
            Callable<StringBuilder> watek = new Wysylacz(latStatic, lngStatic, android_id, "", "",new Float(0.0));


            final Future<StringBuilder> result = executor.submit(watek);
            StringBuilder strona = new StringBuilder();
            try {
                strona = result.get();
            } catch (InterruptedException e) {

            } catch (Exception e) {

            }

        }
    }

    private void wrzucWspolrzedneNaServer() throws JSONException {



            ExecutorService executor = Executors.newFixedThreadPool(1);
            Callable<StringBuilder> watek = new Wysylacz(latStatic, lngStatic, android_id,"" , "", dokladnoscStatic);


            final Future<StringBuilder> result = executor.submit(watek);
            StringBuilder strona = new StringBuilder();
            try {
                strona = result.get();
            } catch (InterruptedException e) {

            } catch (Exception e) {

            }


        if (strona.length()>0)

        {
            Context context = getApplicationContext();
            CharSequence komunikat = strona;
            try {
                JSONObject komunikatJson = new JSONObject(strona.toString());
                //JSONObject komunikatJson2 = komunikatJson.getJSONObject("wynik:");
                komunikat = komunikatJson.getString("wynik:").toString();
            }
            catch (JSONException e)
            {
                komunikat = e.getMessage();
            }
            catch (Exception e)
            {
                komunikat = getString(R.string.jakisblad);
            }


            Toast toast = Toast.makeText(context, komunikat, Toast.LENGTH_LONG);
            toast.show();
        }

    }
}
