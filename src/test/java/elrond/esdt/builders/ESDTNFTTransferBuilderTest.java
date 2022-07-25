package elrond.esdt.builders;

import elrond.Address;
import elrond.Exceptions;
import elrond.esdt.common.TokenIdentifier;
import elrond.esdt.common.TokenPayment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ESDTNFTTransferBuilderTest {

    @Test
    public void testNftTransferBuild() throws Exceptions.AddressException {
        String result = new ESDTNFTTransferBuilder()
                .setPayment(TokenPayment.semiFungible(TokenIdentifier.fromString("TOKEN-1q2w3e"), 18, 10L))
                .setReceiver(Address.fromBech32("erd1qyu5wthldzr8wx5c9ucg8kjagg0jfs53s8nr3zpz3hypefsdd8ssycr6th"))
                .build();
        assertEquals("ESDTNFTTransfer@544f4b454e2d317132773365@0a@12@0139472eff6886771a982f3083da5d421f24c29181e63888228dc81ca60d69e1", result);

        result = new ESDTNFTTransferBuilder()
                .setPayment(TokenPayment.nonFungible(TokenIdentifier.fromString("NFT-1q2w3e"), 5L))
                .setReceiver(Address.fromBech32("erd1qyu5wthldzr8wx5c9ucg8kjagg0jfs53s8nr3zpz3hypefsdd8ssycr6th"))
                .build();
        assertEquals("ESDTNFTTransfer@4e46542d317132773365@05@01@0139472eff6886771a982f3083da5d421f24c29181e63888228dc81ca60d69e1", result);

        result = new ESDTNFTTransferBuilder()
                .setPayment(TokenPayment.nonFungible(TokenIdentifier.fromHex("4552444a4156412d333866323439"), 1L))
                .setReceiver(Address.fromBech32("erd1qyu5wthldzr8wx5c9ucg8kjagg0jfs53s8nr3zpz3hypefsdd8ssycr6th"))
                .build();
        assertEquals("ESDTNFTTransfer@4552444a4156412d333866323439@01@01@0139472eff6886771a982f3083da5d421f24c29181e63888228dc81ca60d69e1", result);

    }
}
