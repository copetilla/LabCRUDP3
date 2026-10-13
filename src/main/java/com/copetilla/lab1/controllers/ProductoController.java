package com.copetilla.lab1.controllers;

import com.copetilla.lab1.dto.ProductoDTO;
import com.copetilla.lab1.mappers.ProductoMapper;
import com.copetilla.lab1.model.Producto;
import com.copetilla.lab1.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    public ProductoController(
            ProductoService productoService,
            ProductoMapper productoMapper
    ) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    @PreAuthorize("hasAnyRole('SUPER-ADMIN-ROLE', 'USER')")
    @GetMapping
    public List<ProductoDTO> findAll() {
        return productoMapper.toDtoList(productoService.findAll());
    }

    @PreAuthorize("hasAnyRole('SUPER-ADMIN-ROLE', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.of(
                productoService.findById(id)
                        .map(productoMapper::toDto)
        );
    }

    @PreAuthorize("hasRole('SUPER-ADMIN-ROLE')")
    @PostMapping
    public ResponseEntity<ProductoDTO> create(
            @RequestBody ProductoDTO productoDTO
    ) {
        Producto creado = productoService.create(
                productoMapper.toEntity(productoDTO),
                productoDTO.categoriaId()
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(productoMapper.toDto(creado));
    }

    @PreAuthorize("hasRole('SUPER-ADMIN-ROLE')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> update(
            @PathVariable Long id,
            @RequestBody ProductoDTO productoDTO
    ) {
        return ResponseEntity.of(
                productoService.update(
                        id,
                        productoMapper.toEntity(productoDTO),
                        productoDTO.categoriaId()
                ).map(productoMapper::toDto)
        );
    }

    @PreAuthorize("hasRole('SUPER-ADMIN-ROLE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productoService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}