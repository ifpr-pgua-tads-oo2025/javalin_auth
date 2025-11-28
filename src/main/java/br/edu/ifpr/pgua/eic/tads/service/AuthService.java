package br.edu.ifpr.pgua.eic.tads.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.model.User;

public class AuthService {
    
    private static ArrayList<User> users;
    
    static {
        var temp = Arrays.asList(
            new User("Alice", "alice123"),
            new User("Bob", "bob123"),
            new User("Carol", "carol123"),
            new User("Dave", "dave123")
        );
        
        users = new ArrayList<>(temp);
    }


    public Resultado<User> autenticar(String nome, String senha){

        var user = users.stream()
                   .filter(u->u.getNome().equals(nome) 
                            && u.getSenha().equals(senha))
                   .findFirst();

        if(user.isPresent()){
            return Resultado.sucesso("Usuário logado", user.get());
        }

        return Resultado.erro("Erro de login");

    }


}
