package com.pluralsight.conferencedemo.controllers;

import java.util.List;

import com.pluralsight.conferencedemo.models.Session;
import com.pluralsight.conferencedemo.repositories.SessionRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {
    @Autowired
    private SessionRepository sessionRepository;

    // the bellow does not have a request mapping meaning if you make a call to "/api/v1/sessions", it will route to this method
    @GetMapping
    public List<Session> list() {
        // findAll is a autobuilt method
        return sessionRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Session get(@PathVariable Long id){
        return sessionRepository.getOne(id); //auto built method
    }

    // the bellow does not have a request mapping meaning if you make a call to "/api/v1/sessions", it will route to this method
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Session create(@RequestBody final Session session){
        // spring mvc is taking in all the attributes of a JSON payload and automatically martialling them into a session object
        // you can save objects as you are working with it but it doesn't actually save until you flush, this can be done all at once.
        return sessionRepository.saveAndFlush(session); 
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        // also need to check for children records before deleting
        // you could put in logic here to delete the children records
        sessionRepository.deleteById(id);
    }

    // we are requiring the ID on the URL similar to the delete and get endpoint and changing the http verb to a put
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Session update(@PathVariable Long id, @RequestBody Session session) {
        //becayse this is a PUT, we expect all attributes to be passed in. A PATCH would only require the information being changed.
        //TODO: Add validation that all attributes are passed in, otherwise return a 400 bad payload
        // in order to update a record, we first need the existing one
        Session existingSession = sessionRepository.getOne(id); 
        // this takes the existing sessiong and copies the incoming session data onto it
        // the third parameter allows us to ignore properties on the entities or java objects that we do not want to copy from one to another
        // we want to ignore it as it is the primary Key, if we copy it, it will replace it with null which will cause an exception as the pkey cannot be null
        BeanUtils.copyProperties(session, existingSession, "session_id");
        return sessionRepository.saveAndFlush(existingSession);
    }
    
}
