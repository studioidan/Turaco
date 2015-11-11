package com.studioidan.turaco.connection;

import android.content.Context;

import java.util.ArrayList;

/**
 * A class to manage Signal objects. Use this class to represent any
 * asynchronous method. Each SignalManager contains 3 main events: onLoading
 * When loading state is changed. onCompleted When completed is changed. onError
 * When an error is set to the Signal.
 *
 * @author Hz
 *
 */
public class SignalManager extends ManagerBase implements ManagerBase.PropertyChangedListener
{
	private ArrayList<SignalLoadingObserver> loadingObservers;
	private ArrayList<SignalCompletedObserver> completedObservers;
	private ArrayList<SignalErrorObserver> errorObservers;

	private SignalManager						childLoadingSignal;
	private SignalManager						childCompletedSignal;
	private SignalManager						childErrorSignal;

	private int									progressPercentage;
	private boolean								loading;
	private boolean								completed;
	private APIErrorManager						errorManager;

	public SignalManager(Context context)
	{
		super(context);
	}

	/**
	 * Binds the given signal to a new signal
	 * 
	 * @param signalManager
	 *            A signal to bind the constructed signal to.
	 */
	public SignalManager(SignalManager signalManager)
	{
		super(signalManager.getContext());
		setSignalManager(signalManager);
	}

	protected void clearSignal()
	{
		if (errorObservers != null)
			errorObservers.clear();

		if (loadingObservers != null)
			loadingObservers.clear();

		if (completedObservers != null)
			completedObservers.clear();

		removeAllListeners();
	}

	public boolean isLoading()
	{
		return loading;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public APIErrorManager getError()
	{
		return errorManager;
	}

	public int getProgressPercentage()
	{
		return progressPercentage;
	}

	public SignalManager setSignalManager(SignalManager signalManager)
	{
		this.setLoading(signalManager).setCompleted(signalManager).setError(signalManager);
		return this;
	}

	public SignalManager onSignalManager(SignalManager signalManager)
	{
		this.onLoading(signalManager).onError(signalManager).onCompleted(signalManager);
		return this;
	}

	/**
	 * Add an observer to onLoading event for the current Signal.
	 */
	public SignalManager onLoading(SignalLoadingObserver observer)
	{
		if (observer != null)
		{
			if (loadingObservers == null)
			{
				loadingObservers = new ArrayList<SignalLoadingObserver>();
			}

			// Update loading state
			if (this.isLoading())
				observer.onLoading(this, this.isLoading());

			if (!loadingObservers.contains(observer))
				loadingObservers.add(observer);
		}
		return this;
	}

	/**
	 * Set child SignalManager for the current SignalManager for onLoading
	 * event. Child SignalManager will fire same onLoading event when its parent
	 * event is fired.
	 */
	public SignalManager onLoading(SignalManager signalManager)
	{
		if (signalManager != null && signalManager != childLoadingSignal)
		{
			childLoadingSignal = signalManager;
			this.onLoading(new SignalLoadingObserver()
			{
				@Override
				public void onLoading(SignalManager signal, boolean loading)
				{
					childLoadingSignal.setLoading(loading);
				}
			});
		}
		return this;
	}

	/**
	 * Add an observer to onCompleted event for the current Signal.
	 */
	public SignalManager onCompleted(SignalCompletedObserver observer)
	{
		if (observer != null)
		{
			if (completedObservers == null)
			{
				completedObservers = new ArrayList<SignalCompletedObserver>();
			}

			// Update completed state
			if (this.isCompleted())
				observer.onCompleted(this, this.isCompleted());
			else if (!completedObservers.contains(observer))
				completedObservers.add(observer);
		}
		return this;
	}

	/**
	 * Set child SignalManager for the current SignalManager for onCompleted
	 * event. Child SignalManager will fire same onCompleted event when its
	 * parent event is fired.
	 */
	public SignalManager onCompleted(SignalManager signalManager)
	{
		if (signalManager != null && signalManager != childCompletedSignal)
		{
			childCompletedSignal = signalManager;
			this.onCompleted(new SignalCompletedObserver()
			{
				@Override
				public void onCompleted(SignalManager signal, boolean completed)
				{
					childCompletedSignal.setCompleted(completed);
				}
			});
		}
		return this;
	}

	/**
	 * Add an observer to onError event for the current Signal.
	 */
	public SignalManager onError(SignalErrorObserver observer)
	{
		if (observer != null)
		{
			if (errorObservers == null)
			{
				errorObservers = new ArrayList<SignalErrorObserver>();
			}

			// Update error state
			if (this.getError() != null)
				observer.onError(this, this.getError());
			else if (!errorObservers.contains(observer))
				errorObservers.add(observer);
		}
		return this;
	}

	/**
	 * Set child SignalManager for the current SignalManager for onError event.
	 * Child SignalManager will fire same onError event when its parent event is
	 * fired.
	 */
	public SignalManager onError(SignalManager signalManager)
	{
		if (signalManager != null && signalManager != childErrorSignal)
		{
			childErrorSignal = signalManager;
			this.onError(new SignalErrorObserver()
			{
				@Override
				public void onError(SignalManager signal, APIErrorManager error)
				{
					childErrorSignal.setError(error);
				}
			});
		}
		return this;
	}

	/**
	 * Sets loading state for the current Signal.
	 */
	protected void setLoading(boolean loading)
	{
		if (isLoading() != loading)
		{
			this.loading = loading;
			if (loadingObservers != null)
			{
				for (SignalLoadingObserver observer : loadingObservers)
				{
					observer.onLoading(this, loading);
				}
			}
		}
	}

	/**
	 * Sets parent SignalManager for the current SignalManager. When a parent
	 * SignalManager event is fired, the current SignalManager corresponding
	 * event will be fired as well.
	 */
	protected SignalManager setLoading(SignalManager signalManager)
	{
		if (signalManager != null)
		{
			signalManager.onLoading(new SignalLoadingObserver()
			{
				@Override
				public void onLoading(SignalManager signal, boolean loading)
				{
					SignalManager.this.setLoading(loading);
				}
			});
		}
		return this;
	}

	/**
	 * Sets completed state for the current Signal.
	 */
	public void setCompleted(boolean completed)
	{
		if (isCompleted() != completed)
		{
			this.completed = completed;
			if (completedObservers != null)
			{
				for(int j = 0;j<completedObservers.size();j++)
				{
					completedObservers.get(j).onCompleted(this, completed);
				}
			}

			if (completed)
			{
				clearSignal();
			}
		}
	}

	/**
	 * Sets parent SignalManager for the current SignalManager. When a parent
	 * SignalManager event is fired, the current SignalManager corresponding
	 * event will be fired as well.
	 */
	protected SignalManager setCompleted(SignalManager signalManager)
	{
		if (signalManager != null)
		{
			signalManager.onCompleted(new SignalCompletedObserver()
			{
				@Override
				public void onCompleted(SignalManager signal, boolean completed)
				{
					SignalManager.this.setCompleted(completed);
				}
			});
		}
		return this;
	}

	/**
	 * Sets error for the current Signal.
	 */
	public void setError(APIErrorManager error)
	{
		if (this.getError() != error)
		{
			this.errorManager = error;

			if (errorObservers != null)
			{
				for (SignalErrorObserver observer : errorObservers)
				{
					observer.onError(this, error);
				}
			}

			if (error != null)
				clearSignal();
		}
	}

	/**
	 * Sets parent SignalManager for the current SignalManager. When a parent
	 * SignalManager event is fired, the current SignalManager corresponding
	 * event will be fired as well.
	 */
	protected SignalManager setError(SignalManager signalManager)
	{
		if (signalManager != null)
		{
			signalManager.onError(new SignalErrorObserver()
			{
				@Override
				public void onError(SignalManager signal, APIErrorManager error)
				{
					SignalManager.this.setError(error);
				}
			});
		}
		return this;
	}

	public interface SignalLoadingObserver
	{
		/**
		 * Called when loading state changes at the current Signal. Implement
		 * this method
		 * 
		 * @param signal
		 *            The current SignalManager.
		 * @param loading
		 *            true means the Signal is loading, false otherwise.
		 */
		public void onLoading(SignalManager signal, boolean loading);
	}

	public interface CustomProgressLoadingObserver extends SignalLoadingObserver{
	    public void setCustomProgressBar(Object comingView);
	}
	public interface SignalCompletedObserver
	{
		/**
		 * Called when the completed state changes at the current Signal.
		 * 
		 * @param signal
		 *            The current SignalManager.
		 * @param completed
		 *            true when Signal is completed, false otherwise.
		 */
		public void onCompleted(SignalManager signal, boolean completed);
	}

	public interface SignalErrorObserver
	{
		/**
		 * Called when an error changes at the current Signal.
		 * 
		 * @param signal
		 *            The current SignalManager.
		 * @param error
		 *            The new error manager.
		 */
		public void onError(SignalManager signal, APIErrorManager error);
	}

	@Override
	public void onPropertyChanged(String propertyName, ManagerBase caller)
	{
		if (caller instanceof SignalManager)
		{
				propertyChanged(propertyName);
		}
	}

}
