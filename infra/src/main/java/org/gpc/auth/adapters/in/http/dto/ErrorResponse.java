package org.gpc.auth.adapters.in.http.dto;

public record ErrorResponse(String failure, String detail) implements DTO {
}
