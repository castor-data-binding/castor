

package org.castor.jpa.scenario.named_native_queries;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@Entity
@Table(name = "NamedNativeQueries_student")
@NamedNativeQueries({
    @NamedNativeQuery(name = "nativeFetchAllStudents", query = "SELECT * FROM NamedNativeQueries_student"),
    @NamedNativeQuery(name = "nativeSelectMax", query = "SELECT * FROM NamedNativeQueries_student WHERE firstname = 'Max'")
})
public class StudentWithValidQueries implements Student{
    private long id;
    private String firstName;
    private String lastName;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}
