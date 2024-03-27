package com.cms.audit.api.Config.Jwt;

public interface TokenBlacklist {
    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
}