package io.hyosub.wordclassifier.task.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.zip.CRC32;

public class DefaultPartitionIdGenerator implements PartitionIdGenerator {
    private static final Logger logger = LoggerFactory.getLogger(DefaultPartitionIdGenerator.class);

    @Override
    public int generatePartitionId(String word, int totalPartitionCount) {
        CRC32 crc32 = new CRC32();
        crc32.update(word.getBytes());
        int id = (int) (crc32.getValue() % totalPartitionCount);
        logger.debug("Generate id. (id={})", id);
        return id;
    }
}
