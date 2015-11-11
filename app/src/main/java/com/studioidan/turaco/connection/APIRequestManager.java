package com.studioidan.turaco.connection;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.apache.http.NameValuePair;


/**
 * A manager used to represent an APIRequest. This is where the actual requests
 * are being made to the server. <br>
 * If you want to run them in a queue, instead use APIRequestsManager.
 * 
 *
 * 
 */
public class APIRequestManager extends SignalManager
{
	public static final int							USER_CANCELED						= 0;
	public static final int							SYSTEM_CANCELED						= 1;
	private boolean isFileRequest=false;
	private JSONObject responseJSON;
	private byte									data[];
	private ArrayList<NameValuePair> postData;
	private URI uri;
	private String responseString;
	private int										cancel_reason;
	private ConnectionTask							currentTask;
	public APIRequestManager(Context context)
	{
		super(context);
	}
	private File mDownloadedFile;

	public File getmDownloadedFile() {
		return mDownloadedFile;

	}

	public void setmDownloadedFile(File mDownloadedFile) {
		this.mDownloadedFile = mDownloadedFile;
	}

	public boolean isFileRequest() {
		return isFileRequest;
	}

	public void setIsFileRequest(boolean isFileRequest) {
		this.isFileRequest = isFileRequest;
	}

	protected ArrayList<NameValuePair> getPostData()
	{
		return this.postData;
	}

	protected void setPostData(ArrayList<NameValuePair> postData)
	{
		this.postData = postData;
	}

	public byte[] getData()
	{
		return this.data;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public String getResponseString()
	{
		return this.responseString;
	}

	public void setResponseString(String responseString)
	{
		this.responseString = responseString;
		if (responseString != null)
		{
			JSONObject json = null;
			try
			{
				json = new JSONObject(responseString);
			}
			catch (JSONException ex)
			{

			}
			setResponseJSON(json);
		}
		else
		{
			setResponseJSON(null);
		}
	}

	protected void setResponseJSON(JSONObject json)
	{
		this.responseJSON = json;
	}

	public JSONObject getResponseJSON()
	{
		return this.responseJSON;
	}

	/**
	 * get response result
	 * 
	 * @return true if result is true ,false otherwise
	 */
	public boolean getResponseResult()
	{
		JSONObject json = getResponseJSON();
//		try
//		{
//			if (json != null && json.has("result") && json.getString("result").equalsIgnoreCase("success"))
            if(json!=null)
			{
				return true;
			}
//		}
//		catch (JSONException e)
//		{
//
//		}
		return false;
	}

	public Object getMessageJSON()
	{
		JSONObject json = getResponseJSON();

		try
		{
			return json.get("message");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public APIErrorManager getError()
	{
		JSONObject json = getResponseJSON();
		if (!getResponseResult() && json != null)
		{
			try
			{
				Object message = json.get("message");
				if(message instanceof JSONObject)
				{
					message = ((JSONObject)message).getString("result");
				}
				if (message instanceof String)
				{
					APIErrorManager error = APIErrorManager.getError(getContext(),
							APIErrorManager.APIErrorsFactory.getErrorID((String) message));
					error.setMessage((String) message);
//					if (error.getID() == APIErrorManager.INVALID_ACCESS_TOKEN
//							|| error.getID() == APIErrorManager.ACCESS_TOKEN_EXPIRED)
//					{
//					}
//					else if(error.getID() == APIErrorManager.UNREGISTERED_DEVICE_ERROR)
//					{
//						DeviceManager.getSharedManager(getContext()).unregister();
//						DeviceManager.getSharedManager(getContext()).register();
//					}
					return error;
				}
			}
			catch (JSONException e)
			{
				return APIErrorManager.getError(getContext(), APIErrorManager.PARSING_ERROR);
			}
		}
//		else if(!getResponseResult()){
//			APIErrorManager error = APIErrorManager.getError(getContext(), APIErrorManager.NO_RESPONSE_FROM_SERVER);
//			error.setMessage(APIErrorsFactory.getErrorMessage(error.getID()));
//			return error;
//		}

		return super.getError();
	}

	public URI getUri()
	{
		return uri;
	}

	public void setUri(String fullPath)
	{
		if (fullPath != null)
            try {
                this.uri = new URI(fullPath);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
	}

	public void setUri(URI uri)
	{
		this.uri = uri;
	}

	public void addPostParameter(NameValuePair nameValuePair)
	{
		if (postData == null)
		{
			postData = new ArrayList<NameValuePair>();
		}

		postData.add(nameValuePair);
	}

	public void addPostParameter(String name, String value)
	{
		this.addPostParameter(new BasicNameValuePair(name, value));
	}

	public int getCancel_reason()
	{
		return cancel_reason;
	}

	public void setCancel_reason(int cancel_reason)
	{
		this.cancel_reason = cancel_reason;
	}

	public void execute()
	{
		setLoading(true);
		startInternal();
	}

	private void startInternal()
	{
		currentTask = new ConnectionTask();
//		currentTask.execute(this);
		currentTask.executeAsyncTask(currentTask, this);
	}

	/**
	 * <p>
	 * A class to handle connections in background using AsyncTask.
	 * </p>
	 * <p>
	 * <ul>
	 * Use only this class to perform different types of Requests such as:
	 * <li>GET: HTTP GET request.</li>
	 * <li>POST: HTTP POST request with POST parameters.</li>
	 * <li>Download: Request to download a binary file.</li>
	 * </ul>
	 * ProgressUpdate expects an array of up to 4 parameters, the first two for
	 * download progress and the other two are for upload progress (if
	 * applicable).
	 * </p>
	 * 
	 *
	 * 
	 */
	protected class ConnectionTask extends AsyncTask<APIRequestManager, Long, APIErrorManager>
	{
		private APIRequestManager	request;

		public APIRequestManager getRequest()
		{
			return request;
		}

		@Override
		protected void onProgressUpdate(Long... progress)
		{
			super.onProgressUpdate(progress);

//			Log.i(logTAG, "progress = " + progress[0] + " total = " + progress[1]);

			if (progress.length >= 4)
			{
			}

		}

		@Override
		protected APIErrorManager doInBackground(APIRequestManager... params) {
			Thread.currentThread().setName("APIRequestManager");

			if (params != null && params.length > 0)
			{
				request = params[0];
			}

			if (request == null || request.getUri() == null)
			{
				throw new NullPointerException(
						"Attempt to run a ConnectionTask with null request or null URI");
			}

			if (request.getUri() != null)
			{
				String paramters = "";
				if(request.getPostData() != null && request.getPostData().size() > 0)
				{
					paramters = " with parameters " + request.getPostData().toString();
				}
				Log.i(logTAG, request.getUri().toString() + paramters);
			}
			
			APIErrorManager errorManager = null;
			if(request.isFileRequest()){
				errorManager =performDownloadRequest();
			}
            else if (request.getPostData() != null && request.getPostData().size() > 0)
			{
                errorManager = performPOSTRequest();
			}
			else
			{
                errorManager = performGETRequest();
			}

			return errorManager;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			currentTask = null;
		}

		@Override
		protected void onPostExecute(APIErrorManager error)
		{
			super.onPostExecute(error);

			request.setLoading(false);

			if (error == null)
			{
				Log.i(logTAG, "Request done ");

				if (request.getResponseString() != null)
					Log.i(logTAG, request.getResponseString());

				request.setCompleted(true);
			}
			else
			{
				Log.i(logTAG, "Request done with error id " + error.getID() + " msg: " + error.getMessage());
				request.setError(error);
			}

			currentTask = null;
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
		public <T> void executeAsyncTask(AsyncTask<APIRequestManager, Long, APIErrorManager> asyncTask, T... params) {
		    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (APIRequestManager[]) params);
		    else
		        asyncTask.execute((APIRequestManager[]) params);
		} 
		/**
		 * Processes the current APIRequestManager as an HTTP GET Request.
		 * 
		 * @return Errors if any occurred.
		 */
		private APIErrorManager performGETRequest()
		{
			HttpGet httpGet = new HttpGet();
			httpGet.setURI(getRequest().getUri());
			return performHTTPRequest(httpGet);
		}

		/**
		 * Processes the current APIRequestManager as a POST HTTP Request.
		 * 
		 * @return Errors if any occurred.
		 */
		private APIErrorManager performPOSTRequest()
		{
//			try
//			{
//				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(getRequest().getPostData(), HTTP.UTF_8);
                HttpPost mPost=new HttpPost(getRequest().getUri());
                return performHTTPRequest(mPost);
//				return performPOSTRequestFromEntity(entity);
//			}
//			catch (UnsupportedEncodingException e)
//			{
//				APIErrorManager error = APIErrorManager.getError(getContext(), APIErrorManager.UNKNOWN_ERROR);
//				error.setMessage(e.getMessage());
//				return error;
//			}
		}

		private File readRemoteImage(String mComingPath){
			File mFile=null;
			InputStream is=null;
			OutputStream os=null;

			try {
				is = (InputStream) new URL(mComingPath).getContent();
				String ex=mComingPath.substring(mComingPath.lastIndexOf(""), mComingPath.length());
				mFile=new File(getContext().getCacheDir(),"remoted."+ex);
				os=new FileOutputStream(mFile);

				final byte[] buffer = new byte[1024];
	            int read;
	            try{
					while ((read=is.read(buffer))!=-1) {
						os.write(buffer, 0, read);
					}
				os.flush();
	            }finally{
					os.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return mFile;
		}
		private APIErrorManager performPOSTRequestFromEntity(HttpEntity entity)
		{
			HttpPost httppost = new HttpPost(getRequest().getUri());
			httppost.setEntity(entity);
			return performHTTPRequest(httppost);
		}

		/**
		 * Performs given HTTPUriRequest whether it's GET or POST.. <br >
		 * Also it publishes download progress of the response. <br >
		 * It should be improved to also report upload progress of the request.
		 * 
		 * @param httpRequest
		 * @return
		 */
		private APIErrorManager performHTTPRequest(HttpUriRequest httpRequest)
		{
			APIErrorManager error = null;
			try
			{
				BufferedReader in = null;
				String data = null;
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httpRequest);
				InputStream inputStream = response.getEntity().getContent();
				long length = response.getEntity().getContentLength();
				long downloaded = 0;
				in = new BufferedReader(new InputStreamReader(inputStream));

				StringBuilder sb = new StringBuilder();

				publishProgress(new Long[] { downloaded, length, 1L, 1L });

				char[] buffer = new char[1024];
				int count = 0;
				while ((count = in.read(buffer)) >= 0 && !isCancelled())
				{
					sb.append(buffer, 0, count);
					downloaded += count;
					publishProgress(new Long[] { downloaded, length, 1L, 1L });
				}

				in.close();
				data = sb.toString();
				try{

						getRequest().setResponseString(data);
//						error=APIErrorManager.getError(getContext(), APIErrorManager.NO_RESPONSE_FROM_SERVER);
				}catch(NullPointerException e){
					e.printStackTrace();
					error=APIErrorManager.getError(getContext(), APIErrorManager.UNKNOWN_ERROR);
				}
			}
			catch (ClientProtocolException ex)
			{
				error = APIErrorManager.getError(getContext(), APIErrorManager.NO_CONNECTION_ERROR);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
				error = APIErrorManager.getError(getContext(), APIErrorManager.NO_CONNECTION_ERROR);
			}
			return error;
		}

		/**
		 * Processes the current APIRequestManager as a request to download the
		 * object
		 *
		 * @return Errors if any occurred.
		 */
		private APIErrorManager performDownloadRequest()
		{
			APIErrorManager errorManager = null;
			try
			{
				URL url = getRequest().getUri().toURL();
				long fullSize = getContentSizeFromURL(url);


				File fileObj = new File(getContext().getCacheDir(),"remoted");
//				if (!validateFile(fileObj, fullSize))
//				{
//					fileObj = new File(getContext().getCacheDir(),"remoted");
//					if (!validateFile(fileObj, fullSize))
//					{
//						if (fileObj.length() > fullSize)
//						{
//							fileObj.delete();
//						}

						errorManager = downloadURL(url, fileObj, fileObj.length());
//					}
//				}
			}
			catch (MalformedURLException ex)
			{
				errorManager = APIErrorManager.getError(getContext(), APIErrorManager.UNKNOWN_ERROR);
				errorManager.setMessage(ex.getMessage());
			}
			catch (IOException ex)
			{
				errorManager = APIErrorManager.getError(getContext(), APIErrorManager.NO_CONNECTION_ERROR);
				errorManager.setMessage(ex.getMessage());
			}

			return errorManager;
		}

		private long getContentSizeFromURL(URL url) throws IOException
		{
			URLConnection connection = url.openConnection();

			connection.connect();

			return connection.getContentLength();
		}

		/**
		 * Validates a given {@link File} object exists and is same as given
		 * full size. Should be modified later to also do an MD5 check.
		 */
		private boolean validateFile(File fileObj, long fullSize)
		{
			if (fileObj.exists() && fileObj.length() == fullSize)
			{
				return true;
			}
			return false;
		}

		/**
		 * Downloads given URL content to a file.
		 * @param url The URL that contains the content to download.
		 * @param downloadFile The {@link File} which be will used to download to.
		 * @param startPosition The start position of the download, this will be include
		 *            as Range value for the header in the request.
		 */
		private APIErrorManager downloadURL(URL url, File downloadFile, long startPosition)
		{
			APIErrorManager errorManager = null;

			if (url == null || downloadFile == null)
				return errorManager;

			try
			{
				URLConnection connection = url.openConnection();
				if (startPosition > 0)
				{// bytes=from - to
					connection.setRequestProperty("Range", "bytes=" + downloadFile.length() + "-");
				}
				connection.connect();

				long downloadSize = connection.getContentLength();

				if (validateAvailableDiskSpace(downloadSize))
				{
					BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
					FileOutputStream output = new FileOutputStream(downloadFile, true);

					int count = 0;
					Long totalDownloaded = startPosition;
					Long filesLength = downloadSize + startPosition;

					publishProgress(new Long[] { totalDownloaded, filesLength });

					long lastPublishedProgress = 0;

					byte data[] = new byte[1024];
					int bytesRead=0;//this is used to count how many bytes in total was read so we can stop after reading 3MB
					while(totalDownloaded < filesLength)
					{
						while((count = input.read(data)) != -1 && !isCancelled())
						{
							totalDownloaded += count;
							bytesRead += count;
							output.write(data, 0, count);
							// Publish progress every 10 KB
							if(totalDownloaded > lastPublishedProgress + 10240)
							{
								publishProgress(new Long[]{totalDownloaded, filesLength});
								lastPublishedProgress = totalDownloaded;
							}
							if(bytesRead >= 2097152)
							{//if i read 2 MB, break then start again
								bytesRead = 0;
								input.close();
								connection = url.openConnection();
								connection.setRequestProperty("Range", "bytes=" + totalDownloaded + "-");
								connection.connect();
								input = new BufferedInputStream(connection.getInputStream());
								break;
							}
						}
						if(count == -1)
							break;
					}
					output.flush();
					output.close();
					input.close();
					// Make sure progress is published when the download is completed
					publishProgress(new Long[] { totalDownloaded, filesLength });

					if (!validateFile(downloadFile, filesLength) && !isCancelled())
					{
						errorManager = APIErrorManager.getError(getContext(), APIErrorManager.UNKNOWN_ERROR);
						errorManager.setMessage("Downloaded file was not same size as requested file");
					}
					getRequest().setmDownloadedFile(downloadFile);
				}
				else
				{
					errorManager = APIErrorManager.getError(getContext(),
							APIErrorManager.NO_ENOUGH_SPACE_TO_DOWNLOAD);
				}
			}
			catch (FileNotFoundException ex)
			{
				errorManager = APIErrorManager.getError(getContext(), APIErrorManager.FILE_NOT_FOUND);
			}
			catch (IOException ex)
			{
				errorManager = APIErrorManager.getError(getContext(), APIErrorManager.NO_CONNECTION_ERROR);
				errorManager.setMessage(ex.getMessage());
			}
			catch (Exception ex)
			{
				errorManager = APIErrorManager.getError(getContext(), APIErrorManager.UNKNOWN_ERROR);
				errorManager.setMessage(ex.getMessage());
			}

			return errorManager;
		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
		@SuppressWarnings("deprecation")
		private boolean validateAvailableDiskSpace(long downloadSize)
		{
			StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
			long sdAvailSize = 0;

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
			{
				sdAvailSize = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
			}
			else
			{
				sdAvailSize = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
			}

			if (downloadSize >= sdAvailSize)
			{
				return false;
			}

			return true;
		}
	}
}
