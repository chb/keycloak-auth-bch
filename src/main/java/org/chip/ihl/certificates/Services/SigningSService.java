package org.chip.ihl.certificates.Services;

import lombok.SneakyThrows;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.chip.ihl.certificates.CertificateSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.chip.ihl.certificates.Models.SigningRequest;

@Component
public class SigningSService implements SService {
    public static final Logger log = LoggerFactory.getLogger(SigningSService.class);

    @Autowired
    CertificateSigner cs ;

    @SneakyThrows
    @Override
    public String sign(SigningRequest csr, String forPrincipal) {
        String b64EncodedCSR = csr.getCsrB64();
        CertificationRequest cr = cs.PEMDecodeCSR(b64EncodedCSR);
        RDN cnRDN = cr.getCertificationRequestInfo().getSubject().getRDNs(BCStyle.CN)[0];
        String cn = IETFUtils.valueToString(cnRDN.getFirst().getValue());
        if (forPrincipal.equals(cn))
        {
            return cs.PEMEncodeCert(cs.sign(cr));
        } else {
            log.debug("SigningService.sign will not sign a certificate for " + forPrincipal + " that has a CN=" + cn);
            return null;
        }
    }
}
