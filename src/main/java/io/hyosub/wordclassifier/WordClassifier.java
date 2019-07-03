package io.hyosub.wordclassifier;

import io.hyosub.wordclassifier.task.broker.WritingWordTaskBroker;
import io.hyosub.wordclassifier.task.consumer.WritingWordTaskConsumer;
import io.hyosub.wordclassifier.task.producer.WritingWordTaskProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public final class WordClassifier {
    private final int totalPartitionCount;
    private final String targetTextRootPath;

    private final WritingWordTaskBroker taskBroker;
    private final WritingWordTaskProducer taskProducer;
    private final ExecutorService workerService;

    private static final boolean TARGET_DIRECTORY_REMOVE_FIRST = true;

    private static final Logger logger = LoggerFactory.getLogger(WordClassifier.class);

    public WordClassifier(String targetTextRootPath, int totalPartitionCount,
                          WritingWordTaskBroker taskBroker, WritingWordTaskProducer taskProducer,
                          ExecutorService workerService) {
        this.totalPartitionCount = totalPartitionCount;
        this.targetTextRootPath = targetTextRootPath;
        this.taskBroker = taskBroker;
        this.taskProducer = taskProducer;
        this.workerService = workerService;
    }

    public Future start() throws IOException {
        initTextRootPath(TARGET_DIRECTORY_REMOVE_FIRST);
        createAndStartConsumers(totalPartitionCount, targetTextRootPath);
        return startProducer();
    }

    public void stop() {
        stopWorkerService();
    }

    private void initTextRootPath(boolean removedFirst) throws IOException {
        if (removedFirst) {
            Path textRootPath = Paths.get(targetTextRootPath);
            if (Files.exists(textRootPath)) {
                Files.walk(textRootPath)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                Files.createDirectories(textRootPath);
            }
        }
    }

    private Future startProducer() {
        return workerService.submit(taskProducer);
    }

    private void createAndStartConsumers(int totalPartitionCount, String targetTextRootPath) {
        for (int partitionId = 0; partitionId < totalPartitionCount; partitionId++) {
            workerService.execute(new WritingWordTaskConsumer(partitionId, taskBroker, targetTextRootPath));
        }
    }

    private void stopWorkerService() {
        workerService.shutdown();
        /*while (!workerService.isTerminated()) {
            //logger.info("Tasks are done.");
            //System.out.println(System.currentTimeMillis());
        }*/
        /*try {
            if (!workerService.awaitTermination(10, TimeUnit.SECONDS)) {
                workerService.shutdownNow();
            }
        } catch (InterruptedException e) {
            workerService.shutdownNow();
        }*/
    }
}
