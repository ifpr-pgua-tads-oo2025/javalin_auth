package br.edu.ifpr.pgua.eic.tads.controllers;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.model.User;
import br.edu.ifpr.pgua.eic.tads.service.AuthService;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class LoginController {
    
    private AuthService auth;

    public LoginController(AuthService auth){
        this.auth = auth;
    }

    public void get(Context ctx){
        ctx.render("login.html");
    };

    public void login(Context ctx){
        String nome = ctx.formParam("nome");
        String senha = ctx.formParam("senha");

        Resultado<User> resultado = auth.autenticar(nome, senha);

        if(resultado.foiSucesso()){
            ctx.sessionAttribute("usuario", resultado.comoSucesso().getObj());
            ctx.redirect("/protegido/10");
        }else{
            ctx.render("501.html");
        }
        
    };

    public void logout(Context ctx){
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    };

}
