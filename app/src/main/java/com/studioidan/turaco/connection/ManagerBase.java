package com.studioidan.turaco.connection;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.studioidan.turaco.singeltones.DataStore;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class ManagerBase
{
	private Context context;
	protected String logTAG;

//    <WEB server ip address> : <port number> <API service>
   // private String TuracoWebSite= CPM.getString(Keys.BASE_URL,Keys.DEFAULT_URL,getContext()) + ":8080/api/";
	public Context getContext()
	{
		return context;
	}

	public void setContext(Context context) {
		if(context == null)
		{
			throw new RuntimeException("Cannot initialize manager base with null context");
		}
		this.context = context;
	}

	public ManagerBase(Context context) {
		if(context == null)
		{
			throw new RuntimeException("Cannot initialize manager base with null context");
		}
		this.context = context;
		logTAG = getClass().getSimpleName();
	}

	protected APIRequestManager getRequest(String fullPath)
	{
		APIRequestManager request = new APIRequestManager(context);
		request.setUri(buildPath(fullPath));
		return request;
	}

	protected String buildPath(String subPath)
	{
		StringBuilder sb = new StringBuilder(DataStore.getInstance().getBaseUrl() + ":8080/api/");

		sb.append(subPath);

		return sb.toString();
	}

	/**
	 * Hash map that holds an array list as a value for every given property
	 * name. This array list contains weak references of property listeners.
	 */
	protected HashMap<String, ArrayList<WeakReference<PropertyChangedListener>>> propertiesListeners;

	// protected abstract void setProperty(String propertyName);
	// protected abstract void getProperty(String propertyName);

	/**
	 * Informs all added listeners for this property that this property's value
	 * has been changed. <br >
	 * Call this method in your setter when the new value is different from
	 * previous value.
	 * 
	 * @param propertyName
	 */
	protected void propertyChanged(String propertyName)
	{
		ArrayList<WeakReference<PropertyChangedListener>> listeners = getListeners(propertyName);
		if (listeners != null)
		{
			for(int j=0;j<listeners.size();j++)
			{
				if (listeners.get(j).get()!= null)
				{
					if(Looper.myLooper() == Looper.getMainLooper())
					{
						listeners.get(j).get().onPropertyChanged(propertyName, this);
					}
					else
					{
						Handler mainHandler = new Handler(context.getMainLooper());
						final WeakReference<PropertyChangedListener> fReference = listeners.get(j);
						final String propName = propertyName;
						Runnable run = new Runnable()
						{
							@Override
							public void run()
							{
								fReference.get().onPropertyChanged(propName, ManagerBase.this);
							}
						};
						mainHandler.post(run);
					}
				}
			}
		}

		cleanListeners();
	}

	/**
	 * Find an array list of listeners to a given property or null if no
	 * listeners exist.
	 * 
	 * @param propertyName
	 * @return
	 */
	protected ArrayList<WeakReference<PropertyChangedListener>> getListeners(String propertyName)
	{
		if (propertiesListeners != null)
		{
			return propertiesListeners.get(propertyName);
		}
		return null;
	}

	/**
	 * Adds a listener to a defined property name. <br >
	 * When you want to fire "propertyChanged" event for a specific property
	 * call {@link #propertyChanged(String)}
	 * 
	 * @param propertyName
	 *            The property name that its value has been changed.
	 * @param listener
	 *            A listener that will be informed when the property value
	 *            changed. <br >
	 *            You must keep a strong reference for this listener because
	 *            this method only holds a weak reference for it. This means
	 *            that creating anonymous class that implements this interface
	 *            may not work. <br >
	 *            i.e (new PropertyChangedListener() { } ) This is not going to
	 *            work unless you keep a reference for this object.
	 */
	public void addListener(String propertyName, PropertyChangedListener listener)
	{
		if (propertiesListeners == null)
		{
			propertiesListeners = new HashMap<String, ArrayList<WeakReference<PropertyChangedListener>>>();
		}

		ArrayList<WeakReference<PropertyChangedListener>> listeners = getListeners(propertyName);
		if (listeners == null)
		{
			listeners = new ArrayList<WeakReference<PropertyChangedListener>>();
			propertiesListeners.put(propertyName, listeners);
		}

		listeners.add(new WeakReference<PropertyChangedListener>(listener));
	}

	/**
	 * Removes a listener for a specific property name if this listener's been
	 * added to this property.
	 * 
	 * @param propertyName
	 *            The property name.
	 * @param listener
	 */
	public void removeListener(String propertyName, PropertyChangedListener listener)
	{
		if (propertiesListeners != null)
		{
			ArrayList<WeakReference<PropertyChangedListener>> listeners = getListeners(propertyName);
			if (listeners != null)
			{
				WeakReference<PropertyChangedListener> r = null;
				for (WeakReference<PropertyChangedListener> weakReference : listeners)
				{
					if (weakReference.get() != null && weakReference.get() == listener)
					{
						r = weakReference;
						break;
					}
				}

				if (r != null)
				{
					listeners.remove(r);
				}
			}
		}
	}

	/**
	 * Removes this objects from listening to any property on this object.
	 * 
	 * @param listener
	 */
	public void removeListener(PropertyChangedListener listener)
	{
		if (propertiesListeners != null)
		{
			for (String propertyName : propertiesListeners.keySet())
			{
				removeListener(propertyName, listener);
			}
		}

		cleanListeners();
	}
	
	protected void removeAllListeners()
	{
		if (propertiesListeners != null)
		{
			propertiesListeners.clear();
		}
	}

	/**
	 * Removes weak references that has null values. This should be improved to
	 * throttle calls or something to improve performance.
	 */
	protected void cleanListeners()
	{
		if (propertiesListeners != null)
		{
			ArrayList<String> propertiesToRemove = new ArrayList<String>();
			for (String propertyName : propertiesListeners.keySet())
			{
				ArrayList<WeakReference<PropertyChangedListener>> listeners = getListeners(propertyName);
				if (listeners != null)
				{
					ArrayList<WeakReference<PropertyChangedListener>> listenersToRemove = new ArrayList<WeakReference<PropertyChangedListener>>();
					for (WeakReference<PropertyChangedListener> weakReference : listeners)
					{
						if (weakReference.get() == null)
						{
							listenersToRemove.add(weakReference);
						}
					}

					listeners.removeAll(listenersToRemove);
				}

				if (listeners == null || listeners.size() == 0)
				{
					propertiesToRemove.add(propertyName);
				}
			}

			for (String string : propertiesToRemove)
			{
				propertiesListeners.remove(string);
			}
		}
	}

	public static interface PropertyChangedListener
	{
		/**
		 * Callback method when an object has a change in a property.
		 * 
		 * @param propertyName
		 *            The name of the property that has changed.
		 * @param caller
		 *            The caller Object itself that has a property changed.
		 */
		public void onPropertyChanged(String propertyName, ManagerBase caller);
	}
	
}
