package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import jogo.Mapa;
import personagens.Player;

public class FramePrincipal extends JFrame {

    private final PainelJogo painelJogo;
    private final PainelHud painelHud;

    public FramePrincipal(JogoFinal jogoFinal, Mapa mapa, Player jogador) {

        setTitle("Sobrevivência Jurássica");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        painelJogo = new PainelJogo(mapa);

        gbc.gridx = 0;
        gbc.weightx = 0.7;

        add(painelJogo, gbc);

        painelHud = new PainelHud(jogoFinal, mapa, jogador, painelJogo);

        gbc.gridx = 1;
        gbc.weightx = 0.3;

        add(painelHud, gbc);


        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }

    public PainelJogo getPainelJogo() {
        return painelJogo;
    }

    public PainelHud getPainelHud() {
        return painelHud;
    }
}
