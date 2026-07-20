package jogo;

import interfaces.CombateIO;
import itens.Arma;
import personagens.Player;
import personagens.Dinossauro;

import java.util.List;

public class Combate {

    public static final int DERROTA = 0;
    public static final int VITORIA = 1;
    public static final int FUGA = 2;

    private final CombateIO io;
    private final Dado dado;

    public Combate(CombateIO io) {
        this.io = io;
        this.dado = Dado.getInstance();
    }

    public synchronized int iniciarCombate(Player player, Dinossauro dino, Mapa mapa) {
        int turno = 1;

        io.log("Combate contra " + dino.getNome() + " iniciado!");
        io.atualizarDinoStats(dino.getNome(), dino.getVida(), dino.getVidaMaxima());

        while (player.estaVivo() && dino.estaVivo()) {
            io.log("-".repeat(20) + " Turno " + turno + " " + "-".repeat(20));
            executarAtaque(dino, player);

            if (!player.estaVivo()) {
                break;
            }

            if (turno > 1 && io.desejaFugir()) {
                if (tentarFugir(player, dino, mapa)) {
                    io.limparDinoStats();
                    return FUGA;
                } else {
                    io.log("Não há posição livre para fugir!");
                }
            }

            int armaEscolhida = menuEscolhaArma(player);
            executarAtaqueJogador(player, dino, armaEscolhida);

            if (dino.estaVivo()) {
                testarEsquivaPosAtaque(player);
            }

            io.log(dino.getNome() + " " + dino.getVida() + "/" + dino.getVidaMaxima());
            io.log(player.getNome() + " " + player.getVida() + "/" + player.getVidaMaxima());
            io.atualizarDinoStats(dino.getNome(), dino.getVida(), dino.getVidaMaxima());
            turno++;
        }

        io.limparDinoStats();

        if (!player.estaVivo()) {
            return DERROTA;
        }
        return VITORIA;
    }

    private boolean tentarFugir(Player player, Dinossauro dino, Mapa mapa) {
        int[][] direcoes = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : direcoes) {
            int novoX = player.getX() + dir[0];
            int novoY = player.getY() + dir[1];
            if (novoX >= 0 && novoX < mapa.getTamanho() && novoY >= 0 && novoY < mapa.getTamanho()
                    && mapa.getCelula(novoX, novoY) == null) {
                player.mover(novoX, novoY, mapa);
                io.log(player.getNome() + " fugiu do combate!");
                return true;
            }
        }
        return false;
    }

    private void executarAtaqueJogador(Player atacante, Dinossauro alvo, int arma) {
        int resultadoDado = dado.rolar();
        io.log(atacante.getNome() + " ataca!\n" + "Resultado do dado: " + resultadoDado);

        if (arma == 1) {
            atacante.atacarComPunhos(alvo, resultadoDado);
        } else {
            List<Arma> armas = atacante.getInventario().getArmas();
            int indice = arma - 2;
            if (indice < 0 || indice >= armas.size()) {
                throw new IllegalArgumentException("Arma não existe");
            }
            Arma armaEscolhida = armas.get(indice);
            armaEscolhida.usarArma(alvo, resultadoDado);
            if (armaEscolhida.isConsumivel()) {
                atacante.getInventario().removerArma(armaEscolhida);
            }
        }
    }

    private void testarEsquivaPosAtaque(Player player) {
        if (player.esquivar(dado)) {
            io.log(player.getNome() + " esquivou do contra-ataque!");
        } else {
            player.tomarDano(1);
            io.log(player.getNome() + " sofreu 1 de dano!");
        }
    }

    private void executarAtaque(Dinossauro atacante, Player alvo) {
        io.log(atacante.getNome() + " ataca!");

        if (!alvo.esquivar(dado)) {
            atacante.atacar(alvo, 1);
            io.log(atacante.getNome() + " acertou o player!");
        } else {
            io.log(alvo.getNome() + " esquivou!");
        }
    }

    private int menuEscolhaArma(Player player) {
        List<Arma> armas = player.getInventario().getArmas();
        String[] nomes = new String[armas.size()];
        for (int i = 0; i < armas.size(); i++) {
            nomes[i] = armas.get(i).getNome();
        }
        return io.escolherArma(armas.size(), nomes);
    }
}