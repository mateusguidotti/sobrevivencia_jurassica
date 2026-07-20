package gui;

import javax.swing.SwingUtilities;

import interfaces.InventarioListener;

public class InventarioListenerGUI implements InventarioListener {

    private final PainelHud painelHud;

    public InventarioListenerGUI(PainelHud painelHud) {
        this.painelHud = painelHud;
    }

    @Override
    public void onInventarioAlterado() {
        SwingUtilities.invokeLater(painelHud::atualizarInventario);
    }
}