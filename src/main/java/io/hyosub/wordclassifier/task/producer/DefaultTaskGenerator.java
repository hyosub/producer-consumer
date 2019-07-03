package io.hyosub.wordclassifier.task.producer;

import io.hyosub.wordclassifier.task.WritingWordTask;

public class DefaultTaskGenerator implements TaskGenerator {

    @Override
    public WritingWordTask generate(String word) {
        return new WritingWordTask(word, false);
    }
}
