package com.victordev.urban.urbanpty.persistencia;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DatosIniciosesion {
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    boolean sesion = true;

    public DatosIniciosesion(Activity activity) {
        this.activity = activity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
        editor = sharedPreferences.edit();
    }

    public boolean guardarSescion(boolean ini){

        if(ini){
            sesion = true;
        }else {
            sesion = false;
        }
        return sesion;
    }

    public boolean ini(){
        boolean i = true;
        if(sesion){
            i = sesion;
        }else {
            i  = sesion;
        }
        return i;
    }

}
