package com.example.gestionproductosreactivo.repository;

import com.example.gestionproductosreactivo.model.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {
    Mono<Producto> findByNombre(String nombre);
}
