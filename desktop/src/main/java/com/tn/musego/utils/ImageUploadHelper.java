package com.tn.musego.utils;

import com.cloudinary.Cloudinary;
import com.tn.musego.exceptions.MyCustomException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Skander Ben Fredj
 * @created 07-Mar-23
 * @project musego
 */

public class ImageUploadHelper {
    Cloudinary cloudinary;

    public ImageUploadHelper() {
        Map<String, String> config = new HashMap();
        config.put("cloud_name", new FunctionHelper().getProperty("project.properties", "cloudinary-cloudname"));
        config.put("api_key", new FunctionHelper().getProperty("project.properties", "cloudinary-api_key"));
        config.put("api_secret", new FunctionHelper().getProperty("project.properties", "cloudinary-api-secret"));
        cloudinary = new Cloudinary(config);
    }


    public String upload(File multipartFile, String type) throws IOException, MyCustomException {
        try {
            Map<String, String> optionsMap = new HashMap<>();
            optionsMap.put("folder", type);
            Map result = cloudinary.uploader().upload(multipartFile, optionsMap);
//            file.delete();
            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new MyCustomException("Erreur lors de l'upload de l'image");
        }
    }
}
