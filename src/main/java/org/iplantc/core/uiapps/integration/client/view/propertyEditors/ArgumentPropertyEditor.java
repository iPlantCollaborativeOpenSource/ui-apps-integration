package org.iplantc.core.uiapps.integration.client.view.propertyEditors;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.impl.AbstractEditorDelegate;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.ContentPanel;

import org.iplantc.core.uiapps.integration.client.events.UpdateCommandLinePreviewEvent.HasUpdateCommandLinePreviewEventHandlers;
import org.iplantc.de.apps.widgets.client.models.Argument;
import org.iplantc.de.apps.widgets.client.view.AppTemplateForm.ArgumentEditor;
import org.iplantc.de.apps.widgets.client.view.HasLabelOnlyEditMode;

public interface ArgumentPropertyEditor extends Editor<Argument>, IsWidget, HasValueChangeHandlers<Splittable>, HasLabelOnlyEditMode, HasUpdateCommandLinePreviewEventHandlers {

    /**
     * Cleans up any event handlers and instance variables related to the bound {@code ArgumentEditor}
     */
    void clean();

    ContentPanel createContentPanel();

    void edit(Argument argument);

    AbstractEditorDelegate<Argument, ? extends Editor<Argument>> getBoundEditorDelegate();

    @Ignore
    EditorDriver<Argument> getEditorDriver();

    void setBoundArgumentEditor(ArgumentEditor argumentEditor);
}
