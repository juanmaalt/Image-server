package com.juanmaalt.imageServer.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AWSS3Config() {

    private val logger = KotlinLogging.logger {}

    @Value("\${cloud.aws.credentials.accessKey}")
    private val accessKey: String = ""

    @Value("\${cloud.aws.credentials.secretKey}")
    private val secretKey: String = ""

    @Value("\${cloud.aws.region.static}")
    private val region: String = ""

    @Bean
    fun getAmazonS3Client(): AmazonS3 {
        logger.info { """Setting up AWS credentials""" }
        val credentials = BasicAWSCredentials(accessKey, secretKey)

        logger.info { """Building AWS S3 client""" }
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withRegion(region)
            .build()
    }
}
