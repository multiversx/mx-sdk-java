package multiversx.esdt.builders;

import multiversx.Address;
import multiversx.Exceptions;
import multiversx.esdt.common.TokenIdentifier;
import multiversx.esdt.common.TokenPayment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiTransferBuilderTest {

    @Test
    public void testMultiTransferBuild() throws Exceptions.AddressException {
        TokenPayment[] payments = {
                TokenPayment.nonFungible(TokenIdentifier.fromString("MXJAVA-38f249"),1L),
                TokenPayment.fungibleFromAmount(TokenIdentifier.fromString("BAR-c80d29"), "10", 18)
        };

        String result = new MultiTransferBuilder()
                .setPayments(payments)
                .setReceiver(Address.fromBech32("erd1qyu5wthldzr8wx5c9ucg8kjagg0jfs53s8nr3zpz3hypefsdd8ssycr6th"))
                .build();
        assertEquals("MultiESDTNFTTransfer@0139472eff6886771a982f3083da5d421f24c29181e63888228dc81ca60d69e1@02@4d584a4156412d333866323439@01@01@4241522d633830643239@00@008ac7230489e80000", result);
    }
}
