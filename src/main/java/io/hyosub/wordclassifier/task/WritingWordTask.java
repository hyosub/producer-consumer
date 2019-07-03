package io.hyosub.wordclassifier.task;

import java.time.LocalDateTime;

public class WritingWordTask {
    private final String word;
    private final String primeCharacter;
    private final LocalDateTime createTime;
    private final boolean doRetry;

    public WritingWordTask(String word, boolean doRetry) {
        this.word = word;
        primeCharacter = word.substring(0, 1).toLowerCase();
        createTime = LocalDateTime.now();
        this.doRetry = doRetry;
    }

    public String getWordWithNewLine() {
        return word + "\n";
    }

    public String getWord() {
        return word;
    }

    public String getPrimeCharacter() {
        return primeCharacter;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public boolean isDoRetry() {
        return doRetry;
    }
}
