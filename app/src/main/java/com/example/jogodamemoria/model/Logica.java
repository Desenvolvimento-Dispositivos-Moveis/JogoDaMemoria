package com.example.jogodamemoria.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Logica {

    private int estadoJogo = 0;
    private int vidaPlayer;

    private int tentativas;

    private final ArrayList<Carta> mesa = new ArrayList<>();
    private final ArrayList<Carta> viradas = new ArrayList<>();
    private final Stack<Carta> todas = new Stack<>();

    private final int MAX_VIDAS = 3;

    private final List<String> COMPONENTES = Arrays.asList(
            "chip", "hd", "memoria-ram", "monitor", "pc", "placa-de-video", "placa-mae"
    );

    public Logica() {
        carregaTodas();
    }

    public void carregaTodas() {
        todas.clear();
        for (String nome : COMPONENTES) {
            todas.add(new Carta(nome));
        }
    }

    public void iniciarJogo(int dificuldade, boolean reset) {
        viradas.clear();
        tentativas = 0;
        vidaPlayer = MAX_VIDAS;

        if (todas.isEmpty() || todas.size() < dificuldade) {
            carregaTodas();
        }

        Collections.shuffle(todas);

        for (int i = 0; i < dificuldade; i++) {
            if (todas.isEmpty()) break;

            Carta topo = todas.pop();

            mesa.add(new Carta(topo.getImg()));
            mesa.add(new Carta(topo.getImg()));
        }

        Collections.shuffle(mesa);
        estadoJogo = 1;
    }

    public void aoClicarNaCarta(int indexCartaClicada) {
        Carta cartaClicada = mesa.get(indexCartaClicada);
        if (cartaClicada.isEncontrada() || viradas.size() == 2) {
            return;
        }

        if (viradas.contains(cartaClicada)) {
            return;
        }

        viradas.add(cartaClicada);
        cartaClicada.virarCarta();
        if (viradas.size() == 1) {
            return;
        }
    }

    public boolean verificaCombinacao() {
        if (viradas.size() < 2) return false;

        Carta cartaVirada1 = viradas.get(0);
        Carta cartaVirada2 = viradas.get(1);
        if (cartaVirada1.getImg().equals(cartaVirada2.getImg())) {
            cartaVirada1.marcarComoEncontrada();
            cartaVirada2.marcarComoEncontrada();

            if (mesa.stream().allMatch(Carta::isEncontrada)) {
                estadoJogo = 3;
            }
            viradas.clear();
            return true;
        } else {
            vidaPlayer--;
            tentativas++;
            if (vidaPlayer <= 0) {
                estadoJogo = 2;
            }
            return false;
        }
    }

    public void desvirarViradas() {
        if (viradas.size() == 2) {
            viradas.get(0).desvirarCarta();
            viradas.get(1).desvirarCarta();
        }
        viradas.clear();
    }

    public void virarTodasCartas() {
        for (Carta carta : mesa) {
            carta.virarCarta();
        }
    }

    public void desvirarTodasCartas() {
        for (Carta carta : mesa) {
            carta.desvirarCarta();
        }
    }

    public ArrayList<Carta> getMesa() {
        return mesa;
    }

    public int getTentativas() {
        return tentativas;
    }

    public int getEstadoJogo() {
        return estadoJogo;
    }

    public int getVidaPlayer() {
        return vidaPlayer;
    }

    public int getTotalPares() {
        return mesa.size() / 2;
    }

    public int getParesEncontrados() {
        long found = mesa.stream().filter(Carta::isEncontrada).count();
        return (int) (found / 2);
    }

    public int getNumViradas() {
        return viradas.size();
    }

    public void setEstadoJogo(int estadoJogo) {
        this.estadoJogo = estadoJogo;
    }

    public void setVidaPlayer(int vidaPlayer) {
        this.vidaPlayer = vidaPlayer;
    }

}