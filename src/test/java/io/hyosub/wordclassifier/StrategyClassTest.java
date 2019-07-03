package io.hyosub.wordclassifier;

import io.hyosub.wordclassifier.task.producer.DefaultPartitionIdGenerator;
import io.hyosub.wordclassifier.task.producer.DefaultWordValidator;
import io.hyosub.wordclassifier.task.producer.PartitionIdGenerator;
import io.hyosub.wordclassifier.task.producer.WordValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StrategyClassTest {
    private PartitionIdGenerator partitionIdGenerator;
    private WordValidator wordValidator;

    @Before
    public void init() {
        partitionIdGenerator = new DefaultPartitionIdGenerator();
        wordValidator = new DefaultWordValidator();
    }

    @Test
    public void testValidatingWord() {
        String validateWord = "abc!d2";
        String validateWord2 = "Bbc!d2";
        String invalidateWord = "!abc0d2";
        String invalidateWord2 = "\n";
        String invalidateWord3 = "1abc.d";
        String invalidateWord4 = " abc.d";

        Assert.assertEquals(isValidateWord(validateWord), true);
        Assert.assertEquals(isValidateWord(validateWord2), true);
        Assert.assertEquals(isValidateWord(invalidateWord), false);
        Assert.assertEquals(isValidateWord(invalidateWord2), false);
        Assert.assertEquals(isValidateWord(invalidateWord3), false);
        Assert.assertEquals(isValidateWord(invalidateWord4), false);
    }

    @Test
    public void testGeneratingPartitionId() {
        int totalPartitionCount = 7;
        String validateWord = "abc!2c";
        String validateWord2 = "Abc!2c";

        Assert.assertEquals(generatePartitionId(validateWord, totalPartitionCount), generatePartitionId(validateWord, totalPartitionCount));
        Assert.assertNotEquals(generatePartitionId(validateWord, totalPartitionCount), generatePartitionId(validateWord2, totalPartitionCount));
    }

    private boolean isValidateWord(String word) {
        return wordValidator.validate(word);
    }

    private int generatePartitionId(String word, int totalPartitionCount) {
        return partitionIdGenerator.generatePartitionId(word, totalPartitionCount);
    }
}
