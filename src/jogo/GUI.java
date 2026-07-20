package jogo;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import gui.FramePrincipal;
import personagens.Player;

public class GUI {

    public static void iniciarJogo() {
        SwingUtilities.invokeLater(GUI::exibirMenuPrincipal);
    }

    private static void exibirMenuPrincipal() {
        Object[] opcoes = {"Jogar", "Sair"};
        int escolha = JOptionPane.showOptionDialog(
                null,
                "=================================================\n" +
                "   Bem-vindo a SOBREVIVÊNCIA JURÁSSICA!\n" +
                "=================================================",
                "Menu Principal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha != 0) {
            JOptionPane.showMessageDialog(null, "Obrigado por jogar!");
            return;
        }

        int percepcao = exibirMenuDificuldade();
        jogarPartida(percepcao);
    }

    private static int exibirMenuDificuldade() {
        Object[] opcoes = {
            "Fácil (Percepção 3)",
            "Médio (Percepção 2)",
            "Difícil (Percepção 1)"
        };

        int escolha = JOptionPane.showOptionDialog(
                null,
                "Escolha a dificuldade:",
                "Dificuldade",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        switch (escolha) {
            case 1:
                return 2;
            case 2:
                return 1;
            default:
                return 3; // Fácil por padrão (inclui janela fechada)
        }
    }

    private static void jogarPartida(int percepcao) {
        Combate combate = new Combate();
        Mapa mapa = Mapa.getInstance();
        mapa.gerar(percepcao); // também limpa o estado da partida anterior

        Player player = mapa.getPlayer();

        FramePrincipal.getInstance().iniciarPartida(mapa, player, GUI::exibirResultadoFinal);

        mapa.iniciarThreadsDinossauros(player, combate);
    }

    private static void exibirResultadoFinal(Mapa mapa, Player player) {
        mapa.pararThreadsDinossauros();

        String mensagem;
        if (!player.estaVivo()) {
            mensagem = "Você foi devorado pelos dinossauros... FIM DE JOGO.";
        } else if (mapa.getListaDinossauros().isEmpty()) {
            mensagem = "Parabéns! Você eliminou todos os dinossauros e sobreviveu!";
        } else {
            mensagem = "Você saiu do parque.";
        }

        JOptionPane.showMessageDialog(null, mensagem, "Fim de Partida",
                JOptionPane.INFORMATION_MESSAGE);

        exibirMenuPrincipal();
    }
}