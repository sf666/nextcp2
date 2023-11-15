package nextcp.upnp.device.mediaserver.extended;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nextcp.db.service.BasicDbService;
import nextcp.db.service.KeyValuePair;
import nextcp.dto.ServerPlaylistDto;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import nextcp.upnp.device.mediaserver.UmsServerDevice;

public class DefaultPlaylistManager
{
    private static final Logger log = LoggerFactory.getLogger(DefaultPlaylistManager.class.getName());

    private ObjectMapper om = new ObjectMapper();

    private final static String KEY_ALL_PLAYLISTS = "ALL_PLAYLISTS";

    private BasicDbService db = null;
    
    private UmsServerDevice umsServerDevice = null;

    public DefaultPlaylistManager(BasicDbService db, UmsServerDevice umsServerDevice)
    {
        super();
        this.db = db;
        this.umsServerDevice = umsServerDevice;
    }

    public List<String> getSortedDefaultPlaylist(List<String> serverList)
    {
        try
        {
            updateAddDeleteChangesToDatabase(serverList);
            sortPlaylist(serverList);
            return serverList;
        }
        catch (Exception e)
        {
            log.warn("getDefaultPlaylist error", e);
            return new ArrayList<>();
        }
    }

    public void touchPlaylist(ExtendedApiMediaDevice device, String playlistName)
    {
        try
        {
            List<String> serverList = device.getAllPlaylists();
            if (serverList.remove(playlistName.toLowerCase()))
            {
                serverList.add(0, playlistName.toLowerCase());
            }
            storePlaylist(serverList);
        }
        catch (Exception e)
        {
            log.warn("touchPlaylist error", e);
        }
    }

    public List<ServerPlaylistDto> getSortedServerPlaylists(List<ServerPlaylistDto> serverPlaylist)
    {
        try
        {
            List<String> serverPlaylistNames = new ArrayList<>();
            serverPlaylist.stream().forEach(name -> serverPlaylistNames.add(name.playlistName.toLowerCase()));
            updateAddDeleteChangesToDatabase(serverPlaylistNames);
            sortServerPlaylist(serverPlaylist);
            return serverPlaylist;
        }
        catch (Exception e)
        {
            log.warn("getDefaultPlaylist error", e);
            return new ArrayList<>();
        }
    }

    private void updateAddDeleteChangesToDatabase(List<String> serverList)
    {
        Deque<String> database = getDatabasePlaylist();
        boolean changesDone = false;

        List<String> removedItems = new ArrayList<>();
        removedItems.addAll(database);
        removedItems.removeAll(serverList);
        database.removeAll(removedItems);
        if (removedItems.size() > 0)
        {
            changesDone = true;
        }

        Deque<String> addedItems = new LinkedList<>();
        addedItems.addAll(serverList);
        addedItems.removeAll(database);
        addedItems.stream().forEach(entry -> {
            database.addFirst(entry);
        });
        if (addedItems.size() > 0)
        {
            changesDone = true;
        }

        if (changesDone)
        {
            storePlaylist(database);
        }
        else
        {
            log.debug("server playlist and database playlist are in sync. No changes were saved.");
        }
    }

    private void sortServerPlaylist(List<ServerPlaylistDto> deliveredList)
    {
        LinkedList<String> database = new LinkedList<>();
        database.addAll(getDatabasePlaylist());
        Collections.sort(deliveredList, new Comparator<ServerPlaylistDto>()
        {
            @Override
            public int compare(ServerPlaylistDto o1, ServerPlaylistDto o2)
            {
                Integer idx1 = database.indexOf(o1.playlistName.toLowerCase());
                Integer idx2 = database.indexOf(o2.playlistName.toLowerCase());
                if (idx1 < 0 || idx2 < 0)
                {
                    log.warn("sorting playlists doesn't work. Some playlist items not found.");
                }
                return idx1.compareTo(idx2);
            }
        });
    }

    private void sortPlaylist(List<String> deliveredList)
    {
        LinkedList<String> database = new LinkedList<>();
        database.addAll(getDatabasePlaylist());
        Collections.sort(deliveredList, new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                Integer ixd1 = database.indexOf(o1);
                return ixd1.compareTo(database.indexOf(o2));
            }
        });
    }

    private void storePlaylist(Collection<String> list)
    {
        try
        {
            String playlistToStore = om.writeValueAsString(list);
            KeyValuePair kv = new KeyValuePair(KEY_ALL_PLAYLISTS, playlistToStore);
            db.updateJsonStoreValue(kv);
        }
        catch (Exception e)
        {
            log.warn("cannot write playlist ...", e);
        }
    }

    private Deque<String> getDatabasePlaylist()
    {
        String storedPlaylist = db.selectJsonStoreValue(KEY_ALL_PLAYLISTS);
        if (StringUtils.isAllBlank(storedPlaylist))
        {
            return new LinkedList<>();
        }
        try
        {
            return om.readValue(storedPlaylist, new TypeReference<LinkedList<String>>()
            {
            });
        }
        catch (Exception e)
        {
            log.warn("cannot read stored playlist ...", e);
            return new LinkedList<>();
        }
    }

}
