package br.edu.ifpr.pgua.eic.tads.controllers;

import java.util.HashMap;
import java.util.Map;

import br.edu.ifpr.pgua.eic.tads.model.User;
import io.javalin.http.Handler;

public class ProtegidoController {
 
    
    public Handler get = ctx->{
        
        int id = Integer.valueOf(ctx.pathParam("id"));

        User user = ctx.sessionAttribute("usuario");
        
        Map<String,Object> dados = new HashMap<>();

        dados.put("usuario", user);
        dados.put("id",id);

        ctx.render("protegido.html", dados);

    };

}
