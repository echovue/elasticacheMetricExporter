package com.echovue;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.echovue.model.ScheduledEvent;

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

    private static final String AVERAGE = "Average";
    private static final String MAXIMUM = "Maximum";
    private static final String MINIMUM = "Minimum";
    private static final String SAMPLE_COUNT = "SampleCount";
    private static final String SUM = "Sum";

    public String handleRequest(final ScheduledEvent event, final Context context) {
        LambdaLogger logger = context.getLogger();

        try {
            GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            calendar.add(GregorianCalendar.SECOND, -1 * calendar.get(GregorianCalendar.SECOND)); // 1 second ago
            Date endTime = calendar.getTime();
            calendar.add(GregorianCalendar.MINUTE, -1); // 5 minutes ago
            Date startTime = calendar.getTime();

            AmazonCloudWatch cwClient = AmazonCloudWatchClientBuilder.defaultClient();

            GetMetricStatisticsResult cpuResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, CPU_UTILIZATION, Arrays.asList(AVERAGE, MAXIMUM, MINIMUM)));

            for (Datapoint p : cpuResult.getDatapoints()) {
                logger.log(p.getTimestamp() + " CPU Metric:" + p.getAverage() + " " + p.getUnit() + "\n");
            }

            GetMetricStatisticsResult hitsResult = cwClient.getMetricStatistics(getGetMetricStatisticsRequest(
                    endTime, startTime, EC_NAMESPACE, CACHE_HITS, Arrays.asList(AVERAGE, MAXIMUM, MINIMUM, SAMPLE_COUNT)));

            for (Datapoint p : hitsResult.getDatapoints()) {
                logger.log(p.getTimestamp() + " HIT Metric:" + p.getAverage() + " " + p.getUnit() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Complete";
    }

    private GetMetricStatisticsRequest getGetMetricStatisticsRequest(final Date endTime,
                                                                     final Date startTime,
                                                                     final String namespace,
                                                                     final String cpuUtilization,
                                                                     final List<String> statistics) {
        return new GetMetricStatisticsRequest()
                        .withStartTime(startTime)
                        .withEndTime(endTime)
                        .withPeriod(300)
                        .withNamespace(namespace)
                        .withMetricName(cpuUtilization)
                        .withStatistics(statistics);
                        //.withDimensions(Arrays.asList(new Dimension().withName("cacheClusterId").withValue(cacheClusterId)));
    }

/*    private List<Dimension> getDimensions() {
        List<Dimension> dimensions = Lists.newArrayList();
        dimensions.add(new Dimension().withName("CacheClusterId").withValue(CACHE_CLUSTER_ID));
//        dimensions.add(new Dimension().withName("CacheNodeId").withValue(CACHE_NODE_ID));
        return dimensions;
    }

    private Collection<String> getStatistics() {
//        CPUUtilization|Average CPUUtilization|Minimum CPUUtilization|Maximum NetworkIn|Sum NetworkOut|Sum
        List<String> statistics = Lists.newArrayList();
        statistics.add("Average");
        return statistics;
    }
/*        Pattern pattern = Pattern.compile(ZIP_CODE_REGEX);
        ObjectMapper objectMapper = new ObjectMapper();
        for (DynamodbStreamRecord record : ddbEvent.getRecords()){
            try {
                if (record.getEventName().equals(INSERT)) {
                    if (null == dynamoDB) {
                        dynamoDB = new DynamoDB(new AmazonDynamoDBClient().withRegion(
                                Regions.fromName(record.getAwsRegion())));
                        table = dynamoDB.getTable(TABLE_NAME);
                    }
                    Address address = objectMapper.readValue(
                            record.getDynamodb().getNewImage().get(ADDRESS).getS().toString(), Address.class);
                    if (!Boolean.TRUE.equals(address.getValidated())) {
                        address.setValidated(pattern.matcher(address.getZipcode()).matches());
                        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                            .withPrimaryKey(ID, record.getDynamodb().getKeys().get(ID).getS())
                            .withUpdateExpression("set address = :a")
                            .withValueMap(new ValueMap()
                                .withString(":a", objectMapper.writeValueAsString(address)))
                            .withReturnValues(ReturnValue.UPDATED_NEW);
                        table.updateItem(updateItemSpec);
                    }
                }
            } catch (IOException e) {
                logger.log("Exception thrown when validating Zip Code. " + e.getMessage());
            }
        }*/
//        return "Validated " + ddbEvent.getRecords().size() + " records.";
//    }
/*
    //        Properties properties = setProperties(event);
    private Properties setProperties(ScheduledEvent event) {
        LocalDate endDate = LocalDate.parse(event.getTime(), DateTimeFormatter.ISO_INSTANT);
        LocalDate startDate = endDate.minus(1, MINUTES);
        Properties properties = new Properties();
        properties.setProperty("startTime", startDate.format(DateTimeFormatter.ISO_INSTANT));
        properties.setProperty("endTime", endDate.format(DateTimeFormatter.ISO_INSTANT));
        properties.setProperty("EC_NAMESPACE", "AWS/ElastiCache");
        return properties;
    }*/
}
