package edu.example.ocfl.size_as_fixity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import io.ocfl.api.DigestAlgorithmRegistry;
import io.ocfl.api.OcflRepository;
import io.ocfl.api.model.ObjectVersionId;
import io.ocfl.api.model.OcflObjectVersion;
import io.ocfl.api.model.OcflObjectVersionFile;
import io.ocfl.api.model.VersionInfo;
import io.ocfl.core.OcflRepositoryBuilder;
import io.ocfl.core.extension.storage.layout.config.HashedNTupleLayoutConfig;

public class OCFLTestWithSizeNew {

    public void run() {
        Path repoDir = Paths.get("c:\\Temp\\ocfl-test\\ocfl-repo"); // This directory contains the OCFL storage root.
        Path workDir = Paths.get("c:\\Temp\\ocfl-test\\ocfl-work"); // This directory is used to assemble OCFL versions. It cannot be within the OCFL storage root.

        Path sampleFile1 = Paths.get("c:\\Temp\\ocfl-test\\temp\\rfc3986.txt.pdf");
        Path sampleFile2 = Paths.get("c:\\Temp\\ocfl-test\\temp\\rfc8493.txt.pdf");
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

            repo.putObject(ObjectVersionId.head("o1"), sampleFile1,
                new VersionInfo().setMessage("initial commit"));

            //retrieve size from File
            String s = String.valueOf(Files.size(sampleFile2));

            System.out.println("Size: from file: " + s);

            //add new files to OCFL object (with size as fixity)
            repo.updateObject(ObjectVersionId.head("o1"), new VersionInfo().setMessage("update"), updater -> {
                updater.addPath(sampleFile2, "bagit/rfc8493.txt.pdf")
                    .addFileFixity("bagit/rfc8493.txt.pdf", DigestAlgorithmRegistry.getAlgorithm("size"), s)
                    .addPath(sampleFile2, "bagit__copy/rfc8493.txt.pdf")
                    .addFileFixity("bagit__copy/rfc8493.txt.pdf", DigestAlgorithmRegistry.getAlgorithm("size"), s);
            });

            //retrieve size from OCFL repo
            OcflObjectVersion obj = repo.getObject(ObjectVersionId.head("o1"));
            OcflObjectVersionFile file = obj.getFile("bagit__copy/rfc8493.txt.pdf");
            String sizeOut = file.getFixity().get(DigestAlgorithmRegistry.getAlgorithm("size"));
            System.out.println("Size: from OCFL: " + sizeOut);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
