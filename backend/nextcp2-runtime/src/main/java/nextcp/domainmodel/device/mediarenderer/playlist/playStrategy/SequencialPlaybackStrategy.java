package nextcp.domainmodel.device.mediarenderer.playlist.playStrategy;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Playlist will be played in source enumeration order.
 */
public class SequencialPlaybackStrategy implements IPlaylistFillStrategy
{

    @Override
    public LinkedList<Integer> init(Collection<? extends Object> source)
    {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < source.size(); i++)
        {
            list.add(i);
        }
        return list;
    }

    @Override
    public LinkedList<Integer> addElement(LinkedList<Integer> playbackItems, int indexOfNewSong)
    {
        playbackItems.add(indexOfNewSong);
        return playbackItems;
    }

    @Override
    public LinkedList<Integer> addAllElement(LinkedList<Integer> playbackItems, int firstElementPosition, int lastElementPosition)
    {
        for (int i = firstElementPosition; i <= lastElementPosition; i++)
        {
            playbackItems.add(i);
        }
        return playbackItems;
    }
}
