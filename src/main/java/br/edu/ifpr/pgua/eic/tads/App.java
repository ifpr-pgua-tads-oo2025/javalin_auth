package br.edu.ifpr.pgua.eic.tads;

import br.edu.ifpr.pgua.eic.tads.controllers.IndexController;
import br.edu.ifpr.pgua.eic.tads.controllers.LoginController;
import br.edu.ifpr.pgua.eic.tads.controllers.ProtegidoController;
import br.edu.ifpr.pgua.eic.tads.model.User;
import br.edu.ifpr.pgua.eic.tads.service.AuthHandler;
import br.edu.ifpr.pgua.eic.tads.service.AuthService;
import br.edu.ifpr.pgua.eic.tads.utils.JavalinUtils;
import io.javalin.Javalin;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        var app = JavalinUtils.makeApp(7070);
        
        AuthService auth = new AuthService();
        AuthHandler authHandler = new AuthHandler(auth);
        IndexController indexController = new IndexController();
        LoginController loginController = new LoginController(auth);
        ProtegidoController protegidoController = new ProtegidoController();

        app.before("/",ctx->{
            
            if (!authHandler.isLogged(ctx)){
                ctx.status(403).redirect("/login");
                ctx.skipRemainingHandlers();
            }
        });

        app.get("/", indexController.get);
        app.get("/login", loginController.get);
        app.post("/login", loginController.login);
        app.get("/logout",loginController.logout);
        app.get("/protegido/{id}",protegidoController.get);
    }
}
