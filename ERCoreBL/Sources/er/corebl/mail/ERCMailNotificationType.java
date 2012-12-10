package er.corebl.mail;

public enum ERCMailNotificationType {
	BOUNCE(ERCMailStopReason.BOUNCED),COMPLAINT(ERCMailStopReason.COMPLAINT);
	
	private ERCMailStopReason _stopReason;
	
	private ERCMailNotificationType(ERCMailStopReason stopReason) {
		_stopReason = stopReason;
	}
	
	public ERCMailStopReason stopReason() {
		return _stopReason;
	}
}
