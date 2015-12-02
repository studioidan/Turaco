package com.studioidan.turaco.data;

import com.studioidan.popapplibrary.CPM;
import com.studioidan.turaco.App;
import com.studioidan.turaco.entities.Keys;
import com.studioidan.turaco.entities.Panel;
import com.studioidan.turaco.entitiesNew.ClientUser;
import com.studioidan.turaco.entitiesNew.Site;
import com.studioidan.turaco.utils.ArrayUtils;

/**
 * Created by PopApp_laptop on 02/12/2015.
 */
public class UserManager {
    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    private ClientUser mClientUser;
    private Site activeSite;
    private Panel activePanel;

    public Panel getCurrentPanel(){
        return  activePanel;
    }

    public UserManager() {
        mClientUser = CPM.getObject(Keys.CLIENT_USER, ClientUser.class, App.getContext());
        if (mClientUser == null)
            mClientUser = new ClientUser();
        /*update current site and panel*/
        setClientUser(mClientUser);
    }

    public ClientUser getClientUser() {
        return mClientUser;
    }

    public boolean setClientUser(ClientUser clientUser) {
        mClientUser = clientUser;

        /* set active site and panel */
        if (!ArrayUtils.isArrayListEmpty(mClientUser.client.sites)) {
            this.activeSite = mClientUser.client.sites.get(0);
            if (!ArrayUtils.isArrayListEmpty(activeSite.Panels)) {
                this.activePanel = activeSite.Panels.get(0);
            }
        }

        /*save it to prefs*/
        return CPM.putObject(Keys.CLIENT_USER, mClientUser, App.getContext());
    }


}
