package multiversx.esdt.builders;

import multiversx.Address;
import multiversx.esdt.common.Constants;
import multiversx.esdt.common.TokenPayment;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;

public class ESDTNFTTransferBuilder {
    private TokenPayment payment;
    private Address receiver;

    public ESDTNFTTransferBuilder() {
    }

    public ESDTNFTTransferBuilder setPayment(TokenPayment payment) {
        this.payment = payment;
        return this;
    }

    public ESDTNFTTransferBuilder setReceiver(Address receiver) {
        this.receiver = receiver;
        return this;
    }

    public String build() {
        BigInteger nonceBI = new BigInteger(this.payment.getNonce().toString());
        String nonceHex = Hex.encodeHexString(nonceBI.toByteArray());
        return String.join(Constants.ArgsDelimiter,
                Constants.ESDTNFTTransferFunction,
                this.payment.getTokenIdentifier().toHexString(),
                nonceHex,
                this.payment.valueToHexString(),
                this.receiver.hex());
    }
}
