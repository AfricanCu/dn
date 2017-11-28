package com.ems358.sdk.share;

import android.graphics.Bitmap;

public class WXShare {

	public static final int SHARE_INVITE = 1;
	public static final int SHARE_FLAUNT = 2;
	public static final int SHARE_FREEND = 3;

	int shareType;
	String content;
	String img;
	Bitmap icon;
	String title;
	String link;

	public int getShareType() {
		return shareType;
	}

	public void setShareType(int shareType) {
		this.shareType = shareType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Bitmap getIcon() {
		return icon;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}

	public WXShare(int shareType, String content, String img, String title, String link) {
		super();
		this.shareType = shareType;
		this.content = content;
		this.img = img;
		this.title = title;
		this.link = link;
	}

}
