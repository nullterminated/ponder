package com.example.delegates;

import java.io.File;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.QDRect;
import quicktime.std.movies.Movie;

import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSSet;

import er.attachment.model.ERAttachment;
import er.attachment.processors.ERAttachmentProcessor;
import er.attachment.processors.IERAttachmentProcessorDelegate;
import er.r2d2w.interfaces.R2DAttachmentContainer;

public class QuickTimeProcessorDelegate implements
		IERAttachmentProcessorDelegate, R2DAttachmentContainer {
	private File upload;
	
	public static final NSSet<String> QUICKTIME_AUDIO_TYPES = 
		new NSSet<String>(new String[] {
			"audio/aiff",
			"audio/mpeg",
			"audio/mp4",
			"audio/wav",
			});
	public static final NSSet<String> QUICKTIME_IMAGE_TYPES = 
		new NSSet<String>(new String[] {
			"image/bmp",
			"image/gif",
			"image/jpeg",
			"image/jp2",
			"image/png",
			"image/tiff",
			"image/vnd.adobe.photoshop",
			});
	public static final NSSet<String> QUICKTIME_VIDEO_TYPES = 
		new NSSet<String>(new String[] {
			"video/3gpp",
			"video/3gpp2",
			"video/mp4",
			"video/mpeg",
			"video/msvideo",
			"video/quicktime",
			});
	public static final NSSet<String> QUICKTIME_MIME_TYPES = 
		QUICKTIME_AUDIO_TYPES.setByUnioningSet(QUICKTIME_IMAGE_TYPES).setByUnioningSet(QUICKTIME_VIDEO_TYPES);


	/**
	 * Called when an attachment is created. Adds height, width, and duration
	 * info to the attachment.
	 * 
	 * @param processor
	 *            the attachment processor
	 * @param attachment
	 *            the attachment
	 */
	public void attachmentCreated(ERAttachmentProcessor processor,
			ERAttachment attachment) {
		
		if(upload != null) {
			try {
				QTSession.open();
				QTFile qtFile = new QTFile(upload);
				Movie m = Movie.fromFile(OpenMovieFile.asRead(qtFile));
				
				Integer duration = new Integer(m.getDuration());
				Integer timeScale = new Integer(m.getTimeScale());
				Integer durationMillis = new Integer((duration * 1000)/timeScale);
				attachment.setDuration(durationMillis);
				
				QDRect rect = m.getBounds();
				if(rect != null) {
					Integer height = new Integer(rect.getHeight());
					Integer width = new Integer(rect.getWidth());
					attachment.setHeight(height);
					attachment.setWidth(width);
				}
				
			} catch (QTException e) {
				throw NSForwardException._runtimeExceptionForThrowable(e);
			} finally {
				QTSession.close();
			}
		}
	}

	/**
	 * Called when an attachment is made available.
	 * 
	 * @param processor
	 *            the attachment processor
	 * @param attachment
	 *            the attachment
	 */
	public void attachmentAvailable(ERAttachmentProcessor processor,
			ERAttachment attachment) {
	}

	/**
	 * Called when an attachment is determined to be unavailable. This provides
	 * the opportunity to clean up the attachment in whatever way is appropriate
	 * for your application. Note: There is currently a failure mode with this
	 * method where it will not be called if the application crashes. If it is
	 * essential that you process all unavailable attachments, you may want to
	 * handle that at application startup by selecting all of the available =
	 * false attachments and running your custom processing on them.
	 * 
	 * @param processor
	 *            the attachment processor
	 * @param attachment
	 *            the attachment
	 * @param failureReason
	 *            the reason why the attachment is not available
	 */
	public void attachmentNotAvailable(ERAttachmentProcessor processor,
			ERAttachment attachment, String failureReason) {
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}
	
	public File upload() {
		return upload;
	}
}
