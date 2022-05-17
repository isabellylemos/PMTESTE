package org.generation.Mangrove.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.generation.Mangrove.model.UsuariosLogin;
import org.generation.Mangrove.model.UsuariosModel;
import org.generation.Mangrove.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

       
	   @Autowired 
	   private UsuariosRepository usuariosRepository;
	   
	   public Optional<UsuariosModel> cadastrarUsuario(UsuariosModel usuario){
		   
		    if(usuariosRepository.findByEmailUsuario(usuario.getEmailUsuario()).isPresent())
		    	return Optional.empty();
		   
		   usuario.setPasswordUsuario(criptografarSenha(usuario.getPasswordUsuario()));
		   return Optional.of(usuariosRepository.save(usuario));
		        
	   }
	
	   
	    public Optional<UsuariosModel> atualizarUsuario(UsuariosModel usuario){
	    	
	    	if(usuariosRepository.findById(usuario.getId()).isPresent()) {
	    		Optional<UsuariosModel> buscaUsuario = usuariosRepository.findByEmailUsuario(usuario.getEmailUsuario());
	    		
	    		
	    		if((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
	    			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Usuário já existe",null); 
	    	         
	            usuario.setPasswordUsuario(criptografarSenha(usuario.getPasswordUsuario()));
	   		    return Optional.ofNullable(usuariosRepository.save(usuario));
	   	
	    	}
	    	
	    	return Optional.empty();
	    		
	    }
	    
	    public Optional<UsuariosLogin> autenticarUsuario(Optional<UsuariosLogin> usuarioLogin) {

			Optional<UsuariosModel> usuario = usuariosRepository.findByEmailUsuario(usuarioLogin.get().getEmailUsuario());
			
			    if (usuario.isPresent()) {

			     if (compararSenhas(usuarioLogin.get().getPasswordUsuario(), usuario.get().getPasswordUsuario())) {

			    	    usuarioLogin.get().setId(usuario.get().getId());
						usuarioLogin.get().setNomeUsuario(usuario.get().getNomeUsuario());
						usuarioLogin.get().setFotoUsuario(usuario.get().getFotoUsuario());
						usuarioLogin.get().setDescricaoUsuario(usuario.get().getDescricaoUsuario());
						usuarioLogin.get().setTipoUsuario(usuario.get().getTipoUsuario());
						usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getEmailUsuario(), usuarioLogin.get().getPasswordUsuario()));
						usuarioLogin.get().setPasswordUsuario(usuario.get().getPasswordUsuario());
				
					return  usuarioLogin ;
				}
			}	
			
			    return Optional.empty();
			
		  }

		   private String criptografarSenha(String senha) {
			   BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			   return encoder.encode(senha);
	        }
		

		   private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
			
			   BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			
			   return encoder.matches(senhaDigitada, senhaBanco);
		}

		    private String gerarBasicToken(String usuario, String senha) {

		    	String token = usuario + ":" + senha;
				byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
				return "Basic " + new String(tokenBase64);

		}	
}
