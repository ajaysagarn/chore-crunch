package com.ajsa.dyrepo.repository.content.dao;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class ContentDaoImpl implements ContentDao{

    private AmazonS3 s3Client;

    @Value("${amazon.s3.bucket}")
    private String bucket;

    @Autowired
    public ContentDaoImpl(AmazonS3 s3Client){
        this.s3Client = s3Client;
    }

    @Override
    public PutObjectResult addContent(String contentPath, MultipartFile file) throws IOException {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            PutObjectRequest request = new PutObjectRequest(bucket, contentPath, file.getInputStream(),metadata);
            return s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            throw e;
        } catch (SdkClientException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public Resource readContent(String contentPath, String filename) throws IOException {
        // Get an entire object, overriding the specified response headers, and print the object's content.
        ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides()
                .withCacheControl("No-cache")
                .withContentDisposition("attachment; filename=".concat(filename));
        GetObjectRequest getObjectRequestHeaderOverride = new GetObjectRequest(bucket, contentPath)
                .withResponseHeaders(headerOverrides);
        S3Object headerOverrideObject = s3Client.getObject(getObjectRequestHeaderOverride);
        return getS3ResourceBytes(headerOverrideObject.getObjectContent());
    }

    @Override
    public void deleteContent(String contentPath) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucket, contentPath));
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFolder(String folderPath){
        List<S3ObjectSummary> summaries = s3Client.listObjectsV2(bucket, folderPath).getObjectSummaries();
        for (S3ObjectSummary summary: summaries) {
            deleteContent(summary.getKey());
        }
    }


    private static Resource getS3ResourceBytes(S3ObjectInputStream input) throws IOException {
        // read the input stream
        Resource resource = new ByteArrayResource(input.readAllBytes());
        return resource;
    }
}
