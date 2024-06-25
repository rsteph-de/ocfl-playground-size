package edu.example.ocfl.createdate_as_fixity.createdate_digest;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;

public class CreateDateMessageDigest extends MessageDigest {
    private Instant createDate = null;
    
    public CreateDateMessageDigest(Instant createDate) {
        super("create_date");
        this.createDate = createDate;
    }

    @Override
    protected void engineUpdate(byte input) {
        //do nothing

    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len) {
        //do nothing

    }

    //leading zero for hex
    @Override
    protected byte[] engineDigest() {
        String s = Long.toString(createDate.toEpochMilli());
        return s.length() % 2 == 0 ? HexFormat.of().parseHex(s) : HexFormat.of().parseHex("0" + s);
    }

    @Override
    protected void engineReset() {
        //do nothing
    }
    
    /** return the byte array as hex compatible String
     * add leading zero to ensure that the length of string is even 
     * 
     * @param digest as byte[]
     * @return the String containing the decimal value of the size
     */
    public static String formatHex(byte[] digest) {
        String sizeResult = HexFormat.of().formatHex(digest);
        return sizeResult.length() % 2 == 0 ? sizeResult : "0" + sizeResult;
    }
    
    /** return the byte array as hex compatible String
     * add leading zero to ensure that the length of string is even 
     * 
     * @param the size as long value
     * @return the String containing the decimal value of the size
     */
    public static String formatHex(Instant instant) {
        String sizeResult = Long.toString(instant.toEpochMilli());
        return sizeResult.length() % 2 == 0 ? sizeResult : "0" + sizeResult;
    }

}
