package br.edu.ifpr.pgua.eic.tads.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.model.Role;
import br.edu.ifpr.pgua.eic.tads.model.User;

//classe para simular a persistência de dados
//para a autenticação.
public class AuthService {
    
    private static ArrayList<User> users;
    
    static {
        var temp = Arrays.asList(
            new User("Alice", "alice123",Role.ADMIN),
            new User("Bob", "bob123",Role.USER),
            new User("Carol", "carol123",Role.USER),
            new User("Dave", "dave123",Role.USER)
        );
        
        users = new ArrayList<>(temp);
    }

    //verifica se o nome e a senha do usuário estão corretas
    public Resultado<User> autenticar(String nome, String senha){
        /* 
        for(User u:users){
            if(u.getNome().equals(nome) && u.getSenha().equals(senha)){
                return Resultado.sucesso("Usuário logado", u);
            }
        }
        return Resultado.erro("Erro de login!");
        */


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
