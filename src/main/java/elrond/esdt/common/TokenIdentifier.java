package elrond.esdt.common;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

public class TokenIdentifier {
    private TokenIdentifier() {}
    private String hexTokenIdentifier;

    public static TokenIdentifier fromString(String tokenIdentifier) {
        TokenIdentifier ti = new TokenIdentifier();
        ti.hexTokenIdentifier = Hex.encodeHexString(tokenIdentifier.getBytes(StandardCharsets.UTF_8));

        return ti;
    }

    public static TokenIdentifier fromHex(String hexTokenIdentifier) {
        TokenIdentifier ti = new TokenIdentifier();
        ti.hexTokenIdentifier = hexTokenIdentifier;

        return ti;
    }

    @Override
    public String toString() {
        byte[] bytes = new byte[0];
        try {
            bytes = Hex.decodeHex(this.hexTokenIdentifier.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public String toHexString() {
        return this.hexTokenIdentifier;
    }
}
