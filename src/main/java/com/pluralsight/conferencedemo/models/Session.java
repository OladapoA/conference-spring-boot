package com.pluralsight.conferencedemo.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "sessions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //added to avoid the error when trying to get a specific session
public class Session {
    // the variable names are not java standard camel case names if kept the same name spelling and format, jpa will 
    // auto bind to those coloumns and you won't need to annotate them. To match standard Java cammel case, you can do
    // that by adding a @column annotation on each attribute and mapping them accordingly
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long session_id;
    private String session_name;
    private String session_description;
    private Integer session_length;

    // picking sessions to be the owner of the relationship between sessions and speakers
    // with this defined, jpa will set up the sql join automatically when we call the speakers attribute
    @ManyToMany
    @JoinTable(
        name = "session_speakers", // we have a table showing the join in our database called session_speaker
        joinColumns = @JoinColumn(name = "session_id"), // It had a session_id
        inverseJoinColumns = @JoinColumn(name = "speaker_id") // and a speaker_id foreign ID
    )
    private List<Speaker> speakers;

    public Session() {
    }

    public Long getSession_id() {
        return session_id;
    }

    public void setSession_id(Long session_id) {
        this.session_id = session_id;
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public String getSession_description() {
        return session_description;
    }

    public void setSession_description(String session_description) {
        this.session_description = session_description;
    }

    public Integer getSession_length() {
        return session_length;
    }

    public void setSession_length(Integer session_length) {
        this.session_length = session_length;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
    }
}
