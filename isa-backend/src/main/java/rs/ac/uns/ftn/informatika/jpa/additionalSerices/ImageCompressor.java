package rs.ac.uns.ftn.informatika.jpa.additionalSerices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;

import java.io.File;
import java.io.IOException;

@Service
public class ImageCompressor {

    // Define the threshold for file age (1 month)
    private static final long ONE_MONTH_IN_MS =   30L * 24 * 60 * 60 * 1000; // 30 days in milliseconds
    private static final long ONE_MONTH_AND_DAY_IN_MS =   31L * 24 * 60 * 60 * 1000;

    @Autowired
    private PostRepository postRepository;

    // Method to compress images in the storage folder
    public void compressImagesInStorage() {
        // Path to your storage directory
        File folder = new File("storage");

        // List all JPG and PNG files in the storage directory, excluding already compressed files (those with '_compressed' in their name)
        File[] files = folder.listFiles((dir, name) ->
                (name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")) &&
                        !name.toLowerCase().contains("_compressed")
        );

        // Iterate over each file and compress if it's older than a month
        if (files != null) {
            for (File file : files) {
                // Get the last modified time of the file
                long lastModified = file.lastModified();

                // Calculate the time difference in milliseconds
                long currentTime = System.currentTimeMillis();
                long timeDifference = currentTime - lastModified;

                // If the file is older than one month, proceed with compression
                if (ONE_MONTH_AND_DAY_IN_MS > timeDifference && timeDifference > ONE_MONTH_IN_MS ) {
                    try {
                        // Build the command to compress the image
                        String inputImage = file.getName();
                        String outputImage;
                        if(file.getAbsolutePath().endsWith(".jpg")){
                            outputImage = file.getName().replace(".jpg", "_compressed.jpg");
                        }else{
                            outputImage = file.getName().replace(".png", "_compressed.png");
                        }
                        String command = "magick " + inputImage + " -quality 80 " + outputImage;

                        // Run the command using ProcessBuilder
                        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
                        processBuilder.directory(new File(folder.getAbsolutePath()));
                        processBuilder.inheritIO(); // Optionally inherit the output
                        processBuilder.start().waitFor(); // Start the process and wait for it to complete

                        postRepository.updateImagePath(inputImage, outputImage);

                    } catch (IOException | InterruptedException e) {
                        // Handle exceptions
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("No files found in the directory.");
        }
    }
//
//    public static void main(String[] args) {
//        // Create an instance of ImageCompressor and compress images
//        ImageCompressor compressor = new ImageCompressor();
//        compressor.compressImagesInStorage();
//    }
}