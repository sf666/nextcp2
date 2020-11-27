package nextcp.upnp.device.mediarenderer.playlist.playStrategy;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * shuffel playlist strategy
 */
public class RandomPlaybackStrategy extends SequencialPlaybackStrategy
{
    private Random rand = new Random(System.currentTimeMillis());

    @Override
    public LinkedList<Integer> init(Collection<? extends Object> source)
    {
        LinkedList<Integer> list = super.init(source);
        randomize(list);
        return list;
    }

    @Override
    public LinkedList<Integer> addElement(LinkedList<Integer> playbackItems, int indexOf)
    {
        LinkedList<Integer> list = super.addElement(playbackItems, indexOf);
        randomize(list);
        return list;
    }

    @Override
    public LinkedList<Integer> addAllElement(LinkedList<Integer> playbackItems, int firstElementPosition, int lastElementPosition)
    {
        LinkedList<Integer> list = super.addAllElement(playbackItems, firstElementPosition, lastElementPosition);
        randomize(list);
        return list;
    }

    private void randomize(LinkedList<Integer> list)
    {
        //
        // Fisher–Yates shuffle Algorithm
        //
        int n = list.size();
        for (int i = n - 1; i > 0; i--)
        {
            int j = rand.nextInt(i + 1);
            Collections.swap(list, i, j);
        }
    }

}
