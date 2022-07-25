package elrond.esdt.common;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

public class TokenIdentifier {
    private TokenIdentifier() {
    }

    private String hexTokenIdentifier;

    public static TokenIdentifier fromString(String tokenIdentifier) {
        TokenIdentifier ti = new TokenIdentifier();
        ti.hexTokenIdentifier = Utils.castToPaddedHex(tokenIdentifier);

        return ti;
    }

    public static TokenIdentifier fromHex(String hexTokenIdentifier) {
        TokenIdentifier ti = new TokenIdentifier();
        ti.hexTokenIdentifier = hexTokenIdentifier;

        return ti;
    }

    public String toRegularString() throws DecoderException {
        byte[] bytes = Hex.decodeHex(this.hexTokenIdentifier.toCharArray());

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public String toHexString() {
        return this.hexTokenIdentifier;
    }
}
