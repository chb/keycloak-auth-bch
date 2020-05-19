package org.chip.ihl.certificates;

import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.operator.OperatorCreationException;
import org.chip.ihl.certificates.Services.CertificateSigner;
import org.chip.ihl.certificates.utils.LocalResourceUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.TestPropertySource;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Spy;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

//@Profile("test")
@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(properties = { "cacertificates.cert=test-certs/ca-certificate.pem",
        "cacertificates.key=test-certs/ca-key.pem"})
public class CertificateSignerTest {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(CertificateSignerTest.class);

    @Spy
    public LocalResourceUtils ru;

    @InjectMocks
    private CertificateSigner cs ;

    @Before
    public void inject_props() {
        cs.CA_KEY_FILENAME = "test-certs/ca-key.pem";
        cs.CA_CERT_FILENAME = "test-certs/ca-certificate.pem";
    }

    @Test
    public void testGetPrivateKey() {
        try{
            PrivateKey privateKey = cs.getPrivateKey("test-certs/key.pem");
            assertEquals("RSA", privateKey.getAlgorithm());
        } catch (Exception ex){
            fail("Exception thrown: \n" + ex.getMessage() );
            ex.printStackTrace();
        }
    }

    @Test
    public void testPemToCert() {
        try {
            X509Certificate cert = cs.pemToCert("test-certs/certificate.pem");


            System.out.println(cert.getSubjectX500Principal());
            assertThat(cert.getSubjectX500Principal().toString(), containsString("christopher.gentle@childrens.harvard.edu"));
            System.out.println(cert.getPublicKey().toString());
            assertThat(cert.getPublicKey().toString(), containsString("RSA public key"));
            System.out.println(cert.toString());
        } catch (CertificateException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetPublicKey() {
        X509Certificate cert = null;
        try {
            cert = cs.pemToCert("test-certs/certificate.pem");
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            PublicKey pk = cs.getPublicKey(cert);
            assertThat(pk.getAlgorithm(), containsString("RSA"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testPemDecodeCSR(){
        String CSR_TO_TEST = "MIIDGTCCAgECAQAwgdMxCzAJBgNVBAYTAlVTMSowKAYDVQQIDCFUaGUgQ29tbW9ud2VhbHRoIG9mIE1hc3NhY2h1c2V0dHMxDzANBgNVBAcMBkJvc3RvbjENMAsGA1UECgwEQ0hJUDEMMAoGA1UECwwDSUhMMTEwLwYDVQQDDChjaHJpc3RvcGhlci5nZW50bGVAY2hpbGRyZW5zLmhhcnZhcmQuZWR1MTcwNQYJKoZIhvcNAQkBFihjaHJpc3RvcGhlci5nZW50bGVAY2hpbGRyZW5zLmhhcnZhcmQuZWR1MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvQ4/wDNi4RF4c7XpIE+FxC9gW4NHWLHgvomaFhBORjU+1M7RZCK1KqYxvcpNtFvyW6lMba5+dBDfmkdG5QAAknzZ8WVqHxI/CrPYExZld9lTWY9HtZIiMt2M8QWDAmXjR6A/cJ9bnR7gm1rCgVL7soLuvjLs3xC60Zlz+s21UQleJapGpkTqzD0d327fB7XgtMzzE+M1hUzXDgY4gMIW7ElBM+3MrSzLs/NjfRlwb0mAxvTf6jZs183Z/4nNq2BuKRde2zDUE+GT7W3Q54eC4a1+645vxuJjI8gK1ExV/l72ZH3ne/BrYSWMUciLFkxgkj0rpe3EtOINoCMMLdVSEQIDAQABoAAwDQYJKoZIhvcNAQELBQADggEBABmQpYUg7cKDbCT24bvTTIulDi+vnOczbt9+IjnX9Q2FEyW3aWOnQJk6cXzJnxX+th9TEHu8ssEDvXV3/cv2h80bAQ3ZmbJEhBwv0taOxvMBv0GMvIPriSTIfrrb3H4Y2koX9phVzS6kgxNR6S9pYk9Oz5HtFy3VILvNzc5ht+3hnddMgeCMiLzt8DoDERBhKRD7pGk/GkDSb/8mWIY5mpzLDoI0ysW7NVfeQcpG5i6KOKKG0CEP0GDm0I+CdWTGY0zU/skADjDNTq2HInmun1jcHEuXSxYqsr611rXDBQZeor/UMvBb08dJEUyZw3rbSqcAG6uxsRD3v4iBNpRNspI=";
        CertificationRequest cr = cs.PEMDecodeCSR(CSR_TO_TEST);
        assertNotNull(cr);
    }

    @Test
    public void testGetKeyPairAndMakeSigningRequest() {
        X509Certificate cert = null;
        try {
            cert = cs.pemToCert("test-certs/certificate.pem");
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PublicKey pubKey = null;
        try {
            pubKey = cs.getPublicKey(cert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrivateKey privateKey = null;
        try {
            privateKey = cs.getPrivateKey("test-certs/key.pem");
        } catch (IOException e) {
            e.printStackTrace();
        }

        KeyPair kp = cs.getKeyPair(pubKey, privateKey);
        System.out.println(kp.getPrivate().toString());
        assertThat(kp.getPrivate().toString().toLowerCase(), containsString("private"));
        System.out.println(kp.getPublic().toString());
        assertThat(kp.getPublic().toString().toLowerCase(), containsString("public"));

        try {
            CertificationRequest csr = cs.makeSigningRequest(kp, cert);
            String stringCsr = cs.PEMEncodeCSR(csr);
            System.out.println("String CSR: " + stringCsr);

            String recodedCsr = cs.PEMEncodeCSR(cs.PEMDecodeCSR(stringCsr));

            assert(stringCsr.equals(recodedCsr));

            assertThat(csr.getCertificationRequestInfo().getSubject().toString(), containsString("chris"));
        } catch (OperatorCreationException e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void testSignIDCertificate()  {
        // Load ca Certificate and Private Key
        X509Certificate caCert = null;
        PrivateKey caPrivateKey = null;
        // Load ca Certificate and Private Key
        X509Certificate cert = null;
        PrivateKey privateKey = null;
        try {
            caCert = cs.pemToCert("test-certs/ca-certificate.pem");
            caPrivateKey = cs.getPrivateKey("test-certs/ca-key.pem");
            cert = cs.pemToCert("test-certs/certificate.pem");
            privateKey = cs.getPrivateKey("test-certs/key.pem");
            PublicKey pubKey = null;
            pubKey = cs.getPublicKey(cert);

            KeyPair kp = cs.getKeyPair(pubKey, privateKey);

            // Make the signing request
            CertificationRequest csr = cs.makeSigningRequest(kp, cert);

            // Use the CA private key and public key to sign the request

            X509Certificate signedCert = cs.signCert(csr);

            System.out.println(signedCert.toString());

            assertThat(signedCert.toString(), containsString("IssuerDN: E=ca-test@chip.org"));


        } catch (CertificateException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (OperatorCreationException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void signingTest() {
        X509Certificate cert = null;
        try {
            cert = cs.pemToCert("test-certs/certificate.pem");
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PublicKey pubKey = null;
        try {
            pubKey = cs.getPublicKey(cert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrivateKey privateKey = null;
        try {
            privateKey = cs.getPrivateKey("test-certs/key.pem");
        } catch (IOException e) {
            e.printStackTrace();
        }

        KeyPair kp = cs.getKeyPair(pubKey, privateKey);

        String detatchedSignature = cs.generateSignatureBase64(kp.getPrivate(), "TEST");

        System.out.println("Signature = " + detatchedSignature);
        assertTrue(cs.verifySignature("TEST", detatchedSignature, kp.getPublic()));
        assertFalse(cs.verifySignature("BAD DATA", detatchedSignature, kp.getPublic()));
    }


}