package nextcp.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerService
{
    private long counter = 0;

    private List<ISchedulerService> notifyListOneSecond = new ArrayList<>();

    public SchedulerService()
    {
    }

    public synchronized void addNotifierOneSecond(ISchedulerService notifier)
    {
        if (!notifyListOneSecond.contains(notifier))
        {
            notifyListOneSecond.add(notifier);
        }
    }

    @Scheduled(fixedRate = 1000)
    public synchronized void oneSecondTicker()
    {
        counter++;
        for (ISchedulerService iSchedulerService : notifyListOneSecond)
        {
            iSchedulerService.tick(counter);
        }
    }

}
