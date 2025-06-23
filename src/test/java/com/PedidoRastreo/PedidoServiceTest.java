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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido1;
    private Pedido pedido2;

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
    }

    @Test
    void crearPedido_DeberiaGuardarPedido() {
        // Arrange
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido1);

        // Act
        Pedido resultado = pedidoService.crearPedido(1L, 2L, "Producto 1", "Descripción del producto 1", 100.0);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedido1.getTrackingId(), resultado.getTrackingId());
        assertEquals(pedido1.getIdComprador(), resultado.getIdComprador());
        assertEquals(pedido1.getIdVendedor(), resultado.getIdVendedor());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void consultarPorTracking_ConTrackingIdExistente_DeberiaRetornarPedido() {
        // Arrange
        when(pedidoRepository.findByTrackingId("PED-0001")).thenReturn(Optional.of(pedido1));

        // Act
        Pedido resultado = pedidoService.consultarPorTracking("PED-0001");

        // Assert
        assertNotNull(resultado);
        assertEquals(pedido1.getTrackingId(), resultado.getTrackingId());
        verify(pedidoRepository).findByTrackingId("PED-0001");
    }

    @Test
    void consultarPorTracking_ConTrackingIdInexistente_DeberiaLanzarExcepcion() {
        // Arrange
        when(pedidoRepository.findByTrackingId("PED-9999")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.consultarPorTracking("PED-9999");
        });
        
        assertEquals("No se encontró el pedido con ID: PED-9999", exception.getMessage());
        verify(pedidoRepository).findByTrackingId("PED-9999");
    }

    @Test
    void listarPedidosDeUsuario_DeberiaRetornarPedidosDelUsuario() {
        // Arrange
        List<Pedido> pedidosUsuario = Arrays.asList(pedido1, pedido2);
        when(pedidoRepository.findAllByIdCompradorOrIdVendedor(1L, 1L)).thenReturn(pedidosUsuario);

        // Act
        List<Pedido> resultado = pedidoService.listarPedidosDeUsuario(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pedidoRepository).findAllByIdCompradorOrIdVendedor(1L, 1L);
    }

    @Test
    void crearPedido_DeberiaGenerarTrackingIdUnico() {
        // Arrange
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedido = invocation.getArgument(0);
            pedido.setId(1L);
            return pedido;
        });

        // Act
        Pedido resultado1 = pedidoService.crearPedido(1L, 2L, "Producto 1", "Descripción 1", 100.0);
        Pedido resultado2 = pedidoService.crearPedido(3L, 4L, "Producto 2", "Descripción 2", 200.0);

        // Assert
        assertNotNull(resultado1.getTrackingId());
        assertNotNull(resultado2.getTrackingId());
        assertNotEquals(resultado1.getTrackingId(), resultado2.getTrackingId());
        assertTrue(resultado1.getTrackingId().startsWith("PED-"));
        assertTrue(resultado2.getTrackingId().startsWith("PED-"));
    }

    @Test
    void crearPedido_DeberiaInicializarCamposPorDefecto() {
        // Arrange
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedido = invocation.getArgument(0);
            pedido.setId(1L);
            return pedido;
        });

        // Act
        Pedido resultado = pedidoService.crearPedido(1L, 2L, "Producto Test", "Descripción Test", 150.0);

        // Assert
        assertNotNull(resultado.getFechaCreacion());
        assertEquals("Pendiente de envío", resultado.getEstadoRastreo());
        assertEquals(1L, resultado.getIdComprador());
        assertEquals(2L, resultado.getIdVendedor());
        assertEquals("Producto Test", resultado.getProductoNombre());
        assertEquals("Descripción Test", resultado.getProductoDescripcion());
        assertEquals(150.0, resultado.getPrecioFinal());
    }
} 