package org.castor.jpa.scenario.mapped_superclass;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Hardware {

    protected String name;
    protected Integer price;
    protected long Id;

    @Id
    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
