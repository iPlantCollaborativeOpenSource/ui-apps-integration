/**
 * 
 */
package org.iplantc.core.uiapps.integration.client.view;

import java.util.List;

import org.iplantc.core.uiapps.integration.client.models.DeployedComponent;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author sriram
 *
 */
public interface DeployedComponentsListingView extends IsWidget {
    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

        DeployedComponent getSelectedDC();

        void searchDC(String searchTerm);

        void loadDeployedComponents();

    }

    public void setPresenter(final Presenter presenter);

    public void loadDC(List<DeployedComponent> list);

    public void showInfo(DeployedComponent dc);

    public void mask();

    public void unmask();

    public DeployedComponent getSelectedDC();

}