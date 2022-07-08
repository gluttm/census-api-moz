package mz.co.truetech.enums;

public enum Zone {
	RURAL("rural"), URBAN("urbana");

	private String code;

	private Zone(String code) {
        this.code = code;
    }

	public String getCode() {
		return code;
	}

	
}
