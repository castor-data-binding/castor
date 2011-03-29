package org.castor.jpa.scenario.table_generator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Entity(name = "table_generator_field_subject")
public class AnnotatedField {

    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @TableGenerator(name = "tableGeneratorOnFieldWithDefinitions", table = "table_key_generator", 
            pkColumnName = "custom_id_name", valueColumnName = "custom_id_value", pkColumnValue = "FIELD_GEN")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGeneratorOnFieldWithDefinitions")
    public Long getId() {
        return id;
    }
}
