package org.castor.jpa.scenario.lob;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "Lob_entity")
public class LobEntity {

    private long id;
    private String clob;
    private byte[] blob;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Lob
    public String getClob() {
        return clob;
    }

    public void setClob(final String clob) {
        this.clob = clob;
    }

    @Lob
    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(final byte[] blob) {
        this.blob = blob;
    }

}
