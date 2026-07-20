package jogo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MapaListenerCLI implements interfaces.MapaListener {

    private static final long DELAY_MS = 1000;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "MapaListenerCLI-debounce");
        t.setDaemon(true);
        return t;
    });

    private final Object taskLock = new Object();
    private ScheduledFuture<?> tarefaAgendada;

    @Override
    public void onMapaAtualizado(Mapa mapa) {
        synchronized (taskLock) {
            // se já existe um print agendado, cancela — o "timer" reseta
            if (tarefaAgendada != null && !tarefaAgendada.isDone()) {
                tarefaAgendada.cancel(false);
            }
            tarefaAgendada = scheduler.schedule(() -> imprimirEstado(mapa), DELAY_MS, TimeUnit.MILLISECONDS);
        }
    }

    private void imprimirEstado(Mapa mapa) {
        mapa.imprimirParaJogador(mapa.getPlayer(), mapa.isDebug());
        System.out.println("\nAções disponíveis:\n");
        System.out.println("1 - Movimentar");
        System.out.println("2 - Cura");
        System.out.println("3 - DEBUG");
        System.out.println("4 - Sair");
    }

    public void encerrar() {
        synchronized (taskLock) {
            if (tarefaAgendada != null) {
                tarefaAgendada.cancel(false);
            }
        }
        scheduler.shutdownNow();
    }
}