package br.edu.ifpr.pgua.eic.tads.controllers;

import br.edu.ifpr.pgua.eic.tads.service.AuthHandler;
import io.javalin.http.Context;

public class AdminController {
    
    private AuthHandler authHandler;

    public AdminController(AuthHandler  authHandler){
        this.authHandler = authHandler;
    }

    //decide o que renderizar baseado no papel do usuário logado
    public void get(Context ctx){
        if(authHandler.isAdmin(ctx)){
            ctx.render("admin.html");
        }else{
            ctx.render("501.html");
        }
        
        
    }


}
