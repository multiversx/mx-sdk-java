package elrond;

import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;

public class MessageSigning {
    private static final String signerMessagePrefix = "\u0017Elrond Signed Message:\n";

    public MessageSigning() {
    }

    /**
     * It will sign the provided message with the internal stored private key.
     *
     * @param privateKey private key as bytes
     * @param msg the message to be signed
     *
     * @return signature as hex encoded string
     */
    public static String sign(byte[] privateKey, byte[] msg) {
        Ed25519Signer signer = createSignerWithPrivateKey(privateKey);
        byte[] message = computeHashOnMessage(msg);

        signer.update(message, 0, message.length);
        byte[] signature = signer.generateSignature();
        byte[] hexSig = Hex.encode(signature);
        return new String(hexSig);
    }

    /**
     * Verify signature for a given message.
     *
     * @param address corresponding address which signed the message
     * @param msg the message to be verified
     * @param signature the provided signature
     *
     * @return true if signature match and false otherwise
     */
    public static boolean verify(Address address, byte[] msg, String signature) {
        Ed25519Signer signer = createSignerWithPublicKey(address.pubkey());
        byte[] message = computeHashOnMessage(msg);
        byte[] sig = Hex.decode(signature);

        Ed25519PublicKeyParameters publicKeyParameters = new Ed25519PublicKeyParameters(address.pubkey(), 0);
        signer.init(false, publicKeyParameters);
        signer.update(message, 0, message.length);
        return signer.verifySignature(sig);
    }

    private static String composeMessage(byte[] msg) {
        final StringBuilder sb = new StringBuilder();

        sb.append(signerMessagePrefix);
        sb.append(msg.length);
        for (byte b : msg) {
            sb.append((char)b);
        }

        return sb.toString();
    }

    private static byte[] computeHashOnMessage(byte[] msg) {
        Keccak.DigestKeccak hasher = createKeccakHasher();
        String message = composeMessage(msg);
        byte[] messageBytes = message.getBytes();

        byte[] hash = hasher.digest(messageBytes);

        return hash;
    }

    private static Ed25519Signer createSignerWithPrivateKey(byte[] privateKey) {
        Ed25519Signer signer = new Ed25519Signer();
        Ed25519PrivateKeyParameters parameters = new Ed25519PrivateKeyParameters(privateKey, 0);
        signer.init(true, parameters);
        return signer;
    }

    private static Ed25519Signer createSignerWithPublicKey(byte[] publicKey) {
        Ed25519Signer signer = new Ed25519Signer();
        Ed25519PublicKeyParameters publicKeyParameters = new Ed25519PublicKeyParameters(publicKey, 0);
        signer.init(false, publicKeyParameters);
        return signer;
    }

    private static Keccak.DigestKeccak createKeccakHasher() {
        return new Keccak.Digest256();
    }
}
