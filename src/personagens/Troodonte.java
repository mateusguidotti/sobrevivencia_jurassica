package personagens;

import estrategias.EstrategiaPerseguicao;
import interfaces.Movel;
import jogo.Mapa;

public class Troodonte extends Dinossauro implements Movel{
    public Troodonte(int x, int y){
        super("Troodonte", x, y, 'T', 2, 3);
        setEstrategia(new EstrategiaPerseguicao());
    }
    
    @Override
    public boolean perseguePersonagem(){
        return true;
    }
    
    @Override
    public int getPassosMovimento() {
        return 1;
    }
    
    @Override
    public void mover(int x, int y, Mapa mapa) {
        mapa.setCelula(this.getX(), this.getY(), null);
        this.setPosition(x, y);
        mapa.setCelula(x, y, this);
    }
}
