package mz.co.truetech.enums;

public enum UserStatus {
    LOCKED("locked"), UNLOCKED("unlocked");

    private final String code;

    private UserStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


}
