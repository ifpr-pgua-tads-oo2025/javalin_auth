package br.edu.ifpr.pgua.eic.tads.model;

public class User {
    
    private int id;
    private String nome;
    private String senha;
    
    public User(int id, String nome, String senha) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    public User(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
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
