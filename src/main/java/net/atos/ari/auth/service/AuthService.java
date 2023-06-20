/*
 * Copyright (C) 2018  Atos Spain SA. All rights reserved.
 *
 * This file is part of the Auth project.
 *
 * This is free software: you can redistribute it and/or modify it under the
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
 * Auth Spring boot application
 */
package net.atos.ari.auth.service;

import net.atos.ari.auth.exception.NotAuthorizedException;
import net.atos.ari.auth.model.AccessTokenResponse;
import net.atos.ari.auth.model.KeycloakUser;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthService implements Service {

	public static final Logger log = LoggerFactory.getLogger(AuthService.class);

	//Constants
	@Value("${keycloak.url}")
	private String keycloakUrl;

	@Value("${keycloak.realm}")
	private String keycloakRealm;

	@Value("${keycloak.client_id}")
	private String keycloakClientId;


	HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
	RestTemplate restTemplate = new RestTemplate(factory);
	private static final String BEARER = "BEARER ";

	@Override
	public AccessTokenResponse login(KeycloakUser user) throws NotAuthorizedException {
		System.out.println("New login receivedd");
		try {
			String uri = keycloakUrl + "/realms/" + keycloakRealm +
					"/protocol/openid-connect/token";
			String data = "grant_type=password&username="+
					user.getUsername()+"&password="+user.getPassword()+"&client_id="+
					keycloakClientId;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/x-www-form-urlencoded");

			HttpEntity<String> entity = new HttpEntity<String>(data, headers);
			System.out.println(entity.getHeaders());
			System.out.println(entity.getBody());

			ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(uri,
					HttpMethod.POST, entity, AccessTokenResponse.class);
			System.out.println(response);

			if (response.getStatusCode().value() != HttpStatus.SC_OK) {
				log.error("Unauthorised access to protected resource", response.getStatusCode().value());
				log.error("Body: ", response.getBody().toString());
				throw new NotAuthorizedException("Unauthorised access to protected resource");
			}
			return response.getBody();
		} catch (Exception ex) {
			log.error("|Unauthorised access to protected resource", ex);
			throw new NotAuthorizedException("Unauthorised access to protected resource");
		}
	}

	public void logout(String refresh_token) throws NotAuthorizedException{
		try {
			String uri = keycloakUrl + "/realms/" + keycloakRealm +
					"/protocol/openid-connect/logout";
			String data = "client_id=" + keycloakClientId + "&refresh_token=" + refresh_token;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/x-www-form-urlencoded");

			HttpEntity<String> entity = new HttpEntity<String>(data, headers);

			ResponseEntity<String> response = restTemplate.exchange(uri,
					HttpMethod.POST, entity, String.class);
			System.out.println(response.getStatusCode());
		} catch (Exception e) {
			log.error("Unable to logout, invalid refresh token");
			throw new NotAuthorizedException("Unable to logout, invalid refresh token");
		}
	}

	@Override
	public String user(String authToken) throws NotAuthorizedException {

		if (! authToken.toUpperCase().startsWith(BEARER)) {
			throw new NotAuthorizedException("Invalid OAuth Header. Missing Bearer prefix");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authToken);
		headers.set("Content-Type", "application/x-www-form-urlencoded");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
				keycloakUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/userinfo",
				HttpMethod.GET,
				entity,
				String.class);

		if (response.getStatusCode().value() != HttpStatus.SC_OK) {
			log.error("OAuth2 Authentication failure. "
					+ "Invalid OAuth Token supplied in Authorization Header on Request. Code {}", response.getStatusCode().value());
			throw new NotAuthorizedException("OAuth2 Authentication failure. "
					+ "Invalid OAuth Token supplied in Authorization Header on Request.");
		}

		return response.getBody();
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}