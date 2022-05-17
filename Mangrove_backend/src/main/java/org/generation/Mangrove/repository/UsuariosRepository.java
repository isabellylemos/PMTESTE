package org.generation.Mangrove.repository;

import java.util.Optional;
import org.generation.Mangrove.model.UsuariosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosRepository extends JpaRepository<UsuariosModel, Long> {
	public Optional<UsuariosModel> findByEmailUsuario(String usuario);

}
