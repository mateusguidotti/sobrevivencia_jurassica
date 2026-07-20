package jogo;

import java.util.Random;

public class Dado {
    private static Dado instance;
    private Random random;
    
    private Dado(){
        this.random = new Random();
    }
    
    public static Dado getInstance(){
        if(instance == null){
            instance = new Dado();
        }
        return instance;
    }

    public int rolar(int lados){
        return random.nextInt(lados)+1;
    }
    

    public int rolar(){
        return rolar(6);
    }
}
