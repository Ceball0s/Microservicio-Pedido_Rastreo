package com.PedidoRastreo;

import com.PedidoRastreo.Models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByTrackingId(String trackingId);
    List<Pedido> findAllByIdCompradorOrIdVendedor(Long idComprador, Long idVendedor);
}
