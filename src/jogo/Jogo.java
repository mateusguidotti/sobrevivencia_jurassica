package jogo;

public class Jogo {
    public static void main(String[] args) {
        Combate combate = new Combate();
        Mapa mapa = new Mapa(10);
        
        //combate.iniciarCombate(player, compso);
        mapa.gerar();
        mapa.imprimir();
        
    }
    /*
    private static void criarEntidades(){
        Player player = new Player()
    }
    */
}
