package com.PedidoRastreo;

import com.PedidoRastreo.Models.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PedidoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PedidoRepository pedidoRepository;

    private Pedido pedido1;
    private Pedido pedido2;
    private Pedido pedido3;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        entityManager.clear();

        // Crear pedidos de prueba
        pedido1 = Pedido.builder()
                .trackingId("TRK001")
                .idComprador(1L)
                .idVendedor(2L)
                .productoNombre("Producto 1")
                .productoDescripcion("Descripción del producto 1")
                .precioFinal(100.0)
                .estadoRastreo("CREADO")
                .fechaCreacion(new Date())
                .build();

        pedido2 = Pedido.builder()
                .trackingId("TRK002")
                .idComprador(2L)
                .idVendedor(3L)
                .productoNombre("Producto 2")
                .productoDescripcion("Descripción del producto 2")
                .precioFinal(200.0)
                .estadoRastreo("EN_TRANSITO")
                .fechaCreacion(new Date())
                .build();

        pedido3 = Pedido.builder()
                .trackingId("TRK003")
                .idComprador(1L)
                .idVendedor(4L)
                .productoNombre("Producto 3")
                .productoDescripcion("Descripción del producto 3")
                .precioFinal(300.0)
                .estadoRastreo("ENTREGADO")
                .fechaCreacion(new Date())
                .build();
    }

    @Test
    void save_DeberiaGuardarPedido() {
        // Act
        Pedido pedidoGuardado = pedidoRepository.save(pedido1);

        // Assert
        assertNotNull(pedidoGuardado.getId());
        assertEquals(pedido1.getTrackingId(), pedidoGuardado.getTrackingId());
        assertEquals(pedido1.getIdComprador(), pedidoGuardado.getIdComprador());
        assertEquals(pedido1.getIdVendedor(), pedidoGuardado.getIdVendedor());
        assertEquals("CREADO", pedidoGuardado.getEstadoRastreo());
    }

    @Test
    void findById_ConIdExistente_DeberiaRetornarPedido() {
        // Arrange
        Pedido pedidoGuardado = entityManager.persistAndFlush(pedido1);

        // Act
        Optional<Pedido> resultado = pedidoRepository.findById(pedidoGuardado.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(pedido1.getTrackingId(), resultado.get().getTrackingId());
        assertEquals(pedido1.getIdComprador(), resultado.get().getIdComprador());
    }

    @Test
    void findById_ConIdInexistente_DeberiaRetornarEmpty() {
        // Act
        Optional<Pedido> resultado = pedidoRepository.findById(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void findByTrackingId_ConTrackingIdExistente_DeberiaRetornarPedido() {
        // Arrange
        entityManager.persistAndFlush(pedido1);

        // Act
        Optional<Pedido> resultado = pedidoRepository.findByTrackingId("TRK001");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(pedido1.getTrackingId(), resultado.get().getTrackingId());
        assertEquals(pedido1.getIdComprador(), resultado.get().getIdComprador());
    }

    @Test
    void findByTrackingId_ConTrackingIdInexistente_DeberiaRetornarEmpty() {
        // Act
        Optional<Pedido> resultado = pedidoRepository.findByTrackingId("TRK999");

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void findAllByIdCompradorOrIdVendedor_ConUsuarioExistente_DeberiaRetornarPedidosDelUsuario() {
        // Arrange
        entityManager.persistAndFlush(pedido1); // idComprador = 1L
        entityManager.persistAndFlush(pedido2); // idComprador = 2L
        entityManager.persistAndFlush(pedido3); // idComprador = 1L

        // Act
        List<Pedido> resultado = pedidoRepository.findAllByIdCompradorOrIdVendedor(1L, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(pedido -> assertTrue(
            pedido.getIdComprador().equals(1L) || pedido.getIdVendedor().equals(1L)
        ));
    }

    @Test
    void findAllByIdCompradorOrIdVendedor_ConUsuarioInexistente_DeberiaRetornarListaVacia() {
        // Act
        List<Pedido> resultado = pedidoRepository.findAllByIdCompradorOrIdVendedor(999L, 999L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void save_ConPedidoModificado_DeberiaActualizarPedido() {
        // Arrange
        Pedido pedidoGuardado = entityManager.persistAndFlush(pedido1);
        String nuevoEstado = "EN_TRANSITO";
        pedidoGuardado.setEstadoRastreo(nuevoEstado);

        // Act
        Pedido pedidoActualizado = pedidoRepository.save(pedidoGuardado);

        // Assert
        assertEquals(nuevoEstado, pedidoActualizado.getEstadoRastreo());
        assertEquals(pedidoGuardado.getId(), pedidoActualizado.getId());
    }

    @Test
    void findAll_DeberiaRetornarTodosLosPedidos() {
        // Arrange
        entityManager.persistAndFlush(pedido1);
        entityManager.persistAndFlush(pedido2);
        entityManager.persistAndFlush(pedido3);

        // Act
        List<Pedido> resultado = pedidoRepository.findAll();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.size() >= 3);
    }

    @Test
    void delete_DeberiaEliminarPedido() {
        // Arrange
        Pedido pedidoGuardado = entityManager.persistAndFlush(pedido1);
        Long pedidoId = pedidoGuardado.getId();

        // Act
        pedidoRepository.delete(pedidoGuardado);
        entityManager.flush();

        // Assert
        Optional<Pedido> pedidoEliminado = pedidoRepository.findById(pedidoId);
        assertFalse(pedidoEliminado.isPresent());
    }

    @Test
    void save_DeberiaInicializarCamposPorDefecto() {
        // Act
        Pedido pedidoGuardado = pedidoRepository.save(pedido1);

        // Assert
        assertNotNull(pedidoGuardado.getFechaCreacion());
        assertEquals("CREADO", pedidoGuardado.getEstadoRastreo());
    }
} 