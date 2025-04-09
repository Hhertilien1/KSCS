package com.kitchensaver.backend.DTO; // Package declaration for the DTO (Data Transfer Object) layer

// DTO class for handling image URL requests related to a job
public class JobImageRequest {
    private String imageUrl; // Variable to store the image URL

    // Getter method to retrieve the image URL
    public String getImageUrl() {
        return imageUrl;
    }

    // Setter method to assign a value to the image URL
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
