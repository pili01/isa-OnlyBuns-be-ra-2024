package rs.ac.uns.ftn.informatika.jpa.scheduled;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.additionalSerices.ImageCompressor;

@Component
public class ScheduleTask {
    private final ImageCompressor imageCompressor = new ImageCompressor();

    @Scheduled(cron = "0 0 0 * * *")
    public void compressImages() {
        imageCompressor.compressImagesInStorage();
    }

}
