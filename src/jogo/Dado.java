package jogo;

import java.util.Random;

public class Dado {
    private static Dado instance;
    private Random random;
    
    private Dado(){
        this.random = new Random();
    }
    /* Acesso à instância do Singleton */
    public static Dado getInstance(){
        if(instance == null){
            instance = new Dado();
        }
        return instance;
    }
    /*Rola um dado de N lados*/
    public int rolar(int lados){
        return random.nextInt(lados)+1;
    }
    
    /* Rola um dado de 6 lados*/
    public int rolar(){
        return rolar(6);
    }
}
