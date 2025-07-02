package com.example.petapp.cloudinary;
import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;


public class CloudinaryService {

    private Cloudinary cloudinary;

    public CloudinaryService() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "SEU_CLOUD_NAME");
        config.put("api_key", "SUA_API_KEY");
        config.put("api_secret", "SEU_API_SECRET");
        cloudinary = new Cloudinary(config);
    }

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

}
