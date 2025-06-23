package com.PedidoRastreo.schedulers;

import com.PedidoRastreo.Models.Pedido;
import com.PedidoRastreo.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RastreoSchedulerTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private RastreoScheduler rastreoScheduler;

    private Pedido pedidoPendiente;
    private Pedido pedidoEnCamino;

    @BeforeEach
    void setUp() {
        pedidoPendiente = Pedido.builder()
                .id(1L)
                .trackingId("PED-0001")
                .idComprador(1L)
                .idVendedor(2L)
                .estadoRastreo("Pendiente de envío")
                .build();

        pedidoEnCamino = Pedido.builder()
                .id(2L)
                .trackingId("PED-0002")
                .idComprador(3L)
                .idVendedor(4L)
                .estadoRastreo("En camino")
                .build();
    }

    @Test
    void testRevisarCambiosDeRastreo() {
        List<Pedido> pedidos = Arrays.asList(pedidoPendiente, pedidoEnCamino);
        when(pedidoRepository.findAll()).thenReturn(pedidos);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoPendiente);

        rastreoScheduler.revisarCambiosDeRastreo();

        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testRevisarCambiosDeRastreoSinPedidosPendientes() {
        List<Pedido> pedidos = Arrays.asList(pedidoEnCamino);
        when(pedidoRepository.findAll()).thenReturn(pedidos);

        rastreoScheduler.revisarCambiosDeRastreo();

        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testIniciar() {
        rastreoScheduler.iniciar();
        // Este método solo imprime un mensaje, no hay mucho que testear
        // pero verificamos que no lance excepciones
    }
} 