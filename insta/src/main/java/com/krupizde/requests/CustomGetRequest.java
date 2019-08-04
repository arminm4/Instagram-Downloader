package com.krupizde.requests;

import org.brunocvcunha.instagram4j.requests.InstagramGetRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetMediaInfoResult;

public class CustomGetRequest extends InstagramGetRequest<InstagramGetMediaInfoResult>{

	
	
    public CustomGetRequest() {
		super();
	}

	public CustomGetRequest(long mediaId) {
		super();
		this.mediaId = mediaId;
	}

	private long mediaId;

    @Override
    public String getUrl() {
        return "media/" + mediaId + "/info/";
    }

    @Override
    public InstagramGetMediaInfoResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramGetMediaInfoResult.class);
    }
}
