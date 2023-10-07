package br.com.pupposoft.fiap.sgr.security.gateway.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {
	private String secretKey;
	private String issuer;
	private String subject;
	private LocalDateTime expiration;
	private Map<String, Object> infos;
	
	public Date getExpirationDate() {
		return Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());
	}

}
