package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import itens.Arma;
import itens.ArmaDeDardos;
import itens.BastaoChoque;
import itens.Item;
import itens.KitMedico;
import jogo.Mapa;
import personagens.Dinossauro;
import personagens.Player;

public class PainelHud extends JPanel {

    private final JogoFinal jogoFinal;
    private final Mapa mapa;
    private final Player jogador;
    private final PainelJogo painelJogo;

    private JPanel painelInfo;
    private JPanel painelLog;
    private JPanel painelControles;
    private JPanel painelStatus;
    private JPanel painelInventario;
    private JPanel painelDinoStats;

    private JLabel lblVida;
    private JLabel lblPercepcao;
    private JLabel lblBastao;
    private JLabel lblDardos;
    private JLabel lblKits;
    private JLabel lblNomeDino;
    private JLabel lblVidaDino;

    private JTextArea areaLog;

    private JButton btnDebug;
    private JButton btnCurar;

    public PainelHud(JogoFinal jogoFinal, Mapa mapa, Player jogador, PainelJogo painelJogo) {
        this.jogoFinal = jogoFinal;
        this.mapa = mapa;
        this.jogador = jogador;
        this.painelJogo = painelJogo;

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        painelInfo = criarPainelInfo();
        painelLog = criarPainelLog();
        painelControles = criarPainelControles();

        add(painelInfo, BorderLayout.NORTH);
        add(painelLog, BorderLayout.CENTER);
        add(painelControles, BorderLayout.SOUTH);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 700);
    }

    private JPanel criarPainelInfo() {
        JPanel painel = new JPanel(new GridLayout(3,1,5,5));

        painelStatus = criarPainelStatus();
        painelInventario = criarPainelInventario();
        painelDinoStats = criarPainelDinoStats();
        painel.add(painelStatus);
        painel.add(painelInventario);
        painel.add(painelDinoStats);

        return painel;
    }

    private JPanel criarPainelStatus() {
        JPanel painel = new JPanel(new GridLayout(2,1));

        painel.setBorder(BorderFactory.createTitledBorder("Status"));
        lblVida = new JLabel();
        lblPercepcao = new JLabel();
        painel.add(lblVida);
        painel.add(lblPercepcao);

        atualizarStatus();

        return painel;
    }

    private JPanel criarPainelInventario() {
        JPanel painel = new JPanel(new GridLayout(3,1));

        painel.setBorder(BorderFactory.createTitledBorder("Inventário"));

        lblBastao = new JLabel();
        lblDardos = new JLabel();
        lblKits = new JLabel();

        painel.add(lblBastao);
        painel.add(lblDardos);
        painel.add(lblKits);

        atualizarInventario();

        return painel;
    }

    private JPanel criarPainelDinoStats() {
        JPanel painel = new JPanel(new GridLayout(2,1));

        painel.setBorder(BorderFactory.createTitledBorder("Dinossauro em Combate"));

        lblNomeDino = new JLabel();
        lblVidaDino = new JLabel();
        painel.add(lblNomeDino);
        painel.add(lblVidaDino);

        limparDinossauro();

        return painel;
    }

    private JPanel criarPainelLog() {
        JPanel painel = new JPanel(new BorderLayout());

        painel.setBorder(BorderFactory.createTitledBorder("Log"));

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(areaLog);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelControles() {
        JPanel painel = new JPanel(new GridLayout(1,2,5,5));

        painel.setBorder(BorderFactory.createTitledBorder("Controles (WASD para mover)"));

        btnDebug = new JButton("Debug");
        btnCurar = new JButton("Curar");

        btnDebug.addActionListener(e -> jogoFinal.alternarDebug());
        btnCurar.addActionListener(e -> jogoFinal.usarKitMedico());

        painel.add(btnDebug);
        painel.add(btnCurar);

        return painel;
    }

    public void atualizarStatus() {
        lblVida.setText("Vida: " + jogador.getVida() + "/" + jogador.getVidaMaxima());
        lblPercepcao.setText("Percepção: " + jogador.getPercepcao());
    }

    public void atualizarInventario() {
        boolean temBastao = false;
        int dardos = 0;
        int kits = 0;

        for (Arma arma : jogador.getInventario().getArmas()) {
            if (arma instanceof BastaoChoque) {
                temBastao = true;
            } else if (arma instanceof ArmaDeDardos) {
                dardos = arma.getQuantidade();
            }
        }

        for (Item item : jogador.getInventario().getInventario()) {
            if (item instanceof KitMedico) {
                kits = item.getQuantidade();
            }
        }

        lblBastao.setText("Bastão Elétrico: " + (temBastao ? "Possui" : "Não possui"));
        lblDardos.setText("Dardos: " + dardos);
        lblKits.setText("Kits Médicos: " + kits);
    }

    public void atualizarDinossauro(Dinossauro dino) {
        lblNomeDino.setText("Nome: " + dino.getNome());
        lblVidaDino.setText("Vida: " + dino.getVida() + "/" + dino.getVidaMaxima());
    }

    public void limparDinossauro() {
        lblNomeDino.setText("Nome: -");
        lblVidaDino.setText("Vida: -/-");
    }

    public void atualizarTextoDebug(boolean debugAtivo) {
        btnDebug.setText(debugAtivo ? "Debug: ON" : "Debug");
    }

    public void desabilitarControles() {
        btnCurar.setEnabled(false);
    }

    public void adicionarMensagem(String mensagem) {
        areaLog.append(mensagem + "\n");

        // Faz o scroll ir para a mensagem mais recente
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
}
