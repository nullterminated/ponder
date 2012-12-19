package er.awsplugin.enums;

public enum SESBounceType {
	PERMANENT("Permanent"), TRANSIENT("Transient"), UNKNOWN("Undetermined");
	
	private final String _typeString;
	
	private SESBounceType(String typeString) {
		_typeString = typeString;
	}
	
	public static SESBounceType typeForString(String typeString) {
		for(SESBounceType type: values()) {
			if(type._typeString.equalsIgnoreCase(typeString)) {
				return type;
			}
		}
		throw new IllegalArgumentException("No type found for string: " + typeString);
	}
}
