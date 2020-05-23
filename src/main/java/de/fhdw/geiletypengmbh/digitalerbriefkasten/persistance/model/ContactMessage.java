//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
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

    @Column(nullable = false)
    @Email
    private String emailAddress;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private java.sql.Date creationDate;

    public ContactMessage() {
        super();
    }

    public ContactMessage(String message, String emailAddress) {
        super();
        this.message = message;
        this.emailAddress = emailAddress;
    }

    public ContactMessage(String message, User user, String emailAddress) {
        super();
        this.message = message;
        this.user = user;
        this.emailAddress = emailAddress;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
