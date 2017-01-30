package io.vidyo.service.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;

public class GenerateToken {

    public static final String PROVISION_TOKEN = "provision";
    private static final long EPOCH_SECONDS = 62167219200l;
    private static final String DELIM = "\0";


    public static String generateProvisionToken(String key, String jid, int expires, String vcard) throws NumberFormatException {
        String payload = String.join(DELIM, PROVISION_TOKEN, jid, calculateExpiry(expires), vcard);
        return new String(Base64.encodeBase64(
            (String.join(DELIM, payload, HmacUtils.hmacSha384Hex(key, payload))).getBytes()
        ));
    }

    public static String calculateExpiry(int expiresLong) throws NumberFormatException {
        long currentUnixTimestamp = System.currentTimeMillis() / 1000;
        return ""+(EPOCH_SECONDS + currentUnixTimestamp + expiresLong);
    }

}
