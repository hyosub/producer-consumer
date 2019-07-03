package io.hyosub.wordclassifier.config;

public class Configs {
    private final String sourceTextPath;
    private final String targetTextRootPath;
    private final int totalPartitionCount;

    public Configs(String[] args) {
        sourceTextPath = args[0];
        targetTextRootPath = args[1];
        totalPartitionCount = Integer.parseInt(args[2]);

        if (!isAvaliablePartitionCountRange()) {
            throw new IllegalArgumentException("Wrong range. (1 < totalPartitionCount < 27)");
        }
    }

    public String getSourceTextPath() {
        return sourceTextPath;
    }

    public String getTargetTextRootPath() {
        return targetTextRootPath;
    }

    public int getTotalPartitionCount() {
        return totalPartitionCount;
    }

    private boolean isAvaliablePartitionCountRange() {
        if (totalPartitionCount > 1 && totalPartitionCount < 27) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"sourceTextPath\": \"").append(sourceTextPath).append("\", ")
                .append("\"targetTextRootPath\": \"").append(targetTextRootPath).append("\", ")
                .append("\"totalPartitionCount\": \"").append(totalPartitionCount).append("\"}");
        return builder.toString();
    }
}
