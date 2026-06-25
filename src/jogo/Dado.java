package jogo;

import java.util.Random;

public class Dado {
    private Random random;
    
    public Dado(){
        this.random = new Random();
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
