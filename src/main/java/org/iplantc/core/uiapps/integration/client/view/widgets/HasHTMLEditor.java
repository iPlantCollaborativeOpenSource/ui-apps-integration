package org.iplantc.core.uiapps.integration.client.view.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHTML;

import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;

public class HasHTMLEditor implements LeafValueEditor<String> {

    private String hasHtmlModel;
    private final HasHTML peer;
    private final AppTemplateWizardAppearance wizAppearance;

    public HasHTMLEditor(HasHTML peer, AppTemplateWizardAppearance wizAppearance) {
        this.peer = peer;
        this.wizAppearance = wizAppearance;
    }

    @Override
    public String getValue() {
        return hasHtmlModel;
    }

    @Override
    public void setValue(String value) {
        this.hasHtmlModel = value;
        peer.setHTML(wizAppearance.createContentPanelHeaderLabel(SafeHtmlUtils.fromString(value), false).asString());
    }

}