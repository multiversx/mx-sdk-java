package elrond;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;

import static org.junit.jupiter.api.Assertions.*;

public class MessageSigningTest {

    @Test
    public void signAndVerifyMessage() throws UnsupportedEncodingException {
        Ed25519KeyPairGenerator keyGen = new Ed25519KeyPairGenerator();
        keyGen.init(new Ed25519KeyGenerationParameters(new SecureRandom()));

        AsymmetricCipherKeyPair keyPair = keyGen.generateKeyPair();
        Ed25519PrivateKeyParameters privatekey = (Ed25519PrivateKeyParameters) keyPair.getPrivate();
        Ed25519PublicKeyParameters publicKey = (Ed25519PublicKeyParameters) keyPair.getPublic();

        MessageSigning messageSigner = new MessageSigning(privatekey.getEncoded());

        byte[] message = "message to sign".getBytes("utf-8");
        String sigHex = messageSigner.SignMessage(message);

        boolean verified = messageSigner.VerifyMessageSignature(publicKey.getEncoded(), message, sigHex);
        assertTrue(verified);
    }
}
