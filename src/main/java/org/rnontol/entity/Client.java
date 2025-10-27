package org.rnontol.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@AllArgsConstructor
@NoArgsConstructor
public class Client extends PanacheEntity {

    public Long id;

    public String firstName;

    public String lastName;

    public String document;
    public int age;

    public String type;

    public static Client findByDocument(String document) {
        return find("document", document).firstResult();
    }




}
