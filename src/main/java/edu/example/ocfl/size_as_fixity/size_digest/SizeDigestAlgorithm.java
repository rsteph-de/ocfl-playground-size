package edu.example.ocfl.size_as_fixity.size_digest;
import java.security.MessageDigest;
import java.util.Objects;

import io.ocfl.api.model.DigestAlgorithm;

public class SizeDigestAlgorithm extends DigestAlgorithm {
    public SizeDigestAlgorithm() {
        super("size", "");
    }

    @Override
    public MessageDigest getMessageDigest() {
        return new SizeMessageDigest();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || ! (o instanceof DigestAlgorithm)) {
            return false;
        }
        DigestAlgorithm that = (DigestAlgorithm) o;
        return getOcflName().equals(that.getOcflName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOcflName());
    }

}
