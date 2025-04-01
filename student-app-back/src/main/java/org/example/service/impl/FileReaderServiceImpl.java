package org.example.service.impl;

import org.example.service.FileReaderService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileReaderServiceImpl implements FileReaderService {
    @Override
    public String getFromFile(String path) {
        var text = "";
        try {
            text =
                    Files.readString(Path.of(path));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return text;
    }
}
