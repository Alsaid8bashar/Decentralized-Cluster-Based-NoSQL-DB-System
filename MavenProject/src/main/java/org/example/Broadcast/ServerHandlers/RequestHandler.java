package org.example.Broadcast.ServerHandlers;

public abstract class RequestHandler {
    protected RequestHandler nextHandler;

    public void setNextHandler (RequestHandler nextHandler){
        if(nextHandler!=null)
            this.nextHandler=nextHandler;
    }


    /**
     * Handles a request for the specified topic and data.
     *
     * @param topic the topic of the request
     * @param data the data of the request
     */

    public abstract void handleRequest(String topic,Object data);

    /**
     * Receives data and processes it.
     *
     * @param dto the data to be processed
     */

    public abstract void receive(Object dto);
}
