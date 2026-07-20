package personagens;

import jogo.Mapa;

public class TRex extends Dinossauro{
    public TRex(int x, int y){
        super("T-Rex", x, y, 'R', 3, 4, "t_rex.png");
    }

    @Override
    public void atacar(Personagem alvo, int dano){
        super.atacar(alvo, 2);
    }

    @Override
    public boolean podeSerAtacadoSemArma(){
        return false;
    }
}