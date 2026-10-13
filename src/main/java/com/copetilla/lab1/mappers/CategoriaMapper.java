package com.copetilla.lab1.mappers;

import com.copetilla.lab1.dto.CategoriaDTO;
import com.copetilla.lab1.model.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoriaMapper {

    CategoriaDTO toDto(Categoria categoria);

    Categoria toEntity(CategoriaDTO categoriaDTO);

    List<CategoriaDTO> toDtoList(List<Categoria> categorias);
}