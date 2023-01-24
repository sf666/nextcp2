package nextcp.domainmodel.device.services;

import java.util.List;

import nextcp.dto.ContainerItemDto;
import nextcp.dto.MusicItemDto;
import nextcp.dto.PlaylistState;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.InsertInput;

public interface IPlaylistService
{

    void setShuffle(boolean inp);

    void pause();

    long getTracksMax();

    boolean getShuffle();

    void deleteAll();

    boolean getRepeat();

    void deleteId(long id);

    void play();

    void next();

    void stop();

    void setRepeat(boolean repeat);

    void previous();

    void seekId(long id);

    /**
     * 
     * @param streamUrl
     * 
     * @return TRUE if url/song was found in playlist.
     */
    boolean seekId(String streamUrl);

    void seekSecondRelative(int sec);

    void seekSecondAbsolute(long sec);

    long insert(InsertInput inp);

    PlaylistState getState();

    List<MusicItemDto> getPlaylistItems();

    void insertContainer(ContainerItemDto items);

    void insertAndPlayContainer(ContainerItemDto items);

}