package com.studioidan.turaco.connection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.studioidan.turaco.Model.Model;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public abstract class ModelManagerBase<T extends Model> extends ManagerBase implements
        Comparable<ModelManagerBase<T>>
{
	private T																					model;
	private static HashMap<String, ArrayList<WeakReference<ModelManagerBase<? extends Model>>>> managerReferences;

	public ModelManagerBase(Context context, T model)
	{
		super(context);
		setModel(model);
		addManager(this);
	}

	protected T getModel()
	{
		return model;
	}

	protected void setModel(T model)
	{
		if (this.model != model)
		{
			T oldModel = this.model;
			this.model = model;
			onModelChanged(oldModel, model);
		}
	}

	/**
	 * This is called when the internal model is changed. Override this method
	 * to update internal objects, lists or any model related info.
	 * 
	 * @param oldModel
	 * @param newModel
	 */
	protected abstract void onModelChanged(T oldModel, T newModel);

	public int getID()
	{
		return getModel().getID();
	}

	public static synchronized ModelManagerBase<? extends Model> getManager(int modelId,
			Class<? extends ModelManagerBase<? extends Model>> aClass, Context context)
	{
		ModelManagerBase<? extends Model> manager = null;

		if (managerReferences != null)
		{
			ArrayList<WeakReference<ModelManagerBase<? extends Model>>> array = getArrayListForManagers(aClass);

			if (array != null)
			{
				for (WeakReference<ModelManagerBase<? extends Model>> weakReference : array)
				{
					ModelManagerBase<? extends Model> xManager = weakReference.get();
					if (xManager != null && xManager.getModel().getID() == modelId)
					{
						manager = xManager;
						break;
					}
				}
			}
		}
		
		return manager;
	}
	
	public static synchronized ModelManagerBase<? extends Model> getManager(Model model,
			Class<? extends ModelManagerBase<? extends Model>> aClass, Context context)
	{
		ModelManagerBase<? extends Model> manager = null;

		if (managerReferences != null)
		{
			ArrayList<WeakReference<ModelManagerBase<? extends Model>>> array = getArrayListForManagers(aClass);

			if (array != null)
			{
				for (WeakReference<ModelManagerBase<? extends Model>> weakReference : array)
				{
					ModelManagerBase<? extends Model> xManager = weakReference.get();
					if (xManager != null && xManager.getModel().equals(model))
					{
						manager = xManager;
						break;
					}
				}
			}

		}
		
		return manager;
	}

	@SuppressWarnings("unchecked")
	private static synchronized void addManager(ModelManagerBase<? extends Model> manager)
	{
		ArrayList<WeakReference<ModelManagerBase<? extends Model>>> array = getArrayListForManagers((Class<? extends ModelManagerBase<? extends Model>>) manager
				.getClass());
		if (array == null)
		{
			array = new ArrayList<WeakReference<ModelManagerBase<? extends Model>>>();

			if (managerReferences == null)
			{
				managerReferences = new HashMap<String, ArrayList<WeakReference<ModelManagerBase<? extends Model>>>>();
			}
			managerReferences.put(manager.getClass().getName(), array);
		}

		array.add(new WeakReference<ModelManagerBase<? extends Model>>(manager));
	}

	@SuppressWarnings("unchecked")
	private static synchronized void removeManager(ModelManagerBase<? extends Model> manager)
	{
		ArrayList<WeakReference<ModelManagerBase<? extends Model>>> array = getArrayListForManagers((Class<? extends ModelManagerBase<? extends Model>>) manager
				.getClass());
		if (array != null)
		{
			WeakReference<ModelManagerBase<? extends Model>> weakR = null;

			for (WeakReference<ModelManagerBase<? extends Model>> weakReference : array)
			{
				ModelManagerBase<? extends Model> xManager = weakReference.get();
				if (manager != null && manager == xManager)
				{
					weakR = weakReference;
					break;
				}
			}

			if (weakR != null)
			{
				array.remove(weakR);
				if (array.size() == 0)
				{
					managerReferences.remove(manager.getClass());
					if (managerReferences.size() == 0)
					{
						managerReferences = null;
					}
				}
			}
		}
	}

	private static ArrayList<WeakReference<ModelManagerBase<? extends Model>>> getArrayListForManagers(
			Class<? extends ModelManagerBase<? extends Model>> aClass)
	{
		ArrayList<WeakReference<ModelManagerBase<? extends Model>>> array = null;
		String className = aClass.getName();
		if (managerReferences != null && managerReferences.containsKey(className))
		{
			array = managerReferences.get(className);
		}
		return array;
	}
	
	protected void parseJSON(JSONObject json)
	{
		
	}

	protected String createTableString()
	{
		return null;
	}

	protected boolean isTableExists(SQLiteDatabase db, String tableName)
	{
		if (tableName == null || db == null || !db.isOpen())
		{
			return false;
		}
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
				new String[] { "table", tableName });
		if (!cursor.moveToFirst())
		{
			return false;
		}
		int count = cursor.getInt(0);
		cursor.close();
		return count > 0;
	}

//	protected ModelDatabaseTable<T> getDatabaseTable()
//	{
//		return null;
//	}
//
//	protected boolean insertToDatabase()
//	{
//		ModelDatabaseTable<T> table = getDatabaseTable();
//		if (table != null)
//		{
//			return table.insertOrUpdate(getModel());
//		}
//		return false;
//	}
//
//	protected boolean loadFromDatabase()
//	{
//		DatabaseTable<T> table = getDatabaseTable();
//		if (table != null)
//		{
//			return table.find(getModel());
//		}
//		return false;
//	}
//
//	protected boolean deleteFromDatabase()
//	{
//		DatabaseTable<T> table = getDatabaseTable();
//		if (table != null)
//		{
//			return table.delete(getModel());
//		}
//		return false;
//	}
//
//	protected boolean updateDatabase()
//	{
//		DatabaseTable<T> table = getDatabaseTable();
//		if (table != null)
//		{
//			return table.update(getModel());
//		}
//		return false;
//	}

	@Override
	public int compareTo(ModelManagerBase<T> another)
	{
		if (this.getID() > another.getID())
			return -1;
		else if (this.getID() < another.getID())
			return 1;
		else
			return 0;
	}

	public Date getUpdateDate()
	{
		return getModel().getUpdateDate();
	}

	public String getLogoPath()
	{
		return getModel().getLogoPath();
	}

	public String getTitle()
	{
		return getModel().getTitle();
	}

	public Date getDateCreated()
	{
		return getModel().getDateCreated();
	}

	public String getDateCreatedString()
	{
		return getModel().getDateCreatedString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		return o.getClass().equals(this.getClass())
				&& ((ModelManagerBase<? extends Model>) o).getModel().equals(getModel());
	}

	@Override
	protected void finalize() throws Throwable
	{
		removeManager(this);
		super.finalize();
	}
}
