package org.castor.jpa.scenario.temporal;

import static javax.persistence.TemporalType.DATE;
import static javax.persistence.TemporalType.TIME;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "Temporal_person")
public class Person {

    private long id;
    private Date birthDate;
    private Date anotherDate;
    private Date yetAnotherDate;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Temporal(TIMESTAMP)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(final Date birthDate) {
        this.birthDate = birthDate;
    }

    @Temporal(TIME)
    public Date getAnotherDate() {
        return anotherDate;
    }

    public void setAnotherDate(final Date anotherDate) {
        this.anotherDate = anotherDate;
    }

    @Temporal(DATE)
    public Date getYetAnotherDate() {
        return yetAnotherDate;
    }

    public void setYetAnotherDate(final Date yetAnotherDate) {
        this.yetAnotherDate = yetAnotherDate;
    }

}
