package personagens;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import interfaces.VidaListener;
import jogo.Entidade;

public abstract class Personagem extends Entidade{
    private int vida;
    private final int vidaMaxima;

    private final List<VidaListener> vidaListeners = new CopyOnWriteArrayList<>();

    public Personagem(String nome, int x, int y, char simbolo, int vida, String imagem){
        super(nome, x, y, simbolo, imagem);
        this.vida = vida;
        this.vidaMaxima = vida;
    }

    public void tomarDano(int dano){
        this.vida -= dano;
        notificarVidaListeners();
    }

    public void curar(int quantidade){
        this.vida = Math.min(this.vida + quantidade, this.vidaMaxima);
        notificarVidaListeners();
    }

    public boolean estaVivo(){
        return this.vida > 0;
    }

    public int getVida(){
        return this.vida;
    }

    public int getVidaMaxima(){
        return this.vidaMaxima;
    }

    public void addVidaListener(VidaListener listener){
        vidaListeners.add(listener);
    }

    public void removeVidaListener(VidaListener listener){
        vidaListeners.remove(listener);
    }

    private void notificarVidaListeners(){
        for(VidaListener listener : vidaListeners){
            listener.onVidaAlterada(this, this.vida, this.vidaMaxima);
        }
    }

    @Override
    public String toString(){
        return super.toString() + "\n" +
                "Vida: " + this.vida + "/" + this.vidaMaxima;
    }
}