package com.socgen.shield;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;

public class InterceptorMonitor implements Interceptor {

    private final Interceptor wrapped;
    private ShieldCounters counters;

    private InterceptorMonitor(Interceptor wrappedClass) {
        this.wrapped = wrappedClass;
        counters = new ShieldCounters("shield.interceptor");
    }

    @Override
    public void initialize() {
        counters.start();
        wrapped.initialize();
    }

    @Override
    public Event intercept(Event event) {
        Event intercept = wrapped.intercept(event);
        if(event != null)
            counters.incrementIn();
        return intercept;
    }

    @Override
    public List<Event> intercept(List<Event> inList) {
        counters.incrementIn(inList.size());
        List<Event> outList = wrapped.intercept(inList);
        counters.incrementOut(outList.size());
        counters.incrementRejected(inList.size() - outList.size());
        return outList;
    }

    @Override
    public void close() {
        wrapped.close();
        counters.stop();
    }

    public static class Builder implements Interceptor.Builder{

        private Interceptor.Builder wrappedClassBuilder;

        @Override
        public Interceptor build() {
            return new InterceptorMonitor(wrappedClassBuilder.build());
        }

        @Override
        public void configure(Context context) {
            try {
                wrappedClassBuilder = (Interceptor.Builder) Class.forName(context.getString("wrappedClass")).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            wrappedClassBuilder.configure(context);
        }
    }
}