package com.copetilla.lab1.controllers;

import com.copetilla.lab1.dto.CategoriaDTO;
import com.copetilla.lab1.mappers.CategoriaMapper;
import com.copetilla.lab1.model.Categoria;
import com.copetilla.lab1.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    public CategoriaController(
            CategoriaService categoriaService,
            CategoriaMapper categoriaMapper
    ) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    @PreAuthorize("hasAnyRole('SUPER-ADMIN-ROLE', 'USER')")
    @GetMapping
    public List<CategoriaDTO> findAll() {
        return categoriaMapper.toDtoList(categoriaService.findAll());
    }

    @PreAuthorize("hasAnyRole('SUPER-ADMIN-ROLE', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.of(
                categoriaService.findById(id)
                        .map(categoriaMapper::toDto)
        );
    }

    @PreAuthorize("hasRole('SUPER-ADMIN-ROLE')")
    @PostMapping
    public ResponseEntity<CategoriaDTO> create(
            @RequestBody CategoriaDTO categoriaDTO
    ) {
        Categoria creada = categoriaService.create(
                categoriaMapper.toEntity(categoriaDTO)
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(categoriaMapper.toDto(creada));
    }

    @PreAuthorize("hasRole('SUPER-ADMIN-ROLE')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> update(
            @PathVariable Long id,
            @RequestBody CategoriaDTO categoriaDTO
    ) {
        return ResponseEntity.of(
                categoriaService.update(
                        id,
                        categoriaMapper.toEntity(categoriaDTO)
                ).map(categoriaMapper::toDto)
        );
    }

    @PreAuthorize("hasRole('SUPER-ADMIN-ROLE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return categoriaService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}