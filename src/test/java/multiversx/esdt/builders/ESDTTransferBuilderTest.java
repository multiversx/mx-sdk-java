package multiversx.esdt.builders;

import multiversx.esdt.common.TokenIdentifier;
import multiversx.esdt.common.TokenPayment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ESDTTransferBuilderTest {

    @Test
    public void testEsdtTransferBuilder() {
        String result = new ESDTTransferBuilder()
                .setPayment(TokenPayment.fungibleFromAmount(TokenIdentifier.fromString("WEGLD-bd4d79"), "10", 18))
                .build();
        assertEquals("ESDTTransfer@5745474c442d626434643739@008ac7230489e80000", result);

        result = new ESDTTransferBuilder()
                .setPayment(TokenPayment.fungibleFromAmount(TokenIdentifier.fromString("FUNG-1q2w3e"), "1.5", 8))
                .build();
        assertEquals("ESDTTransfer@46554e472d317132773365@08f0d180", result);
    }
}
