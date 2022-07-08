package mz.co.truetech.enums;

public enum Gender {
	FAMALE("F"), MALE("M");
	
	private String code;
	
	private Gender(String code) {
        this.code = code;
    }

	public String getCode() {
		return code;
	}

	
}
