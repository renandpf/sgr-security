package br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResponseJson {
	private String token;
}
