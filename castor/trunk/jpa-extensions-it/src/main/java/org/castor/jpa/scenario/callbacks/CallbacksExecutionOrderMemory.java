package org.castor.jpa.scenario.callbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton helper holding an ordered collection of executed callback names.
 */
public class CallbacksExecutionOrderMemory {

    private final List<String> orderedCallbackNames;
    private static final CallbacksExecutionOrderMemory INSTANCE =
            new CallbacksExecutionOrderMemory();

    private CallbacksExecutionOrderMemory() {
        this.orderedCallbackNames = new ArrayList<String>();
    }

    protected static List<String> getOrderedCallbackNames() {
        return INSTANCE.orderedCallbackNames;
    }

    protected static void addCallbackName(final String callbackName) {
        INSTANCE.orderedCallbackNames.add(callbackName);
    }

}
