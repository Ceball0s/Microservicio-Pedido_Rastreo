package com.PedidoRastreo;

import com.PedidoRastreo.Models.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private Pedido pedido1;
    private Pedido pedido2;
    private PedidoController.CrearPedidoRequest request;

    @BeforeEach
    void setUp() {
        pedido1 = Pedido.builder()
                .id(1L)
                .trackingId("PED-0001")
                .idComprador(1L)
                .idVendedor(2L)
                .productoNombre("Producto 1")
                .productoDescripcion("Descripción del producto 1")
                .precioFinal(100.0)
                .estadoRastreo("Pendiente de envío")
                .fechaCreacion(new Date())
                .build();

        pedido2 = Pedido.builder()
                .id(2L)
                .trackingId("PED-0002")
                .idComprador(2L)
                .idVendedor(3L)
                .productoNombre("Producto 2")
                .productoDescripcion("Descripción del producto 2")
                .precioFinal(200.0)
                .estadoRastreo("En camino")
                .fechaCreacion(new Date())
                .build();

        request = new PedidoController.CrearPedidoRequest();
        request.setIdComprador(1L);
        request.setIdVendedor(2L);
        request.setNombreProducto("Producto 1");
        request.setDescripcionProducto("Descripción del producto 1");
        request.setPrecioFinal(100.0);
    }

    @Test
    void crearPedido_DeberiaRetornarPedidoCreado() {
        // Arrange
        when(pedidoService.crearPedido(1L, 2L, "Producto 1", "Descripción del producto 1", 100.0))
                .thenReturn(pedido1);

        // Act
        Pedido resultado = pedidoController.crearPedido(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedido1.getTrackingId(), resultado.getTrackingId());
        assertEquals(pedido1.getIdComprador(), resultado.getIdComprador());
        verify(pedidoService).crearPedido(1L, 2L, "Producto 1", "Descripción del producto 1", 100.0);
    }

    @Test
    void consultarPedido_ConTrackingIdExistente_DeberiaRetornarPedido() {
        // Arrange
        when(pedidoService.consultarPorTracking("PED-0001")).thenReturn(pedido1);

        // Act
        Pedido resultado = pedidoController.consultarPedido("PED-0001");

        // Assert
        assertNotNull(resultado);
        assertEquals(pedido1.getTrackingId(), resultado.getTrackingId());
        verify(pedidoService).consultarPorTracking("PED-0001");
    }

    @Test
    void consultarPedido_ConTrackingIdInexistente_DeberiaLanzarExcepcion() {
        // Arrange
        when(pedidoService.consultarPorTracking("PED-9999"))
                .thenThrow(new RuntimeException("No se encontró el pedido con ID: PED-9999"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoController.consultarPedido("PED-9999");
        });
        
        assertEquals("No se encontró el pedido con ID: PED-9999", exception.getMessage());
        verify(pedidoService).consultarPorTracking("PED-9999");
    }

    @Test
    void listarPedidosDeUsuario_DeberiaRetornarListaDePedidos() {
        // Arrange
        List<Pedido> pedidosUsuario = Arrays.asList(pedido1, pedido2);
        when(pedidoService.listarPedidosDeUsuario(1L)).thenReturn(pedidosUsuario);

        // Act
        List<Pedido> resultado = pedidoController.listarPedidosDeUsuario(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pedidoService).listarPedidosDeUsuario(1L);
    }

    @Test
    void listarPedidosDeUsuario_ConUsuarioSinPedidos_DeberiaRetornarListaVacia() {
        // Arrange
        when(pedidoService.listarPedidosDeUsuario(999L)).thenReturn(Arrays.asList());

        // Act
        List<Pedido> resultado = pedidoController.listarPedidosDeUsuario(999L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(pedidoService).listarPedidosDeUsuario(999L);
    }

    @Test
    void crearPedido_DeberiaManejarExcepciones() {
        // Arrange
        when(pedidoService.crearPedido(any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Error al crear pedido"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoController.crearPedido(request);
        });
        
        assertEquals("Error al crear pedido", exception.getMessage());
        verify(pedidoService).crearPedido(1L, 2L, "Producto 1", "Descripción del producto 1", 100.0);
    }

    @Test
    void listarPedidosDeUsuario_DeberiaManejarExcepciones() {
        // Arrange
        when(pedidoService.listarPedidosDeUsuario(any()))
                .thenThrow(new RuntimeException("Error al listar pedidos"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoController.listarPedidosDeUsuario(1L);
        });
        
        assertEquals("Error al listar pedidos", exception.getMessage());
        verify(pedidoService).listarPedidosDeUsuario(1L);
    }
} 