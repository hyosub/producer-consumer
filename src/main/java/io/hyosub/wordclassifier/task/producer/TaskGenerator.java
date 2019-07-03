package io.hyosub.wordclassifier.task.producer;

import io.hyosub.wordclassifier.task.WritingWordTask;

public interface TaskGenerator {
    WritingWordTask generate(String word);
}
