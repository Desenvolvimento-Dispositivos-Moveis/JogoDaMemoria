package com.example.jogodamemoria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Logica {

    private int estadoJogo = 0;
    private int vidaPlayer;
    private int pontuacao;
    ArrayList<Carta> mesa = new ArrayList<>();
    ArrayList<Carta> viradas = new ArrayList<>();

    Stack<Carta> todas = new Stack<>();

    public void carregaTodas() {
        todas.add(new Carta("chip"));
        todas.add(new Carta("hd"));
        todas.add(new Carta("memoria-ram"));
        todas.add(new Carta("monitor"));
        todas.add(new Carta("pc"));
        todas.add(new Carta("placa-de-video"));
        todas.add(new Carta("placa-mae"));
    }

    public void iniciarJogo(int dificuldade, boolean reset){
        mesa.clear();
        viradas.clear();
        pontuacao =0;
        vidaPlayer=3;
        Collections.shuffle(todas);

        for (int i = 0; i < dificuldade; i++) {
            Carta topo = todas.pop();
            for (int j = 0; j < 2; j++) {
                mesa.add(new Carta(topo.getImg()));
            }
        }
        Collections.shuffle(mesa);
        estadoJogo = 1;
    }

    public void aoClicarNaCarta(int indexCartaClicada){
        Carta cartaClicada = mesa.get(indexCartaClicada);
        if (cartaClicada.isEncontrada() || viradas.size()==2 ){
            return;
        }
        viradas.add(cartaClicada);
        cartaClicada.virarCarta();
        if (viradas.size() ==1){
            return;
        }
        if (viradas.size()==2){
            verificaCombinacao();
        }
    }

    public void verificaCombinacao() {
        Carta cartaVirada1 = viradas.get(0);
        Carta cartaVirada2 = viradas.get(1);
            if (cartaVirada1.getImg().equals(cartaVirada2.getImg())) {
                cartaVirada1.marcarComoEncontrada();
                cartaVirada2.marcarComoEncontrada();
                pontuacao+=50;
                if (mesa.stream().allMatch(Carta::isEncontrada)){
                    estadoJogo = 3;
                }
            }else {
                cartaVirada1.desvirarCarta();
                cartaVirada2.desvirarCarta();
                vidaPlayer--;
                if (vidaPlayer<=0){
                    estadoJogo = 2;
                }
            }
            viradas.clear();


    }

    public int getEstadoJogo() {
        return estadoJogo;
    }

    public void setEstadoJogo(int estadoJogo) {
        this.estadoJogo = estadoJogo;
    }

    public int getVidaPlayer() {
        return vidaPlayer;
    }

    public void setVidaPlayer(int vidaPlayer) {
        this.vidaPlayer = vidaPlayer;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }
}
