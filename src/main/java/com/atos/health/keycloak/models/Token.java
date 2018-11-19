/*
 * Copyright (C) 2018  Atos Spain SA. All rights reserved.
 * 
 * This file is part of the phs-backend.
 * 
 * Token.java is free software: you can redistribute it and/or modify it under the 
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
 * Class to manage tokens
 */

package com.atos.health.keycloak.models;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Token {
	private String id;
	private String expiration;
	private String info;

	@Override
	public String toString() {
		return new Gson().toJson(this, Token.class);
	} 
}
