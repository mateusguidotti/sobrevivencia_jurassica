package gui;

import javax.swing.SwingUtilities;

import interfaces.VidaListener;
import personagens.Personagem;

public class VidaListenerGUI implements VidaListener {

    private final PainelHud painelHud;

    public VidaListenerGUI(PainelHud painelHud) {
        this.painelHud = painelHud;
    }

    @Override
    public void onVidaAlterada(Personagem personagem, int vidaAtual, int vidaMaxima) {
        SwingUtilities.invokeLater(painelHud::atualizarStatus);
    }
}