package multiversx.esdt.dtos;

import multiversx.Address;

import java.math.BigInteger;

public class ESDTNFTTransferTypes extends ESDTTransferTypes {
    public long nonce;

    public ESDTNFTTransferTypes(Address sender, Address receiver, String tokenIdentifier, BigInteger valueToTransfer, long nonce) {
        super(sender, receiver, tokenIdentifier, valueToTransfer);
        this.nonce = nonce;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }
}
