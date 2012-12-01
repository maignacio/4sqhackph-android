package com.poweroflove.anomeron.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "Entries")
public class Entry extends Model {
	@Column(name = "Body")
	public String body;
	
	@Column(name = "LocationName")
	public String location;
	
	@Column(name = "Longitude")
	public Double longitude;
	
	@Column(name = "Lattitude")
	public Double lattitude;
	
	@Column(name = "ImageURI")
	public String imageURI;
	
	@Column(name = "User")
	public String user;
	
	@Column(name = "UserThumbURI")
	public String userThumbUri;
	
	public static List<Entry> getEntries() {
		return new Select().from(Entry.class).execute();
	}
	
	public static void deleteAllEntries() {
		new Delete().from(Entry.class).execute();
	}
}
