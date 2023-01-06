package multiversx;

import java.io.IOException;
import java.math.BigInteger;

import multiversx.Exceptions.AddressException;
import multiversx.Exceptions.ProxyRequestException;

public class Account {
    private final Address address;
    private long nonce = 0;
    private BigInteger balance = BigInteger.valueOf(0);

    public Account(Address address) {
        this.address = address;
    }

    /**
     * Synchronizes account properties with the ones queried from the Network
     * 
     * @param provider the Network provider
     * @throws IOException if HTTP call fails
     * @throws AddressException if the address isn't correct
     * @throws ProxyRequestException if there is an issue with the proxy request
     */
    public void sync(IProvider provider) throws AddressException, IOException, ProxyRequestException {
        AccountOnNetwork accountOnNetwork = provider.getAccount(this.address);
        this.nonce = accountOnNetwork.getNonce();
        this.balance = accountOnNetwork.getBalance();
    }

    public Address getAddress() {
        return address;
    }

    public long getNonce() {
        return nonce;
    }

    public void incrementNonce() {
        this.nonce++;
    }

    public BigInteger getBalance() {
        return balance;
    }
}
