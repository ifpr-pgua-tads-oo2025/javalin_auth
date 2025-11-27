package br.edu.ifpr.pgua.eic.tads.controllers;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.model.User;
import br.edu.ifpr.pgua.eic.tads.service.AuthService;
import io.javalin.http.Handler;

public class LoginController {
    
    private AuthService auth;

    public LoginController(AuthService auth){
        this.auth = auth;
    }

    public Handler get = ctx->{
        ctx.render("login.html");
    };

    public Handler login = ctx->{
        String nome = ctx.formParam("nome");
        String senha = ctx.formParam("senha");

        Resultado<User> resultado = auth.autenticar(nome, senha);

        if(resultado.foiSucesso()){
            ctx.sessionAttribute("usuario", resultado.comoSucesso().getObj());
            ctx.redirect("/protegido");
        }else{
            ctx.render("501.html");
        }
        
    };

    public Handler logout = ctx->{
        auth.logout();
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    };

}
