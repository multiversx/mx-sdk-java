package multiversx;

import com.google.protobuf.ByteString;

import java.math.BigInteger;

public class BigIntegerCodec {
    public static ByteString serializeValue(BigInteger value) {
        if (value == null) {
            return ByteString.copyFrom("".getBytes());
        }

        byte[] valueBytes = value.toByteArray();
        boolean isFirstByteForTheSign = valueBytes[0] == (byte) 0;
        boolean isZero = value.intValue() == 0;

        if (!isFirstByteForTheSign || isZero) {
            byte[] bytes = new byte[valueBytes.length + 1];
            bytes[0] = 0; // positive sign expected on the mx-chain-go side

            System.arraycopy(valueBytes, 0, bytes, 1, valueBytes.length);
            return ByteString.copyFrom(bytes);
        }

        return ByteString.copyFrom(valueBytes);
    }
}
