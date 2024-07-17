package com.example.sarajevolocations;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

public class HardMap extends AppCompatActivity implements OnMapReadyCallback {

    RelativeLayout overlay;
    ImageView slika;
    TextView bodovi;
    HardImages[] slike = new HardImages[5];
    GoogleMap map;
    LatLng pokusaj;
    Button potvrda,dalje,start,nazad;
    double distanca;
    int rezultat=0,idslike=0;
    private static final String TAG = "test";

    LatLngBounds sarajevo = new LatLngBounds(
            new LatLng(43.80151341424579, 18.306542557954888), // SW bounds
            new LatLng(43.896841040339034, 18.469727456407142)  // NE bounds
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_map);

        View layout = findViewById(R.id.hard_layout);
        layout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

        slika = findViewById(R.id.hard_image);
        potvrda = findViewById(R.id.potvrdi);
        dalje = findViewById(R.id.dalje);
        start = findViewById(R.id.start);
        overlay = findViewById(R.id.overlay);
        nazad = findViewById(R.id.nazad);
        bodovi = findViewById(R.id.bodovi);

        potvrda.setVisibility(View.GONE);
        dalje.setVisibility(View.GONE);
        slika.setVisibility(View.GONE);
        slika.animate().translationY(1000);

        slike[0] = new HardImages(R.drawable.hard_tunelspasa,43.819775794129946, 18.33728533435195);
        slike[1] = new HardImages(R.drawable.hard_bistricka, 43.85269073929638, 18.433415718121278);
        slike[2] = new HardImages(R.drawable.hard_kapija,43.861894438939295, 18.445795370322745);
        slike[3] = new HardImages(R.drawable.hard_vrace,43.84445723225454, 18.3994659319477);
        slike[4] = new HardImages(R.drawable.hard_sejhovakorija,43.85776971216241, 18.450045408194136);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sarajevo.getCenter(), 11));


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setVisibility(View.GONE);
                pogadjaj(idslike);
            }
        });


    }

    public void pogadjaj(int id)
    {
        dalje.setVisibility(View.GONE);
        slika.setVisibility(View.VISIBLE);
        slika.animate().translationY(0);
        LatLng start = new LatLng(slike[id].getX(), slike[id].getY());
        slika.setImageResource(slike[id].getIme());
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                //allPoints.add(point);
                map.clear();
                map.addMarker(new MarkerOptions()
                        .position(point)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                distanca = SphericalUtil.computeDistanceBetween(start, point);
                pokusaj = point;
                potvrda.setVisibility(View.VISIBLE);
            }

        });

        potvrda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                potvrda.setVisibility(View.GONE);
                dalje.setVisibility(View.VISIBLE);
                int x =(int)(100*Math.pow(0.998036,distanca))+1;
                rezultat+=x;
                Toast.makeText(HardMap.this,"Osvojeni bodovi:"+x, Toast.LENGTH_LONG).show();
                slika.animate().translationY(1000);
                //slika.setVisibility(View.GONE);
                map.addMarker(new MarkerOptions()
                        .position(start));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(start,19));
                map.addPolyline((new PolylineOptions()).add(start, pokusaj).
                        width(5)
                        .color(Color.RED));
            }
        });

        dalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id<4) {
                    map.clear();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(sarajevo.getCenter(), 11));
                    idslike++;
                    pogadjaj(idslike);

                }
                else{

                    map.clear();
                    dalje.setVisibility(View.GONE);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(sarajevo.getCenter(), 11));
                    bodovi.setText(String.valueOf(rezultat));
                    overlay.setVisibility(View.VISIBLE);

                }
            }
        });

        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HardMap.this, MainActivity.class));
                finish();
            }
        });

    }

}
