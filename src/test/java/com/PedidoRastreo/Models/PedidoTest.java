package com.PedidoRastreo.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    void testPedidoCreation() {
        Pedido pedido = new Pedido();
        assertNotNull(pedido);
    }

    @Test
    void testPedidoBuilder() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .trackingId("PED-0001")
                .idComprador(1L)
                .idVendedor(2L)
                .productoNombre("Producto Test")
                .productoDescripcion("Descripción del producto")
                .precioFinal(100.0)
                .estadoRastreo("Pendiente")
                .build();
        
        assertEquals(1L, pedido.getId());
        assertEquals("PED-0001", pedido.getTrackingId());
        assertEquals(1L, pedido.getIdComprador());
        assertEquals(2L, pedido.getIdVendedor());
        assertEquals("Producto Test", pedido.getProductoNombre());
        assertEquals("Descripción del producto", pedido.getProductoDescripcion());
        assertEquals(100.0, pedido.getPrecioFinal());
        assertEquals("Pendiente", pedido.getEstadoRastreo());
    }

    @Test
    void testPedidoEqualsAndHashCode() {
        Pedido pedido1 = Pedido.builder()
                .id(1L)
                .trackingId("PED-0001")
                .idComprador(1L)
                .idVendedor(2L)
                .build();
        
        Pedido pedido2 = Pedido.builder()
                .id(1L)
                .trackingId("PED-0001")
                .idComprador(1L)
                .idVendedor(2L)
                .build();
        
        assertEquals(pedido1, pedido2);
        assertEquals(pedido1.hashCode(), pedido2.hashCode());
    }
} 