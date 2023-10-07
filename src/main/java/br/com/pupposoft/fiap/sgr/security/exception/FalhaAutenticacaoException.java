package br.com.pupposoft.fiap.sgr.security.exception;

import lombok.Getter;

@Getter
public class FalhaAutenticacaoException extends RuntimeException {
	private static final long serialVersionUID = 344604220304910588L;
	private Integer httpStatus = 401;
}
