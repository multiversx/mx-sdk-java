package elrond;

import com.google.protobuf.ByteString;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BigIntegerCodecTest {
    static class TxValueBytesPair {
        String input;
        String expected;

        public TxValueBytesPair(String input, String expected) {
            this.input = input;
            this.expected = expected;
        }
    }

    @Test
    public void TestTxBytesValue() {
        List<TxValueBytesPair> pairs = Arrays.asList(
                new TxValueBytesPair("", ""),
                new TxValueBytesPair("0", "0000"),
                new TxValueBytesPair("10", "000a"),
                new TxValueBytesPair("100", "0064"),
                new TxValueBytesPair("1000", "0003e8"),
                new TxValueBytesPair("1500", "0005dc"),
                new TxValueBytesPair("1505", "0005e1"),
                new TxValueBytesPair("1793", "000701"),
                new TxValueBytesPair("50000000000000", "002d79883d2000"),
                new TxValueBytesPair("1000000000000000000", "000de0b6b3a7640000"),
                new TxValueBytesPair("1000000000000000001", "000de0b6b3a7640001"),
                new TxValueBytesPair("1000000000000000000", "000de0b6b3a7640000"),
                new TxValueBytesPair("1799999999999990000", "0018fae27693b3d8f0"),
                new TxValueBytesPair("11485000000000000000", "009f62eaa65d5c8000"),
                new TxValueBytesPair("15940000000000000000", "00dd36418bd7ba0000"),
                new TxValueBytesPair("15500159000000000000000000", "000cd2494d96e1f8369c0000"),
                new TxValueBytesPair("15500159777799995555333311", "000cd24958622ef1cffc44bf"),
                new TxValueBytesPair("77993311779933110000000000", "004083b9e74f854654399c00"),
                new TxValueBytesPair("77993311779933117799331177", "004083b9e74f85482519f569"));

        for (TxValueBytesPair pair : pairs) {
            assertBytesValue(pair);
        }
    }

    public void assertBytesValue(TxValueBytesPair pair) {
        // arrange
        BigInteger value;
        if ("".equals(pair.input)) {
            value = null;
        } else {
            value = new BigInteger(pair.input);
        }

        // act
        ByteString res = BigIntegerCodec.serializeValue(value);
        String hexBI = Hex.encodeHexString(res.toByteArray());

        // assert
        assertEquals(pair.expected, hexBI);
    }
}
