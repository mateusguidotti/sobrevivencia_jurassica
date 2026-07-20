package gui;

import interfaces.CombateIO;
import javax.swing.JOptionPane;

public class CombateIOGui implements CombateIO {

    private final PainelHud painelHud;

    public CombateIOGui(PainelHud painelHud) {
        this.painelHud = painelHud;
    }

    @Override
    public void log(String mensagem) {
        painelHud.adicionarMensagem(mensagem);
    }

    @Override
    public boolean desejaFugir() {
        int escolha = JOptionPane.showConfirmDialog(painelHud, "Deseja fugir do combate?",
                "Combate", JOptionPane.YES_NO_OPTION);
        return escolha == JOptionPane.YES_OPTION;
    }

    @Override
    public int escolherArma(int numArmas, String[] nomesArmas) {
        String[] opcoes = new String[numArmas + 1];
        opcoes[0] = "Punhos";
        System.arraycopy(nomesArmas, 0, opcoes, 1, numArmas);

        int escolha = JOptionPane.showOptionDialog(painelHud, "Escolha sua arma:", "Combate",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);

        if (escolha < 0) {
            escolha = 0;
        }
        return escolha + 1;
    }

    @Override
    public void atualizarDinoStats(String nome, int vidaAtual, int vidaMaxima) {
        painelHud.atualizarDinossauro(nome, vidaAtual, vidaMaxima);
    }

    @Override
    public void limparDinoStats() {
        painelHud.limparDinossauro();
    }
}