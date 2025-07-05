package com.pitchain.common.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pitchain.upload.application.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class S3UrlSerializer extends JsonSerializer<String> {

    private final S3Service s3Service;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String fileURL = s3Service.getFileURL(value);
        gen.writeString(fileURL);
    }
}
