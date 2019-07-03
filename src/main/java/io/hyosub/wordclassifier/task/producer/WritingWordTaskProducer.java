package io.hyosub.wordclassifier.task.producer;

import io.hyosub.wordclassifier.task.WritingWordTask;
import io.hyosub.wordclassifier.task.broker.WritingWordTaskBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class WritingWordTaskProducer implements Runnable {
    private final String textSourcePath;
    private final WritingWordTaskBroker taskBroker;
    private final TaskGenerator taskGenerator;
    private final WordValidator wordValidator;
    private final PartitionIdGenerator partitionIdGenerator;

    private static final Logger logger = LoggerFactory.getLogger(WritingWordTaskProducer.class);

    public WritingWordTaskProducer(String textSourcePath, WritingWordTaskBroker taskBroker,
                                   TaskGenerator taskGenerator, WordValidator wordValidator,
                                   PartitionIdGenerator partitionIdGenerator) {
        this.textSourcePath = textSourcePath;
        this.taskBroker = taskBroker;
        this.taskGenerator = taskGenerator;
        this.wordValidator = wordValidator;
        this.partitionIdGenerator = partitionIdGenerator;
    }

    public void run() {
        try (Stream<String> lineStream = Files.lines(Paths.get(textSourcePath))) {
            lineStream.forEach(this::classifyWordAndAllocateTask);
            taskBroker.setPublishingFinished(true);
        } catch (IOException e) {
            logger.error("Read text-file error.", e);
        }
    }

    private void classifyWordAndAllocateTask(String word) {
        if (validate(word)) {
            int partitionId = selectPartitionId(word, taskBroker.getTotalPartitionCount());
            WritingWordTask task = taskGenerator.generate(word);
            try {
                allocateTaskToPartition(partitionId, task);
            } catch (InterruptedException e) {
                logger.error("Allocate task to partition error.", e);
            }
        }
    }

    private boolean validate(String word) {
        return wordValidator.validate(word);
    }

    private int selectPartitionId(String word, int totalPartitionCount) {
        return partitionIdGenerator.generatePartitionId(word, totalPartitionCount);
    }

    private void allocateTaskToPartition(int partitionId, WritingWordTask task) throws InterruptedException {
        taskBroker.put(partitionId, task);
    }
}
