package com.mio.jrdv.firebasemio;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joseramondelgado on 04/11/16.
 */

public class PADRES {

      private String nombre;
      private String email;
      private String password;
      private String padrefirebaseuid;


    //constructors

    public PADRES() {
    }

    public PADRES(String email, String nombre, String password,String padrefirebaseuid) {
        this.email = email;
        this.nombre = nombre;
        this.padrefirebaseuid = padrefirebaseuid;
        this.password = password;
    }

    //getters (no necesiat los setter)

    public String getEmail() {
        return email;
    }



    public String getNombre() {
        return nombre;
    }

    public String getPadrefirebaseuid() {
        return padrefirebaseuid;
    }



    public String getPassword() {
        return password;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", padrefirebaseuid);
        result.put("email", email);
        result.put("nombre", nombre);
        result.put("password", password);

        return result;
    }



}
