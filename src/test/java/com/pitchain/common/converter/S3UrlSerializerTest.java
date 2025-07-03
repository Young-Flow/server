package com.pitchain.common.converter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

@SpringBootTest
class S3UrlSerializerTest {

    @Value("${spring.cloud.aws.cdn}")
    private String cdnDomain;

    @Autowired
    private S3UrlSerializer s3UrlSerializer;

    @Test
    void URL_반환_성공() throws IOException {
        //given
        String value = "img_key";
        Writer jsonWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();

        //when
        s3UrlSerializer.serialize(value, jsonGenerator, serializerProvider);

        //then
        jsonGenerator.flush();
        String expectedValue = cdnDomain + "/" + value;
        Assertions.assertThat(jsonWriter.toString()).isEqualTo("\"" + expectedValue + "\"");
    }

}