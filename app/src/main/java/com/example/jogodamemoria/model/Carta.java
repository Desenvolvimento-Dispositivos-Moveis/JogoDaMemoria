package com.example.jogodamemoria.model;

public class Carta {
    private String img;
    private boolean estado;
    private boolean encontrada;

    public Carta(String img) {
        this.img = img;
        this.estado = false;
        this.encontrada = false;
    }

    public void virarCarta(){
        if (!encontrada){
            estado = true;
        }
    }
    public void desvirarCarta(){
        if (!encontrada){
            estado = false;
        }
    }
    public void marcarComoEncontrada(){
        encontrada=true;
    }

    public boolean isEncontrada() {
        return encontrada;
    }

    public boolean isEstado() {
        return estado;
    }

    public String getImg() {
        return img;
    }
}
