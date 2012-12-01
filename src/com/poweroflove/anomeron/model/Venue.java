package com.poweroflove.anomeron.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "Venues")
public class Venue extends Model {
	@Column(name = "Name")
	public String name;
	
	@Column(name = "Address")
	public String address;
	
	@Column(name = "Lattitude")
	public String lattitude;
	
	@Column(name = "Longitude")
	public String longitude;
	
	@Column(name = "FSQ_id")
	public String fsq_id;
	
	public static List<Venue> getEntries() {
		return new Select().from(Venue.class).execute();
	}
	
	public static void deleteAllEntries() {
		new Delete().from(Venue.class).execute();
	}
}
