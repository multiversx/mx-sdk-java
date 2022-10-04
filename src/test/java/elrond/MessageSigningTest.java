package elrond;

import org.bouncycastle.util.encoders.Hex;
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
    public void signMessage() throws UnsupportedEncodingException {
        String privateKeyHex = "06a180420e608220a6c2f997751a53f5bd3bbe63d36260a858a6d925daed593d";
        byte[] privateKey = Hex.decode(privateKeyHex);

        MessageSigning messageSigner = new MessageSigning();
        byte[] message = "message to sign".getBytes("utf-8");

        String expectedSigHex = "c95ada60e1a58849234a5e95e0a80e149630585cb1b589c20cff09c283438e163c8a5bc595730538809bbcfbafbef2a991c74523268712d3220d636b60de8309";
        String sigHex = messageSigner.sign(privateKey, message);
        assertEquals(expectedSigHex, sigHex);
    }

    @Test
    public void signAndVerifyMessage() throws UnsupportedEncodingException, Exceptions.AddressException {
        Ed25519KeyPairGenerator keyGen = new Ed25519KeyPairGenerator();
        keyGen.init(new Ed25519KeyGenerationParameters(new SecureRandom()));

        AsymmetricCipherKeyPair keyPair = keyGen.generateKeyPair();
        Ed25519PrivateKeyParameters privatekey = (Ed25519PrivateKeyParameters) keyPair.getPrivate();
        Ed25519PublicKeyParameters publicKey = (Ed25519PublicKeyParameters) keyPair.getPublic();

        MessageSigning messageSigner = new MessageSigning();

        byte[] message = "message to sign".getBytes("utf-8");
        String sigHex = messageSigner.sign(privatekey.getEncoded(), message);
        assertNotNull(sigHex);

        String publicKeyHex = Hex.toHexString(publicKey.getEncoded());
        Address address = Address.fromHex(publicKeyHex);

        boolean verified = messageSigner.verify(address, message, sigHex);
        assertTrue(verified);
    }
}
