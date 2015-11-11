package com.studioidan.turaco.connection;

import android.content.Context;


import com.studioidan.turaco.Model.Model;

import java.util.Collection;

/**
 * Created by macbook on 8/29/15.
 */
public abstract class ListModelManagerBase<T extends Model> extends ModelManagerBase {
    protected Collection<T>	managers;

    public ListModelManagerBase(Context context)
    {
        super(context,null);
    }

    public abstract void addManager(T manager);

    public abstract boolean removeManager(T manager);

    /**
     * get a collection of all managers
     */
    public Collection<T> getManagers()
    {
        return managers;
    }

    /**
     * @return count for managers list size and will return 0 if it is null
     */
    public int getManagersCount()
    {
        if(managers != null)
            return managers.size();
        else
            return 0;
    }

    protected void setManager(Collection<T> managers)
    {
        this.managers = managers;
    }

    @Override
    protected void onModelChanged(Model oldModel, Model newModel) {

    }
}
