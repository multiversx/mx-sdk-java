package elrond.esdt.common;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Collections;

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
    public void fungibleFromAount() {
        assertEquals("50000000000000", TokenPayment.fungibleFromAmount(Constants.EGLDIdentifier, "0.00005", 18).toString());
        assertEquals("2d79883d2000", TokenPayment.fungibleFromAmount(Constants.EGLDIdentifier, "0.00005", 18).valueToHexString());

        assertEquals("150000000", TokenPayment.fungibleFromAmount(Constants.EGLDIdentifier, "15", 7).toString());
        assertEquals("08f0d180", TokenPayment.fungibleFromAmount(Constants.EGLDIdentifier, "15", 7).valueToHexString());
        assertEquals(Integer.valueOf(7), TokenPayment.fungibleFromAmount(Constants.EGLDIdentifier, "15", 7).getNumDecimals());
    }

    @Test
    public void TestBigInteger() {
        BigInteger bi = new BigInteger("5");
        BigInteger multipliedBy = new BigInteger("1000000000000000000");

        BigInteger res = bi.multiply(multipliedBy);

        System.out.println(res);

        System.out.println("1" + String.join("", Collections.nCopies(5, "0")));
    }

    @Test
    public void TestBIEvenChar() {

    }

    @Test
    public void egldFromBigInteger() {
    }

    @Test
    public void fungibleFromAmount() {
    }
}
