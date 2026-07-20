package jogo;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import gui.FramePrincipal;
import personagens.Player;

public class GUI {

    public static void iniciarJogo() {
        SwingUtilities.invokeLater(GUI::exibirMenuPrincipal);
    }

    private static void exibirMenuPrincipal() {
        Object[] opcoes = {"Jogar", "Carregar Partida", "Sair"};
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

        if (escolha == 1) {
            carregarPartida();
            return;
        }

        if (escolha != 0) {
            JOptionPane.showMessageDialog(null, "Obrigado por jogar!");
            System.exit(0);
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
                return 3;
        }
    }

    private static void jogarPartida(int percepcao) {
        Mapa mapa = Mapa.getInstance();
        mapa.gerar(percepcao);

        Player player = mapa.getPlayer();

        FramePrincipal frame = FramePrincipal.getInstance();
        frame.iniciarPartida(mapa, player, GUI::exibirResultadoFinal);

        mapa.iniciarThreadsDinossauros(player, frame.getCombate());
    }

    private static void carregarPartida() {
        try {
            SaveManager.carregar();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível carregar a partida salva.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            exibirMenuPrincipal();
            return;
        }

        Mapa mapa = Mapa.getInstance();
        Player player = mapa.getPlayer();

        if (player == null) {
            JOptionPane.showMessageDialog(null, "Não há partida salva.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            exibirMenuPrincipal();
            return;
        }

        FramePrincipal frame = FramePrincipal.getInstance();
        frame.iniciarPartida(mapa, player, GUI::exibirResultadoFinal);

        mapa.iniciarThreadsDinossauros(player, frame.getCombate());
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