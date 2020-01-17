package com.bitstrips.example.contentproviderdemo.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class ExecutorUtils {

    public static final Executor IMAGE_DECODING_EXECUTOR = Executors.newFixedThreadPool(4);
}
