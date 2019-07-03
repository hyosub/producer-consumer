package io.hyosub.wordclassifier.task.producer;

public interface PartitionIdGenerator {
    int generatePartitionId(String word, int totalPartitionCount);
}
