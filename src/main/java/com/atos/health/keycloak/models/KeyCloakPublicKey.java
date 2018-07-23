/*
 * Copyright (C) 2018  Atos Spain SA. All rights reserved.
 * 
 * This file is part of the phs-backend.
 * 
 * KeyCloakPublicKey.java is free software: you can redistribute it and/or modify it under the 
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
 *			e-mail ana.quintero@atos.net 
 * 
 * KeyCloak public key
 */

package com.atos.health.keycloak.models;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class KeyCloakPublicKey {
	
	/**
	 * keycloak public key, set it into realm setting key tabs, copy public key
	 * 
	 * @param public key configured in KeyCloak server
	 * @return java security public key
	 */
	public static PublicKey getPublicKey(String public_keycloak) {
		PublicKey myKey = null;
		KeyFactory kf = null;
		X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(public_keycloak));
		
		try {
			kf = KeyFactory.getInstance("RSA");
			myKey = kf.generatePublic(spec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return myKey;
	}

}
