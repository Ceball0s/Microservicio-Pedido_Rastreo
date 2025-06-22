package com.PedidoRastreo;

import com.PedidoRastreo.Models.Pedido;
import com.PedidoRastreo.PedidoService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/crear")
    public Pedido crearPedido(@RequestBody CrearPedidoRequest request) {
        return pedidoService.crearPedido(
                request.getIdComprador(),
                request.getIdVendedor(),
                request.getNombreProducto(),
                request.getDescripcionProducto(),
                request.getPrecioFinal()
        );
    }

    @GetMapping("/rastrear/{trackingId}")
    public Pedido consultarPedido(@PathVariable String trackingId) {
        return pedidoService.consultarPorTracking(trackingId);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> listarPedidosDeUsuario(@PathVariable Long usuarioId) {
        return pedidoService.listarPedidosDeUsuario(usuarioId);
    }

    @Data
    public static class CrearPedidoRequest {
        private Long idComprador;
        private Long idVendedor;
        private String nombreProducto;
        private String descripcionProducto;
        private Double precioFinal;
    }
}
