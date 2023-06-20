/*
 * Copyright (C) 2018  Atos Spain SA. All rights reserved.
 *
 * This file is part of the KeyCloak Auth API.
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
 * Spring boot controller for KeyCloak auth
 */

package net.atos.ari.auth.controller;

import org.chip.ihl.certificates.Models.SigningRequest;
import org.chip.ihl.certificates.Services.SigningSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.http.HttpHeaders;

import net.atos.ari.auth.exception.NotAuthorizedException;
import net.atos.ari.auth.model.AccessTokenResponse;
import net.atos.ari.auth.model.KeycloakUser;
import net.atos.ari.auth.service.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

//@ComponentScan("org.chip.ihl.certificates")
@ComponentScan({ "net.atos.ari.auth", "org.chip.ihl.certificates" })
@RestController
@EnableAutoConfiguration
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	Service authService;

	@Autowired
	SigningSService sService;

	@ApiOperation(value = "Give OAuth access token given user and password")
	@CrossOrigin(origins="*", allowCredentials="true")
	@PostMapping("/login")
	public AccessTokenResponse login(@RequestBody KeycloakUser user)
			throws NotAuthorizedException {
		log.info("Login user");
		return authService.login(user);
	}

	@PostMapping("/logout")
	public void logout(@RequestBody String refreshToken)
			throws NotAuthorizedException {
		log.info("Login user");
		authService.logout(refreshToken);
	}

	@ApiOperation(value = "Give OAuth access token as cookie given user and password from a browser")
	@CrossOrigin(origins="*", allowCredentials="true")
	@PostMapping("/login_cookie")
	public AccessTokenResponse browserLogin(@RequestBody KeycloakUser user, HttpServletResponse response)
			throws NotAuthorizedException {
		AccessTokenResponse token = authService.login(user);
		response.addCookie(new Cookie("access_token", "BEARER " + token.getAccess_token()));
		response.addCookie(new Cookie("token_type", token.getToken_type()));
		response.addCookie(new Cookie("refresh_token", token.getRefresh_token()));
		response.addCookie(new Cookie("expires_in", token.getExpires_in().toString()));
		response.addCookie(new Cookie("jti", token.getJti()));
		log.info("Login user browser");
		return token;
	}



	@ApiOperation(value = "Provide the user preferred name given the token")
	@GetMapping("/user")
	public String user(@ApiParam(value="Bearer <token>") @RequestHeader(HttpHeaders.AUTHORIZATION) String token)
			throws NotAuthorizedException {
		log.info("Get User info");
		return authService.user(token);
	}

	@ApiOperation(value = "Sign an identity certificate for me")
	@PostMapping("/sign")
	public String sign(@ApiParam(value="Bearer <token>") @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody SigningRequest sReq, HttpServletResponse response)
			throws NotAuthorizedException {
		//
		// Certificate signing requests should only be authorized when the OAuth2-authenticated principal mnatched the CN in the request
		// or the client could ask for a different user's certificate to be signed.
		//
		String principal = authService.user(token);
		log.info("Signing request received for " + principal + "\nCSR = " + sReq.getCsrB64());

		String signedB64 = sService.sign(sReq, principal);
		if(signedB64 == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "Bad request or CSR CN doesn't match authenticated principal";
		}
		return signedB64;
	}

}