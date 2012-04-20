package org.castor.jpa.scenario.callbacks;

import javax.persistence.PostPersist;

public class GoldenRetrieverListener {

    @PostPersist
    protected void postPersistGoldenRetrieverListener() {
        CallbacksExecutionOrderMemory.addCallbackName(
                "postPersistGoldenRetrieverListener");
    }

}
