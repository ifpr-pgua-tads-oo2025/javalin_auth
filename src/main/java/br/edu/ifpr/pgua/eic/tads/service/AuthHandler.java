package br.edu.ifpr.pgua.eic.tads.service;

import br.edu.ifpr.pgua.eic.tads.model.User;
import io.javalin.http.Context;

public class AuthHandler {
   
    private AuthService authService;

    public AuthHandler(AuthService authService){
        this.authService = authService;
    }

    public boolean isLogged(Context ctx){

        User user = ctx.sessionAttribute("usuario");
        System.out.println(user);
        return user!=null; 
    }


}
