package com.studioidan.turaco.Model;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class JSONBase
{
	protected Context context;
	private boolean			result;
	protected Object message;
	protected String logTAG;
	protected Object error;

	protected JSONObject mainJSON;

    public JSONBase(){

    }
	public JSONBase(Context context, JSONObject json)
	{
		logTAG = getClass().getSimpleName();
		this.context = context;
		this.mainJSON = json;
		if (this.mainJSON != null)
		{
			try
			{
				if (!json.isNull("result"))
				{
					JSONObject mObject = json.getJSONObject("result");
					if (mObject!=null)
						result = true;
					else
						result = false;
				}
				if (json.has("response")&&!json.isNull("response"))
					message = json.get("response");

				parseData(message);
			}
			catch (JSONException e)
			{
				Log.e(logTAG, "Exception in jsonBase  " + e.getLocalizedMessage());
			}
		}
	}

	/**
	 * @return message from recieved JSON
	 */
	public Object getMessage()
	{
		return message;
	}

	/**
	 * used to parse JSON data
	 */
	protected abstract void parseData(Object message);

	/**
	 * @return true if result was success ,false if result is error in json
	 */
	public boolean getResult()
	{
		return result;
	}
	
	protected void setResult(boolean result)
	{
		this.result = result;
	}
}
