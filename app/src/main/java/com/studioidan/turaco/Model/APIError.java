package com.studioidan.turaco.Model;

public class APIError extends Model
{
	private String message;

	public APIError(int ID)
	{
		super(ID);
	}
    public APIError(String mComingMessage){
        setMessage(mComingMessage);
    }
	
	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
