package com.example.event.services;

import com.example.event.entities.Event;

import java.util.List;

public interface IEventService {

    Event addEvent(Event event);
    List<Event> getAllEvents();
    Event getEventById(Long id);
    Event updateEvent(Event event);
    void deleteEvent(Long id);
}
