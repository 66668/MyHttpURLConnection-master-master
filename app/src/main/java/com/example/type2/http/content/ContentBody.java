package com.example.type2.http.content;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by sjy on 2017/6/28.
 */

public interface ContentBody extends ContentDescriptor {
    String getFilename();

    void writeTo(OutputStream var1) throws IOException;
}
