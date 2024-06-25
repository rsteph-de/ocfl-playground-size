package edu.example.ocfl.createdate_as_fixity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Comparator;

import edu.example.ocfl.createdate_as_fixity.createdate_digest.CreateDateDigestAlgorithm;
import edu.example.ocfl.createdate_as_fixity.createdate_digest.CreateDateMessageDigest;
import edu.example.ocfl.size_as_fixity.size_digest.SizeDigestAlgorithm;
import edu.example.ocfl.size_as_fixity.size_digest.SizeMessageDigest;
import io.ocfl.api.OcflRepository;
import io.ocfl.api.model.ObjectVersionId;
import io.ocfl.api.model.OcflObjectVersion;
import io.ocfl.api.model.OcflObjectVersionFile;
import io.ocfl.api.model.VersionInfo;
import io.ocfl.core.OcflRepositoryBuilder;
import io.ocfl.core.extension.storage.layout.config.HashedNTupleLayoutConfig;

public class OCFLTestWithCreateDateAsFixity {

    public void run() {
        Path repoDir = Paths.get("c:\\Temp\\ocfl-test\\ocfl-repo"); // This directory contains the OCFL storage root.
        Path workDir = Paths.get("c:\\Temp\\ocfl-test\\ocfl-work"); // This directory is used to assemble OCFL versions. It cannot be within the OCFL storage root.

        Path sampleFile1 = Paths.get("c:\\Temp\\ocfl-test\\temp\\rfc3986.txt.pdf");
        Path sampleFile2 = Paths.get("c:\\Temp\\ocfl-test\\temp\\rfc8493.txt.pdf");
        Path sampleFile3 = Paths.get("c:\\Temp\\ocfl-test\\temp\\rfc8493_updated.txt.pdf"); 
        try {

            //init OCFL repository
            Files.walk(repoDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
            Files.createDirectories(repoDir);

            OcflRepository repo = new OcflRepositoryBuilder()
                .defaultLayoutConfig(new HashedNTupleLayoutConfig())
                .storage(storage -> storage.fileSystem(repoDir))
                .workDir(workDir)
                .build();

            //does not work with customized fixity fields
            //repo.putObject(ObjectVersionId.head("o2"), sampleFile1,
            //    new VersionInfo().setMessage("initial commit"));
            Instant inst1 = Instant.now();
            String s1 = SizeMessageDigest.formatHex(Files.size(sampleFile1));
            
            repo.updateObject(ObjectVersionId.head("o2"), new VersionInfo().setMessage("update"), updater -> {
                
                updater
                    .addPath(sampleFile1, "uri/rfc3986.txt.pdf")
                    .addFileFixity("uri/rfc3986.txt.pdf", new CreateDateDigestAlgorithm(inst1),
                        CreateDateMessageDigest.formatHex(inst1))
                    .addFileFixity("uri/rfc3986.txt.pdf", new SizeDigestAlgorithm(), s1);
                   
            });

            

            //retrieve size from File
            String s = SizeMessageDigest.formatHex(Files.size(sampleFile2));

            //example for use of sizeMessageDigest
            SizeMessageDigest sizeDigest = new SizeMessageDigest();
            sizeDigest.update(Files.readAllBytes(sampleFile2));
            String sizeResult = SizeMessageDigest.formatHex(sizeDigest.digest());

            System.out.println("Size: from file: " + s + " / from digest algorithm: " + sizeResult);

            //add new files to OCFL object (with size as fixity)
            repo.updateObject(ObjectVersionId.head("o2"), new VersionInfo().setMessage("update"), updater -> {
                Instant inst2 = Instant.now();
                updater
                    .addPath(sampleFile3, "bagit/rfc8493_updated.txt.pdf")
                    .addFileFixity("bagit/rfc8493_updated.txt.pdf", new CreateDateDigestAlgorithm(inst1),
                        CreateDateMessageDigest.formatHex(inst1))
                    .addFileFixity("bagit/rfc8493_updated.txt.pdf", new SizeDigestAlgorithm(), s)
                    .addPath(sampleFile2, "bagit/rfc8493.txt.pdf")
                    .addFileFixity("bagit/rfc8493.txt.pdf", new CreateDateDigestAlgorithm(inst2),
                        CreateDateMessageDigest.formatHex(inst2))
                    .addFileFixity("bagit/rfc8493.txt.pdf", new SizeDigestAlgorithm(), s)
                    .addPath(sampleFile2, "bagit__copy/rfc8493.txt.pdf")
                    .addFileFixity("bagit__copy/rfc8493.txt.pdf", new CreateDateDigestAlgorithm(inst2),
                        CreateDateMessageDigest.formatHex(inst2))
                    .addFileFixity("bagit__copy/rfc8493.txt.pdf", new SizeDigestAlgorithm(), s);
            });

            //retrieve Date from OCFL repo
            OcflObjectVersion obj = repo.getObject(ObjectVersionId.head("o2"));
            OcflObjectVersionFile file = obj.getFile("bagit__copy/rfc8493.txt.pdf");
            String sizeOut = file.getFixity().get(new CreateDateDigestAlgorithm(Instant.now()));
            System.out.println("Date: from OCFL: " + sizeOut);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
