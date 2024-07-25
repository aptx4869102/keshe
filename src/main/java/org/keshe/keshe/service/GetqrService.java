package org.keshe.keshe.service;

import org.keshe.keshe.pojo.Result;

import java.io.IOException;

public interface GetqrService {
    Result getqr() throws IOException, InterruptedException;
    Result listen() throws InterruptedException;
}
