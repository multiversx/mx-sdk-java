package elrond.esdt.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenPaymentTest {
    @Test
    public void egldFromAmount() {
        assertEquals("50000000000000", TokenPayment.egldFromAmount("0.00005").toString());
        assertEquals("2d79883d2000", TokenPayment.egldFromAmount("0.00005").valueToHexString());

        assertEquals("1000000000000000000", TokenPayment.egldFromAmount("1").toString());
        assertEquals("0de0b6b3a7640000", TokenPayment.egldFromAmount("1").valueToHexString());
    }

    @Test
    public void fungibleFromAmount() {
        assertEquals("50000000000000", TokenPayment.fungibleFromAmount(Constants.EGLDIdentifier, "0.00005", 18).toString());
        assertEquals("2d79883d2000", TokenPayment.fungibleFromAmount(Constants.EGLDIdentifier, "0.00005", 18).valueToHexString());

        assertEquals("150000000", TokenPayment.fungibleFromAmount(Constants.EGLDIdentifier, "15", 7).toString());
        assertEquals("08f0d180", TokenPayment.fungibleFromAmount(Constants.EGLDIdentifier, "15", 7).valueToHexString());
        assertEquals(Integer.valueOf(7), TokenPayment.fungibleFromAmount(Constants.EGLDIdentifier, "15", 7).getNumDecimals());
    }

    @Test
    public void metaEsdtFromAmount() {
        assertEquals("50000000000000000000", TokenPayment.metaEsdtFromAmount(TokenIdentifier.fromString("META-6y7u8i"), "50", 18, 5L).toString());
        assertEquals("042c1d80", TokenPayment.metaEsdtFromAmount(TokenIdentifier.fromString("META-6y7u8i"), "0.07", 9, 7L).valueToHexString());
    }
}
