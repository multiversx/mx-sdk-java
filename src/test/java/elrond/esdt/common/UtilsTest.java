package elrond.esdt.common;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void testCastToPaddedHex() {
        assertEquals("0a", Utils.castToPaddedHex(10L));
        assertEquals("07", Utils.castToPaddedHex(7L));
        assertEquals("64", Utils.castToPaddedHex(100L));

        assertEquals("0a", Utils.castToPaddedHex(new BigInteger("10")));
        assertEquals("07", Utils.castToPaddedHex(new BigInteger("7")));
        assertEquals("64", Utils.castToPaddedHex(new BigInteger("100")));

        assertEquals("0a", Utils.castToPaddedHex(10));
        assertEquals("07", Utils.castToPaddedHex(7));
        assertEquals("64", Utils.castToPaddedHex(100));

    }
}
