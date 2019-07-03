package io.hyosub.wordclassifier.task.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultWordValidator implements WordValidator {
    private static final Pattern pattern = Pattern.compile("(^[a-zA-Z])");

    private static Logger logger = LoggerFactory.getLogger(DefaultWordValidator.class);

    @Override
    public boolean validate(String word) {
        Matcher matcher = pattern.matcher(word);
        if (matcher.find()) {
            logger.debug("Validate. (word={})", word);
            return true;
        }
        logger.debug("Invalidate. (word={})", word);
        return false;
    }
}
