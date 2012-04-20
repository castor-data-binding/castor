package org.castor.jpa.scenario.table_generator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Entity(name = "table_generator_class_subject")
@TableGenerator(name = "tableGeneratorOnClassWithDefinitions", table = "table_key_generator", 
        pkColumnName = "custom_id_name", valueColumnName = "custom_id_value", pkColumnValue = "CLASS_GEN")
public class AnnotatedClass {

    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGeneratorOnClassWithDefinitions")
    public Long getId() {
        return id;
    }
}
