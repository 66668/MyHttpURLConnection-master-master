package com.example.type2.http;

import com.example.type2.http.content.ContentBody;

import org.apache.http.util.ByteArrayBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sjy on 2017/6/28.
 */

public class HttpMultipart  {
    private static final ByteArrayBuffer FIELD_SEP;
    private static final ByteArrayBuffer CR_LF;
    private static final ByteArrayBuffer TWO_DASHES;
    private final String subType;
    private final Charset charset;
    private final String boundary;
    private final List<FormBodyPart> parts;
    private final HttpMultipartMode mode;

    private static ByteArrayBuffer encode(Charset charset, String string) {
        ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
        ByteArrayBuffer bab = new ByteArrayBuffer(encoded.remaining());
        bab.append(encoded.array(), encoded.position(), encoded.remaining());
        return bab;
    }

    private static void writeBytes(ByteArrayBuffer b, OutputStream out) throws IOException {
        out.write(b.buffer(), 0, b.length());
    }

    private static void writeBytes(String s, Charset charset, OutputStream out) throws IOException {
        ByteArrayBuffer b = encode(charset, s);
        writeBytes(b, out);
    }

    private static void writeBytes(String s, OutputStream out) throws IOException {
        ByteArrayBuffer b = encode(MIME.DEFAULT_CHARSET, s);
        writeBytes(b, out);
    }

    private static void writeField(MinimalField field, OutputStream out) throws IOException {
        writeBytes(field.getName(), out);
        writeBytes(FIELD_SEP, out);
        writeBytes(field.getBody(), out);
        writeBytes(CR_LF, out);
    }

    private static void writeField(MinimalField field, Charset charset, OutputStream out) throws IOException {
        writeBytes(field.getName(), charset, out);
        writeBytes(FIELD_SEP, out);
        writeBytes(field.getBody(), charset, out);
        writeBytes(CR_LF, out);
    }

    public HttpMultipart(String subType, Charset charset, String boundary, HttpMultipartMode mode) {
        if(subType == null) {
            throw new IllegalArgumentException("Multipart subtype may not be null");
        } else if(boundary == null) {
            throw new IllegalArgumentException("Multipart boundary may not be null");
        } else {
            this.subType = subType;
            this.charset = charset != null?charset:MIME.DEFAULT_CHARSET;
            this.boundary = boundary;
            this.parts = new ArrayList();
            this.mode = mode;
        }
    }

    public HttpMultipart(String subType, Charset charset, String boundary) {
        this(subType, charset, boundary, HttpMultipartMode.STRICT);
    }

    public HttpMultipart(String subType, String boundary) {
        this(subType, (Charset)null, boundary);
    }

    public String getSubType() {
        return this.subType;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public HttpMultipartMode getMode() {
        return this.mode;
    }

    public List<FormBodyPart> getBodyParts() {
        return this.parts;
    }

    public void addBodyPart(FormBodyPart part) {
        if(part != null) {
            this.parts.add(part);
        }
    }

    public String getBoundary() {
        return this.boundary;
    }

    private void doWriteTo(HttpMultipartMode mode, OutputStream out, boolean writeContent) throws IOException {
        ByteArrayBuffer boundary = encode(this.charset, this.getBoundary());

        for(Iterator i$ = this.parts.iterator(); i$.hasNext(); writeBytes(CR_LF, out)) {
            FormBodyPart part;
            part = (FormBodyPart)i$.next();
            writeBytes(TWO_DASHES, out);
            writeBytes(boundary, out);
            writeBytes(CR_LF, out);
            Header header = part.getHeader();
            label26:
//            switch(HttpMultipart.SyntheticClass_1.$SwitchMap$org$apache$http$entity$mime$HttpMultipartMode[mode.ordinal()]) {
//                case 1:
//                    Iterator cd1 = header.iterator();
//
//                    while(true) {
//                        if(!cd1.hasNext()) {
//                            break label26;
//                        }
//
//                        MinimalField filename1 = (MinimalField)cd1.next();
//                        writeField(filename1, out);
//                    }
//                case 2:
//                    MinimalField cd = part.getHeader().getField("Content-Disposition");
//                    writeField(cd, this.charset, out);
//                    String filename = part.getBody().getFilename();
//                    if(filename != null) {
//                        MinimalField ct = part.getHeader().getField("Content-Type");
//                        writeField(ct, this.charset, out);
//                    }
//            }

            writeBytes(CR_LF, out);
            if(writeContent) {
                part.getBody().writeTo(out);
            }
        }

        writeBytes(TWO_DASHES, out);
        writeBytes(boundary, out);
        writeBytes(TWO_DASHES, out);
        writeBytes(CR_LF, out);
    }

    public void writeTo(OutputStream out) throws IOException {
        this.doWriteTo(this.mode, out, true);
    }

    public long getTotalLength() {
        long contentLen = 0L;

        long len;
        for(Iterator out = this.parts.iterator(); out.hasNext(); contentLen += len) {
            FormBodyPart ex = (FormBodyPart)out.next();
            ContentBody body = ex.getBody();
            len = body.getContentLength();
            if(len < 0L) {
                return -1L;
            }
        }

        ByteArrayOutputStream out1 = new ByteArrayOutputStream();

        try {
            this.doWriteTo(this.mode, out1, false);
            byte[] ex1 = out1.toByteArray();
            return contentLen + (long)ex1.length;
        } catch (IOException var8) {
            return -1L;
        }
    }

    static {
        FIELD_SEP = encode(MIME.DEFAULT_CHARSET, ": ");
        CR_LF = encode(MIME.DEFAULT_CHARSET, "\r\n");
        TWO_DASHES = encode(MIME.DEFAULT_CHARSET, "--");
    }
}
