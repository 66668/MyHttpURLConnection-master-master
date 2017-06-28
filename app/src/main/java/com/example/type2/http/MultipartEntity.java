package com.example.type2.http;

import com.example.type2.http.content.ContentBody;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by sjy on 2017/6/28.
 */

public class MultipartEntity implements HttpEntity {
    private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final HttpMultipart multipart;
    private final Header contentType;
    private long length;
    private volatile boolean dirty;

    public MultipartEntity(HttpMultipartMode mode, String boundary, Charset charset) {
        if(boundary == null) {
            boundary = this.generateBoundary();
        }

        if(mode == null) {
            mode = HttpMultipartMode.STRICT;
        }

        this.multipart = new HttpMultipart("form-data", charset, boundary, mode);
        this.contentType = new BasicHeader("Content-Type", this.generateContentType(boundary, charset));
        this.dirty = true;
    }

    public MultipartEntity(HttpMultipartMode mode) {
        this(mode, (String)null, (Charset)null);
    }

    public MultipartEntity() {
        this(HttpMultipartMode.STRICT, (String)null, (Charset)null);
    }

    protected String generateContentType(String boundary, Charset charset) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("multipart/form-data; boundary=");
        buffer.append(boundary);
        if(charset != null) {
            buffer.append("; charset=");
            buffer.append(charset.name());
        }

        return buffer.toString();
    }

    protected String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30;

        for(int i = 0; i < count; ++i) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }

        return buffer.toString();
    }

    public void addPart(FormBodyPart bodyPart) {
        this.multipart.addBodyPart(bodyPart);
        this.dirty = true;
    }

    public void addPart(String name, ContentBody contentBody) {
        this.addPart(new FormBodyPart(name, contentBody));
    }

    public boolean isRepeatable() {
        Iterator i$ = this.multipart.getBodyParts().iterator();

        ContentBody body;
        do {
            if(!i$.hasNext()) {
                return true;
            }

            FormBodyPart part = (FormBodyPart)i$.next();
            body = part.getBody();
        } while(body.getContentLength() >= 0L);

        return false;
    }

    public boolean isChunked() {
        return !this.isRepeatable();
    }

    public boolean isStreaming() {
        return !this.isRepeatable();
    }

    public long getContentLength() {
        if(this.dirty) {
            this.length = this.multipart.getTotalLength();
            this.dirty = false;
        }

        return this.length;
    }

    public Header getContentType() {
        return this.contentType;
    }

    public Header getContentEncoding() {
        return null;
    }

    public void consumeContent() throws IOException, UnsupportedOperationException {
        if(this.isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    public InputStream getContent() throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Multipart form entity does not implement #getContent()");
    }

    public void writeTo(OutputStream outstream) throws IOException {
        this.multipart.writeTo(outstream);
    }
}
