package jogo;

import interfaces.Movel;
import itens.Arma;
import itens.Item;
import java.util.List;
import personagens.Player;
import personagens.Dinossauro;
import personagens.Velociraptor;
import java.util.Scanner;

public class Combate {

    public static final int DERROTA = 0;
    public static final int VITORIA = 1;
    public static final int FUGA = 2;

    private final Scanner scan;
    private final Dado dado;
    
    public Combate(){
        scan = new Scanner(System.in);
        this.dado = new Dado();
    }
    
    /**
     * Inicia um combate entre o jogador e um dinossauro.
     * O dinossauro desfere o primeiro golpe em cada turno.
     * Retorna Combate.VITORIA, Combate.DERROTA ou Combate.FUGA.
     */
    public int iniciarCombate(Player player, Dinossauro dino, Mapa mapa){
        int turno = 1;
        
        while(player.estaVivo() && dino.estaVivo()){
            System.out.println("-".repeat(20) + " Turno " + turno + "-".repeat(20));
            executarAtaque(dino, player);
            
            if(!player.estaVivo()){
                break;
            }
            
            if(turno > 1 && desejaFugir()){
                if(tentarFugir(player, dino, mapa)){
                    return FUGA;
                } else {
                    System.out.println("Não há posição livre para fugir!");
                }
            }
            
            int armaEscolhida = menuEscolhaArma(player);
            executarAtaqueJogador(player, dino, armaEscolhida);
            
            if(dino.estaVivo()){
                testarEsquivaPosAtaque(player);
            }
            
            System.out.println(dino.getNome() + " " + dino.getVida() + "/" + dino.getVidaMaxima());
            System.out.println(player.getNome() + " " + player.getVida() + "/" + player.getVidaMaxima());
            turno++;
        }
        
        if(!player.estaVivo()){
            return DERROTA;
        }
        return VITORIA;
    }
    
    private boolean desejaFugir(){
        System.out.println("Deseja continuar o combate ou fugir? (1-Continuar 2-Fugir)");
        int escolha = lerInteiro();
        return escolha == 2;
    }
    
    private boolean tentarFugir(Player player, Dinossauro dino, Mapa mapa){
        int[][] direcoes = {{1,0},{-1,0},{0,1},{0,-1}};
        for(int[] dir : direcoes){
            int novoX = player.getX() + dir[0];
            int novoY = player.getY() + dir[1];
            if(novoX >= 0 && novoX < mapa.getTamanho() && novoY >= 0 && novoY < mapa.getTamanho()
                    && mapa.getCelula(novoX, novoY) == null){
                player.mover(novoX, novoY, mapa);
                System.out.println(player.getNome() + " fugiu do combate!");
                return true;
            }
        }
        return false;
    }
    
    private void executarAtaqueJogador(Player atacante, Dinossauro alvo, int arma){
        
        int resultadoDado = dado.rolar();
        System.out.println(atacante.getNome() + " ataca!\n" + "Resultado do dado: " + resultadoDado);
        if ( arma == 1 ) {
            atacante.atacarComPunhos(alvo, resultadoDado);
        } else {
            if ( atacante.getInventario().getArmas().size() < arma - 2 || arma < 0 ) {
                throw new IllegalArgumentException("Arma não existe");
            }
            atacante.getInventario().getArmas().get(arma - 2).usarArma(alvo, resultadoDado);
            if ( atacante.getInventario().getArmas().get(arma - 2).isConsumivel() ) {
                atacante.getInventario().removerArma(atacante.getInventario().getArmas().get(arma - 2));
            }
        }
    }
    
    /** Após o ataque do jogador, se o dinossauro sobreviver, testa a percepção do jogador para esquivar do contra-ataque. */
    private void testarEsquivaPosAtaque(Player player){
        if(player.esquivar(dado)){
            System.out.println(player.getNome() + " esquivou do contra-ataque!");
        } else {
            player.tomarDano(1);
            System.out.println(player.getNome() + " sofreu 1 de dano!");
        }
    }
    
    private void executarAtaque(Dinossauro atacante, Player alvo){
        System.out.println(atacante.getNome() + " ataca!");
        
        if(!alvo.esquivar(dado)){
            atacante.atacar(alvo, 1);
            System.out.println(atacante.getNome() + " acertou o player!");
        }else{
            System.out.println(alvo.getNome() + " esquivou!");
        }
    }
    
    private int menuEscolhaArma(Player player){
        System.out.println("-".repeat(20) + " ESCOLHA A SUA ARMA " + "-".repeat(20));
        System.out.println("1 - Punhos");
        for ( int i = 0; i < player.getInventario().getArmas().size(); i++ ) {
            System.out.println( (i + 2) + " - " + player.getInventario().getArmas().get(i).getNome());
        }
        int escolha = lerInteiro();
        return escolha;
    }
    
    private int lerInteiro(){
        while(!scan.hasNextInt()){
            scan.next();
        }
        return scan.nextInt();
    }
}
