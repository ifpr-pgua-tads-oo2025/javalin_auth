package br.edu.ifpr.pgua.eic.tads.model;

public class User {
    
    private int id;
    private String nome;
    private String senha;
    private Role papel;
    
    public User(int id, String nome, String senha) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    public User(String nome, String senha, Role papel) {
        this.nome = nome;
        this.senha = senha;
        this.papel = papel;
    }

    public Role getPapel(){
        return papel;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    @Override
    public String toString() {
        return "Nome:"+nome;
    }
    

}
