package edu.example.ocfl.size_as_fixity.size_digest;
import java.security.MessageDigest;
import java.util.HexFormat;

public class SizeMessageDigest extends MessageDigest {

    public SizeMessageDigest() {
        super("size");
    }

    long size = 0;

    @Override
    protected void engineUpdate(byte input) {
        size = size + 1;

    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len) {
        size = size + len;

    }

    //leading zero for hex
    @Override
    protected byte[] engineDigest() {
        String s = Long.toString(size);
        return s.length() % 2 == 0 ? HexFormat.of().parseHex(s) : HexFormat.of().parseHex("0" + s);
    }

    @Override
    protected void engineReset() {
        size = 0;
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
    public static String formatHex(long size) {
        String sizeResult = Long.toString(size);
        return sizeResult.length() % 2 == 0 ? sizeResult : "0" + sizeResult;
    }

}
