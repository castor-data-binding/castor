package org.castor.jpa.scenario.callbacks;

import javax.persistence.PostPersist;

public class BarListener2 {

    @PostPersist
    public void postPersistBarListener2() {
        CallbacksExecutionOrderMemory.addCallbackName(
                "postPersistBarListener2");
    }

}
