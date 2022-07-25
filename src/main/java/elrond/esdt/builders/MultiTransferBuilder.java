package elrond.esdt.builders;

import elrond.Address;
import elrond.esdt.common.Constants;
import elrond.esdt.common.TokenPayment;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;

public class MultiTransferBuilder {
    private TokenPayment[] payments;
    private Address receiver;

    public MultiTransferBuilder() {
    }

    public MultiTransferBuilder setPayments(TokenPayment[] payments) {
        this.payments = payments;
        return this;
    }

    public MultiTransferBuilder setReceiver(Address receiver) {
        this.receiver = receiver;
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.join(Constants.ArgsDelimiter, Constants.MultiESDTNFTTransferFunction, this.receiver.hex()));

        String numTransfers = Integer.toHexString(this.payments.length);
        numTransfers = numTransfers.length() % 2 == 0 ? numTransfers : "0"+numTransfers;

        builder.append(Constants.ArgsDelimiter).append(numTransfers);

        for (TokenPayment payment : payments) {
            BigInteger nonceBI = new BigInteger(payment.getNonce().toString());
            String nonceHex = Hex.encodeHexString(nonceBI.toByteArray());
            builder.append(Constants.ArgsDelimiter)
                    .append(String.join(Constants.ArgsDelimiter, payment.getTokenIdentifier().toHexString(), nonceHex, payment.valueToHexString()));
        }

        return builder.toString();
    }
}
