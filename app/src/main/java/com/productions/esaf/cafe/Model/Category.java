package com.productions.esaf.cafe.Model;

public class Category {
    private String Imagem;
    private String Name;

    public Category() {
    }

    public Category(String imagem, String name) {
        Imagem = imagem;
        Name = name;
    }

    public String getImagem() {
        return Imagem;
    }

    public void setImagem(String imagem) {
        Imagem = imagem;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
