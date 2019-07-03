package io.hyosub.wordclassifier.task.consumer;

import io.hyosub.wordclassifier.task.WritingWordTask;
import io.hyosub.wordclassifier.task.broker.WritingWordTaskBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class WritingWordTaskConsumer implements Runnable {
    private final int allocatedPartitionId;
    private final WritingWordTaskBroker taskBroker;
    private final String targetRootPath;

    private static final String TARGET_FILE_EXTENSION = ".txt";

    private static final Logger logger = LoggerFactory.getLogger(WritingWordTaskConsumer.class);

    public WritingWordTaskConsumer(int allocatedPartitionId, WritingWordTaskBroker taskBroker, String targetRootPath) {
        this.allocatedPartitionId = allocatedPartitionId;
        this.taskBroker = taskBroker;
        this.targetRootPath = targetRootPath;
    }

    @Override
    public void run() {
        while (!taskBroker.isPublishingFinished()) {
            while (!taskBroker.isEmpty(allocatedPartitionId)) {
                WritingWordTask task = taskBroker.get(allocatedPartitionId);
                process(task);
            }
        }
        logger.info("Done tasks.");
    }

    private void process(WritingWordTask task) {
        try {
            String targetPath = targetRootPath + File.separator + task.getPrimeCharacter() + TARGET_FILE_EXTENSION;
            checkAndCreateTextRootPath(targetRootPath);
            Files.write(Paths.get(targetPath), task.getWordWithNewLine().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Handle file error.", e);
        }
    }

    private void checkAndCreateTextRootPath(String targetRootPath) throws IOException {
        Path rootPath = Paths.get(targetRootPath);
        if (!Files.exists(rootPath)) {
            Files.createDirectories(rootPath);
        }
    }
}
