package com.studioidan.turaco.Model;

import java.io.File;
import java.util.Date;

public abstract class Model
{
	private int		ID;
	private Date insertDate;
	private Date updateDate;
	private String logoPath;
	private String title;
	private Date dateCreated;
	private String dateCreatedString;

    public Model()
	{
	}

	public Model(int ID)
	{
		this.ID = ID;
	}

	public int getID()
	{
		return ID;
	}

	public void setID(int iD)
	{
		ID = iD;
	}

	public Date getInsertDate()
	{
		return insertDate;
	}

	public void setInsertDate(Date insertDate)
	{
		this.insertDate = insertDate;
	}

	public Date getUpdateDate()
	{
		return updateDate;
	}

	public String getLogoPath()
	{
		return logoPath;
	}

	public void setLogoPath(String logoPath)
	{
		this.logoPath = logoPath;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Date getDateCreated()
	{
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}
	
	/**
	 * @return created date string which came from server ,hence it can be NULL
	 */
	public String getDateCreatedString()
	{
		return dateCreatedString;
	}

	public void setDateCreatedString(String dateCreatedString)
	{
		this.dateCreatedString = dateCreatedString;
	}

	@Override
	public boolean equals(Object o)
	{
		return o.getClass().equals(this.getClass()) && ((Model) o).getID() == getID();
	}


}
