package dev.xfj.app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Keys {
    @Id
    private String id;
    private String secret;

    public String getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }
}
