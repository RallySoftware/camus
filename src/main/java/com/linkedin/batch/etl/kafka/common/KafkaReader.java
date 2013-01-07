package com.linkedin.batch.etl.kafka.common;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kafka.api.FetchRequest;
import kafka.common.ErrorMapping;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.Message;
import kafka.message.MessageAndOffset;

import org.apache.hadoop.io.BytesWritable;

import com.linkedin.batch.etl.kafka.EtlJob;

/**
 * Poorly named class that handles kafka pull events within each
 * KafkaRecordReader.
 * 
 * @author Richard Park
 */
@SuppressWarnings({ "deprecation" })
public class KafkaReader {
	// index of context
	private EtlRequest kafkaRequest = null;
	private SimpleConsumer simpleConsumer = null;

	private long beginOffset;
	private long currentOffset;
	private long lastOffset;
	private long currentCount;

	private Iterator<MessageAndOffset> messageIter = null;

	private long totalFetchTime = 0;
	private long lastFetchTime = 0;

	private int fetchBufferSize;

	/**
	 * Construct using the json represention of the kafka request
	 */
	public KafkaReader(EtlRequest request, int clientTimeout, int fetchBufferSize) throws Exception {
		this.fetchBufferSize = fetchBufferSize;

		System.out.println("bufferSize=" + fetchBufferSize);
		System.out.println("timeout=" + clientTimeout);

		// Create the kafka request from the json
		kafkaRequest = request;

		beginOffset = request.getOffset();
		currentOffset = request.getOffset();
		lastOffset = request.getLastOffset();
		currentCount = 0;
		totalFetchTime = 0;

		// read data from queue
		URI uri = kafkaRequest.getURI();
		simpleConsumer = new SimpleConsumer(uri.getHost(), uri.getPort(), clientTimeout, fetchBufferSize, EtlJob.getKafkaClientName());
		
		fetch();

		System.out.println("Connected to node " + uri + " beginning reading at offset " + beginOffset + " latest offset=" + lastOffset);
	}

	public boolean hasNext() throws IOException {
		return (messageIter != null && messageIter.hasNext()) || fetch();
	}

	private int i;

	/**
	 * Fetches the next Kafka message and stuffs the results into the key and
	 * value
	 * 
	 * @param key
	 * @param value
	 * @return true if there exists more events
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public boolean getNext(EtlKey key, BytesWritable value) throws IOException {
		if (hasNext()) {
			MessageAndOffset msgAndOffset = messageIter.next();

			Message message = msgAndOffset.message();

			ByteBuffer buf = message.payload();
			int origSize = buf.remaining();
			byte[] bytes = new byte[origSize];
			buf.get(bytes, buf.position(), origSize);
			value.set(bytes, 0, origSize);
			
			key.clear();
			key.set(kafkaRequest.getTopic(), kafkaRequest.getNodeId(), kafkaRequest.getPartition(), currentOffset, msgAndOffset.offset(), message.checksum());

			currentOffset = msgAndOffset.offset(); // increase offset
			currentCount++; // increase count

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Creates a fetch request.
	 * 
	 * @return false if there's no more fetches
	 * @throws IOException
	 */
	public boolean fetch() throws IOException
	  {
	    if (currentOffset >= lastOffset)
	    {
	      return false;
	    }

	    long tempTime = System.currentTimeMillis();
	    FetchResponse fetchResponse = null;
	    int retries = 2;
	    while (true && retries > 0)
	    {
	      KafkaClient kafkaClient = new KafkaClient();
	      fetchResponse =
	          kafkaClient.getFetchRequests(simpleConsumer,
	                                       kafkaRequest.getTopic(),
	                                       kafkaRequest.getPartition(),
	                                       Integer.parseInt(kafkaRequest.getNodeId()),
	                                       currentOffset,
	                                       fetchBufferSize);
	      if (fetchResponse.hasError())
	      {
	        ArrayList<String> topic = new ArrayList<String>();
	        topic.add(kafkaRequest.getTopic());
	        HashMap<String, List<EtlRequest>> response = KafkaClient.loadKafkaMetadata(topic);
	        List<EtlRequest> requests = response.get(kafkaRequest.getTopic());
	        for (EtlRequest request : requests)
	        {
	          if (request.getPartition() == kafkaRequest.getPartition())
	          {
	            kafkaRequest.setNodeId(request.getNodeId());
	            kafkaRequest.setURI(request.getURI());
	          }
	        }
	        retries--;
	      }
	      else
	      {
	        break;
	      }
	    }
	    if (fetchResponse.hasError())
	    {
	      // What needs to be done here?
	      System.err.println("Issue in getting getch requests for topic "
	          + kafkaRequest.getTopic() + " and partition : " + kafkaRequest.getPartition());
	    }

	    ByteBufferMessageSet messageBuffer = null;

	    messageBuffer =
	        fetchResponse.messageSet(kafkaRequest.getTopic(), kafkaRequest.getPartition());
	    lastFetchTime = (System.currentTimeMillis() - tempTime);
	    totalFetchTime += lastFetchTime;

	    if (!hasError(fetchResponse.errorCode(kafkaRequest.getTopic(),
	                                          kafkaRequest.getPartition())))
	    {
	      messageIter = messageBuffer.iterator();
	      return true;
	    }
	    else
	    {
	      return false;
	    }

	  }

	/**
	 * Closes this context
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (simpleConsumer != null) {
			simpleConsumer.close();
		}
	}

	/**
	 * Called by the default implementation of {@link #map} to check error code
	 * to determine whether to continue.
	 */
	private boolean hasError(Short errorCode) throws IOException
	  {

	    if (errorCode == ErrorMapping.OffsetOutOfRangeCode())
	    {
	      // offset cannot cross the maximum offset (guaranteed by Kafka
	      // protocol).
	      // Kafka server may delete old files from time to time
	      if (currentOffset != kafkaRequest.getEarliestOffset())
	      {
	        // get the current offset range
	        currentOffset = kafkaRequest.getEarliestOffset();
	        return false;
	      }
	      throw new IOException(kafkaRequest + " earliest offset=" + currentOffset
	          + " : invalid offset.");
	    }
	    else if (errorCode == ErrorMapping.InvalidMessageCode())
	    {
	      throw new IOException(kafkaRequest + " current offset=" + currentOffset
	          + " : invalid offset.");
	    }
	    else if (errorCode != ErrorMapping.NoError())
	    {
	      throw new IOException(kafkaRequest + " current offset=" + currentOffset + " error:"
	          + errorCode);
	    }
	    else
	    {
	      return false;
	    }
	  }

	/**
	 * Returns the total bytes that will be fetched. This is calculated by
	 * taking the diffs of the offsets
	 * 
	 * @return
	 */
	public long getTotalBytes() {
		return (lastOffset > beginOffset) ? lastOffset - beginOffset : 0;
	}

	/**
	 * Returns the total bytes that have been fetched so far
	 * 
	 * @return
	 */
	public long getReadBytes() {
		return currentOffset - beginOffset;
	}

	/**
	 * Returns the number of events that have been read
	 * 
	 * @return
	 */
	public long getCount() {
		return currentCount;
	}

	/**
	 * Returns the fetch time of the last fetch in ms
	 * 
	 * @return
	 */
	public long getFetchTime() {
		return lastFetchTime;
	}
	
	/**
	 * Returns the totalFetchTime in ms
	 * 
	 * @return
	 */
	public long getTotalFetchTime() {
		return totalFetchTime;
	}
}
