package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import itens.Arma;
import jogo.Combate;
import jogo.Entidade;
import jogo.Mapa;
import personagens.Dinossauro;
import personagens.Player;

public class PainelHud extends JPanel {

    private final Mapa mapa;
    private final Player jogador;
    private final PainelJogo painelJogo;
    private final Combate combate;

    private final BiConsumer<Mapa, Player> fimDeJogoListener;

    private JPanel painelInfo;
    private JPanel painelLog;
    private JPanel painelControles;
    private JPanel painelStatus;
    private JPanel painelInventario;

    private JLabel lblVida;
    private JLabel lblPercepcao;
    private JLabel lblBastao;
    private JLabel lblDardos;
    private JLabel lblKits;

    private JTextArea areaLog;

    private JButton btnCima, btnBaixo, btnEsquerda, btnDireita, btnCurar, btnSair;

    private boolean partidaEncerrada = false;

    public PainelHud(Mapa mapa, Player jogador, PainelJogo painelJogo, BiConsumer<Mapa, Player> fimDeJogoListener) {
        this.mapa = mapa;
        this.jogador = jogador;
        this.painelJogo = painelJogo;
        this.combate = new Combate();
        this.fimDeJogoListener = fimDeJogoListener;

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        painelInfo = criarPainelInfo();
        painelLog = criarPainelLog();
        painelControles = criarPainelControles();

        add(painelInfo, BorderLayout.NORTH);
        add(painelLog, BorderLayout.CENTER);
        add(painelControles, BorderLayout.SOUTH);

        atualizarInventario();

        jogador.addVidaListener(new VidaListenerGUI(this));
        jogador.getInventario().addInventarioListener(new InventarioListenerGUI(this));

        mapa.addListener(new MapaListenerGUI(painelJogo, this, this::aoFimMovimentacaoDinossauros));
    }

    public Mapa getMapa() {
        return mapa;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 700);
    }

    private JPanel criarPainelInfo() {
        JPanel painel = new JPanel(new GridLayout(2,1,5,5));

        painelStatus = criarPainelStatus();
        painelInventario = criarPainelInventario();
        painel.add(painelStatus);
        painel.add(painelInventario);

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

        lblBastao = new JLabel("Bastão Elétrico: Não possui");
        lblDardos = new JLabel("Dardos: 0");
        lblKits = new JLabel("Kits Médicos: 0");

        painel.add(lblBastao);
        painel.add(lblDardos);
        painel.add(lblKits);

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
        JPanel painel = new JPanel(new GridBagLayout());

        painel.setBorder(BorderFactory.createTitledBorder("Controles"));

        btnCima = new JButton("↑");
        btnBaixo = new JButton("↓");
        btnEsquerda = new JButton("←");
        btnDireita = new JButton("→");
        btnCurar = new JButton("Usar Kit Médico");
        btnSair = new JButton("Sair da Partida");

        btnCima.addActionListener(e -> moverJogador('w'));
        btnBaixo.addActionListener(e -> moverJogador('s'));
        btnEsquerda.addActionListener(e -> moverJogador('a'));
        btnDireita.addActionListener(e -> moverJogador('d'));
        btnCurar.addActionListener(e -> usarKitMedico());
        btnSair.addActionListener(e -> sairDaPartida());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(2,2,2,2);

        gbc.gridx = 1;
        gbc.gridy = 0;
        painel.add(btnCima,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(btnEsquerda,gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        painel.add(new JButton("•"),gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        painel.add(btnDireita,gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        painel.add(btnBaixo,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        painel.add(btnCurar, gbc);

        gbc.gridy = 4;
        painel.add(btnSair, gbc);

        return painel;
    }

    private void moverJogador(char direcao) {
        if (partidaEncerrada) {
            return;
        }

        Entidade entidade = mapa.moverPlayer(jogador, direcao);
        boolean derrotaImediata = false;

        if (mapa.isDinossauro(entidade)) {
            Dinossauro dino = (Dinossauro) entidade;
            adicionarMensagem(dino.getNome() + " estava no caminho! Iniciando combate (veja o console)...");

            int resultado = combate.iniciarCombate(jogador, dino, mapa);

            if (resultado == Combate.VITORIA) {
                mapa.matarDinossauro(dino);
                mapa.moverPlayer(jogador, direcao);
                adicionarMensagem("Você derrotou " + dino.getNome() + "!");
            } else if (resultado == Combate.DERROTA) {
                derrotaImediata = true;
                adicionarMensagem("Você foi derrotado por " + dino.getNome() + "!");
            } else if (resultado == Combate.FUGA) {
                adicionarMensagem("Você fugiu do combate contra " + dino.getNome() + "!");
            }
        } else {
            adicionarMensagemMovimento(direcao);
        }

        painelJogo.repaint();

        if (derrotaImediata || !jogador.estaVivo()) {
            verificarFimDePartida();
        }
    }

    private void aoFimMovimentacaoDinossauros(int resultado, Dinossauro dino) {
        if (resultado == Combate.VITORIA) {
            adicionarMensagem("Você derrotou " + (dino != null ? dino.getNome() : "o dinossauro") + " que te encontrou!");
        } else if (resultado == Combate.DERROTA) {
            adicionarMensagem("Um dinossauro te encontrou e você foi derrotado!");
        }

        verificarFimDePartida();
    }

    private void adicionarMensagemMovimento(char direcao) {
        switch (direcao) {
            case 'w':
                adicionarMensagem("Jogador moveu-se para cima.");
                break;
            case 's':
                adicionarMensagem("Jogador moveu-se para baixo.");
                break;
            case 'a':
                adicionarMensagem("Jogador moveu-se para a esquerda.");
                break;
            case 'd':
                adicionarMensagem("Jogador moveu-se para a direita.");
                break;
        }
    }

    private void usarKitMedico() {
        if (partidaEncerrada) {
            return;
        }

        int indice = jogador.getInventario().procurarItem(1);
        if (indice == -1) {
            adicionarMensagem("Não há kits médicos no inventário.");
            return;
        }

        jogador.getInventario().getInventario().get(indice).usarItem(jogador);
        adicionarMensagem("Kit médico utilizado.");
    }

    private void sairDaPartida() {
        if (partidaEncerrada) {
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja sair da partida atual?", "Sair",
                JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            encerrarPartida();
        }
    }

    private void verificarFimDePartida() {
        if (!jogador.estaVivo() || mapa.getListaDinossauros().isEmpty()) {
            encerrarPartida();
        }
    }

    private void encerrarPartida() {
        if (partidaEncerrada) {
            return;
        }
        partidaEncerrada = true;
        desabilitarControles();

        if (fimDeJogoListener != null) {
            SwingUtilities.invokeLater(() -> fimDeJogoListener.accept(mapa, jogador));
        }
    }

    private void desabilitarControles() {
        btnCima.setEnabled(false);
        btnBaixo.setEnabled(false);
        btnEsquerda.setEnabled(false);
        btnDireita.setEnabled(false);
        btnCurar.setEnabled(false);
        btnSair.setEnabled(false);
    }

    public void atualizarStatus() {
        lblVida.setText("Vida: " + jogador.getVida() + "/" + jogador.getVidaMaxima());
        lblPercepcao.setText("Percepção: " + jogador.getPercepcao());
    }

    public void atualizarInventario() {
        int kits = 0;
        int indiceKit = jogador.getInventario().procurarItem(1);
        if (indiceKit != -1) {
            kits = jogador.getInventario().getInventario().get(indiceKit).getQuantidade();
        }

        boolean temBastao = false;
        int dardos = 0;
        for (Arma arma : jogador.getInventario().getArmas()) {
            if (arma.getId() == 2) {
                temBastao = true;
            } else if (arma.getId() == 3) {
                dardos = arma.getQuantidade();
            }
        }

        lblBastao.setText("Bastão Elétrico: " + (temBastao ? "Possui" : "Não possui"));
        lblDardos.setText("Dardos: " + dardos);
        lblKits.setText("Kits Médicos: " + kits);
    }

    public void adicionarMensagem(String mensagem) {
        areaLog.append(mensagem + "\n");

        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
}