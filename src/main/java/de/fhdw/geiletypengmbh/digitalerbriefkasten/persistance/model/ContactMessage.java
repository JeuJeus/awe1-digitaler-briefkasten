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
    private String title;

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

    @Column(nullable = false)
    private Boolean answered;

    public ContactMessage() {
        super();
        this.answered = false;
    }

    public ContactMessage(String title, String message, String emailAddress) {
        super();
        this.title = title;
        this.message = message;
        this.emailAddress = emailAddress;
        this.answered = false;
    }

    public ContactMessage(String title, String message, User user, String emailAddress) {
        super();
        this.title = title;
        this.message = message;
        this.user = user;
        this.emailAddress = emailAddress;
        this.answered = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }
}
