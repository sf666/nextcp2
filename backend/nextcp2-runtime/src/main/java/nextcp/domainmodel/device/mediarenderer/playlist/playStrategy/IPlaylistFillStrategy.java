package nextcp.domainmodel.device.mediarenderer.playlist.playStrategy;

import java.util.Collection;
import java.util.LinkedList;

public interface IPlaylistFillStrategy
{
    public LinkedList<Integer> init(Collection<? extends Object> source);

    public LinkedList<Integer> addElement(LinkedList<Integer> playbackItems, int indexOfNewElement);

    public LinkedList<Integer> addAllElement(LinkedList<Integer> playbackItems, int firstElementPosition, int lastElementPosition);
}
