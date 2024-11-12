package rs.ac.uns.ftn.informatika.jpa.scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.additionalSerices.ImageCompressor;

@Component
public class ScheduleTask {
    // Autowire ImageCompressor instead of manually instantiating it
    @Autowired
    private final ImageCompressor imageCompressor;

    // Constructor injection of ImageCompressor
    public ScheduleTask(ImageCompressor imageCompressor) {
        this.imageCompressor = imageCompressor;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void compressImages() {
        imageCompressor.compressImagesInStorage();
    }
}
