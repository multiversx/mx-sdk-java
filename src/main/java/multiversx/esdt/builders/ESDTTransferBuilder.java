package multiversx.esdt.builders;

import multiversx.esdt.common.Constants;
import multiversx.esdt.common.TokenPayment;

public class ESDTTransferBuilder {
    private TokenPayment payment;

    public ESDTTransferBuilder() {
    }

    public ESDTTransferBuilder setPayment(TokenPayment payment) {
        this.payment = payment;
        return this;
    }

    public String build() {
        return String.join(Constants.ArgsDelimiter, Constants.ESDTTransferFunction, this.payment.getTokenIdentifier().toHexString(), this.payment.valueToHexString());
    }
}
