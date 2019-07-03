package io.hyosub.wordclassifier;

import io.hyosub.wordclassifier.config.Configs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    /*  @non-javadoc
        ValidArgs Example: src/main/resources/words.txt src/main/resources/out 10
    */
    public static void main(String[] args) {
        try {
            logger.info("Start application.");

            if (isValidArgsLength(args)) {
                throw new IllegalArgumentException("A minimum of three arguments is required. (SourceTextFilePath, ResultSavedRootPath, TotalPartitionCount)");
            } else {
                Assembler assembler = new Assembler(new Configs(args));
                assembler.init();

                WordClassifier wordClassifier = assembler.getWordClassifier();
                Future classfierFutrue = wordClassifier.start();

                classfierFutrue.get();

                logger.info("Producing tasks done.");

                wordClassifier.stop();
            }
        } catch (Exception e) {
            logger.error("Error occured. Exit application.", e);
        }
    }

    private static boolean isValidArgsLength(String[] args) {
        if (args.length < 3) {
            return true;
        }
        return false;
    }
}
