package multiversx;

import multiversx.esdt.builders.ESDTNFTTransferBuilder;
import multiversx.esdt.builders.ESDTTransferBuilder;
import multiversx.esdt.common.TokenIdentifier;
import multiversx.esdt.common.TokenPayment;
import org.junit.Test;

import java.math.BigInteger;

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
    public void shouldSerializeWhenGuardianFieldsAreSet() throws Exception {
        Transaction transaction = new Transaction();

        transaction.setNonce(92);
        transaction.setValue(new BigInteger("123456789000000000000000000000"));
        transaction.setSender(Address.fromBech32("erd1qyu5wthldzr8wx5c9ucg8kjagg0jfs53s8nr3zpz3hypefsdd8ssycr6th"));
        transaction.setReceiver(Address.fromBech32("erd1spyavw0956vq68xj8y4tenjpq2wd5a9p2c6j8gsz7ztyrnpxrruqzu66jx"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(150000);
        transaction.setChainID("local-testnet");
        transaction.setData("test data field");

        transaction.setGuardianAddress(Address.fromBech32("erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r"));
        transaction.setGuardianSignature("11001100110011001100110011001100110011001100110011001100110011001100110011001100110011001100110011001100110011001100110011001100");
        // we also assert that the guardian signature isn't included in the tx's serialization
        String expected = ("{'nonce':92,'value':'123456789000000000000000000000','receiver':'erd1spyavw0956vq68xj8y4tenjpq2wd5a9p2c6j8gsz7ztyrnpxrruqzu66jx','sender':'erd1qyu5wthldzr8wx5c9ucg8kjagg0jfs53s8nr3zpz3hypefsdd8ssycr6th','gasPrice':1000000000,'gasLimit':150000,'data':'dGVzdCBkYXRhIGZpZWxk','chainID':'local-testnet','version':2,'options':2,'guardian':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r'}").replace('\'', '"');
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

        // With data field
        transaction.setData("test data");

        transaction.sign(wallet);

        assertEquals("3fb8406c408fbdd9b01ce8c8a0dcbb1a382cba713a132f40d552ec8db63c89a5", transaction.computeHash());
    }

    @Test
    public void shouldComputeHashWhenGuardianFieldsAreSet() throws Exception {
        Wallet wallet = new Wallet("413f42575f7f26fad3317a778771212fdb80245850981e48b58a4f25e344e8f9");

        Transaction transaction = new Transaction();
        transaction.setNonce(92);
        transaction.setValue(new BigInteger("123456789000000000000000000000"));
        transaction.setSender(Address.fromBech32("erd1qyu5wthldzr8wx5c9ucg8kjagg0jfs53s8nr3zpz3hypefsdd8ssycr6th"));
        transaction.setReceiver(Address.fromBech32("erd1spyavw0956vq68xj8y4tenjpq2wd5a9p2c6j8gsz7ztyrnpxrruqzu66jx"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(150000);
        transaction.setChainID("local-testnet");
        transaction.setData("test data field");

        transaction.setGuardianAddress(Address.fromBech32("erd1x23lzn8483xs2su4fak0r0dqx6w38enpmmqf2yrkylwq7mfnvyhsxqw57y"));
        transaction.setGuardianSignature("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        transaction.sign(wallet);

        assertEquals("e574d78b19e1481a6b9575c162e66f2f906a3178aec537509356385c4f1a5330a9b73a87a456fc6d7041e93b5f8a1231a92fb390174872a104a0929215600c0c", transaction.getSignature());
        assertEquals("242022e9dcfa0ee1d8199b0043314dbda8601619f70069ebc441b9f03349a35c", transaction.computeHash());
    }

    @Test
    public void shouldConstructESDTTransferTransaction() throws Exception {
        // Without data field
        Transaction transaction = new Transaction();
        transaction.setNonce(1);
        transaction.setValue(new BigInteger("1000000000000000"));
        transaction.setSender(Address.fromBech32("erd1lta2vgd0tkeqqadkvgef73y0efs6n3xe5ss589ufhvmt6tcur8kq34qkwr"));
        transaction.setReceiver(Address.fromBech32("erd1p72ru5zcdsvgkkcm9swtvw2zy5epylwgv8vwquptkw7ga7pfvk7qz7snzw"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(50000);
        transaction.setChainID("T");

        String txData = new ESDTTransferBuilder()
                .setPayment(TokenPayment.fungibleFromAmount(TokenIdentifier.fromString("WEGLD-4r5t6y"), "155", 18))
                .build();

        // the action of this transaction is:
        // erd1lta2vgd0tkeqqadkvgef73y0efs6n3xe5ss589ufhvmt6tcur8kq34qkwr sends 155 WEGLD-4r5t6y tokens
        // to erd1p72ru5zcdsvgkkcm9swtvw2zy5epylwgv8vwquptkw7ga7pfvk7qz7snzw

        transaction.setData(txData);
        assertEquals("ESDTTransfer@5745474c442d347235743679@08670e9ec6598c0000", transaction.getData());
        assertEquals("c802d9ecd9e1ce1a1f445b9454a385248afbfbd39e263c14447f6be83ef06da9", transaction.computeHash());
    }

    @Test
    public void shouldConstructESDTNFTTransferTransaction() throws Exception {
       // Without data field
        Transaction transaction = new Transaction();
        transaction.setNonce(1);
        transaction.setValue(new BigInteger("1000000000000000"));
        transaction.setSender(Address.fromBech32("erd1lta2vgd0tkeqqadkvgef73y0efs6n3xe5ss589ufhvmt6tcur8kq34qkwr"));
        transaction.setReceiver(Address.fromBech32("erd1lta2vgd0tkeqqadkvgef73y0efs6n3xe5ss589ufhvmt6tcur8kq34qkwr"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(50000);
        transaction.setChainID("T");

        String txData = new ESDTNFTTransferBuilder()
                .setPayment(TokenPayment.nonFungible(TokenIdentifier.fromString("MYNFT-4r5t6y"), 37L))
                .setReceiver(Address.fromBech32("erd1p72ru5zcdsvgkkcm9swtvw2zy5epylwgv8vwquptkw7ga7pfvk7qz7snzw"))
                .build();

        // the action of this transaction is:
        // erd1lta2vgd0tkeqqadkvgef73y0efs6n3xe5ss589ufhvmt6tcur8kq34qkwr sends one NFT called MYNFT-4r5t6y with nonce 37
        // to erd1p72ru5zcdsvgkkcm9swtvw2zy5epylwgv8vwquptkw7ga7pfvk7qz7snzw

        transaction.setData(txData);
        assertEquals("ESDTNFTTransfer@4d594e46542d347235743679@25@01@0f943e50586c188b5b1b2c1cb639422532127dc861d8e0702bb3bc8ef82965bc", transaction.getData());
        assertEquals("76787ac6c5881b5756c4bb3990513d83f9399dec25433e61e8c7e36d78e67f9c", transaction.computeHash());
    }
}
