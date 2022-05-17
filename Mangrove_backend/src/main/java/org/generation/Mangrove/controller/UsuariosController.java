package org.generation.Mangrove.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.generation.Mangrove.model.UsuariosLogin;
import org.generation.Mangrove.model.UsuariosModel;
import org.generation.Mangrove.repository.UsuariosRepository;
import org.generation.Mangrove.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = " * ", allowedHeaders = " * ")
public class UsuariosController {

	@Autowired
	private UsuariosRepository usuariosRepository;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/all")
	public ResponseEntity<List<UsuariosModel>> getAll() {
		return ResponseEntity.ok(usuariosRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuariosModel> getById(@PathVariable Long id) {
		return usuariosRepository.findById(id).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping("/logar")
	public ResponseEntity<UsuariosLogin> login(@RequestBody Optional<UsuariosLogin> usuarioLogin) {
		return usuarioService.autenticarUsuario(usuarioLogin).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}

	@PostMapping("/cadastrar")
	public ResponseEntity<UsuariosModel> postUsuario(@Valid @RequestBody UsuariosModel usuario) {

		return usuarioService.cadastrarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

	}

	@PutMapping("/atualizar")
	public ResponseEntity<UsuariosModel> putUsuario(@Valid @RequestBody UsuariosModel usuario) {
		return usuarioService.atualizarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

}
