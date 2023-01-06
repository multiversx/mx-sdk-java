package multiversx;

import java.io.IOException;

import multiversx.Exceptions.AddressException;
import multiversx.Exceptions.CannotSerializeTransactionException;
import multiversx.Exceptions.ProxyRequestException;

public interface IProvider {
    NetworkConfig getNetworkConfig() throws IOException, ProxyRequestException;
    AccountOnNetwork getAccount(Address address) throws IOException, AddressException, ProxyRequestException;
    String sendTransaction(Transaction transaction) throws IOException, CannotSerializeTransactionException, ProxyRequestException;
}
