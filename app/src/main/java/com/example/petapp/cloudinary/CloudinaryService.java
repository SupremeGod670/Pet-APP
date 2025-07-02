package com.example.petapp.cloudinary;
import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;


public class CloudinaryService {

    private Cloudinary cloudinary;

    public CloudinaryService() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dm5le1bql");
        config.put("api_key", "914539638445221");
        config.put("api_secret", "MnTVlczsGvSeyBUmF6YQO_UTY4c");
        cloudinary = new Cloudinary(config);
    }

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

}
