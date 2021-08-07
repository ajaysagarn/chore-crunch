package com.ajsa.dyrepo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepositoryException extends Exception{

    public int status;
    public Message errorMessage;

    public RepositoryException(int status, String message){
        this.status = status;
        this.errorMessage = new Message(status,message);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class Message{
        int status;
        String message;
    }

}
