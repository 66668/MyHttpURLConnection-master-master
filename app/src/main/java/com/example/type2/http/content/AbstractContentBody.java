package com.example.type2.http.content;

/**
 * Created by sjy on 2017/6/28.
 */

public abstract class AbstractContentBody implements ContentBody {
    private final String mimeType;
    private final String mediaType;
    private final String subType;

    public AbstractContentBody(String mimeType) {
        if(mimeType == null) {
            throw new IllegalArgumentException("MIME type may not be null");
        } else {
            this.mimeType = mimeType;
            int i = mimeType.indexOf(47);
            if(i != -1) {
                this.mediaType = mimeType.substring(0, i);
                this.subType = mimeType.substring(i + 1);
            } else {
                this.mediaType = mimeType;
                this.subType = null;
            }

        }
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public String getSubType() {
        return this.subType;
    }
}

