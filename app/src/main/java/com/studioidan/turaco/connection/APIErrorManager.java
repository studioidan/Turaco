package com.studioidan.turaco.connection;

import android.content.Context;


import com.studioidan.turaco.Model.APIError;

import java.util.HashMap;

/**
 * An object to represent any Error, either by API, or connection error.
 * 
 * @author Hazem Karam
 * 
 */
public class APIErrorManager extends ModelManagerBase<APIError>
{

	public static APIErrorManager getError(Context context, int ERROR_ID)
	{
		APIErrorManager errorManager = (APIErrorManager) getManager(ERROR_ID,
				APIErrorManager.class, context);

		if (errorManager == null)
		{
			errorManager = new APIErrorManager(context, new APIError(ERROR_ID));
		}

		return errorManager;
	}

	public APIErrorManager(Context context, APIError model)
	{
		super(context, model);
	}

	@Override
	protected void onModelChanged(APIError oldModel, APIError newModel)
	{

	}

	/**
	 * Gets the current APIError message.
	 * 
	 * @return The current APIError message.
	 */
	public String getMessage()
	{
		return getModel().getMessage();
	}

	/**
	 * Sets the message for the current error.
	 * 
	 * @param message
	 */
	public void setMessage(String message)
	{
		getModel().setMessage(message);
	}

	/*
	 * Client Side errors
	 */
	/**
	 * Unknown error has occurred.
	 */
	public static final int	UNKNOWN_ERROR					= 0;

	/**
	 * No Internet connection.
	 */
	public static final int	NO_CONNECTION_ERROR				= 1;

	/**
	 * Connection was timed out.
	 */
	public static final int	CONNECTION_TIMEOUT_ERROR		= 2;

	/**
	 * Parsing response error.
	 */
	public static final int	PARSING_ERROR					= 3;

	/**
	 * No enough space is available to download.
	 */
	public static final int	NO_ENOUGH_SPACE_TO_DOWNLOAD		= 4;

	/**
	 * Requested file to download was not found
	 */
	public static final int	FILE_NOT_FOUND					= 5;

	public static final class APIErrorsFactory
	{
		private static HashMap<String, Integer> errorsMap;

		public static int getErrorID(String apiErrorMessage)
		{
			Integer errorID = getErrosMap().get(apiErrorMessage);

			if (errorID == null)
			{
				errorID = APIErrorManager.UNKNOWN_ERROR;
			}

			return errorID;
		}

		private static HashMap<String, Integer> getErrosMap()
		{
			if (errorsMap == null)
			{
				errorsMap = new HashMap<String, Integer>();

				errorsMap.put("Unknown Error", APIErrorManager.UNKNOWN_ERROR);

			}

			return errorsMap;
		}
	}
}
