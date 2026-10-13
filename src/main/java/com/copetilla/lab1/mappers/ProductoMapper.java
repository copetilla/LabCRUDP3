package com.copetilla.lab1.mappers;

import com.copetilla.lab1.dto.ProductoDTO;
import com.copetilla.lab1.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductoMapper {

    @Mapping(target = "categoriaId", source = "categoria.id")
    ProductoDTO toDto(Producto producto);

    @Mapping(target = "categoria", ignore = true)
    Producto toEntity(ProductoDTO productoDTO);

    List<ProductoDTO> toDtoList(List<Producto> productos);
}