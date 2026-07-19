package jogo;

import java.util.List;
import java.util.Scanner;
import personagens.*;
import jogo.CLI;

public class Jogo {

        public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   Bem-vindo a SOBREVIVÊNCIA JURÁSSICA!");
        System.out.println("=================================================");
        
        boolean continuarNoJogo = true;
        while(continuarNoJogo){
            continuarNoJogo = CLI.exibirMenuPrincipal();
        }
        
        System.out.println("Obrigado por jogar!");
    }
}
