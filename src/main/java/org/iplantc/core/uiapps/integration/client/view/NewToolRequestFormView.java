package org.iplantc.core.uiapps.integration.client.view;

import com.google.gwt.user.client.ui.IsWidget;

public interface NewToolRequestFormView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {
        void onRequestComplete();

        void onRequestError();
    }

    void setPresenter(Presenter p);

}