package com.facevisitor.api.service.aws;

import com.amazonaws.auth.AWSCredentialsProvider;

public interface AWSCredentialsService {

    AWSCredentialsProvider credentials();
}
