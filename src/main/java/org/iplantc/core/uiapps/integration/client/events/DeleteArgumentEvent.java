package org.iplantc.core.uiapps.integration.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import org.iplantc.core.uiapps.integration.client.events.DeleteArgumentEvent.DeleteArgumentEventHandler;
import org.iplantc.de.apps.widgets.client.models.Argument;

public class DeleteArgumentEvent extends GwtEvent<DeleteArgumentEventHandler> {

    public interface DeleteArgumentEventHandler extends EventHandler {
        void doArgumentDelete(DeleteArgumentEvent event);
    }

    public static interface HasDeleteArgumentEventHandlers {
        HandlerRegistration addDeleteArgumentEventHandler(DeleteArgumentEventHandler handler);
    }


    public static final GwtEvent.Type<DeleteArgumentEventHandler> TYPE = new GwtEvent.Type<DeleteArgumentEventHandler>();
    private final Argument arg;

    public DeleteArgumentEvent(Argument arg) {
        this.arg = arg;
    }

    public Argument getArgumentToBeDeleted() {
        return arg;
    }

    @Override
    public GwtEvent.Type<DeleteArgumentEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DeleteArgumentEventHandler handler) {
        handler.doArgumentDelete(this);
    }
}
