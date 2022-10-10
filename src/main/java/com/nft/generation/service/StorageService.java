package com.nft.generation.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import com.nft.generation.model.ObjToTxt;
import org.springframework.core.io.Resource;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    public void init();

    public void save(MultipartFile file, String address, String layer);

    public void saveTxt(ObjToTxt file, Path path);

    public Resource load(String filename);

    public void delete(Path root);

    public void deleteAll();

    public Stream<Path> loadAll(Path root);

}
