package gui;

import itens.Arma;
import java.util.List;
import javax.swing.JOptionPane;
import jogo.Combate;
import jogo.Dado;
import jogo.Entidade;
import jogo.Mapa;
import personagens.Dinossauro;
import personagens.Player;

public class JogoFinal {

    private final Mapa mapa;
    private final Player player;
    private final Dado dado;
    private final FramePrincipal frame;

    private boolean debugAtivo = false;

    public static void main(String[] args){
        new JogoFinal();
    }

    public JogoFinal() {
        int percepcao = escolherDificuldade();

        mapa = new Mapa(10);
        mapa.gerar(percepcao);
        player = mapa.getPlayer();
        dado = new Dado();

        frame = new FramePrincipal(this, mapa, player);
    }

    private int escolherDificuldade() {
        String[] opcoes = {"Fácil (Percepção 3)", "Médio (Percepção 2)", "Difícil (Percepção 1)"};
        int escolha = JOptionPane.showOptionDialog(null, "Escolha a dificuldade:", "Sobrevivência Jurássica",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        switch (escolha) {
            case 1:
                return 2;
            case 2:
                return 1;
            default:
                return 3;
        }
    }

    /* Chamado pelos botões de movimentação do PainelHud. */
    public void moverJogador(char direcao) {
        if (!player.estaVivo()) {
            return;
        }

        Entidade entidade = mapa.moverPlayer(player, direcao);

        String evento = mapa.getUltimoEvento();
        if (evento != null) {
            frame.getPainelHud().adicionarMensagem(evento);
        }

        if (mapa.isDinossauro(entidade)) {
            Dinossauro dino = (Dinossauro) entidade;
            frame.getPainelHud().adicionarMensagem(dino.getNome() + " apareceu na sua frente!");
            int resultado = iniciarCombate(dino);

            if (resultado == Combate.VITORIA) {
                mapa.matarDinossauro(dino);
                mapa.moverPlayer(player, direcao);
                frame.getPainelHud().adicionarMensagem(dino.getNome() + " foi derrotado!");
            } else if (resultado == Combate.DERROTA) {
                atualizarHud();
                frame.getPainelJogo().repaint();
                verificarFimDeJogo();
                return;
            }
        }

        if (player.estaVivo()) {
            Dinossauro atacante = mapa.moverDinossaurosEDetectarColisao(player);
            if (atacante != null) {
                frame.getPainelHud().adicionarMensagem(atacante.getNome() + " encontrou o jogador!");
                int resultado = iniciarCombate(atacante);

                if (resultado == Combate.VITORIA) {
                    mapa.matarDinossauro(atacante);
                    frame.getPainelHud().adicionarMensagem(atacante.getNome() + " foi derrotado!");
                } else if (resultado == Combate.DERROTA) {
                    atualizarHud();
                    frame.getPainelJogo().repaint();
                    verificarFimDeJogo();
                    return;
                }
            }
        }

        atualizarHud();
        frame.getPainelJogo().repaint();
        verificarFimDeJogo();
    }

    /* Chamado pelo botão central de controles (antigo botão vazio). */
    public void alternarDebug() {
        debugAtivo = !debugAtivo;
        frame.getPainelJogo().setDebugAtivo(debugAtivo);
        frame.getPainelHud().atualizarTextoDebug(debugAtivo);
        frame.getPainelJogo().repaint();
    }

    private int iniciarCombate(Dinossauro dino) {
        frame.getPainelHud().adicionarMensagem("Combate contra " + dino.getNome() + " iniciado!");
        frame.getPainelHud().atualizarDinossauro(dino);

        int turno = 1;
        while (player.estaVivo() && dino.estaVivo()) {
            frame.getPainelHud().adicionarMensagem("--- Turno " + turno + " ---");

            executarAtaqueDino(dino);
            if (!player.estaVivo()) {
                break;
            }

            if (turno > 1 && desejaFugir()) {
                if (tentarFugir(dino)) {
                    frame.getPainelHud().limparDinossauro();
                    return Combate.FUGA;
                } else {
                    frame.getPainelHud().adicionarMensagem("Não há posição livre para fugir!");
                }
            }

            int arma = escolherArma();
            executarAtaqueJogador(dino, arma);

            if (dino.estaVivo()) {
                testarEsquivaPosAtaque();
            }

            atualizarHud();
            frame.getPainelHud().atualizarDinossauro(dino);
            turno++;
        }

        frame.getPainelHud().limparDinossauro();

        if (!player.estaVivo()) {
            frame.getPainelHud().adicionarMensagem("Você foi devorado pelos dinossauros... Fim de jogo.");
            return Combate.DERROTA;
        }
        return Combate.VITORIA;
    }

    private void executarAtaqueDino(Dinossauro dino) {
        frame.getPainelHud().adicionarMensagem(dino.getNome() + " ataca!");
        if (!player.esquivar(dado)) {
            dino.atacar(player, 1);
            frame.getPainelHud().adicionarMensagem(dino.getNome() + " acertou o player!");
        } else {
            frame.getPainelHud().adicionarMensagem(player.getNome() + " esquivou!");
        }
    }

    private boolean desejaFugir() {
        int escolha = JOptionPane.showConfirmDialog(frame, "Deseja fugir do combate?",
                "Combate", JOptionPane.YES_NO_OPTION);
        return escolha == JOptionPane.YES_OPTION;
    }

    private boolean tentarFugir(Dinossauro dino) {
        int[][] direcoes = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] dir : direcoes) {
            int novoX = player.getX() + dir[0];
            int novoY = player.getY() + dir[1];
            if (novoX >= 0 && novoX < mapa.getTamanho() && novoY >= 0 && novoY < mapa.getTamanho()
                    && mapa.getCelula(novoX, novoY) == null) {
                player.mover(novoX, novoY, mapa);
                frame.getPainelHud().adicionarMensagem(player.getNome() + " fugiu do combate!");
                return true;
            }
        }
        return false;
    }

    private int escolherArma() {
        List<Arma> armas = player.getInventario().getArmas();
        String[] opcoes = new String[armas.size() + 1];
        opcoes[0] = "Punhos";
        for (int i = 0; i < armas.size(); i++) {
            opcoes[i + 1] = armas.get(i).getNome();
        }

        int escolha = JOptionPane.showOptionDialog(frame, "Escolha sua arma:", "Combate",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);

        if (escolha < 0) {
            escolha = 0;
        }
        return escolha + 1;
    }

    private void executarAtaqueJogador(Dinossauro dino, int arma) {
        int resultadoDado = dado.rolar();
        frame.getPainelHud().adicionarMensagem(player.getNome() + " ataca! Resultado do dado: " + resultadoDado);

        if (arma == 1) {
            player.atacarComPunhos(dino, resultadoDado);
        } else {
            Arma armaEscolhida = player.getInventario().getArmas().get(arma - 2);
            armaEscolhida.usarArma(dino, resultadoDado);
            if (armaEscolhida.isConsumivel()) {
                player.getInventario().removerArma(armaEscolhida);
            }
        }
    }

    private void testarEsquivaPosAtaque() {
        if (player.esquivar(dado)) {
            frame.getPainelHud().adicionarMensagem(player.getNome() + " esquivou do contra-ataque!");
        } else {
            player.tomarDano(1);
            frame.getPainelHud().adicionarMensagem(player.getNome() + " sofreu 1 de dano!");
        }
    }

    private void atualizarHud() {
        frame.getPainelHud().atualizarStatus();
        frame.getPainelHud().atualizarInventario();
    }

    private void verificarFimDeJogo() {
        if (!player.estaVivo()) {
            frame.getPainelHud().desabilitarControles();
            JOptionPane.showMessageDialog(frame, "Você foi devorado pelos dinossauros... FIM DE JOGO.",
                    "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
        } else if (mapa.getListaDinossauros().isEmpty()) {
            frame.getPainelHud().desabilitarControles();
            JOptionPane.showMessageDialog(frame, "Parabéns! Você eliminou todos os dinossauros e sobreviveu!",
                    "Vitória", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
