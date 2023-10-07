package br.com.pupposoft.fiap.sgr.security.gateway.token;

import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import javax.xml.bind.DatatypeConverter;

import br.com.pupposoft.fiap.sgr.security.gateway.TokenGateway;
import br.com.pupposoft.fiap.sgr.security.gateway.dto.TokenDto;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAdapterGateway implements TokenGateway {

	@Override
	public String generate(TokenDto dto) {
		try {
			log.trace("Start dto={}", dto);
			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

			Date now = new Date();

			byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(dto.getSecretKey());
			SecretKeySpec signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

			JwtBuilder builder = Jwts.builder()
					.setId(UUID.randomUUID().toString())
					.setIssuedAt(now)
					.setSubject(dto.getSubject())
					.setIssuer(dto.getIssuer())
					.setExpiration(dto.getExpirationDate())
					.setClaims(dto.getInfos())
					.signWith(signatureAlgorithm, signingKey);

			String jwt = builder.compact();
			log.trace("End jwt={}", "***");
			return jwt;

		} catch (Exception e) {
			log.warn("dto", dto);
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}	
	}

}
