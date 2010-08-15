package org.castor.jpa.scenario.enumerated;

import javax.persistence.Entity;
import static javax.persistence.EnumType.STRING;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Enum_entity")
public class EnumEntity {

    private long id;
    private StringEnum stringEnum;
//    private OrdinalEnum ordinalEnum;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Enumerated(STRING)
    public StringEnum getStringEnum() {
        return stringEnum;
    }

    public void setStringEnum(final StringEnum stringEnum) {
        this.stringEnum = stringEnum;
    }

//    public OrdinalEnum getOrdinalEnum() {
//        return ordinalEnum;
//    }

//    public void setOrdinalEnum(final OrdinalEnum ordinalEnum) {
//        this.ordinalEnum = ordinalEnum;
//    }

}
