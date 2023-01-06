package multiversx.esdt.dtos;

import multiversx.Address;

import java.math.BigInteger;

public class ESDTTransferTypes {
    private Address sender;
    private Address receiver;
    private String tokenIdentifier;
    private BigInteger valueToTransfer;

    public ESDTTransferTypes(Address sender, Address receiver, String tokenIdentifier, BigInteger valueToTransfer) {
        this.sender = sender;
        this.receiver = receiver;
        this.tokenIdentifier = tokenIdentifier;
        this.valueToTransfer = valueToTransfer;
    }

    public Address getSender() {
        return sender;
    }

    public void setSender(Address sender) {
        this.sender = sender;
    }

    public Address getReceiver() {
        return receiver;
    }

    public void setReceiver(Address receiver) {
        this.receiver = receiver;
    }

    public String getTokenIdentifier() {
        return tokenIdentifier;
    }

    public void setTokenIdentifier(String tokenIdentifier) {
        this.tokenIdentifier = tokenIdentifier;
    }

    public BigInteger getValueToTransfer() {
        return valueToTransfer;
    }

    public void setValueToTransfer(BigInteger valueToTransfer) {
        this.valueToTransfer = valueToTransfer;
    }
}
