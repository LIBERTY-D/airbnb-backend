package com.daniel.app.airbnb.backend.service;

import com.daniel.app.airbnb.backend.clients.PexelsClient;
import com.daniel.app.airbnb.backend.model.Images;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ImageService {

    @Autowired
    private PexelsClient pexelsClient;

    @Value("${custom.pexel-key}")
    private String PEXELS_API_KEY;

    private static final int IMAGES_PER_REQUEST = 2;

    public Images randomHouseImage() {
        try {
            List<String> imageUrls = fetchPexelsHouseImageUrls(IMAGES_PER_REQUEST);

            List<CompletableFuture<byte[]>> futures = new ArrayList<>();
            for (String url : imageUrls) {
                futures.add(downloadImageAsBytesAsync(url));
            }

            // Wait for all downloads to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            List<String> base64Images = new ArrayList<>();
            for (CompletableFuture<byte[]> future : futures) {
                byte[] bytes = future.get();
                base64Images.add(Base64.getEncoder().encodeToString(bytes));
            }

            String mainImage = base64Images.isEmpty() ? null : base64Images.get(0);
            List<String> others = base64Images.size() > 1 ? base64Images.subList(1, base64Images.size()) : Collections.emptyList();

            return new Images(null, mainImage, others);
        } catch (Exception e) {
            return new Images(null, null, Collections.emptyList());
        }
    }

    private List<String> fetchPexelsHouseImageUrls(int count) {
        // You can randomize the page for variety
        int randomPage = new Random().nextInt(10) + 1;

        Map<String, Object> response = pexelsClient.searchPhotos(
                PEXELS_API_KEY,
                "house",
                count,
                randomPage
        );

        List<String> imageUrls = new ArrayList<>();
        List<Map<String, Object>> photos = (List<Map<String, Object>>) response.get("photos");

        if (photos != null) {
            for (Map<String, Object> photo : photos) {
                Map<String, String> src = (Map<String, String>) photo.get("src");
                if (src != null && src.get("large") != null) {
                    imageUrls.add(src.get("large"));
                }
            }
        }
        return imageUrls;
    }


    @Async
    public CompletableFuture<byte[]> downloadImageAsBytesAsync(String imageUrl) throws IOException {
        byte[] bytes = downloadImageAsBytes(imageUrl);
        return CompletableFuture.completedFuture(bytes);
    }

    private byte[] downloadImageAsBytes(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set User-Agent header to mimic browser request
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");

        try (InputStream in = connection.getInputStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        }
    }
}
