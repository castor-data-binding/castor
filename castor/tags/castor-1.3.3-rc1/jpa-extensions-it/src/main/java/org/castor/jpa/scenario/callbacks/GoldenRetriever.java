package org.castor.jpa.scenario.callbacks;

import javax.persistence.*;

@Entity
@EntityListeners(GoldenRetrieverListener.class)
@Table(name = "EntityListeners_retriever")
public class GoldenRetriever extends Dog {

    private long id;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @PostPersist
    protected void postPersistAnimal() {
        CallbacksExecutionOrderMemory.addCallbackName(
                "postPersistAnimalFromGoldenRetriever");
    }

}
