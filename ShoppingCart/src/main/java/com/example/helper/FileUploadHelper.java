package com.example.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadHelper {
	
	public final String UPLOAD_DIR = new ClassPathResource("/static/images").getFile().getAbsolutePath();
	
	public FileUploadHelper() throws IOException {}
	
	boolean b= false;
	
	public boolean uploadFile(MultipartFile f, int id) {
		String fileName;
		String ext;
		try {
			fileName = f.getOriginalFilename();
			if(fileName.contains(".")){
	        	ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
	            fileName = fileName.substring(0,fileName.lastIndexOf("."));
	            String path = fileName+"-"+id+"."+ext;
	            Files.copy(f.getInputStream(), Paths.get(UPLOAD_DIR+File.separator+path),StandardCopyOption.REPLACE_EXISTING);
	            b = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return b;
	}

}
