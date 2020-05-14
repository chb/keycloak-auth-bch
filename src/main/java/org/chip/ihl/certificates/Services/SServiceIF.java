package org.chip.ihl.certificates.Services;

import lombok.SneakyThrows;
import org.chip.ihl.certificates.Models.SigningRequest;
import org.springframework.stereotype.Component;

import net.atos.ari.auth.exception.NotAuthorizedException;
import net.atos.ari.auth.model.AccessTokenResponse;
import net.atos.ari.auth.model.KeycloakUser;

@Component
public interface SServiceIF {
    public String sign(SigningRequest csr, String forPrincipal);
}

