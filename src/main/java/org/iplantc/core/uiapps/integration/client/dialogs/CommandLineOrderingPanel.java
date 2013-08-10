package org.iplantc.core.uiapps.integration.client.dialogs;

import java.util.List;

import org.iplantc.core.resources.client.uiapps.integration.AppIntegrationMessages;
import org.iplantc.core.uiapps.integration.client.view.AppsIntegrationView;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentProperties;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * TODO JDS Update grid re-ordering on drag drop
 * 
 * @author jstroot
 * 
 */
public class CommandLineOrderingPanel extends Composite {

    private static CommandLineOrderingPanelUiBinder BINDER = GWT.create(CommandLineOrderingPanelUiBinder.class);

    private final class ArgNameValueProvider implements ValueProvider<Argument, String> {
        @Override
        public String getValue(Argument object) {
            String retVal = object.getName();
            if (Strings.isNullOrEmpty(retVal)) {
                retVal = object.getLabel();
                // FIXME JDS Need to add more robust label concatenation logic.
            }
            return retVal;
        }

        @Override
        public void setValue(Argument object, String value) {
        }

        @Override
        public String getPath() {
            return "name"; //$NON-NLS-1$
        }
    }

    interface CommandLineOrderingPanelUiBinder extends UiBinder<Widget, CommandLineOrderingPanel> {}

    @UiField(provided = true)
    ColumnModel<Argument> cm;

    @UiField(provided = true)
    ListStore<Argument> orderedStore;

    @UiField
    Grid<Argument> orderedGrid;

    private final StoreSortInfo<Argument> orderStoreSortInfo;

    private final AppsIntegrationView.Presenter presenter;

    private final ArgumentProperties argProps;

    private final AppIntegrationMessages messages;

    public CommandLineOrderingPanel(List<Argument> arguments, AppsIntegrationView.Presenter presenter, AppIntegrationMessages messages) {
        this.presenter = presenter;
        this.messages = messages;
        argProps = GWT.create(ArgumentProperties.class);
        orderStoreSortInfo = new StoreSortInfo<Argument>(argProps.order(), SortDir.ASC);
        initColumnModels();
        initListStores(arguments);
        initWidget(BINDER.createAndBindUi(this));

        new GridDragSource<Argument>(orderedGrid);
        GridDropTarget<Argument> ordDropTarget = new GridDropTarget<Argument>(orderedGrid);

        DropHandler dropHandler = new DropHandler();
        ordDropTarget.addDropHandler(dropHandler);
        ordDropTarget.setAllowSelfAsSource(true);
        ordDropTarget.setFeedback(Feedback.BOTH);

    }

    private void initColumnModels() {
        ArgNameValueProvider valueProvider = new ArgNameValueProvider();
        ColumnConfig<Argument, String> ordName = new ColumnConfig<Argument, String>(valueProvider, 140, messages.argumentLabel());
        ColumnConfig<Argument, Integer> order = new ColumnConfig<Argument, Integer>(argProps.order(), 30, messages.orderLabel());

        ordName.setSortable(false);
        ordName.setMenuDisabled(true);
        order.setSortable(false);
        order.setMenuDisabled(true);

        List<ColumnConfig<Argument, ?>> cmList = Lists.newArrayList();
        cmList.add(order);
        cmList.add(ordName);
        cm = new ColumnModel<Argument>(cmList);
    }

    private void initListStores(List<Argument> arguments) {
        orderedStore = new ListStore<Argument>(argProps.id());
        for (Argument arg : arguments) {
            if (presenter.orderingRequired(arg)) {
                Integer order = arg.getOrder();

                // JDS If the order is null or 0, set it to a number higher than the length of the
                // list to ensure that already numbered arguments are sorted into their appropriate
                // places
                if ((order == null) || (order <= 0)) {
                    arg.setOrder(arguments.size() + 1);
                }

                orderedStore.add(arg);
            }
        }
        // Set store sort info
        orderedStore.addSortInfo(orderStoreSortInfo);

        // JDS Immediately clear sort info. Otherwise, sorts will be applied when items are added to the
        // store during DnD.
        orderedStore.clearSortInfo();

        updateArgumentOrdering();

    }

    /**
     * Updates the ordering of the given list
     */
    private void updateArgumentOrdering() {
        for (Argument arg : orderedStore.getAll()) {
            arg.setOrder(orderedStore.indexOf(arg) + 1);
        }
        orderedStore.addSortInfo(orderStoreSortInfo);
        // JDS Immediately clear sort info. Otherwise, sorts will be applied when items are added to the
        // store during DnD.
        orderedStore.clearSortInfo();

    }

    private final class DropHandler implements DndDropHandler {
        @Override
        public void onDrop(DndDropEvent event) {
            updateArgumentOrdering();
            presenter.onAppTemplateChanged();
        }
    }

}
