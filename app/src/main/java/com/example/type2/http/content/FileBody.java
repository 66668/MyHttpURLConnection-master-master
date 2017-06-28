package com.example.type2.http.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by sjy on 2017/6/28.
 */

public class FileBody extends AbstractContentBody {
    private final File file;
    private final String filename;
    private final String charset;

    public FileBody(File file, String filename, String mimeType, String charset) {
        super(mimeType);
        if(file == null) {
            throw new IllegalArgumentException("File may not be null");
        } else {
            this.file = file;
            if(filename != null) {
                this.filename = filename;
            } else {
                this.filename = file.getName();
            }

            this.charset = charset;
        }
    }

    public FileBody(File file, String mimeType, String charset) {
        this(file, (String)null, mimeType, charset);
    }

    public FileBody(File file, String mimeType) {
        this(file, mimeType, (String)null);
    }

    public FileBody(File file) {
        this(file, "application/octet-stream");
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    public void writeTo(OutputStream out) throws IOException {
        if(out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        } else {
            FileInputStream in = new FileInputStream(this.file);

            try {
                byte[] tmp = new byte[4096];

                int l;
                while((l = in.read(tmp)) != -1) {
                    out.write(tmp, 0, l);
                }

                out.flush();
            } finally {
                in.close();
            }
        }
    }

    public String getTransferEncoding() {
        return "binary";
    }

    public String getCharset() {
        return this.charset;
    }

    public long getContentLength() {
        return this.file.length();
    }

    public String getFilename() {
        return this.filename;
    }

    public File getFile() {
        return this.file;
    }
}
