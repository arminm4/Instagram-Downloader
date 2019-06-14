package com.krupizde.instagram.entities;

/**
 * Class represents transfer object for instagram profile
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class Profile {

	private int id;
	private String name;
	private String destinationFolder;
	private boolean isValid;
	private boolean isPrivate;
	private boolean fullFinished;

	public Profile(int id, String name, String destinationFolder, boolean isValid, boolean isPrivate,
			boolean fullFinished) {
		super();
		this.id = id;
		this.name = name;
		this.destinationFolder = destinationFolder;
		this.isValid = isValid;
		this.isPrivate = isPrivate;
		this.fullFinished = fullFinished;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDestinationFolder() {
		return destinationFolder;
	}

	public void setDestinationFolder(String destinationFolder) {
		this.destinationFolder = destinationFolder;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public boolean isFullFinished() {
		return fullFinished;
	}

	public void setFullFinished(boolean fullFinished) {
		this.fullFinished = fullFinished;
	}

	@Override
	public String toString() {
		return "Profile [id=" + id + ", name=" + name + ", destinationFolder=" + destinationFolder + ", isValid="
				+ isValid + ", isPrivate=" + isPrivate + ", fullFinished=" + fullFinished + "]";
	}

}
