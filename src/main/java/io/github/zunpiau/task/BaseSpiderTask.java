package io.github.zunpiau.task;

import io.github.zunpiau.dao.BaseRepository;
import io.github.zunpiau.domain.Wallpaper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;

import java.io.IOException;

abstract class BaseSpiderTask<U extends Wallpaper, V extends BaseRepository<U>> {

    Logger logger;
    OkHttpClient client;
    Request request;
    V repository;

    abstract protected String request() throws IOException;

    abstract protected U serial(String s) throws IOException;

    abstract protected void save(U u);

}
