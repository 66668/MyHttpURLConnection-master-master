package com.example.type2.http;

import java.nio.charset.Charset;

/**
 * Created by sjy on 2017/6/28.
 */

public final class MIME {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TRANSFER_ENC = "Content-Transfer-Encoding";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String ENC_8BIT = "8bit";
    public static final String ENC_BINARY = "binary";
    public static final Charset DEFAULT_CHARSET = Charset.forName("US-ASCII");

    public MIME() {
    }
}
