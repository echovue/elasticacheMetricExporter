package com.example;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.OperationType;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.dynamodbv2.model.StreamViewType;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class AddressValidatorTest {
/*    @Test
    public void testValidator() throws Exception {
        Context context = mock(Context.class);
        LambdaLogger logger = mock(LambdaLogger.class);
        when(context.getLogger()).thenReturn(logger);
        AddressValidator addressValidator = new AddressValidator();
        DynamodbEvent ddbEvent = new DynamodbEvent();
        ddbEvent.setRecords(getInsertRecord());
        String result = addressValidator.handleRequest(ddbEvent, context);
        assertEquals("Validated 1 records.", result);
    }

    private List<DynamodbStreamRecord> getInsertRecord() {
        List<DynamodbStreamRecord> records = Lists.newArrayList();
        DynamodbStreamRecord ddbStreamRecord = new DynamodbStreamRecord();
        ddbStreamRecord.setEventID("1");
        ddbStreamRecord.setEventName(OperationType.INSERT);
        ddbStreamRecord.setEventVersion("1");
        ddbStreamRecord.setEventSource("aws.dynamodb");
        ddbStreamRecord.setAwsRegion("us-west-2");
        StreamRecord streamRecord = new StreamRecord();

        HashMap<String, AttributeValue> idMap = Maps.newHashMap();
        idMap.put("id", new AttributeValue("101"));
        HashMap<String, AttributeValue> newImage = Maps.newHashMap();
        newImage.put("id", new AttributeValue("101"));
        newImage.put("address", new AttributeValue("{\"address1\":\"123 Main St\", \"city\": \"Portland\", \"state\": \"OR\", \"zipcode\": \"97229\"}"));

        streamRecord.setKeys(idMap);
        streamRecord.setNewImage(newImage);
        streamRecord.setSequenceNumber("111");
        streamRecord.setSizeBytes(124L);
        streamRecord.setStreamViewType(StreamViewType.NEW_AND_OLD_IMAGES);

        ddbStreamRecord.setDynamodb(streamRecord);
        ddbStreamRecord.setEventSourceARN("stream-ARN");
        records.add(ddbStreamRecord);

        return records;
    }


*/
    private String getDynamoDBEvent() {
        return "{"
                + "Records\":["
                + "{"
                + " \"eventID\":\"1\","
                + " \"eventName\":\"INSERT\","
                + " \"eventVersion\":\"1.0\","
                + " \"eventSource\":\"aws:dynamodb\","
                + " \"awsRegion\":\"us-east-1\","
                + " \"dynamodb\":{"
                + "     \"Keys\":{"
                + "         \"Id\":{"
                + "             \"N\":\"101\""
                + "         }"
                + "     },"
                + "     \"NewImage\":{"
                + "         \"Message\":{"
                + "             \"S\":\"New item!\""
                + "         },"
                + "         \"Id\":{"
                + "             \"N\":\"101\""
                + "         }"
                + "    },"
                + "    \"SequenceNumber\":\"111\","
                + "      \"SizeBytes\":26,"
                + "      \"StreamViewType\":\"NEW_AND_OLD_IMAGES\""
                + "},"
                + "    \"eventSourceARN\":\"stream-ARN\""
                + "},"
                + "{"
                + "    \"eventID\":\"2\","
                + "  \"eventName\":\"MODIFY\","
                + "  \"eventVersion\":\"1.0\","
                + "  \"eventSource\":\"aws:dynamodb\","
                + "  \"awsRegion\":\"us-east-1\","
                + "  \"dynamodb\":{"
                + "    \"Keys\":{"
                + "  \"Id\":{"
                + "      \"N\":\"101\""
                + "  }"
                + "    },"
                + "    \"NewImage\":{"
                + "  \"Message\":{"
                + "      \"S\":\"This item has changed\""
                + "  },"
                + "  \"Id\":{"
                + "      \"N\":\"101\""
                + "  }"
                + "    },"
                + "    \"OldImage\":{"
                + "  \"Message\":{"
                + "      \"S\":\"New item!\""
                + "  },"
                + "  \"Id\":{"
                + "      \"N\":\"101\""
                + "  }"
                + "    },"
                + "    \"SequenceNumber\":\"222\","
                + "      \"SizeBytes\":59,"
                + "      \"StreamViewType\":\"NEW_AND_OLD_IMAGES\""
                + "},"
                + "    \"eventSourceARN\":\"stream-ARN\""
                + "},"
                + "{"
                + "    \"eventID\":\"3\","
                + "  \"eventName\":\"REMOVE\","
                + "  \"eventVersion\":\"1.0\","
                + "  \"eventSource\":\"aws:dynamodb\","
                + "  \"awsRegion\":\"us-east-1\","
                + "  \"dynamodb\":{"
                + "    \"Keys\":{"
                + "  \"Id\":{"
                + "      \"N\":\"101\""
                + "  }"
                + "    },"
                + "    \"OldImage\":{"
                + "  \"Message\":{"
                + "      \"S\":\"This item has changed\""
                + "  },"
                + "  \"Id\":{"
                + "      \"N\":\"101\""
                + "  }"
                + "    },"
                + "    \"SequenceNumber\":\"333\","
                + "      \"SizeBytes\":38,"
                + "      \"StreamViewType\":\"NEW_AND_OLD_IMAGES\""
                + "},"
                + "    \"eventSourceARN\":\"stream-ARN\""
                + "}"
                + "   ]"
                + "    }";
    }
}
                