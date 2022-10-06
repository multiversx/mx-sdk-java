package elrond;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.util.encoders.Hex;

import static org.junit.jupiter.api.Assertions.*;

public class MessageSigningTest {

    @Test
    public void verifyMessageSignature() throws UnsupportedEncodingException, Exceptions.AddressException {
        String message = "custom message of Alice";
        Address address = Address.fromBech32("erd1qyu5wthldzr8wx5c9ucg8kjagg0jfs53s8nr3zpz3hypefsdd8ssycr6th");
        String sig = "b83647b88cdc7904895f510250cc735502bf4fd86331dd1b76e078d6409433753fd6f619fc7f8152cf8589a4669eb8318b2e735e41309ed3b60e64221d814f08";

        boolean verified = MessageSigning.verify(address, message.getBytes(StandardCharsets.UTF_8), sig);
        assertTrue(verified);
    }
    @Test
    public void signMessage() throws UnsupportedEncodingException {
        String privateKeyHex = "413f42575f7f26fad3317a778771212fdb80245850981e48b58a4f25e344e8f9";
        String msg = "custom message of Alice";

        byte[] privateKey = Hex.decode(privateKeyHex);

        String expectedSigHex = "b83647b88cdc7904895f510250cc735502bf4fd86331dd1b76e078d6409433753fd6f619fc7f8152cf8589a4669eb8318b2e735e41309ed3b60e64221d814f08";
        String sigHex = MessageSigning.sign(privateKey, msg.getBytes("utf-8"));
        assertEquals(expectedSigHex, sigHex);
    }

    @Test
    public void signAndVerifyMessage() throws UnsupportedEncodingException, Exceptions.AddressException {
        Ed25519KeyPairGenerator keyGen = new Ed25519KeyPairGenerator();
        keyGen.init(new Ed25519KeyGenerationParameters(new SecureRandom()));

        AsymmetricCipherKeyPair keyPair = keyGen.generateKeyPair();
        Ed25519PrivateKeyParameters privatekey = (Ed25519PrivateKeyParameters) keyPair.getPrivate();
        Ed25519PublicKeyParameters publicKey = (Ed25519PublicKeyParameters) keyPair.getPublic();

        byte[] message = "message to sign".getBytes("utf-8");
        String sigHex = MessageSigning.sign(privatekey.getEncoded(), message);
        assertNotNull(sigHex);

        String publicKeyHex = Hex.toHexString(publicKey.getEncoded());
        Address address = Address.fromHex(publicKeyHex);

        boolean verified = MessageSigning.verify(address, message, sigHex);
        assertTrue(verified);
    }
}
