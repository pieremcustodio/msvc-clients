package com.piere.bootcamp.clients.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
   * Profile type of the client
   */
public enum ProfileTypeEnum {
    VIP("VIP"),
    NORMAL("NORMAL"),
    PYME("PYME");

    private String value;

    ProfileTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ProfileTypeEnum fromValue(String value) {
      for (ProfileTypeEnum b : ProfileTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
