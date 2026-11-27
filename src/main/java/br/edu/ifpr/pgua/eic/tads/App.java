package br.edu.ifpr.pgua.eic.tads;

import br.edu.ifpr.pgua.eic.tads.controllers.AdminController;
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
        AdminController adminController = new AdminController(authHandler);

        //O método before é utilizado para executar uma ação sempre que
        //uma requisição para a url de parâmetro for recebida.
        //Nesse caso, para toda requisição recebida (visto que a url é /),
        //será verificado se o usuário está logado. Caso não esteja, será
        //redirecionado para a url de login.
        app.before("/",ctx->{
            
            if (!authHandler.isLogged(ctx)){
                ctx.status(403).redirect("/login");
                ctx.skipRemainingHandlers();
            }
        });

        app.get("/", indexController::get);
        app.get("/login", loginController::get);
        app.get("/admin",adminController::get);
        app.post("/login", loginController::login);
        app.get("/logout",loginController::logout);
        app.get("/protegido/{id}",protegidoController::get);
    }
}
