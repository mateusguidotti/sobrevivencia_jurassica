package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import jogo.Mapa;
import personagens.Player;

public class FramePrincipal extends JFrame {

    public FramePrincipal(Mapa mapa, Player jogador) {

        setTitle("Sobrevivência Jurássica");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;


        // =========================
        // PAINEL DO JOGO
        // =========================

        PainelJogo painelJogo = new PainelJogo(mapa);

        gbc.gridx = 0;
        gbc.weightx = 0.7;

        add(painelJogo, gbc);


        // =========================
        // HUD
        // =========================

        PainelHud painelHud = new PainelHud(
                mapa,
                jogador,
                painelJogo
        );

        gbc.gridx = 1;
        gbc.weightx = 0.3;

        add(painelHud, gbc);


        setSize(1000, 700);

        setLocationRelativeTo(null);

        setResizable(true);

        setVisible(true);
    }
}