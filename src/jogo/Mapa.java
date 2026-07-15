package jogo;

import interfaces.Movel;
import itens.Arma;
import itens.ArmaDeDardos;
import itens.BastaoChoque;
import itens.KitMedico;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import personagens.*;

public class Mapa {
    private int tamanho;
    private Entidade[][] celulas;
    private Random random;
    private Player player;
    private List<Movel> moveis = new ArrayList<>();
    private List<Dinossauro> dinos = new ArrayList<>();
    private List<CaixaSuprimento> caixas = new ArrayList<>();
    private List<Parede> paredes = new ArrayList<>();
    
    public List<Dinossauro> getListaDinossauros() {
        return dinos;
    }
    
    public Mapa(int tamanho){
        this.tamanho = tamanho;
        celulas = new Entidade[tamanho][tamanho];
        random = new Random();
    }
    
    public void imprimir(){
        
        for(int i = 0; i < tamanho; i++){
            for(int j = 0; j < tamanho; j++){
                if(celulas[i][j] != null){
                    System.out.print("| " + celulas[i][j].getSimbolo() + " ");
                } else {
                    System.out.print("|   ");
                }
            }
            System.out.print("|\n");
        }
    }
    
    public void imprimirParaJogador(Player player, boolean debug){
        boolean[][] visivel = debug ? null : calcularVisibilidade(player);
        
        for(int i = 0; i < tamanho; i++){
            for(int j = 0; j < tamanho; j++){
                if(debug || visivel[i][j]){
                    if(celulas[i][j] != null){
                        System.out.print("| " + celulas[i][j].getSimbolo() + " ");
                    } else {
                        System.out.print("|   ");
                    }
                } else {
                    System.out.print("| ? ");
                }
            }
            System.out.print("|\n");
        }
        
        System.out.println(player.getNome() + " - Vida: " + player.getVida() + "/" + player.getVidaMaxima()
                + " | Percepção: " + player.getPercepcao());
    }
    
    private boolean[][] calcularVisibilidade(Player player){
        boolean[][] visivel = new boolean[tamanho][tamanho];
        int px = player.getX();
        int py = player.getY();
        visivel[py][px] = true;
        
        int alcance = player.getPercepcao();
        
        int[][] direcoes = {{1,0},{-1,0},{0,1},{0,-1}};
        for(int[] dir : direcoes){
            int x = px;
            int y = py;
            for(int passo = 0; passo < alcance; passo++){
                x += dir[0];
                y += dir[1];
                if(x < 0 || x >= tamanho || y < 0 || y >= tamanho){
                    break;
                }
                visivel[y][x] = true;
                if(celulas[y][x] != null){
                    break;
                }
            }
        }
        return visivel;
    }
    
    public int getTamanho(){
        return this.tamanho;
    }
    
    public Entidade getCelula(int x, int y){
        return celulas[y][x];
    }
    
    public void setCelula(int x, int y, Entidade entidade){
        celulas[y][x] = entidade;
    }
    
    public void gerar(){
        gerar(3);
    }
    
    public Player getPlayer ( ) {
        return player;
    }
    
    public void gerar(int percepcaoJogador){
        int numCelulas = tamanho * tamanho;
        int numParedes = random.nextInt(numCelulas - (int) (numCelulas * 0.85))+tamanho;
        
        player = new Player(0,0, 5, percepcaoJogador);
        setCelula(0,0, player);   
        
        alocarParedes(random.nextInt(numCelulas - (int) (numCelulas * 0.85))+tamanho);
        
        alocarDinossauro("Compsognato", 2);
        alocarDinossauro("Velociraptor", 2);
        alocarDinossauro("Troodonte", 5);
        alocarDinossauro("TRex", 1);
        alocarCaixas("MedKit", 1);
        alocarCaixas("ArmaDeDardos", 2);
        alocarCaixas("BastaoChoque", 1);
    }
    
    private void alocarParedes ( int num ) {
        for ( int i = 0; i < num; i++ ) {
            int x = random.nextInt(tamanho);
            int y = random.nextInt(tamanho);
            
            while ( celulas[x][y] != null ) {
                x = random.nextInt(tamanho);
                y = random.nextInt(tamanho);
            }

            Parede parede = new Parede(x, y);
            paredes.add(parede);
            setCelula(x,y, parede);
        }
    }
    
    private void alocarCaixas ( String conteudo, int quantidade ) {
        CaixaSuprimento caixa;
        for ( int i = 0; i < quantidade; i++ ) {
            int x = 0;
            int y = 0;
            
            while ( getCelula(x,y) != null ) {
                x = random.nextInt(tamanho);
                y = random.nextInt(tamanho);
            }
            
            switch ( conteudo ) {
                case "MedKit":
                    caixa = new CaixaSuprimento(x,y, new KitMedico());
                    caixas.add(caixa);
                    setCelula(x,y, caixa);
                    break;
                case "ArmaDeDardos":
                    caixa = new CaixaSuprimento(x,y, new ArmaDeDardos());
                    caixas.add(caixa);
                    setCelula(x,y, caixa);
                    break;
                case "BastaoChoque":
                    caixa = new CaixaSuprimento(x,y, new BastaoChoque());
                    caixas.add(caixa);
                    setCelula(x,y, caixa);
                    break;
            }
            }
        }
    
    private void alocarDinossauro(String tipo, int quantidade){
        
        for(int i = 0; i < quantidade; i++){   
            int x = 0;
            int y = 0;
            
            while(getCelula(x,y) != null){
                x = random.nextInt(tamanho);
                y = random.nextInt(tamanho);
            }
            
            switch(tipo){
                case "Compsognato":
                    Compsognato compsognato = new Compsognato(x,y);
                    dinos.add(compsognato);
                    moveis.add(compsognato);
                    setCelula(x,y, compsognato);
                    break;
                case "Velociraptor":
                    Velociraptor velociraptor = new Velociraptor(x,y);
                    dinos.add(velociraptor);
                    moveis.add(velociraptor);
                    setCelula(x,y, velociraptor );
                    break;
                case "Troodonte":
                    Troodonte troodonte = new Troodonte(x,y);
                    dinos.add(troodonte);
                    moveis.add(troodonte);
                    setCelula(x,y, troodonte );
                    break;
                case "TRex":
                    TRex trex = new TRex(x,y);
                    dinos.add(trex);
                    setCelula(x,y, trex);
                    break;
            }
            
        }
    }
    
    
    public void atualizarMapa(){
        imprimir();
    }
    

    public Entidade moverPlayer(Player player, char direcao){
        int posAlvoX = player.getX();
        int posAlvoY = player.getY();
        
        switch(direcao){
            case 'a':
                posAlvoX--;
                break;
            case 'd':
                posAlvoX++;
                break;
            case 's':
                posAlvoY++;
                break;
            case 'w':
                posAlvoY--;
                break;
            default:
                System.out.println("Escolha inválida");
                return null;
            }
        
        if(posAlvoX < 0 || posAlvoX >= tamanho || posAlvoY < 0 || posAlvoY >= tamanho){
            System.out.println("Não é possível se mover para fora do mapa!");
            return null;
        }
        
        Entidade destino = getCelula(posAlvoX, posAlvoY);
        
        if(destino == null){
            player.mover(posAlvoX, posAlvoY, this);
            return null;
        }
        
        if(caixas.contains(destino)){
            CaixaSuprimento caixa = (CaixaSuprimento) destino;
            Entidade resultado = abrirCaixa(player, caixa, posAlvoX, posAlvoY);
            if(resultado == null){
                player.mover(posAlvoX, posAlvoY, this);
            }
            return resultado;
        }
        
        return destino;
    }

    private Entidade abrirCaixa(Player player, CaixaSuprimento caixa, int x, int y){
        System.out.println("Você encontrou uma Caixa de Suprimentos!");
        
        player.getInventario().pegarItem(caixa.getConteudo());
        System.out.println("Você encontrou " + caixa.getConteudo().getNome());
        caixas.remove(caixa);
        
        if(caixa.isCompsognatoSurpresa()){
            System.out.println("Surpresa! Um Compsognato estava escondido na caixa!");
            Compsognato comp = new Compsognato(x, y);
            dinos.add(comp);
            moveis.add(comp);
            setCelula(x, y, comp);
            return comp;
        }
        
        setCelula(x, y, null);
        return null;
    }
    
    public boolean isDinossauro ( Entidade entidade ) {
        return dinos.contains(entidade);
    }
    
    public void matarDinossauro ( Dinossauro dino ) {
        dinos.remove(dino);
        if ( moveis.contains(dino) ) {
            moveis.remove(dino);
        }
        setCelula(dino.getX(), dino.getY(), null);
    }
    
    public int moverDinossauros(Player player, Combate combate){
        for(Movel movel : moveis){
            Dinossauro dino = (Dinossauro) movel;
            if(!dino.estaVivo()){
                continue;
            }
            
            int passos = movel.getPassosMovimento();
            for(int passo = 0; passo < passos; passo++){
                
                int[] destino = calcularProximoPasso(dino, player);
                if(destino == null){
                    continue;
                }
                
                Entidade alvoCelula = getCelula(destino[0], destino[1]);
                
                if(alvoCelula == player){
                    System.out.println(dino.getNome() + " encontrou o jogador!");
                    int resultado = combate.iniciarCombate(player, dino, this);
                    if(resultado == Combate.VITORIA){
                        matarDinossauro(dino);
                    }
                    return resultado;
                }
                
                if(alvoCelula == null){
                    movel.mover(destino[0], destino[1], this);
                }
            }
        }
        return -1;
    }
    
    private int[] calcularProximoPasso(Dinossauro dino, Player player){
        int dx = 0, dy = 0;
        
        if(dino.perseguePersonagem()){
            int diffX = player.getX() - dino.getX();
            int diffY = player.getY() - dino.getY();
            if(Math.abs(diffX) > Math.abs(diffY)){
                dx = Integer.signum(diffX);
            } else if (diffY != 0){
                dy = Integer.signum(diffY);
            } else if (diffX != 0){
                dx = Integer.signum(diffX);
            }
        } else {
            int[][] direcoes = {{1,0},{-1,0},{0,1},{0,-1}};
            int[] dir = direcoes[random.nextInt(direcoes.length)];
            dx = dir[0];
            dy = dir[1];
        }
        
        int novoX = dino.getX() + dx;
        int novoY = dino.getY() + dy;
        
        if(novoX < 0 || novoX >= tamanho || novoY < 0 || novoY >= tamanho){
            return null;
        }
        return new int[]{novoX, novoY};
    }
}