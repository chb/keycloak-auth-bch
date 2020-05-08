package org.chip.ihl.certificates;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mime.encoding.Base64OutputStream;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.bc.BcPKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.*;
import org.chip.ihl.certificates.utils.DateUtils;
import org.chip.ihl.certificates.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class CertificateSigner {

    private Logger logger ;
    private ResourceUtils ru ;
    private X509Certificate caCert;
    private PrivateKey caPrivateKey;

    public CertificateSigner() {
        this.logger = LoggerFactory.getLogger(CertificateSigner.class);
        this.ru  = new ResourceUtils();
        Security.addProvider(new BouncyCastleProvider());
        try {
            this.caCert = pemToCert("test-certs/ca-certificate.pem");
            this.caPrivateKey = getPrivateKey("test-certs/ca-key.pem");
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public PrivateKey getPrivateKey(String filename) throws IOException {

        PrivateKey key;

        Reader reader = new InputStreamReader(ru.getResourceFileAsInputStream(filename, CertificateSigner.class));

        try (PEMParser pem = new PEMParser(reader)) {
            JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
            Object pemContent = pem.readObject();
            if (pemContent instanceof PEMKeyPair) {
                PEMKeyPair pemKeyPair = (PEMKeyPair) pemContent;
                KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(pemKeyPair);
                key = keyPair.getPrivate();
            } else if (pemContent instanceof PrivateKeyInfo) {
                PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemContent;
                key = jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);
            } else {
                throw new IllegalArgumentException("Unsupported private key format '" +
                        pemContent.getClass().getSimpleName() + '"');
            }
        }

        return key;
    }

    public X509Certificate pemToCert(String filename) throws CertificateException, FileNotFoundException {
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        InputStream is = ru.getResourceFileAsInputStream(filename, CertificateSigner.class);
        X509Certificate cert = (X509Certificate) fact.generateCertificate(is);
        return cert;
    }

    public PublicKey getPublicKey(X509Certificate cert)
            throws Exception {
        PublicKey key = cert.getPublicKey();
        return key;
    }


    public KeyPair getKeyPair(PublicKey pubKey, PrivateKey privKey) {
        KeyPair kp = new KeyPair(pubKey, privKey);
        return kp;

    }

    public CertificationRequest makeSigningRequest(KeyPair kp, X509Certificate certWithSubject) throws OperatorCreationException {
        JcaPKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                certWithSubject.getSubjectX500Principal(), kp.getPublic());
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = csBuilder.build(kp.getPrivate());
        PKCS10CertificationRequest csr = p10Builder.build(signer);
        return csr.toASN1Structure();
    }

    public X509Certificate sign(CertificationRequest inputCSR)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchProviderException, SignatureException, IOException,
            OperatorCreationException, CertificateException {

        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder()
                .find("SHA1withRSA");
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder()
                .find(sigAlgId);

        AsymmetricKeyParameter caPrivateKeyParameter = PrivateKeyFactory.createKey(caPrivateKey.getEncoded());
        SubjectPublicKeyInfo subjectsPublicKeyInfo = SubjectPublicKeyInfo.getInstance(caCert.getPublicKey().getEncoded());

        PKCS10CertificationRequest pk10Holder = new PKCS10CertificationRequest(inputCSR);

        X509v3CertificateBuilder myCertificateGenerator = new X509v3CertificateBuilder(
                new X500Name(caCert.getSubjectDN().getName()),
                new BigInteger("1"),
                new Date(System.currentTimeMillis()),
                DateUtils.futureDate(3650),
                pk10Holder.getSubject(),
                subjectsPublicKeyInfo
        );

        ContentSigner sigGen = new BcRSAContentSignerBuilder(sigAlgId, digAlgId)
                .build(caPrivateKeyParameter);

        X509CertificateHolder holder = myCertificateGenerator.build(sigGen);
        org.bouncycastle.asn1.x509.Certificate eeX509CertificateStructure = holder.toASN1Structure();

        CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");

        InputStream is1 = new ByteArrayInputStream(eeX509CertificateStructure.getEncoded());
        X509Certificate theCert = (X509Certificate) cf.generateCertificate(is1);
        is1.close();
        return theCert;
    }

    public String generateSignatureBase64(PrivateKey privKey, String materialToSign) {
        try{
            Signature signature = Signature.getInstance("SHA1withRSA", BouncyCastleProvider.PROVIDER_NAME);
            signature.initSign(privKey);
            signature.update(materialToSign.getBytes());
            byte sig[] = signature.sign();

            String b64signature = Base64.getEncoder().encodeToString(sig);
            return b64signature;
        }  catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean verifySignature(String materialToCheck, String base64Signature, PublicKey pubKey) {
        try{
            Signature signer = Signature.getInstance(
                    "SHA1withRSA",
                    BouncyCastleProvider.PROVIDER_NAME);
            signer.initVerify(pubKey);
            signer.update(materialToCheck.getBytes());
            boolean isValid = signer.verify(Base64.getDecoder().decode(base64Signature));
            return isValid;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String dumpCert(ASN1Encodable inAsn1) {
        String decodedAsn1 = ASN1Dump.dumpAsString(inAsn1);
        LoggerFactory.getLogger(CertificateSigner.class).info("ASN1 Dump:\n" + decodedAsn1);
        return decodedAsn1;
    }
}
