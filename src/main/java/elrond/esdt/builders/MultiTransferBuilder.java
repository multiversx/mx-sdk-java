package elrond.esdt.builders;

import elrond.Address;
import elrond.esdt.common.Constants;
import elrond.esdt.common.TokenPayment;
import elrond.esdt.common.Utils;

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
