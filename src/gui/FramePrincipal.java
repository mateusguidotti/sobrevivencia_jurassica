package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
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

        configurarTeclasDeMovimento(jogoFinal);

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }

    /* Liga as teclas WASD à movimentação, funcionando não importa qual
       componente da janela esteja com foco (WHEN_IN_FOCUSED_WINDOW). */
    private void configurarTeclasDeMovimento(JogoFinal jogoFinal) {
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        registrarTeclaMovimento(inputMap, actionMap, KeyEvent.VK_W, "mover_cima", 'w', jogoFinal);
        registrarTeclaMovimento(inputMap, actionMap, KeyEvent.VK_A, "mover_esquerda", 'a', jogoFinal);
        registrarTeclaMovimento(inputMap, actionMap, KeyEvent.VK_S, "mover_baixo", 's', jogoFinal);
        registrarTeclaMovimento(inputMap, actionMap, KeyEvent.VK_D, "mover_direita", 'd', jogoFinal);
    }

    private void registrarTeclaMovimento(InputMap inputMap, ActionMap actionMap,
            int codigoTecla, String nomeAcao, char direcao, JogoFinal jogoFinal) {
        inputMap.put(KeyStroke.getKeyStroke(codigoTecla, 0), nomeAcao);
        actionMap.put(nomeAcao, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jogoFinal.moverJogador(direcao);
            }
        });
    }

    public PainelJogo getPainelJogo() {
        return painelJogo;
    }

    public PainelHud getPainelHud() {
        return painelHud;
    }
}
