package com.nft.generation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import javax.annotation.Resource;

import com.nft.generation.service.StorageService;
@SpringBootApplication
public class GenerationApplication implements CommandLineRunner {
	@Resource
	StorageService storageService;

	public static void main(String[] args) {

		SpringApplication.run(GenerationApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}

}
