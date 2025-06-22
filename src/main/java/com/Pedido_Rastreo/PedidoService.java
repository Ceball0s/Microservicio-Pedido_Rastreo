package com.Pedido_Rastreo;

import com.Pedido_Rastreo.Models.Pedido;
import com.Pedido_Rastreo.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;


@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final AtomicLong contador = new AtomicLong(1); // para generar PED-0001, PED-0002

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido crearPedido(Long compradorId, Long vendedorId, String nombre, String descripcion, Double precio) {
        String trackingId = "PED-" + String.format("%04d", contador.getAndIncrement());

        Pedido pedido = Pedido.builder()
                .trackingId(trackingId)
                .idComprador(compradorId)
                .idVendedor(vendedorId)
                .productoNombre(nombre)
                .productoDescripcion(descripcion)
                .precioFinal(precio)
                .fechaCreacion(new Date())
                .estadoRastreo("Pendiente de envío")
                .build();

        return pedidoRepository.save(pedido);
    }

    public Pedido consultarPorTracking(String trackingId) {
        return pedidoRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("No se encontró el pedido con ID: " + trackingId));
    }
    public List<Pedido> listarPedidosDeUsuario(Long usuarioId) {
        return pedidoRepository.findAllByIdCompradorOrIdVendedor(usuarioId, usuarioId);
    }

}
