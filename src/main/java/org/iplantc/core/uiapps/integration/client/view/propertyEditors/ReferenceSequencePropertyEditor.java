package org.iplantc.core.uiapps.integration.client.view.propertyEditors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.de.resources.client.uiapps.widgets.argumentTypes.ReferenceSelectorLabels;
import org.iplantc.de.apps.widgets.client.models.Argument;
import org.iplantc.de.apps.widgets.client.models.metadata.ReferenceGenome;
import org.iplantc.de.apps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.ClearComboBoxSelectionKeyDownHandler;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.SplittableToReferenceGenomeConverter;
import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.apps.widgets.client.view.editors.widgets.CheckBoxAdapter;

public class ReferenceSequencePropertyEditor extends AbstractArgumentPropertyEditor {

    interface EditorDriver extends SimpleBeanEditorDriver<Argument, ReferenceSequencePropertyEditor> {}
    interface ReferenceSequencePropertyEditorUiBinder extends UiBinder<Widget, ReferenceSequencePropertyEditor> {}

    private static ReferenceSequencePropertyEditorUiBinder uiBinder = GWT.create(ReferenceSequencePropertyEditorUiBinder.class);

    @UiField(provided = true)
    AppsWidgetsPropertyPanelLabels appLabels;

    @UiField
    @Path("name")
    TextField argumentOptionEditor;

    @UiField(provided = true)
    ArgumentEditorConverter<ReferenceGenome> defaultValueEditor;

    @UiField
    @Path("visible")
    CheckBoxAdapter doNotDisplay;

    @UiField
    TextField label;

    @UiField
    CheckBoxAdapter omitIfBlank, requiredEditor;

    @UiField(provided = true)
    ReferenceSelectorLabels referenceSequenceSelectorLabels;

    @UiField
    @Path("description")
    TextField toolTipEditor;

    @UiField
    FieldLabel toolTipLabel, argumentOptionLabel, selectionItemDefaultValueLabel;

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @Inject
    public ReferenceSequencePropertyEditor(AppTemplateWizardAppearance appearance, AppsWidgetsPropertyPanelLabels appLabels, AppsWidgetsContextualHelpMessages help,
            AppMetadataServiceFacade appMetadataService) {
        super(appearance);
        this.appLabels = appLabels;
        this.referenceSequenceSelectorLabels = appLabels;

        ComboBox<ReferenceGenome> comboBox = createReferenceGenomeStore(appMetadataService);
        comboBox.setEmptyText(I18N.APPS_MESSAGES.emptyListSelectionText());
        comboBox.setMinChars(1);
        ClearComboBoxSelectionKeyDownHandler handler = new ClearComboBoxSelectionKeyDownHandler(comboBox);
        comboBox.addKeyDownHandler(handler);
        defaultValueEditor = new ArgumentEditorConverter<ReferenceGenome>(comboBox, new SplittableToReferenceGenomeConverter());

        initWidget(uiBinder.createAndBindUi(this));

        selectionItemDefaultValueLabel.setHTML(appearance.createContextualHelpLabel(appLabels.singleSelectionDefaultValue(), help.singleSelectDefaultItem()));

        toolTipLabel.setHTML(appearance.createContextualHelpLabel(appLabels.toolTipText(), help.toolTip()));
        argumentOptionLabel.setHTML(appearance.createContextualHelpLabel(appLabels.argumentOption(), help.argumentOption()));
        doNotDisplay.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(appLabels.doNotDisplay()).toSafeHtml());

        requiredEditor.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(appLabels.isRequired()).toSafeHtml());

        omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                .append(appearance.createContextualHelpLabelNoFloat(appLabels.excludeWhenEmpty(), help.excludeReference()))
                .toSafeHtml());
        editorDriver.initialize(this);
        editorDriver.accept(new InitializeTwoWayBinding(this));
    }

    @Override
    public void edit(Argument argument) {
        super.edit(argument);
        editorDriver.edit(argument);
    }

    @Override
    public com.google.gwt.editor.client.EditorDriver<Argument> getEditorDriver() {
        return editorDriver;
    }

    @Override
    @Ignore
    protected LeafValueEditor<Splittable> getDefaultValueEditor() {
        return defaultValueEditor;
    }

    @Override
    protected void initLabelOnlyEditMode(boolean isLabelOnlyEditMode) {
        argumentOptionEditor.setEnabled(!isLabelOnlyEditMode);
        defaultValueEditor.setEnabled(!isLabelOnlyEditMode);
        doNotDisplay.setEnabled(!isLabelOnlyEditMode);
        omitIfBlank.setEnabled(!isLabelOnlyEditMode);
        requiredEditor.setEnabled(!isLabelOnlyEditMode);
    }

    @UiHandler("defaultValueEditor")
    void onDefaultValueChange(ValueChangeEvent<Splittable> event) {
        // Forward defaultValue onto value.
        model.setValue(event.getValue());
    }

}