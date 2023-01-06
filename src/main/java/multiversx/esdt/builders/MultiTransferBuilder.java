package multiversx.esdt.builders;

import multiversx.Address;
import multiversx.esdt.common.Constants;
import multiversx.esdt.common.TokenPayment;
import multiversx.esdt.common.Utils;

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

        String numTransfers = Utils.castToPaddedHex(this.payments.length);

        builder.append(Constants.ArgsDelimiter).append(numTransfers);

        for (TokenPayment payment : payments) {
            String nonceHex = Utils.castToPaddedHex(payment.getNonce());
            builder.append(Constants.ArgsDelimiter)
                    .append(String.join(Constants.ArgsDelimiter, payment.getTokenIdentifier().toHexString(), nonceHex, payment.valueToHexString()));
        }

        return builder.toString();
    }
}
