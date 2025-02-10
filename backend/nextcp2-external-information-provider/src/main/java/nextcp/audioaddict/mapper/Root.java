package nextcp.audioaddict.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Root {

	public ArrayList<ChannelFilter> channel_filters;
	public ArrayList<Asset> assets;
	public ArrayList<Object> stream_sets;
	public ArrayList<Event> events;
	public Map<String,TrackHistoryItem> track_history;
	public Object notification;
	public String ad_network;
	public ArrayList<Object> ad_networks;
	public Date cached_at;
}
