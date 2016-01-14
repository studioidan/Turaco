package com.studioidan.turaco.data;

import com.studioidan.popapplibrary.CPM;
import com.studioidan.turaco.App;
import com.studioidan.turaco.entities.Keys;
import com.studioidan.turaco.entities.Panel;
import com.studioidan.turaco.entitiesNew.Client;
import com.studioidan.turaco.entitiesNew.ClientUser;
import com.studioidan.turaco.entitiesNew.Site;
import com.studioidan.turaco.utils.ArrayUtils;

import java.util.ArrayList;

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

    public void setActivePanel(Panel activePanel) {
        this.activePanel = activePanel;
    }

    public Site getActiveSite() {
        return activeSite;
    }

    public void setActiveSite(Site activeSite) {
        this.activeSite = activeSite;
        updateActivePanel();
    }

    public Panel getCurrentPanel() {
        if (activePanel == null)
            activePanel = new Panel();
        return activePanel;
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
            for (Site site : mClientUser.client.getSites()) {
                if (site.getPanels().size() > 0) {
                    this.activeSite = site;
                    updateActivePanel();
                    break;
                }
            }
        }

        /*save it to prefs*/
        return CPM.putObject(Keys.CLIENT_USER, mClientUser, App.getContext());
    }

    private void updateActivePanel() {
        /* set's first panel as active panel  */
        this.activePanel = activeSite.getPanels().get(0);
    }

    public ArrayList<Panel> getAllPanels() {
        ArrayList<Panel> answer = new ArrayList<>();
        Client client = getClientUser().client;
        for (Site site : client.sites)
            for (Panel panel : site.getPanels())
                answer.add(panel);

        return answer;
    }
}
