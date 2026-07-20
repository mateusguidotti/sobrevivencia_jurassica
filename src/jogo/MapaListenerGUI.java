package gui;

import java.util.function.BiConsumer;

import javax.swing.SwingUtilities;

import interfaces.MapaListener;
import jogo.Mapa;
import personagens.Dinossauro;

public class MapaListenerGUI implements MapaListener {

    private final PainelJogo painelJogo;
    private final PainelHud painelHud;

    public MapaListenerGUI(PainelJogo painelJogo, PainelHud painelHud,
            BiConsumer<Integer, Dinossauro> aoFimMovimentacaoDinossauros) {
        this.painelJogo = painelJogo;
        this.painelHud = painelHud;

        painelHud.getMapa().setAoEncontrarJogadorListener(aoFimMovimentacaoDinossauros);
    }

    @Override
    public void onMapaAtualizado(Mapa mapa) {
        SwingUtilities.invokeLater(() -> {
            painelJogo.repaint();
            painelHud.atualizarStatus();
            painelHud.atualizarInventario();
        });
    }
}