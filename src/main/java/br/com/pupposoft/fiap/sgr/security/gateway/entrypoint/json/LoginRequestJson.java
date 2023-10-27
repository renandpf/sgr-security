package br.com.pupposoft.fiap.sgr.security.gateway.entrypoint.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class LoginRequestJson {
	private String username;
	private String password;
}
