package nextcp.audioaddict.mapper;

import java.util.ArrayList;

public class Show {

	public int id;
	public String name;
	public String description;
	public String artists_tagline;
	public String description_html;
	public ArrayList<String> human_readable_schedule;
	public int ondemand_episode_count;
	public ArrayList<Channel> channels;
	public Images images;
}
