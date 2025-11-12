package com.example.jogodamemoria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Logica {

    int vidaPlayer;
    int pontuacao;
    ArrayList<Carta> mesa = new ArrayList<>();
    ArrayList<Carta> viradas = new ArrayList<>();

    Stack<Carta> todas = new Stack<>();

    public void carregaTodas() {
        todas.add(new Carta("Maça"));
        todas.add(new Carta("Uva"));
        todas.add(new Carta("Pera"));
        todas.add(new Carta("Kiwi"));
        todas.add(new Carta("Melancia"));
        todas.add(new Carta("Lima"));
        todas.add(new Carta("Laranja"));
        todas.add(new Carta("Ameixa"));
        todas.add(new Carta("Pinha"));
        todas.add(new Carta("Graviola"));
        todas.add(new Carta("Manga"));
        todas.add(new Carta("Banana"));
        todas.add(new Carta("Carambola"));
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
    }




}
