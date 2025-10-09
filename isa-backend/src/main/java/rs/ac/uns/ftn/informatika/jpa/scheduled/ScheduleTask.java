package rs.ac.uns.ftn.informatika.jpa.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.additionalSerices.ImageCompressor;
import rs.ac.uns.ftn.informatika.jpa.additionalSerices.StatisticsToEmail;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
public class ScheduleTask {
    // Autowire ImageCompressor instead of manually instantiating it
    @Autowired
    private final ImageCompressor imageCompressor;
    @Autowired
    private final UserService userService;
    @Autowired
    private final StatisticsToEmail statisticsToEmail;

    // Constructor injection of ImageCompressor
    public ScheduleTask(ImageCompressor imageCompressor, UserService userService, StatisticsToEmail statisticsToEmail) {
        this.imageCompressor = imageCompressor;
        this.userService = userService;
        this.statisticsToEmail = statisticsToEmail;
    }

    @Scheduled(cron = "23 23 23 * * *")
    public void cleanup() {
        LocalDate today = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        if (today.equals(lastDayOfMonth)) {
            System.out.println("Cleaning up");
            this.userService.cleanUpUsers();
        } else {
            System.out.println(today+" it is not last day of month " + lastDayOfMonth);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void compressImages() {
        imageCompressor.compressImagesInStorage();
    }

    @Scheduled(cron = " 0 0 0 * * *")
    public void sendStatisticsToUserEmail(){
        statisticsToEmail.sendUserStatisticToEmail();
    }
}
