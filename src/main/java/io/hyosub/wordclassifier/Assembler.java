package io.hyosub.wordclassifier;

import io.hyosub.wordclassifier.task.broker.WritingWordTaskBroker;
import io.hyosub.wordclassifier.config.Configs;
import io.hyosub.wordclassifier.task.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Assembler {
    private WordValidator wordValidator;
    private TaskGenerator taskGenerator;
    private PartitionIdGenerator partitionIdGenerator;
    private WritingWordTaskBroker taskBroker;
    private WritingWordTaskProducer taskProducer;
    private WordClassifier wordClassifier;
    private ExecutorService workerService;

    private final Configs config;

    private static final int PUBLISHER_THREAD_COUNT = 1;

    private static final Logger logger = LoggerFactory.getLogger(Assembler.class);

    public Assembler(Configs config) {
        logger.info("Check config. [config={}]", config);
        this.config = config;
    }

    public void init() {
        String sourceTextPath = config.getSourceTextPath();
        String targetTextRootPath = config.getTargetTextRootPath();
        int totalPartitionCount = config.getTotalPartitionCount();

        workerService = Executors.newFixedThreadPool(totalPartitionCount + PUBLISHER_THREAD_COUNT);

        wordValidator = new DefaultWordValidator();
        taskGenerator = new DefaultTaskGenerator();
        partitionIdGenerator = new DefaultPartitionIdGenerator();
        taskBroker = new WritingWordTaskBroker(totalPartitionCount);
        taskProducer = new WritingWordTaskProducer(sourceTextPath, taskBroker,
                taskGenerator, wordValidator, partitionIdGenerator);
        wordClassifier = new WordClassifier(targetTextRootPath, totalPartitionCount,
                taskBroker, taskProducer, workerService);
    }

    public WordClassifier getWordClassifier() {
        return wordClassifier;
    }
}
