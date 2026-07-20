/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jogo;

import interfaces.Movel;
import personagens.Dinossauro;
import personagens.Player;

/**
 *
 * @author dudur
 */
public class ThreadDinossauro implements Runnable {
    private final Movel movel;
    private final Dinossauro dino;
    private final Player player;
    private final Mapa mapa;
    private final Combate combate;

    private volatile boolean ativo = true;

    public ThreadDinossauro(Movel movel, Player player, Mapa mapa, Combate combate) {
        this.movel = movel;
        this.dino = (Dinossauro) movel;
        this.player = player;
        this.mapa = mapa;
        this.combate = combate;
    }

    public void encerrar() {
        ativo = false;
    }

    public Dinossauro getDino() {
        return dino;
    }

    @Override
    public void run() {
        while (continuarExecutando()) {
            try {
                Thread.sleep(dino.getIntervaloMovimentoMs());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (!continuarExecutando()) {
                return;
            }

            executarPasso();
        }
    }

    private boolean continuarExecutando() {
        return ativo && dino.estaVivo() && player.estaVivo();
    }

    private void executarPasso() {
        int passos = movel.getPassosMovimento();

        for (int passo = 0; passo < passos; passo++) {
            if (!continuarExecutando()) {
                return;
            }

            synchronized (mapa) {
                if (!dino.estaVivo()) {
                    return;
                }

                int[] destino = dino.getEstrategia().calcularProximoPasso(dino, player, mapa);
                if (destino == null) {
                    continue;
                }

                Entidade alvoCelula = mapa.getCelula(destino[0], destino[1]);

                if (alvoCelula == player) {
                    System.out.println("\n" + dino.getNome() + " encontrou o jogador!");
                    int resultado = combate.iniciarCombate(player, dino, mapa);

                    mapa.notificarEncontroComJogador(resultado, dino);

                    if (resultado == Combate.VITORIA) {
                        mapa.matarDinossauro(dino);
                    }
                    return;
                }

                if (alvoCelula == null) {
                    movel.mover(destino[0], destino[1], mapa);
                }
            }
        }
    }
}