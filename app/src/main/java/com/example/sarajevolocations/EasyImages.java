package com.example.sarajevolocations;

import androidx.appcompat.app.AppCompatActivity;

public class EasyImages extends AppCompatActivity {

    int ime;
    double x, y;

    public EasyImages(int ime1, double x1, double y1){
        ime=ime1;
        x=x1;
        y=y1;

    }

    public int getIme()
    {
        return ime;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }
}
