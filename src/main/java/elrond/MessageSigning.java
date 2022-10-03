package elrond;

import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.util.encoders.Hex;

public class MessageSigning {
    static final String signerMessagePrefix = "\\x17Elrond Signed Message:\n";
    private KeccakDigest hasher;
    private byte[] privateKey;

    public MessageSigning(String privateKeyHex) {
        this(Hex.decode(privateKeyHex));
    }

    public MessageSigning(byte[] privateKey) {
        this.privateKey = privateKey;
        this.hasher = createKeccakHasher();
    }

    public String SignMessage(byte[] msg) {
        Ed25519Signer signer = createEd25519Signer();
        byte[] message = computeHashOnMessage(msg);

        signer.update(message, 0, message.length);
        byte[] signature = signer.generateSignature();
        byte[] hexSig = Hex.encode(signature);
        return new String(hexSig);
    }

    private byte[] computeHashOnMessage(byte[] msg) {
        final StringBuilder sb = new StringBuilder();

        sb.append(signerMessagePrefix);
        sb.append(msg.length);
        for (byte b : msg) {
            sb.append(b);
        }

        byte[] message = sb.toString().getBytes();

        this.hasher.update(message, 0, message.length);
        final byte[] marshalledMsg = new byte[this.hasher.getDigestSize()];
        this.hasher.doFinal(marshalledMsg, 0);

        return marshalledMsg;
    }

    public boolean VerifyMessageSignature(byte[] address, byte[] msg, String signature) {
        Ed25519Signer signer = createEd25519Signer();
        byte[] message = computeHashOnMessage(msg);
        byte[] sig = Hex.decode(signature);

        Ed25519PublicKeyParameters publicKeyParameters = new Ed25519PublicKeyParameters(address, 0);
        signer.init(false, publicKeyParameters);
        signer.update(message, 0, message.length);
        return signer.verifySignature(sig);
    }

    private Ed25519Signer createEd25519Signer() {
        Ed25519PrivateKeyParameters parameters = new Ed25519PrivateKeyParameters(this.privateKey, 0);
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true, parameters);
        return signer;
    }

    private KeccakDigest createKeccakHasher() {
        return new KeccakDigest();
    }
}
