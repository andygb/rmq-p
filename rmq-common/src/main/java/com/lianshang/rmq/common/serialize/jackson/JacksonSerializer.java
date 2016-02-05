package com.lianshang.rmq.common.serialize.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lianshang.rmq.common.dto.Message;
import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.common.serialize.AbstractSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by yuan.zhong on 2016-02-02.
 *
 * @author yuan.zhong
 */
public class JacksonSerializer implements AbstractSerializer {

    ObjectMapper mapper = new ObjectMapper();

    public JacksonSerializer() {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        // mapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
        // mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        // mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        // mapper.disable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        // mapper.disable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

    }

    @Override
    public void serialize(OutputStream os, Object obj) throws SerializationException {
        try {
            mapper.writeValue(os, obj);
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) throws SerializationException {
        try {
//            return mapper.readValue(is, clazz);
            return JacksonObjectMapper.convertObject(mapper.readValue(is, clazz));
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }
}
