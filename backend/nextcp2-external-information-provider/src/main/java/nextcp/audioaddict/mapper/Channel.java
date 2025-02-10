package nextcp.audioaddict.mapper;

import java.util.ArrayList;
import java.util.Date;

public class Channel {

	public int id;
	public String ad_channels;
	public String channel_director;
	public String description_long;
	public String description_short;
	public String key;
	public String name;
	public boolean mypublic;
	public String ad_dfp_unit_id;
	public int network_id;
	public int premium_id;
	public int tracklist_server_id;
	public ArrayList<Artist> artists;
	public int asset_id;
	public String asset_url;
	public String banner_url;
	public String description;
	public Date created_at;
	public Date updated_at;
	public ArrayList<SimilarChannel> similar_channels;
	public Images images;
	public int[] channel_filter_ids;
}
