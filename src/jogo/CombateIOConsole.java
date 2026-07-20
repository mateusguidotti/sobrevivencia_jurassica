package jogo;

import interfaces.CombateIO;
import java.util.Scanner;

public class CombateIOConsole implements CombateIO {

    private final Scanner scan = new Scanner(System.in);

    @Override
    public void log(String mensagem) {
        System.out.println(mensagem);
    }

    @Override
    public boolean desejaFugir() {
        System.out.println("Deseja continuar o combate ou fugir? (1-Continuar 2-Fugir)");
        return lerInteiro() == 2;
    }

    @Override
    public int escolherArma(int numArmas, String[] nomesArmas) {
        System.out.println("-".repeat(20) + " ESCOLHA A SUA ARMA " + "-".repeat(20));
        System.out.println("1 - Punhos");
        for (int i = 0; i < numArmas; i++) {
            System.out.println((i + 2) + " - " + nomesArmas[i]);
        }
        return lerInteiro();
    }

    @Override
    public void atualizarDinoStats(String nome, int vidaAtual, int vidaMaxima) {
    }

    @Override
    public void limparDinoStats() {
    }

    private int lerInteiro() {
        while (!scan.hasNextInt()) {
            scan.next();
        }
        return scan.nextInt();
    }
}