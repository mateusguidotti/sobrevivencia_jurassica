package jogo;

import gui.FramePrincipal;
import java.util.Scanner;
import personagens.Dinossauro;
import personagens.Player;

/**
 *
 * @author dudur
 */
public class CLI {
    private static final Scanner scan = new Scanner(System.in);

    public static void iniciarJogo(){
        System.out.println("=================================================");
        System.out.println("   Bem-vindo a SOBREVIVÊNCIA JURÁSSICA!");
        System.out.println("=================================================");
        
        boolean continuarNoJogo = true;
        while(continuarNoJogo){
            continuarNoJogo = exibirMenuPrincipal();
        }
        
        System.out.println("Obrigado por jogar!");
    }
    
    private static boolean exibirMenuPrincipal(){
        System.out.println("\nMenu:");
        System.out.println("1 - Jogar");
        System.out.println("2 - Sair");
        System.out.print("Escolha uma opção: ");
        
        int escolha = lerInteiro();
        if(escolha == 2){
            return false;
        }
        if(escolha != 1){
            System.out.println("Opção inválida.");
            return true;
        }
        
        int percepcao = exibirMenuDificuldade();
        jogarPartida(percepcao);
        return true;
    }
    
    private static int exibirMenuDificuldade(){
        System.out.println("\nEscolha a dificuldade:");
        System.out.println("1 - Fácil (Percepção 3)");
        System.out.println("2 - Médio (Percepção 2)");
        System.out.println("3 - Difícil (Percepção 1)");
        System.out.print("Escolha uma opção: ");
        
        int escolha = lerInteiro();
        switch(escolha){
            case 2: return 2;
            case 3: return 1;
            default: return 3;
        }
    }
    
    private static void jogarPartida(int percepcao){
        int temp;
        Combate combate = new Combate();
        Mapa mapa = Mapa.getInstance();
        MapaListenerCLI listener = new MapaListenerCLI();
        mapa.addListener(listener);
        mapa.gerar(percepcao);
        Player player = mapa.getPlayer();
        
        System.out.println("-----------------------------------------");

        mapa.iniciarThreadsDinossauros(player, combate);

        boolean jogoEmAndamento = true;

        while(jogoEmAndamento && player.estaVivo() && existemDinossauros(mapa)){
            mapa.imprimirParaJogador(player, mapa.isDebug());

            System.out.println("\nMenu:");
            System.out.println("1 - Movimentar");
            System.out.println("2 - Cura");
            System.out.println("3 - DEBUG (" + (mapa.isDebug() ? "Desativar" : "Ativar") + ")");
            System.out.println("4 - Sair");

            int opcao = lerInteiro();

            switch(opcao){
                case 1:
                    jogoEmAndamento = realizarMovimentacao(mapa, combate, player);
                    break;
                case 2:
                    temp = player.getInventario().procurarItem(1);
                    if ( temp == -1 ) {
                        System.out.println("Não há kit médicos no inventário ");
                        break;
                    } else {
                        player.getInventario().getInventario().get(temp).usarItem(player);
                    }
                    break;
                case 3:
                    mapa.alternarDebug();
                    break;
                case 4:
                    jogoEmAndamento = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }

        mapa.pararThreadsDinossauros();
        listener.encerrar();

        exibirResultadoFinal(player, mapa);
    }
    
    private static boolean realizarMovimentacao(Mapa mapa, Combate combate, Player player){
        System.out.println("Escolha a movimentação desejada:");
        System.out.println("a - esquerda");
        System.out.println("d - direita");
        System.out.println("s - baixo");
        System.out.println("w - cima");
        
        char escolha = scan.next().charAt(0);
        
        Entidade entidade = mapa.moverPlayer(player, escolha);
        
        if(mapa.isDinossauro(entidade)){
            int resultado = combate.iniciarCombate(player, (Dinossauro) entidade, mapa);
            
            if(resultado == Combate.VITORIA){
                mapa.matarDinossauro((Dinossauro) entidade);
                mapa.moverPlayer(player, escolha);
            }
            
            if(resultado == Combate.DERROTA){
                return false;
            }
        }
        
        return true;
    }
    
    private static boolean existemDinossauros(Mapa mapa){
        if ( mapa.getListaDinossauros().isEmpty() ) {
            return false;
        }
        return true;
    }
    
    private static void exibirResultadoFinal(Player player, Mapa mapa){
        mapa.imprimir();
        
        if(!player.estaVivo()){
            System.out.println("\nVocê foi devorado pelos dinossauros... FIM DE JOGO.");
        } else if(!existemDinossauros(mapa)){
            System.out.println("\nParabéns! Você eliminou todos os dinossauros e sobreviveu!");
        } else {
            System.out.println("\nVocê saiu do parque.");
        }
        
        System.out.println("\n1 - Novo Jogo");
        System.out.println("2 - Voltar ao Menu Principal");
        System.out.print("Escolha uma opção: ");
        lerInteiro();
    }
    
    private static int lerInteiro(){
        while(!scan.hasNextInt()){
            scan.next();
        }
        return scan.nextInt();
    }
}
