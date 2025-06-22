package com.Pedido_Rastreo.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "pedidos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String trackingId; // e.g., PED-0001

    private Long idComprador;
    private Long idVendedor;

    private String productoNombre;
    private String productoDescripcion;

    private Double precioFinal;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    private String estadoRastreo; // Ejemplo: "Pendiente", "En camino", "Entregado"
}
