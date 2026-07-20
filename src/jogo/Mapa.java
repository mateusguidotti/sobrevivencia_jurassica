package jogo;

import jogo.Entidades.CaixaSuprimento;
import jogo.Entidades.Parede;
import interfaces.Movel;
import itens.ArmaDeDardos;
import itens.BastaoChoque;
import itens.KitMedico;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import personagens.*;
import interfaces.MapaListener;
import java.util.concurrent.CopyOnWriteArrayList;

public class Mapa {
    private static Mapa instance;
    
    private int tamanho;
    private Entidade[][] celulas;
    private Random random;
    private Player player;
    private List<Movel> moveis = new ArrayList<>();
    private List<Dinossauro> dinos = new ArrayList<>();
    private List<CaixaSuprimento> caixas = new ArrayList<>();
    private List<Parede> paredes = new ArrayList<>();
    private volatile boolean debug = false;

    private final List<ThreadDinossauro> threads = new ArrayList<>();
    private ExecutorService executorDinossauros;
    private Combate combateAtivo;
    
    private final List<MapaListener> listeners = new CopyOnWriteArrayList<>();
    private final Object lock = new Object(); // trava para operações críticas
    
    public List<Dinossauro> getListaDinossauros() {
        return dinos;
    }
    
    private Mapa(int tamanho){
        this.tamanho = tamanho;
        celulas = new Entidade[tamanho][tamanho];
        random = new Random();
    }
    
    public static Mapa getInstance(){
        if(instance == null){
            instance = new Mapa(10);
        }
        return instance;
    }
    
    public synchronized void imprimir(){
        
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
    
    public synchronized void imprimirParaJogador(Player player, boolean debug){
        synchronized (lock) {
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
    
    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void alternarDebug() {
        this.debug = !this.debug;
    }
    
    public int getTamanho(){
        return this.tamanho;
    }
    
    public void addListener(MapaListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MapaListener listener) {
        listeners.remove(listener);
    }

    private void notificarListeners() {
        for (MapaListener listener : listeners) {
            listener.onMapaAtualizado(this);
        }
    }
    
    public Entidade getCelula(int x, int y) {
        synchronized (lock) {
            return celulas[y][x];
        }
    }
    
    public Entidade[][] getMapa(){
        synchronized (lock) {
            return celulas;
        }
    }
    
    public void setCelula(int x, int y, Entidade entidade) {
        synchronized (lock) {
            celulas[y][x] = entidade;
            notificarListeners();
        }
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
    

    public synchronized Entidade moverPlayer(Player player, char direcao){
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
            if(executorDinossauros != null){
                iniciarThread(comp, this.player, combateAtivo);
            }
            return comp;
        }
        
        setCelula(x, y, null);
        return null;
    }
    
    public boolean isDinossauro ( Entidade entidade ) {
        return dinos.contains(entidade);
    }
    
    public synchronized void matarDinossauro ( Dinossauro dino ) {
        dinos.remove(dino);
        if ( moveis.contains(dino) ) {
            moveis.remove(dino);
        }
        setCelula(dino.getX(), dino.getY(), null);

        threads.stream().filter(thread -> thread.getDino() == dino).forEach(ThreadDinossauro::encerrar);
    }

    public void iniciarThreadsDinossauros(Player player, Combate combate){
        this.combateAtivo = combate;
        executorDinossauros = Executors.newCachedThreadPool();
        for(Movel movel : moveis){
            iniciarThread(movel, player, combate);
        }
    }

    private void iniciarThread(Movel movel, Player player, Combate combate){
        ThreadDinossauro thread = new ThreadDinossauro(movel, player, this, combate);
        threads.add(thread);
        executorDinossauros.submit(thread);
    }

    public void pararThreadsDinossauros(){
        for(ThreadDinossauro thread : threads){
            thread.encerrar();
        }
        if(executorDinossauros != null){
            executorDinossauros.shutdownNow();
        }
        threads.clear();
    }
}
