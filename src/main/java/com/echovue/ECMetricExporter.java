package com.echovue;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.echovue.model.ESMetrics;
import com.echovue.model.ScheduledEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class ECMetricExporter implements
        RequestHandler<ScheduledEvent, String> {

    private static final String EC_NAMESPACE = "AWS/ElastiCache";

    private static final String CPU_UTILIZATION = "CPUUtilization";
    private static final String CACHE_HITS = "CacheHits";
    private static final String CACHE_MISSES = "CacheMisses";
    private static final String CURR_ITEMS = "CurrItems";
    private static final String CURR_CONNECTIONS = "CurrConnections";
    private static final String RECLAIMED = "Reclaimed";
    private static final String REPLICATION_BYTES = "ReplicationBytes";
    private static final String EVICTIONS = "Evictions";
    private static final String SWAP_USAGE = "SwapUsage";
    private static final String NETWORK_BYTES_OUT = "NetworkBytesOut";
    private static final String NETWORK_BYTES_IN = "NetworkBytesIn";

    private static final String AVERAGE = "Average";

    private ObjectMapper mapper = new ObjectMapper();

    public String handleRequest(final ScheduledEvent event, final Context context) {
        LambdaLogger logger = context.getLogger();

        try {
            GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            calendar.add(GregorianCalendar.SECOND, -1 * calendar.get(GregorianCalendar.SECOND)); // 1 second ago
            Date endTime = calendar.getTime();
            calendar.add(GregorianCalendar.MINUTE, -1);
            Date startTime = calendar.getTime();

            ESMetrics metrics = new ESMetrics();
            metrics.setStartDate(startTime);
            metrics.setEndDate(endTime);

            AmazonCloudWatch cwClient = AmazonCloudWatchClientBuilder.defaultClient();
            GetMetricStatisticsResult cpuResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, CPU_UTILIZATION, Arrays.asList(AVERAGE)));
            GetMetricStatisticsResult hitsResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, CACHE_HITS, Arrays.asList(AVERAGE)));
            GetMetricStatisticsResult missResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, CACHE_MISSES, Arrays.asList(AVERAGE)));
            GetMetricStatisticsResult currItemsResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, CURR_ITEMS, Arrays.asList(AVERAGE)));
            GetMetricStatisticsResult currConnectionsResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, CURR_CONNECTIONS, Arrays.asList(AVERAGE)));
            GetMetricStatisticsResult reclaimedResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, RECLAIMED, Arrays.asList(AVERAGE)));
            GetMetricStatisticsResult replicationByteResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, REPLICATION_BYTES, Arrays.asList(AVERAGE)));
            GetMetricStatisticsResult evictionResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, EVICTIONS, Arrays.asList(AVERAGE)));
            GetMetricStatisticsResult swapUsageResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, SWAP_USAGE, Arrays.asList(AVERAGE)));
            GetMetricStatisticsResult networkBytesOutResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, NETWORK_BYTES_OUT, Arrays.asList(AVERAGE)));
            GetMetricStatisticsResult networkBytesInResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, NETWORK_BYTES_IN, Arrays.asList(AVERAGE)));

            for (Datapoint p : cpuResult.getDatapoints()) { metrics.setCpuUtilization(p.getAverage()); }
            for (Datapoint p : hitsResult.getDatapoints()) { metrics.setCacheHits(p.getAverage()); }
            for (Datapoint p : missResult.getDatapoints()) { metrics.setCacheMisses(p.getAverage()); }
            for (Datapoint p : currItemsResult.getDatapoints()) { metrics.setCurrItems(p.getAverage()); }
            for (Datapoint p : currConnectionsResult.getDatapoints()) { metrics.setCurrConnections(p.getAverage()); }
            for (Datapoint p : reclaimedResult.getDatapoints()) { metrics.setReclaimed(p.getAverage()); }
            for (Datapoint p : replicationByteResult.getDatapoints()) { metrics.setReplicationBytes(p.getAverage()); }
            for (Datapoint p : evictionResult.getDatapoints()) { metrics.setEvictions(p.getAverage()); }
            for (Datapoint p : swapUsageResult.getDatapoints()) { metrics.setSwapUsage(p.getAverage()); }
            for (Datapoint p : networkBytesInResult.getDatapoints()) { metrics.setNetworkBytesIn(p.getAverage()); }
            for (Datapoint p : networkBytesOutResult.getDatapoints()) { metrics.setNetworkBytesOut(p.getAverage()); }
            logger.log(mapper.writeValueAsString(metrics));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Complete";
    }

    private GetMetricStatisticsRequest getGetMetricStatisticsRequest(final Date endTime,
                                                                     final Date startTime,
                                                                     final String namespace,
                                                                     final String metricName,
                                                                     final List<String> statistics) {
        return new GetMetricStatisticsRequest().withStartTime(startTime).withEndTime(endTime)
                        .withPeriod(300).withNamespace(namespace).withMetricName(metricName)
                        .withStatistics(statistics);
    }
}
