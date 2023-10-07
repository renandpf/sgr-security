package br.com.pupposoft.fiap.sgr.security.gateway.token;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import br.com.pupposoft.fiap.sgr.security.gateway.TokenGateway;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAdapterGateway implements TokenGateway {

	@Override
	public String generate(Map<String, Object> infos) {
		try {
			log.trace("Start infos={}", infos);
			
			final String tokenSecretKey = System.getenv("TOKEN_SECRET_KEY");
			final String tokenExpirationTimeInsecond = System.getenv("TOKEN_EXPIRATION_TIME_IN_SECOND");
			Date expirationDate = Date.from(LocalDateTime.now().plusSeconds(Long.parseLong(tokenExpirationTimeInsecond)).atZone(ZoneId.systemDefault()).toInstant());
			
			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

			Date now = new Date();

			byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(tokenSecretKey);
			SecretKeySpec signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

			JwtBuilder builder = Jwts.builder()
					.setId(UUID.randomUUID().toString())
					.setIssuedAt(now)
					.setSubject("sgr-authorization-token")
					.setIssuer("SGR")
					.setExpiration(expirationDate)
					.setClaims(infos)
					.signWith(signatureAlgorithm, signingKey);

			String jwt = builder.compact();
			log.trace("End jwt={}", "***");
			return jwt;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}	
	}

}
