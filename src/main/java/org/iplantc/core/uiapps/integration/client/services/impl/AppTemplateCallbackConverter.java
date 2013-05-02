package org.iplantc.core.uiapps.integration.client.services.impl;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class AppTemplateCallbackConverter extends AsyncCallbackConverter<String, AppTemplate> {

    public AppTemplateCallbackConverter(AsyncCallback<AppTemplate> callback) {
        super(callback);
    }

    @Override
    public void onFailure(Throwable caught) {
        // TODO Auto-generated method stub

    }

    @Override
    protected AppTemplate convertFrom(String object) {
        AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
        AutoBean<AppTemplate> atAb = AutoBeanCodex.decode(factory, AppTemplate.class, object);
        return atAb.as();
    }

}