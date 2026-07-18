package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jogo.Mapa;
import personagens.Player;

public class PainelHud extends JPanel {


    // =========================
    // REFERÊNCIAS DO JOGO
    // =========================

    private final Mapa mapa;
    private final Player jogador;
    private final PainelJogo painelJogo;


    // =========================
    // PAINÉIS
    // =========================

    private JPanel painelInfo;
    private JPanel painelLog;
    private JPanel painelControles;

    private JPanel painelStatus;
    private JPanel painelInventario;


    // =========================
    // LABELS
    // =========================

    private JLabel lblVida;
    private JLabel lblPercepcao;

    private JLabel lblBastao;
    private JLabel lblDardos;
    private JLabel lblKits;


    // =========================
    // LOG
    // =========================

    private JTextArea areaLog;


    // =========================
    // CONSTRUTOR
    // =========================

    public PainelHud(
            Mapa mapa,
            Player jogador,
            PainelJogo painelJogo
    ) {

        this.mapa = mapa;
        this.jogador = jogador;
        this.painelJogo = painelJogo;


        setLayout(new BorderLayout(5, 5));

        setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        5,
                        5,
                        5
                )
        );


        painelInfo = criarPainelInfo();

        painelLog = criarPainelLog();

        painelControles = criarPainelControles();


        add(
                painelInfo,
                BorderLayout.NORTH
        );

        add(
                painelLog,
                BorderLayout.CENTER
        );

        add(
                painelControles,
                BorderLayout.SOUTH
        );
    }


    @Override
    public Dimension getPreferredSize() {

        return new Dimension(
                300,
                700
        );
    }


    // ==================================================
    // PAINEL DE INFORMAÇÕES
    // ==================================================

    private JPanel criarPainelInfo() {

        JPanel painel = new JPanel(
                new GridLayout(
                        2,
                        1,
                        5,
                        5
                )
        );


        painelStatus = criarPainelStatus();

        painelInventario = criarPainelInventario();


        painel.add(painelStatus);

        painel.add(painelInventario);


        return painel;
    }


    // ==================================================
    // STATUS
    // ==================================================

    private JPanel criarPainelStatus() {

        JPanel painel = new JPanel(
                new GridLayout(
                        2,
                        1
                )
        );


        painel.setBorder(
                BorderFactory.createTitledBorder(
                        "Status"
                )
        );


        lblVida = new JLabel();

        lblPercepcao = new JLabel();


        painel.add(lblVida);

        painel.add(lblPercepcao);


        atualizarStatus();


        return painel;
    }


    // ==================================================
    // INVENTÁRIO
    // ==================================================

    private JPanel criarPainelInventario() {

        JPanel painel = new JPanel(
                new GridLayout(
                        3,
                        1
                )
        );


        painel.setBorder(
                BorderFactory.createTitledBorder(
                        "Inventário"
                )
        );


        lblBastao = new JLabel(
                "Bastão Elétrico: Não possui"
        );

        lblDardos = new JLabel(
                "Dardos: 0"
        );

        lblKits = new JLabel(
                "Kits Médicos: 0"
        );


        painel.add(lblBastao);

        painel.add(lblDardos);

        painel.add(lblKits);


        return painel;
    }


    // ==================================================
    // LOG
    // ==================================================

    private JPanel criarPainelLog() {

        JPanel painel = new JPanel(
                new BorderLayout()
        );


        painel.setBorder(
                BorderFactory.createTitledBorder(
                        "Log"
                )
        );


        areaLog = new JTextArea();


        areaLog.setEditable(false);

        areaLog.setLineWrap(true);

        areaLog.setWrapStyleWord(true);


        JScrollPane scrollPane = new JScrollPane(
                areaLog
        );


        painel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        adicionarMensagem(
                "Sistema iniciado."
        );

        adicionarMensagem(
                "Aguardando jogador..."
        );


        return painel;
    }


    // ==================================================
    // CONTROLES
    // ==================================================

    private JPanel criarPainelControles() {

        JPanel painel = new JPanel(
                new GridBagLayout()
        );


        painel.setBorder(
                BorderFactory.createTitledBorder(
                        "Controles"
                )
        );


        JButton btnCima = new JButton("↑");

        JButton btnBaixo = new JButton("↓");

        JButton btnEsquerda = new JButton("←");

        JButton btnDireita = new JButton("→");


        // =========================
        // EVENTOS DOS BOTÕES
        // =========================

        btnCima.addActionListener(
                e -> moverJogador('w')
        );


        btnBaixo.addActionListener(
                e -> moverJogador('s')
        );


        btnEsquerda.addActionListener(
                e -> moverJogador('a')
        );


        btnDireita.addActionListener(
                e -> moverJogador('d')
        );


        // =========================
        // POSICIONAMENTO
        // =========================

        GridBagConstraints gbc =
                new GridBagConstraints();


        gbc.gridx = 1;

        gbc.gridy = 0;

        painel.add(
                btnCima,
                gbc
        );


        gbc.gridx = 0;

        gbc.gridy = 1;

        painel.add(
                btnEsquerda,
                gbc
        );


        gbc.gridx = 1;

        gbc.gridy = 1;

        painel.add(
                new JButton("•"),
                gbc
        );


        gbc.gridx = 2;

        gbc.gridy = 1;

        painel.add(
                btnDireita,
                gbc
        );


        gbc.gridx = 1;

        gbc.gridy = 2;

        painel.add(
                btnBaixo,
                gbc
        );


        return painel;
    }


    // ==================================================
    // MOVIMENTAÇÃO DO JOGADOR
    // ==================================================

    private void moverJogador(char direcao) {


        // Chama a lógica existente do jogo

        mapa.moverPlayer(
                jogador,
                direcao
        );


        // Atualiza o mapa visualmente

        painelJogo.repaint();


        // Atualiza as informações do HUD

        atualizarStatus();

        atualizarInventario();


        // Registra a ação no log

        switch (direcao) {

            case 'w':
                adicionarMensagem(
                        "Jogador tentou mover-se para cima."
                );
                break;


            case 's':
                adicionarMensagem(
                        "Jogador tentou mover-se para baixo."
                );
                break;


            case 'a':
                adicionarMensagem(
                        "Jogador tentou mover-se para a esquerda."
                );
                break;


            case 'd':
                adicionarMensagem(
                        "Jogador tentou mover-se para a direita."
                );
                break;
        }
    }


    // ==================================================
    // ATUALIZA STATUS
    // ==================================================

    public void atualizarStatus() {

        lblVida.setText(
                "Vida: "
                + jogador.getVida()
                + "/"
                + jogador.getVidaMaxima()
        );


        lblPercepcao.setText(
                "Percepção: "
                + jogador.getPercepcao()
        );
    }


    // ==================================================
    // ATUALIZA INVENTÁRIO
    // ==================================================

    public void atualizarInventario() {

        /*
         * Por enquanto os valores são placeholders.
         *
         * Depois vamos substituir por:
         *
         * jogador.getInventario()
         *
         * e os métodos reais do inventário.
         */


        lblBastao.setText(
                "Bastão Elétrico: Não possui"
        );


        lblDardos.setText(
                "Dardos: 0"
        );


        lblKits.setText(
                "Kits Médicos: 0"
        );
    }


    // ==================================================
    // LOG
    // ==================================================

    public void adicionarMensagem(
            String mensagem
    ) {

        areaLog.append(
                mensagem
                + "\n"
        );


        // Faz o scroll ir para a mensagem mais recente

        areaLog.setCaretPosition(
                areaLog.getDocument().getLength()
        );
    }
}