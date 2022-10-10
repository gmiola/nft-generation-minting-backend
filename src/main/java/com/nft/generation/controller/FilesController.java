package com.nft.generation.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.nft.generation.model.ObjToTxt;
import com.nft.generation.python.PythonRunner;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.nft.generation.model.FileInfo;
import com.nft.generation.message.ResponseMessage;
import com.nft.generation.service.StorageService;

@Controller
@CrossOrigin("http://localhost:8081")
public class FilesController {

    @Autowired
    StorageService storageService;

    @Autowired
    PythonRunner pythonRunner;


//    private final Path root = Paths.get("uploads");


    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam(name="address") String address,
                                                      @RequestParam(name="layer") String layer) {

        String message = "";
        try {
            storageService.save(file, address, layer);

            message = "Uploaded: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!" + "Exception: "+e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

//Jackson utilities convert the json object in the body to ObjToTxt automatically
    @PostMapping("/configure")
    public ResponseEntity<ResponseMessage> prova(@RequestBody JsonNode json) {

        System.out.println("Java got Json: " + json.toString());
        System.out.println("Addresss: " + json.get("address"));
        System.out.println("NumLayers: " + json.get("numLayers"));

        pythonRunner.start(json);


        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(json.toString()));
        }

    @DeleteMapping("/deleteDir")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam(name="address") String address,
                                                      @RequestParam(name="layer") String layer) {

        String message = "";
        Path path = Paths.get("uploads/"+address+"/"+layer);
        try {
            storageService.delete(path);

            message = "Deleted directory: " + path;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not delete the directory: " + path + "!" + "Exception: "+e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll(Paths.get("uploads")).map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }


    @PostMapping("/generate")
    public ResponseEntity<List<FileInfo>> generateNFT(@RequestParam(name="address") String address, @RequestBody JsonNode json) {
        pythonRunner.start(json); //in the json there are all the arguments for the py script

        Path results = Paths.get("uploads/"+address+"/Results");
        List<FileInfo> fileInfos = storageService.loadAll(results).map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }



    //for the browser to load results images
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

}


