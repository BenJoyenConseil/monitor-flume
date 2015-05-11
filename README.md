# Monitoring flume #

## metrics ##

    shieldStartTime : source startTime
    in : number of input events in the given interceptor
    out : number of output events in the given interceptor
    rejected : number of rejected events by the interceptor
    countBatchSizeProcessed : number of processed batchSize

## how to configure agent ##
Use the "type" property to specify monitoring class
There is a property called wrapped using to specify the wrapped class to inspect

### describe/configure the source ###

    a1.sources.r1.type = com.octo.flume.monitoring.SourceMonitor
    a1.sources.r1.wrappedClass = org.apache.flume.source.SequenceGeneratorSource
    a1.sources.r1.type = seq
    a1.sources.r1.batchSize = 100

### describe interceptor ###

    a1.sources.r1.interceptors = i1
    a1.sources.r1.interceptors.i1.type = com.octo.flume.monitoring.InterceptorMonitor$Builder
    a1.sources.r1.interceptors.i1.wrappedClass = org.apache.flume.interceptor.RegexFilteringInterceptor$Builder
    a1.sources.r1.interceptors.i1.excludeEvents = false
    a1.sources.r1.interceptors.i1.regex = 0{3}


### describe the sink ###

    a1.sinks.k1.type = file_roll
    a1.sinks.k1.sink.directory = reception
    a1.sinks.k1.wrappedClass = org.apache.flume.sink.RollingFileSink
    a1.sinks.k1.sink.serializer = com.octo.flume.monitoring.EventSerializerMonitor$Builder
    a1.sinks.k1.sink.serializer.wrappedClass = org.apache.flume.serialization.BodyTextEventSerializer$Builder


## start agent with http monitoring ##
    flume-ng agent --conf conf --conf-file flume.conf --name a1 -Dflume.root.logger=DEBUG,console -Dflume.monitoring.type=http -Dflume.monitoring.port=46100