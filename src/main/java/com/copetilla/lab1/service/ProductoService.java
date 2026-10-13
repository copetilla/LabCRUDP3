package com.copetilla.lab1.service;

import com.copetilla.lab1.model.Categoria;
import com.copetilla.lab1.model.Producto;
import com.copetilla.lab1.repository.CategoriaRepository;
import com.copetilla.lab1.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository
    ) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional
    public Producto create(Producto producto, Long categoriaId) {
        Categoria categoria = buscarCategoria(categoriaId);

        producto.setId(null);
        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }

    @Transactional
    public Optional<Producto> update(
            Long id,
            Producto datos,
            Long categoriaId
    ) {
        return productoRepository.findById(id).map(producto -> {
            Categoria categoria = buscarCategoria(categoriaId);

            producto.setNombre(datos.getNombre());
            producto.setDescripcion(datos.getDescripcion());
            producto.setPrecio(datos.getPrecio());
            producto.setCantidadStock(datos.getCantidadStock());
            producto.setCategoria(categoria);

            return productoRepository.save(producto);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (!productoRepository.existsById(id)) {
            return false;
        }

        productoRepository.deleteById(id);
        return true;
    }

    private Categoria buscarCategoria(Long categoriaId) {
        if (categoriaId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debés indicar la categoría del producto"
            );
        }

        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "La categoría indicada no existe"
                ));
    }
}