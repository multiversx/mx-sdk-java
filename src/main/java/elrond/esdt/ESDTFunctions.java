package elrond.esdt;

import elrond.Address;
import elrond.Exceptions;
import elrond.Transaction;
import elrond.esdt.dtos.ESDTNFTTransferTypes;
import elrond.esdt.dtos.ESDTTransferTypes;

import java.math.BigInteger;

public class ESDTFunctions {
    public static boolean isESDTTransfer(Transaction transaction) {
        return transaction.getData().startsWith(ESDTConstants.ESDTTransferPrefix);
    }

    public static boolean isNFTTransfer(Transaction transaction) {
        return transaction.getData().startsWith(ESDTConstants.ESDTNFTTransferPrefix);
    }

    public static ESDTTransferTypes extractESDTTransferTypes(Transaction transaction) throws Exceptions.InvalidESDTTransferPayload {
        /*
        Format:

        Data: "ESDTTransfer" +
          "@" + <token identifier in hexadecimal encoding> +
          "@" + <value to transfer in hexadecimal encoding>
         */

        if (!isESDTTransfer(transaction)) {
            throw new Exceptions.InvalidESDTTransferPayload();
        }

        String payload = transaction.getData();
        String[] elements = payload.split(ESDTConstants.ScCallArgumentsSeparator);
        if (elements.length < 3) {
            throw new Exceptions.InvalidESDTTransferPayload();
        }

        String tokenIdentifier = elements[1];
        BigInteger valueToTransfer = decodeHexEncodedBigInteger(elements[2]);

        return new ESDTTransferTypes(
                transaction.getSender(),
                transaction.getReceiver(),
                tokenIdentifier,
                valueToTransfer
        );
    }

    public static ESDTNFTTransferTypes extractESDTNFTTransferTypes(Transaction transaction) throws Exceptions.InvalidESDTNFTTransferPayload, Exceptions.AddressException {
        /*
        Format:

        Data: "ESDTNFTTransfer" +
          "@" + <token identifier in hexadecimal encoding> +
          "@" + <the NFT nonce in hexadecimal encoding> +
          "@" + <quantity to transfer in hexadecimal encoding> +
          "@" + <destination address in hexadecimal encoding>
         */

        if (!isNFTTransfer(transaction)) {
            throw new Exceptions.InvalidESDTNFTTransferPayload();
        }

        String payload = transaction.getData();
        String[] elements = payload.split(ESDTConstants.ScCallArgumentsSeparator);
        if (elements.length < 5) {
            throw new Exceptions.InvalidESDTNFTTransferPayload();
        }

        String tokenIdentifier = elements[1];
        long nonce = Long.parseLong(elements[2], 16);
        BigInteger valueToTransfer = decodeHexEncodedBigInteger(elements[3]);
        Address receiver = Address.fromHex(elements[4]);

        return new ESDTNFTTransferTypes(
                transaction.getSender(),
                receiver,
                tokenIdentifier,
                valueToTransfer,
                nonce
        );
    }

    /**
     * This function is deprecated. We recommend using ESDTTransferBuilder
     */
    @Deprecated
    public static String constructESDTTransferPayload(ESDTTransferTypes types) {
        return ESDTConstants.ESDTTransferPrefix +
                ESDTConstants.ScCallArgumentsSeparator +
                types.getTokenIdentifier() +
                ESDTConstants.ScCallArgumentsSeparator +
                prepareHexValue(types.getValueToTransfer().toString(16));
    }

    /**
     * This function is deprecated. We recommend using ESDTNFTTransferBuilder
     */
    @Deprecated
    public static String constructNFTTransferPayload(ESDTNFTTransferTypes types) {
        return ESDTConstants.ESDTNFTTransferPrefix +
                ESDTConstants.ScCallArgumentsSeparator +
                types.getTokenIdentifier() +
                ESDTConstants.ScCallArgumentsSeparator +
                prepareHexValue(Long.toString(types.getNonce(), 16)) +
                ESDTConstants.ScCallArgumentsSeparator +
                prepareHexValue(types.getValueToTransfer().toString(16)) +
                ESDTConstants.ScCallArgumentsSeparator +
                types.getReceiver().hex();
    }

    private static BigInteger decodeHexEncodedBigInteger(String number) {
        return new BigInteger(number, 16);
    }

    private static String prepareHexValue(String value) {
        String result = value;
        if (result.length() % 2 == 1) {
            result = "0" + result;
        }

        return result;
    }
}
