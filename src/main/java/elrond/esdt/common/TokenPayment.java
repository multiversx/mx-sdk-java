package elrond.esdt.common;

import org.apache.commons.codec.binary.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;

public class TokenPayment {
    private final TokenIdentifier tokenIdentifier;
    private final BigDecimal amountAsBigDecimal;
    private final Integer numDecimals;
    private final Long nonce;

    public TokenPayment(TokenIdentifier identifier, BigDecimal amountAsBigDecimal, Integer numDecimals, Long nonce) {
        this.tokenIdentifier = identifier;
        this.amountAsBigDecimal = amountAsBigDecimal;
        this.numDecimals = numDecimals;
        this.nonce = nonce;
    }

    public static TokenPayment egldFromAmount(String amount) {
        BigDecimal amountAsBigDecimal = addDecimalsToBigDecimal(new BigDecimal(amount), Constants.EGLDNumDecimals);
        return TokenPayment.egldFromBigDecimal(amountAsBigDecimal);
    }

    public static TokenPayment egldFromBigDecimal(BigDecimal amountAsBigDecimal) {
        return new TokenPayment(Constants.EGLDIdentifier, amountAsBigDecimal, Constants.EGLDNumDecimals, 0L);
    }

    public static TokenPayment fungibleFromAmount(TokenIdentifier tokenIdentifier, String amount, Integer numDecimals) {
        BigDecimal amountAsBigDecimal = addDecimalsToBigDecimal(new BigDecimal(amount), numDecimals);
        return TokenPayment.fungibleFromBigDecimal(tokenIdentifier, amountAsBigDecimal, numDecimals);
    }

    public static TokenPayment fungibleFromBigDecimal(TokenIdentifier tokenIdentifier, BigDecimal amountAsBigDecimal, Integer numDecimals) {
        return new TokenPayment(tokenIdentifier, amountAsBigDecimal, numDecimals, 0L);
    }

    public static TokenPayment semiFungible(TokenIdentifier tokenIdentifier, Integer quantity, Long nonce) {
        return new TokenPayment(tokenIdentifier, new BigDecimal(quantity.toString()), 0, nonce);
    }

    public static TokenPayment nonFungible(TokenIdentifier tokenIdentifier, Long nonce) {
        return new TokenPayment(tokenIdentifier, addDecimalsToBigDecimal(new BigDecimal("1"), 0), 0, nonce);
    }

    private static BigDecimal addDecimalsToBigDecimal(BigDecimal input, Integer numDecimals) {
        String inputStr = "1" + String.join("", Collections.nCopies(numDecimals, "0")); // numDecimals = 3 will produce 1000
        BigDecimal multipliedBy = new BigDecimal(inputStr);

        return input.multiply(multipliedBy);
    }

    public String toString() {
        return this.amountAsBigDecimal.toBigInteger().toString(10);
    }

    public String valueToHexString() {
        BigInteger bi = this.amountAsBigDecimal.toBigInteger();
        return Hex.encodeHexString(bi.toByteArray());
    }

    public Integer getNumDecimals() {
        return this.numDecimals;
    }

    public Long getNonce() {
        return this.nonce;
    }

    public TokenIdentifier getTokenIdentifier() {
        return this.tokenIdentifier;
    }
}
