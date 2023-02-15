package com.qbit.assets.common.enums;

/**
 * @author martinjiang
 */

public enum SpecialUUID {
    NullUUID("00000000-0000-0000-0000-000000000000");
    public final String value;

    private SpecialUUID(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
