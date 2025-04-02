package com.example.gestionproductosreactivo.service;

import com.example.gestionproductosreactivo.model.Producto;
import com.example.gestionproductosreactivo.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto("1", "Laptop", 1500, 10);
    }

    @Test
    void listarProductos() {
        when(productoRepository.findAll()).thenReturn(Flux.just(producto));

        Flux<Producto> productos = productoService.listarProductos();

        StepVerifier.create(productos)
                .expectNext(producto)
                .verifyComplete();

        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void obtenerProductoPorId() {
        when(productoRepository.findById("1")).thenReturn(Mono.just(producto));

        Mono<Producto> productoMono = productoService.obtenerProductoPorId("1");

        StepVerifier.create(productoMono)
                .expectNext(producto)
                .verifyComplete();

        verify(productoRepository, times(1)).findById("1");
    }

    @Test
    void crearProducto() {
        when(productoRepository.save(producto)).thenReturn(Mono.just(producto));

        Mono<Producto> productoCreado = productoService.crearProducto(producto);

        StepVerifier.create(productoCreado)
                .expectNext(producto)
                .verifyComplete();

        verify(productoRepository, times(1)).save(producto);
    }
}
