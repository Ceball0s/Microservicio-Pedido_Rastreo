package com.PedidoRastreo.schedulers;

import com.PedidoRastreo.Models.Pedido;
import com.PedidoRastreo.PedidoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RastreoScheduler {

    private final PedidoRepository pedidoRepository;

    public RastreoScheduler(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Scheduled(fixedRate = 300000) // cada 5 minutos
    public void revisarCambiosDeRastreo() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        for (Pedido pedido : pedidos) {
            if (pedido.getEstadoRastreo().equals("Pendiente de envío")) {
                // Simular cambio de estado
                pedido.setEstadoRastreo("En camino");
                pedidoRepository.save(pedido);
                System.out.println("Pedido " + pedido.getTrackingId() + " actualizado a: En camino");
                // TODO: enviar correo aquí
            }
        }
    }

    @PostConstruct
    public void iniciar() {
        System.out.println("Rastreador iniciado...");
    }
}
