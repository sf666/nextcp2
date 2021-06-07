package nextcp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerService
{

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class.getName());

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private long counter = 0;

    private List<ISchedulerService> notifyList = new ArrayList<>();

    public SchedulerService()
    {
    }

    public void addNotifier(ISchedulerService notifier)
    {
        notifyList.add(notifier);
    }

    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime()
    {
        counter++;
        for (ISchedulerService iSchedulerService : notifyList)
        {
            iSchedulerService.tick(counter);
        }
    }

}
