package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private long id;

    @Column(nullable = false, updatable = false)
    private String message;

    @ManyToOne
    private User user;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private java.sql.Date creationDate;

    public ContactMessage() {
        super();
    }

    public ContactMessage(String message, User user) {
        super();
        this.message = message;
        this.user = user;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
