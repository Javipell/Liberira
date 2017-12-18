package com.javi.pell.liberia7;

import android.app.Application;

/**
 * Created by javier on 17/12/17.
 */

public class Globales extends Application
{
    private String descargado ="0";
    private String ascendente ="ASC";

    public String getDescargado() {
        return descargado;
    }

    public void setDescargado(String descargado) {
        this.descargado = descargado;
    }

    public String getAscendente() {
        return ascendente;
    }

    public void setAscendente(String ascendente) {
        this.ascendente = ascendente;
    }
}
