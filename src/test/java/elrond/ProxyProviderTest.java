package elrond;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ProxyProviderTest {
    private final Address testAddress = Address.fromBech32("erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz");

    public ProxyProviderTest() throws Exceptions.AddressException {
    }

    @Test
    public void getNetworkConfigInvalidResponse() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("429 Too many requests"));

        ProxyProvider provider = new ProxyProvider(server.url("").toString());
        assertThrows(com.google.gson.JsonSyntaxException.class, provider::getNetworkConfig);

        server.shutdown();
    }

    @Test
    public void getNetworkConfigResponseNotOfExpectedType() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("{'field': 123}"));

        ProxyProvider provider = new ProxyProvider(server.url("").toString());
        assertThrows(Exceptions.ProxyRequestException.class, provider::getNetworkConfig);

        server.shutdown();
    }

    @Test
    public void getNetworkConfigShouldWork() throws Exception {
        MockWebServer server = new MockWebServer();
        String response = "{\n" +
                "  \"data\": {\n" +
                "    \"config\": {\n" +
                "      \"erd_chain_id\": \"T\",\n" +
                "      \"erd_denomination\": 18,\n" +
                "      \"erd_gas_per_data_byte\": 1500,\n" +
                "      \"erd_gas_price_modifier\": \"0.01\",\n" +
                "      \"erd_latest_tag_software_version\": \"T1.2.16.0\",\n" +
                "      \"erd_meta_consensus_group_size\": 400,\n" +
                "      \"erd_min_gas_limit\": 50000,\n" +
                "      \"erd_min_gas_price\": 1000000000,\n" +
                "      \"erd_min_transaction_version\": 1,\n" +
                "      \"erd_num_metachain_nodes\": 400,\n" +
                "      \"erd_num_nodes_in_shard\": 400,\n" +
                "      \"erd_num_shards_without_meta\": 3,\n" +
                "      \"erd_rewards_top_up_gradient_point\": \"2000000000000000000000000\",\n" +
                "      \"erd_round_duration\": 6000,\n" +
                "      \"erd_rounds_per_epoch\": 1200,\n" +
                "      \"erd_shard_consensus_group_size\": 63,\n" +
                "      \"erd_start_time\": 1630954800,\n" +
                "      \"erd_top_up_factor\": \"0.500000\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"error\": \"\",\n" +
                "  \"code\": \"successful\"\n" +
                "}";

        server.enqueue(new MockResponse().setBody(response));
        server.start();

        ProxyProvider provider = new ProxyProvider(server.url("").toString());
        NetworkConfig config = provider.getNetworkConfig();

        server.shutdown();

        assertEquals("T", config.getChainID());
        assertEquals(50000, config.getMinGasLimit());
    }

    @Test
    public void getAccountInvalidResponse() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("429 Too many requests"));

        ProxyProvider provider = new ProxyProvider(server.url("").toString());
        assertThrows(com.google.gson.JsonSyntaxException.class, () -> provider.getAccount(testAddress));

        server.shutdown();
    }

    @Test
    public void getAccountResponseNotOfExpectedType() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("{'field': 123}"));

        ProxyProvider provider = new ProxyProvider(server.url("").toString());
        assertThrows(Exceptions.ProxyRequestException.class, () -> provider.getAccount(testAddress));

        server.shutdown();
    }

    @Test
    public void getAccount() throws Exception {
        MockWebServer server = new MockWebServer();

        String response = "{'data':{'account':{'address':'erd1sea63y47u569ns3x5mqjf4vnygn9whkk7p6ry4rfpqyd6rd5addqyd9lf2'" +
                ",'nonce':1,'balance':'7288750000000000000000000','username':'','code':'','codeHash':null,'rootHash':null," +
                "'codeMetadata':null,'developerReward':'0','ownerAddress':''}},'error':'','code':'successful'}";

        server.enqueue(new MockResponse().setBody(response));
        server.start();
        ProxyProvider provider = new ProxyProvider(server.url("").toString());

        AccountOnNetwork accnt = provider.getAccount(Address.fromBech32("erd1sea63y47u569ns3x5mqjf4vnygn9whkk7p6ry4rfpqyd6rd5addqyd9lf2"));

        server.shutdown();

        assertEquals("7288750000000000000000000", accnt.getBalance().toString(10));

    }

    @Test
    public void getESDTBalance() throws Exception {
        MockWebServer server = new MockWebServer();

        String response = "{\n" +
                "  \"identifier\": \"TEST-b591b0\",\n" +
                "  \"name\": \"TEST\",\n" +
                "  \"type\": \"FungibleESDT\",\n" +
                "  \"owner\": \"erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz\",\n" +
                "  \"minted\": \"1\",\n" +
                "  \"burnt\": \"0\",\n" +
                "  \"decimals\": 3,\n" +
                "  \"isPaused\": false,\n" +
                "  \"canUpgrade\": true,\n" +
                "  \"canMint\": false,\n" +
                "  \"canBurn\": false,\n" +
                "  \"canChangeOwner\": false,\n" +
                "  \"canPause\": false,\n" +
                "  \"canFreeze\": false,\n" +
                "  \"canWipe\": false,\n" +
                "  \"balance\": \"12345\"\n" +
                "}";

        server.enqueue(new MockResponse().setBody(response));
        server.start();
        ProxyProvider provider = new ProxyProvider(server.url("").toString());

        ProxyProvider.ESDTDataResponse esdtData = provider.getESDTData(testAddress, "ALC-64e960");

        server.shutdown();

        assertEquals(esdtData.balance, new BigInteger("12345"));
    }

    @Test
    public void getNFTBalance() throws Exception {
        MockWebServer server = new MockWebServer();

        String response = "{\n" +
                "  \"identifier\": \"TEST-a887c9-01\",\n" +
                "  \"collection\": \"TEST-a887c9\",\n" +
                "  \"timestamp\": 0,\n" +
                "  \"attributes\": \"\",\n" +
                "  \"nonce\": 1,\n" +
                "  \"type\": \"NonFungibleESDT\",\n" +
                "  \"name\": \"TEST\",\n" +
                "  \"creator\": \"sc address\",\n" +
                "  \"royalties\": 5,\n" +
                "  \"uris\": [\n" +
                "    \"\"\n" +
                "  ],\n" +
                "  \"url\": \"\",\n" +
                "  \"thumbnailUrl\": \"\",\n" +
                "  \"balance\": \"511\"\n" +
                "}";

        server.enqueue(new MockResponse().setBody(response));
        server.start();
        ProxyProvider provider = new ProxyProvider(server.url("").toString());

        Address address = Address.fromBech32("erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz");
        BigInteger balance = provider.getNFTBalance(address, "414c432d363465393630", 1);

        server.shutdown();

        assertEquals(balance, new BigInteger("511"));
    }
}
