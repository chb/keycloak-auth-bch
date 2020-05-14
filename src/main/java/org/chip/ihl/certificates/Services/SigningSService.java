package org.chip.ihl.certificates.Services;

import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.operator.OperatorCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.chip.ihl.certificates.Models.SigningRequest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;

@Component
public class SigningSService implements SServiceIF {
    public static final Logger log = LoggerFactory.getLogger(SigningSService.class);

    @Autowired
    CertificateSigner cs ;

    @Override
    public String sign(SigningRequest csr, String forPrincipal) {
        assert(cs != null);
        String b64EncodedCSR = csr.getCsrB64();
        log.debug("b64EncodedCSR=" + b64EncodedCSR);
        CertificationRequest cr = cs.PEMDecodeCSR(b64EncodedCSR);
        RDN cnRDN = cr.getCertificationRequestInfo().getSubject().getRDNs(BCStyle.CN)[0];
        String cn = IETFUtils.valueToString(cnRDN.getFirst().getValue());
        if (forPrincipal.equals(cn))
        {
            try {
                return cs.PEMEncodeCert(cs.signCert(cr));
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (SignatureException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OperatorCreationException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            log.debug("SigningService.sign will not sign a certificate for " + forPrincipal + " that has a CN=" + cn);
            return null;
        }
    }
}
