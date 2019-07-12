package com.krupizde.instagram.entities;

import java.util.Date;

import com.krupizde.downloader.IDownloadItem;
import com.krupizde.downloader.Suffix;

/**
 * Class represents transfer object for instagram medium
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class Medium implements IDownloadItem {

	private final String name;
	private String url;
	private final String destinationFolder;
	private long sizeBytes;
	private final Suffix suffix;
	private final Date dateTimeAdded;
	private final Profile profile;

	public Medium(String name, String url, String destinationFolder, long sizeBytes, Suffix suffix, Date dateTimeAdded,
			Profile profile) {
		super();
		this.name = name;
		this.url = url;
		this.destinationFolder = destinationFolder;
		this.sizeBytes = sizeBytes;
		this.suffix = suffix;
		this.dateTimeAdded = dateTimeAdded;
		this.profile = profile;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getDestinationFolder() {
		return destinationFolder;
	}

	public long getSizeBytes() {
		return sizeBytes;
	}

	public Suffix getSuffix() {
		return suffix;
	}

	public Date getDateTimeAdded() {
		return dateTimeAdded;
	}

	public Profile getProfile() {
		return profile;
	}

	@Override
	public String toString() {
		return "Medium [name=" + name + ", url=" + url + ", destinationFolder=" + destinationFolder + ", sizeBytes="
				+ sizeBytes + ", suffix=" + suffix + ", dateTimeAdded=" + dateTimeAdded + ", user=" + profile + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTimeAdded == null) ? 0 : dateTimeAdded.hashCode());
		result = prime * result + ((destinationFolder == null) ? 0 : destinationFolder.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (sizeBytes ^ (sizeBytes >>> 32));
		result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Medium other = (Medium) obj;
		if (dateTimeAdded == null) {
			if (other.dateTimeAdded != null)
				return false;
		} else if (!dateTimeAdded.equals(other.dateTimeAdded))
			return false;
		if (destinationFolder == null) {
			if (other.destinationFolder != null)
				return false;
		} else if (!destinationFolder.equals(other.destinationFolder))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sizeBytes != other.sizeBytes)
			return false;
		if (suffix != other.suffix)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
