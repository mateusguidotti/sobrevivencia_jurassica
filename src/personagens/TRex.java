package personagens;

public class TRex extends Dinossauro{
    public TRex(int x, int y){
        super("T-Rex", x, y, 'R', 3);
    }
    
    @Override
    public void atacar(Personagem alvo, int dano){
        super.atacar(alvo, 2);
    }
}
