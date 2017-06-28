package com.example.type2.http.content;

/**
 * Created by sjy on 2017/6/28.
 */

public interface ContentDescriptor {
    String getMimeType();

    String getMediaType();

    String getSubType();

    String getCharset();

    String getTransferEncoding();

    long getContentLength();
}
