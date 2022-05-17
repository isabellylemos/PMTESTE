package org.generation.Mangrove.controller;

import java.util.List;

import javax.validation.Valid;

import org.generation.Mangrove.model.ProdutosModel;
import org.generation.Mangrove.repository.CategoriaRepository;
import org.generation.Mangrove.repository.ProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping ("/produtos")
@CrossOrigin (origins = "*", allowedHeaders = "*")
public class ProdutosController {
	
	@Autowired
	private ProdutosRepository produtosRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping("/all")
	public ResponseEntity<List<ProdutosModel>> getAll(){
		return ResponseEntity.ok(produtosRepository.findAll());
	}
	@GetMapping ("/{id}")
	public ResponseEntity<ProdutosModel>getById(@PathVariable Long id){
		return produtosRepository.findById(id)
				.map (resposta-> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND ).build());
	}
	@GetMapping("/nome/{nomeProduto}")
	public ResponseEntity<List<ProdutosModel>> getByTitulo(@PathVariable String nomeProduto){
		return ResponseEntity.ok(produtosRepository.findAllByNomeProdutoContainingIgnoreCase(nomeProduto));
		
	}
	 @PostMapping("/cadastrar")
	    public ResponseEntity<ProdutosModel> post (@Valid @RequestBody ProdutosModel produto ){
		   if (categoriaRepository.existsById(produto.getCategoria().getId()))
		    return ResponseEntity.status(HttpStatus.CREATED).body (produtosRepository.save(produto));
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	
	 }
	 @PutMapping("/atualizar")
	   public ResponseEntity<ProdutosModel> put(@Valid @RequestBody ProdutosModel produto) {
	       if (produtosRepository.existsById(produto.getId())) {
	           if (categoriaRepository.existsById(produto.getCategoria().getId()))
	               return ResponseEntity.status(HttpStatus.OK).body(produtosRepository.save(produto));
	           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	     }
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }
	 @ResponseStatus(HttpStatus.NO_CONTENT)
	   @DeleteMapping("/{id}")
	   public void delete(@PathVariable long id) {
		   java.util.Optional<ProdutosModel> Produto =produtosRepository.findById(id);
			
			if(Produto.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			
			produtosRepository.deleteById(id);
		   
	   }
}
