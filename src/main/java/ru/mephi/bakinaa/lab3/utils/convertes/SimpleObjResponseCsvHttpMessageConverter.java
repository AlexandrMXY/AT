package ru.mephi.bakinaa.lab3.utils.convertes;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import ru.mephi.bakinaa.lab3.commons.objects.SimpleObj;
import ru.mephi.bakinaa.lab3.db.relations.Relation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

public class SimpleObjResponseCsvHttpMessageConverter implements HttpMessageConverter<SimpleObj> {
    public static final MediaType CSV = RelationResponseCsvHttpMessageConverter.CSV;
    private static final byte SEPARATOR = ',';
    private static final byte LINE_SEPARATOR = '\n';
    private final Charset CHARSET = Charset.defaultCharset();

    @Override
    public boolean canRead(@NonNull Class<?> clazz, @NonNull MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(@NonNull Class<?> clazz, MediaType mediaType) {
        return mediaType != null && mediaType.equalsTypeAndSubtype(CSV) &&
                SimpleObj.class.isAssignableFrom(clazz);
    }

    @Override
    @NonNull
    public List<MediaType> getSupportedMediaTypes() {
        return List.of(CSV);
    }

    @Override
    @NonNull
    public SimpleObj read(@NonNull Class<? extends SimpleObj> clazz, @NonNull HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(@NonNull SimpleObj obj, MediaType contentType, @NonNull HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (contentType == null || !contentType.equalsTypeAndSubtype(CSV))
            throw new UnsupportedEncodingException(String.valueOf(contentType));
        if (obj != null)
            outputMessage.getBody().write(obj.toCsvString().getBytes(CHARSET));
    }
}
