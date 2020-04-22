package com.estados.cuenta.Vista;

import android.app.Application;
import android.content.Context;

import com.estados.cuenta.Pojo.ListItem;

import java.util.ArrayList;


public class GlobalApplication extends Application {

    private static Context appcontext;
    ArrayList<ListItem> cuentas;
    private static GlobalApplication instance;

    public ArrayList<ListItem> getCuentas() {
        return cuentas;
    }

    public GlobalApplication(){}

    public void setCuentas(ArrayList<ListItem> cuentas) {
        this.cuentas = cuentas;
    }

    public static synchronized GlobalApplication getInstance(){
        if(instance==null){
            instance=new GlobalApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appcontext = getApplicationContext();
    }

    public static Context getContext(){
        return  appcontext;
    }

}
