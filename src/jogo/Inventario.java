package jogo;

import interfaces.InventarioListener;
import itens.Arma;
import itens.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Inventario {
    private List<Item> inventario = new ArrayList<>();
    private List<Arma> armas = new ArrayList<>();

    private final List<InventarioListener> listeners = new CopyOnWriteArrayList<>();

    public void pegarItem ( Item item ) {
        if ( item.isArma() ) {
            for ( Arma arma : armas ) {
                if ( arma.getId() == item.getId() ) {
                    arma.receberItem();
                    notificarListeners();
                    return;
                }
            }
            armas.add((Arma) item);
        } else {
            for ( Item itemTemp : inventario ) {
                if ( itemTemp.getId() == item.getId() ) {
                    item.receberItem();
                    notificarListeners();
                    return;
                }
            }
            inventario.add(item);
        }
        notificarListeners();
    }

    public void addItem ( Item item ) {
        inventario.add(item);
        notificarListeners();
    }

    public void addArma ( Arma arma ) {
        armas.add(arma);
        notificarListeners();
    }

    public int procurarItem ( int id ) {
        for ( int i = 0; i < inventario.size(); i++ ) {
            if ( id == inventario.get(i).getId() ) {
                return i;
            }
         }
        return -1;
    }

    public List<Item> getInventario() {
        return inventario;
    }

    public List<Arma> getArmas ( ) {
        return armas;
    }

    public void removerItem ( Item item ) {
        if ( !inventario.contains(item) ) {
            throw new IllegalArgumentException("Item não contido na lista");
        }
        item.gastarItem();
        if ( item.getQuantidade() <= 0 ) {
            inventario.remove(item);
        }
        notificarListeners();
    }

    public void removerArma ( Arma arma ) {
        if ( !armas.contains(arma) ) {
            throw new IllegalArgumentException ( "Item não contido na lista" );
        }
        arma.gastarItem();
        if ( arma.getQuantidade() <= 0 ) {
            armas.remove(arma);
        }
        notificarListeners();
    }

    public void addInventarioListener(InventarioListener listener){
        listeners.add(listener);
    }

    public void removeInventarioListener(InventarioListener listener){
        listeners.remove(listener);
    }

    private void notificarListeners(){
        for(InventarioListener listener : listeners){
            listener.onInventarioAlterado();
        }
    }
}