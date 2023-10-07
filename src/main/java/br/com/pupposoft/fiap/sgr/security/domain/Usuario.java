package br.com.pupposoft.fiap.sgr.security.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString(exclude = "senha")
public class Usuario {
	private Long id;
	private String cpf; 
	private String senha;
	private Perfil perfil;
}
