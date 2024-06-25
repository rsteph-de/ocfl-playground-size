package edu.example.ocfl.createdate_as_fixity.createdate_digest;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Objects;

import io.ocfl.api.model.DigestAlgorithm;

public class CreateDateDigestAlgorithm extends DigestAlgorithm {
    private Instant createDate = null;
    public CreateDateDigestAlgorithm(Instant createDate) {
        super("create_date", "");
        this.createDate = createDate;
    }

    @Override
    public MessageDigest getMessageDigest() {
        return new CreateDateMessageDigest(createDate);
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
