package net.artux.pdanetwork.communication;

import javax.servlet.AsyncContext;

public class Waiter extends Thread {

    private final AsyncContext asyncContext;

    Waiter(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    public void complete() {
        asyncContext.complete();
        interrupt();
    }

    @Override
    public void run() {

    }
}
