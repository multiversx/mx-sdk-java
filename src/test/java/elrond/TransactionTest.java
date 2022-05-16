package elrond;

import com.google.protobuf.ByteString;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TransactionTest {
    @Test
    public void shouldSerialize() throws Exception {
        Transaction transaction = new Transaction();

        // Without data field
        transaction.setNonce(0);
        transaction.setValue(new BigInteger("42"));
        transaction.setSender(Address.fromBech32("erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz"));
        transaction.setReceiver(Address.fromBech32("erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(50000);
        transaction.setChainID("1");

        String expected = "{'nonce':0,'value':'42','receiver':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r','sender':'erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz','gasPrice':1000000000,'gasLimit':50000,'chainID':'1','version':1}".replace('\'', '"');
        assertEquals(expected, transaction.serialize());

        // With data field
        transaction.setData("foobar");
        expected = "{'nonce':0,'value':'42','receiver':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r','sender':'erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz','gasPrice':1000000000,'gasLimit':50000,'data':'Zm9vYmFy','chainID':'1','version':1}".replace('\'', '"');
        assertEquals(expected, transaction.serialize());
    }

    @Test
    public void shouldSign() throws Exception {
        String alicePrivateKey = "1a927e2af5306a9bb2ea777f73e06ecc0ac9aaa72fb4ea3fecf659451394cccf";
        Wallet wallet = new Wallet(alicePrivateKey);

        // With data field
        Transaction transaction = new Transaction();
        transaction.setNonce(7);
        transaction.setValue(new BigInteger("10000000000000000000"));
        transaction.setSender(Address.fromBech32("erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz"));
        transaction.setReceiver(Address.fromBech32("erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(70000);
        transaction.setData("for the book with stake");
        transaction.setChainID("1");
        transaction.sign(wallet);

        String expectedSignature = "096c571889352947f285632d79f2b2ee1b81e7acd19ee20510d34002eba0f999b4720f50211b039dd40914284f84c14eb84815bb045c14dbed036f2e87431307";
        String expectedJson = "{'nonce':7,'value':'10000000000000000000','receiver':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r','sender':'erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz','gasPrice':1000000000,'gasLimit':70000,'data':'Zm9yIHRoZSBib29rIHdpdGggc3Rha2U=','chainID':'1','version':1,'signature':'096c571889352947f285632d79f2b2ee1b81e7acd19ee20510d34002eba0f999b4720f50211b039dd40914284f84c14eb84815bb045c14dbed036f2e87431307'}".replace('\'', '"');
        assertEquals(expectedSignature, transaction.getSignature());
        assertEquals(expectedJson, transaction.serialize());

        // Without data field
        transaction = new Transaction();
        transaction.setNonce(8);
        transaction.setValue(new BigInteger("10000000000000000000"));
        transaction.setSender(Address.fromBech32("erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz"));
        transaction.setReceiver(Address.fromBech32("erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(50000);
        transaction.setData("");
        transaction.setChainID("1");
        transaction.sign(wallet);

        expectedSignature = "4a6d8186eae110894e7417af82c9bf9592696c0600faf110972e0e5310d8485efc656b867a2336acec2b4c1e5f76c9cc70ba1803c6a46455ed7f1e2989a90105";
        expectedJson = "{'nonce':8,'value':'10000000000000000000','receiver':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r','sender':'erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz','gasPrice':1000000000,'gasLimit':50000,'chainID':'1','version':1,'signature':'4a6d8186eae110894e7417af82c9bf9592696c0600faf110972e0e5310d8485efc656b867a2336acec2b4c1e5f76c9cc70ba1803c6a46455ed7f1e2989a90105'}".replace('\'', '"');
        assertEquals(expectedSignature, transaction.getSignature());
        assertEquals(expectedJson, transaction.serialize());
    }

    @Test
    public void shouldComputeHash() throws Exception {
        Wallet wallet = Wallet.deriveFromMnemonic("blind wisdom book round sing capable taste refuse simple thunder profit goddess bird adult skirt road box patient cost tape lawn invite visual rabbit", 0);

        // Without data field
        Transaction transaction = new Transaction();
        transaction.setNonce(1);
        transaction.setValue(new BigInteger("1000000000000000"));
        transaction.setSender(Address.fromBech32("erd1lta2vgd0tkeqqadkvgef73y0efs6n3xe5ss589ufhvmt6tcur8kq34qkwr"));
        transaction.setReceiver(Address.fromBech32("erd1p72ru5zcdsvgkkcm9swtvw2zy5epylwgv8vwquptkw7ga7pfvk7qz7snzw"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(50000);
        transaction.setChainID("T");

        transaction.sign(wallet);

        assertEquals("eb000037b70dfe3d89abc50214b3ce0c4afbfe66f2b636834d46e33af690f3d0", transaction.computeHash());
    }

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
        Transaction tx = new Transaction();
        if ("".equals(pair.input)) {
            tx.setValue(null);
        } else {
            tx.setValue(new BigInteger(pair.input));
        }

        // act
        ByteString res = tx.serializeValue();
        String hexBI = Hex.encodeHexString(res.toByteArray());

        // assert
        assertEquals(pair.expected, hexBI);
    }
}
