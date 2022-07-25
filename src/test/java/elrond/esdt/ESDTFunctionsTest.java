package elrond.esdt;

import elrond.Address;
import elrond.Exceptions;
import elrond.Transaction;
import elrond.esdt.dtos.ESDTNFTTransferTypes;
import elrond.esdt.dtos.ESDTTransferTypes;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ESDTFunctionsTest {
    @Test
    public void shouldComputeESDTTransferType() {
        Transaction tx = new Transaction();

        tx.setData("random data");
        assertFalse(ESDTFunctions.isESDTTransfer(tx));

        tx.setData("");
        assertFalse(ESDTFunctions.isESDTTransfer(tx));

        tx.setData("ESDTTransfer@tkn@01");
        assertTrue(ESDTFunctions.isESDTTransfer(tx));
    }

    @Test
    public void shouldComputeESDTNFTTransferType() {
        Transaction tx = new Transaction();

        tx.setData("random data");
        assertFalse(ESDTFunctions.isNFTTransfer(tx));

        tx.setData("");
        assertFalse(ESDTFunctions.isNFTTransfer(tx));

        tx.setData("ESDTTransfer@tkn@01");
        assertFalse(ESDTFunctions.isNFTTransfer(tx));

        tx.setData("ESDTNFTTransfer@tkn@01@01@receiver");
        assertTrue(ESDTFunctions.isNFTTransfer(tx));
    }

    @Test
    public void shouldExtractESDTTransferTypes() throws Exceptions.AddressException, Exceptions.InvalidESDTTransferPayload {
        Transaction tx = new Transaction();

        Address sender = Address.fromHex("fd691bb5e85d102687d81079dffce842d4dc328276d2d4c60d8fd1c3433c3293");
        Address receiver = Address.fromHex("c70cf50b238372fffaf7b7c5723b06b57859d424a2da621bcc1b2f317543aa36");
        String tokenIdentifierHex = "4552444a4156412d333866323439";
        BigInteger valueToTransfer = new BigInteger("100");

        // construct and test the data field
        String txPayload = ESDTConstants.ESDTTransferPrefix +
                ESDTConstants.ScCallArgumentsSeparator +
                tokenIdentifierHex +
                ESDTConstants.ScCallArgumentsSeparator +
                valueToTransfer.toString(16);
        assertEquals("ESDTTransfer@4552444a4156412d333866323439@64", txPayload); // check the construction of the data field

        tx.setSender(sender);
        tx.setReceiver(receiver);
        tx.setData(txPayload);

        ESDTTransferTypes result = ESDTFunctions.extractESDTTransferTypes(tx);
        assertEquals(tx.getSender(), result.getSender());
        assertEquals(tx.getReceiver(), result.getReceiver());
        assertEquals(tokenIdentifierHex, result.getTokenIdentifier());
        assertEquals(valueToTransfer, result.getValueToTransfer());
    }

    @Test
    public void shouldExtractESDTNFTTransferTypes() throws Exceptions.AddressException, Exceptions.InvalidESDTNFTTransferPayload {
        Transaction tx = new Transaction();

        Address sender = Address.fromHex("fd691bb5e85d102687d81079dffce842d4dc328276d2d4c60d8fd1c3433c3293");
        Address receiver = Address.fromHex("c70cf50b238372fffaf7b7c5723b06b57859d424a2da621bcc1b2f317543aa36");
        String tokenIdentifierHex = "4552444a4156412d333866323439";
        BigInteger valueToTransfer = new BigInteger("100");
        long nonce = 20;

        // construct and test the data field
        String txPayload = ESDTConstants.ESDTNFTTransferPrefix +
                ESDTConstants.ScCallArgumentsSeparator +
                tokenIdentifierHex +
                ESDTConstants.ScCallArgumentsSeparator +
                Long.toString(nonce, 16) +
                ESDTConstants.ScCallArgumentsSeparator +
                valueToTransfer.toString(16) +
                ESDTConstants.ScCallArgumentsSeparator +
                receiver.hex();

        assertEquals("ESDTNFTTransfer@4552444a4156412d333866323439@14@64@c70cf50b238372fffaf7b7c5723b06b57859d424a2da621bcc1b2f317543aa36", txPayload); // check the construction of the data field

        tx.setSender(sender);
        tx.setReceiver(receiver);
        tx.setData(txPayload);

        ESDTNFTTransferTypes result = ESDTFunctions.extractESDTNFTTransferTypes(tx);
        assertEquals(tx.getSender(), result.getSender());
        assertEquals(tx.getReceiver().bech32(), result.getReceiver().bech32());
        assertEquals(tokenIdentifierHex, result.getTokenIdentifier());
        assertEquals(valueToTransfer, result.getValueToTransfer());
        assertEquals(nonce, result.getNonce());
    }

    @Test
    public void extractESDTTransferTypesShouldThrowExceptionsForBadPayload() {
        assertThrows(Exceptions.InvalidESDTTransferPayload.class, () -> {
            Transaction tx = new Transaction();
            tx.setData("invalid esdt transfer payload");
            ESDTFunctions.extractESDTTransferTypes(tx);
        });

        assertThrows(Exceptions.InvalidESDTTransferPayload.class, () -> {
            Transaction tx = new Transaction();
            tx.setData("ESDTTransfer@4552444a4156412d333866323439"); // not enough arguments
            ESDTFunctions.extractESDTTransferTypes(tx);
        });
    }

    @Test
    public void extractESDTNFTTransferTypesShouldThrowExceptionsForBadPayload() {
        assertThrows(Exceptions.InvalidESDTNFTTransferPayload.class, () -> {
            Transaction tx = new Transaction();
            tx.setData("invalid nft transfer payload");
            ESDTFunctions.extractESDTNFTTransferTypes(tx);
        });

        assertThrows(Exceptions.InvalidESDTNFTTransferPayload.class, () -> {
            Transaction tx = new Transaction();
            tx.setData("ESDTTransfer@4552444a4156412d333866323439@04"); // esdt transfer, not nft
            ESDTFunctions.extractESDTNFTTransferTypes(tx);
        });

        assertThrows(Exceptions.InvalidESDTNFTTransferPayload.class, () -> {
            Transaction tx = new Transaction();
            tx.setData("ESDTNFTTransfer@4552444a4156412d333866323439@14@64"); // not enough arguments
            ESDTFunctions.extractESDTNFTTransferTypes(tx);
        });
    }

    @Test
    public void shouldConstructESDTTransferPayload() throws Exceptions.AddressException {
        Address receiver = Address.fromHex("fd691bb5e85d102687d81079dffce842d4dc328276d2d4c60d8fd1c3433c3293");
        String tokenIdentifierHex = "4552444a4156412d333866323439";
        BigInteger value = new BigInteger("100");
        ESDTTransferTypes types = new ESDTTransferTypes(receiver, receiver, tokenIdentifierHex, value);

        String result = ESDTFunctions.constructESDTTransferPayload(types);

        assertEquals("ESDTTransfer@4552444a4156412d333866323439@64", result);

        // test value with odd number of characters in hex representation. 10 = a => should be converted to 0a
        types.setValueToTransfer(new BigInteger("10"));
        result = ESDTFunctions.constructESDTTransferPayload(types);
        assertEquals("ESDTTransfer@4552444a4156412d333866323439@0a", result);
    }

    @Test
    public void shouldConstructNFTTransferPayload() throws Exceptions.AddressException {
        Address receiver = Address.fromHex("fd691bb5e85d102687d81079dffce842d4dc328276d2d4c60d8fd1c3433c3293");
        String tokenIdentifierHex = "4552444a4156412d333866323439";
        BigInteger value = new BigInteger("100");
        long nonce = 115;
        ESDTNFTTransferTypes types = new ESDTNFTTransferTypes(receiver, receiver, tokenIdentifierHex, value, nonce);

        String result = ESDTFunctions.constructNFTTransferPayload(types);

        assertEquals("ESDTNFTTransfer@4552444a4156412d333866323439@73@64@fd691bb5e85d102687d81079dffce842d4dc328276d2d4c60d8fd1c3433c3293", result);

        // test nonce with odd number of characters in hex representation. 10 = a => should be converted to 0a
        types.setNonce(10);
        result = ESDTFunctions.constructNFTTransferPayload(types);
        assertEquals("ESDTNFTTransfer@4552444a4156412d333866323439@0a@64@fd691bb5e85d102687d81079dffce842d4dc328276d2d4c60d8fd1c3433c3293", result);

        // test value with odd number of characters in hex representation. 11 = b => should be converted to 0b
        types.setValueToTransfer(new BigInteger("11"));
        result = ESDTFunctions.constructNFTTransferPayload(types);
        assertEquals("ESDTNFTTransfer@4552444a4156412d333866323439@0a@0b@fd691bb5e85d102687d81079dffce842d4dc328276d2d4c60d8fd1c3433c3293", result);
    }
}
