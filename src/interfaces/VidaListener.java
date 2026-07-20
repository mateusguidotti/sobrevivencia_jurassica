package interfaces;

import personagens.Personagem;

public interface VidaListener {
    void onVidaAlterada(Personagem personagem, int vidaAtual, int vidaMaxima);
}