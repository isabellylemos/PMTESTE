package org.generation.Mangrove.repository;

import java.util.List;

import org.generation.Mangrove.model.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {
	public List <CategoriaModel> findAllByNomeCategoriaContainingIgnoreCase(String nomeCategoria);

}
