package elrond.esdt.common;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;

public class Utils {
    public static String castToPaddedHex(BigInteger input) {
        String bigIntegerHex = Hex.encodeHexString(input.toByteArray());
        return zeroPadStringIfOddLength(bigIntegerHex);
    }

    public static String castToPaddedHex(Integer input) {
        String inputHex = Integer.toHexString(input);
        return zeroPadStringIfOddLength(inputHex);
    }

    public static String castToPaddedHex(Long input) {
        String inputHex = Long.toHexString(input);
        return zeroPadStringIfOddLength(inputHex);
    }

    public static String zeroPadStringIfOddLength(String input) {
        boolean shouldPad = input.length() % 2 != 0;
        if (!shouldPad) {
            return input;
        }

        return "0" + input;
    }
}
