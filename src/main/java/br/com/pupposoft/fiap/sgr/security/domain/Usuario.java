package br.com.pupposoft.fiap.sgr.security.domain;

import lombok.Getter;

@Getter
public class Usuario {
	private Long id;
	private String cpf; 
	private String senha;
	private Perfil perfil;
}
