package com.ajsa.dyrepo.repository.content.dao;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class ContentDaoImpl implements ContentDao{


    private S3Client s3Client;

    @Autowired
    private S3Presigner presigner;

    @Value("${amazon.s3.bucket}")
    private String bucket;

    @Autowired
    public ContentDaoImpl(S3Client s3Client){
        this.s3Client = s3Client;
    }

    @Override
    public PutObjectResponse addContent(String contentPath, MultipartFile file) throws IOException {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(contentPath).build();
            return s3Client.putObject(request, RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));
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

        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(contentPath)
                .bucket(bucket)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
        return getS3ResourceBytes(objectBytes.asInputStream());
    }

    @Override
    public void deleteContent(String contentPath) {
        try {

            ArrayList<ObjectIdentifier> toDelete = new ArrayList<ObjectIdentifier>();
            toDelete.add(ObjectIdentifier.builder().key(contentPath).build());

            DeleteObjectsRequest dor = DeleteObjectsRequest.builder()
                    .bucket(bucket)
                    .delete(Delete.builder().objects(toDelete).build())
                    .build();
            s3Client.deleteObjects(dor);
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
        ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(bucket)
                .build();

        ListObjectsResponse res = s3Client.listObjects(listObjects);
        List<S3Object> objects = res.contents();

        for (S3Object object: objects) {
            deleteContent(object.key());
        }
    }

    @Override
    public String getPresignedUrl(String contentPath) {
        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(contentPath)
                        .build();

        GetObjectPresignRequest getObjectPresignRequest =  GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        // Generate the presigned request
        PresignedGetObjectRequest presignedGetObjectRequest =
                presigner.presignGetObject(getObjectPresignRequest);

        return presignedGetObjectRequest.url().toString();
    }

    @Override
    public String getFolderPresignedUrls(String folderPath) {
        return null;
    }


    private static Resource getS3ResourceBytes(InputStream input) throws IOException {
        // read the input stream
        Resource resource = new ByteArrayResource(input.readAllBytes());
        return resource;
    }
}
