package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import javax.swing.JFrame;

import jogo.Combate;
import jogo.Mapa;
import personagens.Player;

public class FramePrincipal extends JFrame {

    private static FramePrincipal instance;

    private PainelJogo painelJogo;
    private PainelHud painelHud;

    private FramePrincipal() {
        setTitle("Sobrevivência Jurássica");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    public static synchronized FramePrincipal getInstance() {
        if (instance == null) {
            instance = new FramePrincipal();
        }
        return instance;
    }

    public void iniciarPartida(Mapa mapa, Player jogador, BiConsumer<Mapa, Player> fimDeJogoListener) {
        getContentPane().removeAll();

        for (WindowListener listener : getWindowListeners()) {
            removeWindowListener(listener);
        }

        AtomicBoolean jaNotificou = new AtomicBoolean(false);
        BiConsumer<Mapa, Player> listenerComFechamento = (m, p) -> {
            if (jaNotificou.compareAndSet(false, true)) {
                setVisible(false);
                if (fimDeJogoListener != null) {
                    fimDeJogoListener.accept(m, p);
                }
            }
        };

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                listenerComFechamento.accept(mapa, jogador);
            }
        });

        painelJogo = new PainelJogo(mapa);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.7;
        add(painelJogo, gbc);

        painelHud = new PainelHud(mapa, jogador, painelJogo, listenerComFechamento);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        add(painelHud, gbc);

        revalidate();
        repaint();
        setVisible(true);
    }

    public Combate getCombate() {
        return painelHud != null ? painelHud.getCombate() : null;
    }
}