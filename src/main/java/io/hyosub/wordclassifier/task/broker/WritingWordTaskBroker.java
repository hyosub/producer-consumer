package io.hyosub.wordclassifier.task.broker;

import io.hyosub.wordclassifier.task.WritingWordTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WritingWordTaskBroker {
    private final Map<Integer, BlockingQueue<WritingWordTask>> taskPartitions;
    private volatile boolean publishingFinished = false;

    public WritingWordTaskBroker(int totalPartitionCount) {
        taskPartitions = new HashMap<>(totalPartitionCount);
        createPartitions(totalPartitionCount);
    }

    public BlockingQueue<WritingWordTask> getPartition(int partitionId) {
        return taskPartitions.get(partitionId);
    }

    public WritingWordTask get(int partitionId) {
        //return taskPartitions.get(partitionId).poll(1, TimeUnit.SECONDS);
        return taskPartitions.get(partitionId).poll();
    }

    public void put(int partitionId, WritingWordTask task) throws InterruptedException {
        taskPartitions.get(partitionId).put(task);
    }

    public boolean isEmpty(int partitionId) {
        return taskPartitions.get(partitionId).isEmpty();
    }

    public int getTotalPartitionCount() {
        return taskPartitions.size();
    }

    public boolean isPublishingFinished() {
        return publishingFinished;
    }

    public void setPublishingFinished(boolean publishingFinished) {
        this.publishingFinished = publishingFinished;
    }

    private void createPartitions(int totalPartitionCount) {
        for (int partitionId = 0; partitionId < totalPartitionCount; partitionId++) {
            taskPartitions.put(partitionId, new LinkedBlockingQueue<>());
        }
    }
}
