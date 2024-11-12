package rs.ac.uns.ftn.informatika.jpa.additionalSerices;
import java.io.File;
import java.io.IOException;

public class ImageCompressor {

    // Define the threshold for file age (1 month)
    private static final long ONE_MONTH_IN_MS = 30L * 24 * 60 * 60 * 1000; // 30 days in milliseconds

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
                if (timeDifference > ONE_MONTH_IN_MS ) {
                    try {
                        // Build the command to compress the image
                        String inputImage = file.getAbsolutePath();
                        String outputImage = file.getAbsolutePath().replace(".", "_compressed.");
                        String command = "magick " + inputImage + " -quality 80 " + outputImage;

                        // Run the command using ProcessBuilder
                        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
                        processBuilder.inheritIO(); // Optionally inherit the output
                        processBuilder.start().waitFor(); // Start the process and wait for it to complete

                        // Log the compression
                        System.out.println("Compressed: " + inputImage + " -> " + outputImage);

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

    public static void main(String[] args) {
        // Create an instance of ImageCompressor and compress images
        ImageCompressor compressor = new ImageCompressor();
        compressor.compressImagesInStorage();
    }
}