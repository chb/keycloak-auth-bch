/*
 * Copyright (C) 2018  Atos Spain SA. All rights reserved.
 * 
 * This file is part of the phs-backend.
 * 
 * AuthController.java is free software: you can redistribute it and/or modify it under the 
 * terms of the Apache License, Version 2.0 (the License);
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * The software is provided "AS IS", without any warranty of any kind, express or implied,
 * including but not limited to the warranties of merchantability, fitness for a particular
 * purpose and noninfringement, in no event shall the authors or copyright holders be 
 * liable for any claim, damages or other liability, whether in action of contract, tort or
 * otherwise, arising from, out of or in connection with the software or the use or other
 * dealings in the software.
 * 
 * See README file for the full disclaimer information and LICENSE file for full license 
 * information in the project root.
 * 
 * @author	Miriam Quintero Padrón
 *			Atos Research and Innovation, Atos SPAIN SA
 * 
 * Spring boot controller for KeyCloak auth
 */

package com.atos.health.keycloak.ws;

import java.util.Date;

import org.keycloak.RSATokenVerifier;
import org.keycloak.TokenVerifier;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.VerificationException;
import org.keycloak.common.util.Time;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.atos.health.keycloak.models.KeyCloakUser;
import com.atos.health.keycloak.models.Token;
import com.atos.health.keycloak.models.IsValid;
import com.atos.health.keycloak.models.KeyCloakPublicKey;

@RestController
@EnableAutoConfiguration
public class AuthController {

	// Constants
	@Value("${client_id}")
	private String client_id;

	@Value("${realm}")
	private String realm;

	@Value("${url_keycloak}")
	private String url_keycloak;

	@Value("${public_key}")
	private String public_key;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	/**
	 * Cheack if a token is valid (or not)
	 * 
	 * @param Token
	 *            to validate
	 * @return true if the token is valid
	 */
	private boolean isValid(Token myToken) {
		boolean isvalid = false;
		AccessToken result;

		try {
			result = RSATokenVerifier.verifyToken(myToken.getId(),
					KeyCloakPublicKey.getPublicKey(public_key), url_keycloak + "realms/" + realm, true, true);

			isvalid = result.isActive();
			Date expiration = Time.toDate(result.getExpiration());

			result.issuedAt(Time.currentTime());

			int resto = (result.getExpiration() - Time.currentTime());

			myToken.setInfo(" Active: " + result.isActive() + " Expiration datetime: " + expiration + " LeftTime: "
					+ resto + " seconds.");

			LOGGER.info(myToken.getInfo());

		} catch (VerificationException e) {
			return false;
		}

		return isvalid;
	}

	/**
	 * Gets keycloak credentias
	 * 
	 * @param userName to access
	 * @param password to access
	 * @return keycloak credentials if credentials are ok (username and login)
	 */
	private Keycloak getKeycloak(String userName, String password) {
		Keycloak kc = Keycloak.getInstance(url_keycloak, realm, userName, password, client_id);
		return kc;
	}

	/**
	 * Gets keycloak credentias by token
	 * 
	 * @param token to access (it must to be a valid one)
	 * @return KeyCloak instance
	 */
	private Keycloak getKeycloak(String token) {
		Keycloak kc = Keycloak.getInstance(url_keycloak, realm, client_id, token);

		return kc;
	}

	/**
	 * Set if a required token is valid (true) or not (false)
	 * 
	 * @param token
	 *            to valid, this token is returned into login
	 * @return true if token is valid (has access)
	 */
	@RequestMapping(value = "/isvalid", method = RequestMethod.GET)
	IsValid isvalid(@RequestHeader("token") String token) {

		Token myToken = Token.builder().id(token).build();
		IsValid isValid = IsValid.builder().valid(isValid(myToken)).tokenInfo(myToken.getInfo()).build();
		return isValid;
	}

	/**
	 * Login with user and password
	 * 
	 * @param username,
	 *            valid user
	 * @param password,
	 *            valid password
	 * @return Token with the information
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	JSONObject login(@RequestHeader(value = "username") String username,
//			@RequestHeader(value = "password") String password) {
	AccessTokenResponse login(@RequestHeader(value = "username") String username,
			@RequestHeader(value = "password") String password) {

		Keycloak keycloak = null;
//		JSONObject obj = new JSONObject();

//		String authToken = "INVALID";
//		long oldLifespan = 0;
//		String error = "";
//		String refreshToken = "";
		AccessTokenResponse accessToken = null;

		try {

			keycloak = getKeycloak(username, password);
			// Gets authorization token (if it is correct)
			// authToken = keycloak.tokenManager().getAccessTokenString(); 

			accessToken = keycloak.tokenManager().getAccessToken();
			//refreshToken = accesstoken.getRefreshToken();
			// oldLifespan = accesstoken.getExpiresIn();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IllegalAccessError(ex.getMessage());
		}

		/*obj.put("authToken", authToken);
		obj.put("Expiration in", oldLifespan + " seconds");
		obj.put("Refresh Token", refreshToken);
		obj.put("Error", error); 
		return obj; */
		return accessToken;
	}

	/**
	 * Logout from KeyCloak invalidating the token
	 * 
	 * @param token,
	 *            valid token to invalidate
	 * @return void
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	void logout(@RequestHeader(value = "token") String token) {

		Keycloak keycloak = null;

		try {
			keycloak = getKeycloak(token);

			// Invalidate the token
			if (keycloak != null)
				keycloak.tokenManager().invalidate(token);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IllegalAccessError(ex.getMessage());
		}
	}

	/**
	 * Get the user given the token
	 * 
	 * @param valid
	 *            token, this token is returned when login
	 * @return the username
	 * @throws VerificationException
	 */
	@GetMapping(value = "/user")
	KeyCloakUser user(@RequestHeader(value = "token") String token) throws VerificationException {

		@SuppressWarnings("deprecation")
		TokenVerifier<AccessToken> verifier = TokenVerifier.create(token, AccessToken.class).realmUrl(url_keycloak)
				.checkActive(false).checkTokenType(false);

		AccessToken tokenVerifier = null;
		try {
			tokenVerifier = verifier.getToken();
		} catch (VerificationException e) {
			e.printStackTrace();
			throw new VerificationException(e.getMessage());
		}

		KeyCloakUser result = new KeyCloakUser();
		result.setUsername(tokenVerifier.getPreferredUsername());
		result.setSession(tokenVerifier.getSessionState());
		return result;
	}
}