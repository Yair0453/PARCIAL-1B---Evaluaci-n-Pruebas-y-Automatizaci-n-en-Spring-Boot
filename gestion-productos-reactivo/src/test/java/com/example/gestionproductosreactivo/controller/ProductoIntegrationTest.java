package com.example.gestionproductosreactivo.controller;

import com.example.gestionproductosreactivo.model.Producto;
import com.example.gestionproductosreactivo.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ProductoIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductoRepository productoRepository;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto(null, "Smartphone", 800, 20);
        productoRepository.deleteAll().block(); // Limpiar la base de datos antes de cada prueba
    }

    @Test
    void crearProducto() {
        webTestClient.post().uri("/api/productos")
                .contentType(APPLICATION_JSON)
                .body(Mono.just(producto), Producto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Smartphone");
    }

    @Test
    void obtenerProductoPorId() {
        Producto savedProducto = productoRepository.save(producto).block();

        webTestClient.get().uri("/api/productos/{id}", savedProducto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Smartphone");
    }

    @Test
    void eliminarProducto() {
        Producto savedProducto = productoRepository.save(producto).block();

        webTestClient.delete().uri("/api/productos/{id}", savedProducto.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri("/api/productos/{id}", savedProducto.getId())
                .exchange()
                .expectStatus().isNotFound();
    }
}
