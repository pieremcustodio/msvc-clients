package com.piere.bootcamp.clients.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
   * Type of the exception
   */
public enum TypeException {
    A("Advertencia"),
    
    E("Error");

    private String value;

    TypeException(String value) {
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
    public static TypeException fromValue(String value) {
      for (TypeException b : TypeException.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
