/**
 * 
 */
package com.pinpad.ejb.util;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * @author H P
 *
 */
public class JWTUtil {
	
	// Loggers
	static final Logger logger = Logger.getLogger(JWTUtil.class.getName());
	
	// Strings
	private static final String SECRET = "pINp@d";
	
	// SimpleDateFormat
	private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	
	public static String createJWT(String idUser, long ttlMillis) 
	throws Exception {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setId("pinpad")
										   .setIssuedAt(now)
										   .setSubject("pINp@dW$")
										   .setIssuer(idUser)
										   .signWith(signatureAlgorithm, signingKey);

		// if it has been specified, let's add the expiration
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
			logger.info("Fecha Expiracion Bearer Token: "+ FORMAT_DATE.format(exp));
		}

		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}
	
	public static boolean tokenValido(String token) {
		boolean valido;
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
			valido = true;
		} catch (MalformedJwtException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			valido = false;
		} catch (ClaimJwtException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			valido = false;
		} catch (SignatureException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			valido = false;
		} catch (UnsupportedJwtException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			valido = false;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			valido = false;
		}
		return valido;
	}
	
	public static boolean tokenExpirado(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
			return false;
		} catch (ExpiredJwtException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			return true;
		}
	}
	
}
