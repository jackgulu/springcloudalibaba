package com.hikvision;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

public class CustomEvent extends RemoteApplicationEvent {

    private User user;

    public CustomEvent() {
    }

    public CustomEvent(Object source, User user, String originService, String destinationService) {
        super(source, originService, destinationService);
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
