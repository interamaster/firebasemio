package com.mio.jrdv.firebasemio;

/**
 * Created by joseramondelgado on 04/11/16.
 */

public class HIJOS {
      String mipadrees;
      String elemaildemipadrees;
      String elpassworddemipadrees;
      String nombreHijo;
      String firebaseuid;

    //constructor
    public HIJOS() {
    }

    public HIJOS(String elemaildemipadrees, String elpassworddemipadrees, String mipadrees, String nombreHijo, String firebaseuid) {
        this.elemaildemipadrees = elemaildemipadrees;
        this.elpassworddemipadrees = elpassworddemipadrees;
        this.firebaseuid = firebaseuid;
        this.mipadrees = mipadrees;
        this.nombreHijo = nombreHijo;
    }

    //getter  no neceiata setters firebase


    public String getElemaildemipadrees() {
        return elemaildemipadrees;
    }

    public String getElpassworddemipadrees() {
        return elpassworddemipadrees;
    }

    public String getFirebaseuid() {
        return firebaseuid;
    }

    public String getMipadrees() {
        return mipadrees;
    }

    public String getNombreHijo() {
        return nombreHijo;
    }
}
