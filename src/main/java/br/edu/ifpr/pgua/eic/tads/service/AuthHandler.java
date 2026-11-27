package br.edu.ifpr.pgua.eic.tads.service;

import br.edu.ifpr.pgua.eic.tads.model.Role;
import br.edu.ifpr.pgua.eic.tads.model.User;
import io.javalin.http.Context;

public class AuthHandler {
   
    private AuthService authService;

    public AuthHandler(AuthService authService){
        this.authService = authService;
    }


    //verifica se o usuário está logado no sistema
    //olhando para a variável de sessão usuario
    public boolean isLogged(Context ctx){

        User user = ctx.sessionAttribute("usuario");
        System.out.println(user);
        return user!=null; 
    }


    //verifica se um usuário logado no sistema
    //possui o papel de administrador
    public boolean isAdmin(Context ctx){
        User user = ctx.sessionAttribute("usuario");

        if(user!=null){
            System.out.println(user.getPapel());
            return user.getPapel() == Role.ADMIN;
        }
        return false;
    }


}
