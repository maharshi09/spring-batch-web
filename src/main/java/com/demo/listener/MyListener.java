package com.demo.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;


/**
 * Created by maharshi.bhatt on 16/4/17.
 */
public class MyListener implements ChunkListener {

    @Override
    public void beforeChunk(ChunkContext chunkContext) {

    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        System.out.println(chunkContext.toString());
    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {

    }
}
