package com.example.propertyview.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SwaggerMessage {

    @UtilityClass
    public static class Code {

        public static final String CODE_200 = "200";
        public static final String CODE_201 = "201";
        public static final String CODE_204 = "204";
        public static final String CODE_400 = "400";
        public static final String CODE_404 = "404";
    }

    @UtilityClass
    public static class Message {

        public static final String MESSAGE_200 = "Request successfully processed";
        public static final String MESSAGE_201 = "Resource created";
        public static final String MESSAGE_204 = "No Content";
        public static final String MESSAGE_400 = "Received incorrect data";
        public static final String MESSAGE_404 = "The resource you were trying to reach is not found";
    }
}
