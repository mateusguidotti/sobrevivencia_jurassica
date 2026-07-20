package jogo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveManager {
    public static void salvar(Mapa mapa) throws IOException {

        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream("save.dat"))) {

            out.writeObject(new Partida(mapa));
        }
    }

    public static void carregar() throws IOException, ClassNotFoundException {

        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream("save.dat"))) {

            Partida partida = (Partida) in.readObject();
            Mapa.getInstance().restaurarEstadoDe(partida.getMapa());
        }
    }
}
