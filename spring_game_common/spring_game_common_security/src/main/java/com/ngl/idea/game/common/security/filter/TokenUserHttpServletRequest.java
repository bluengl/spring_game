package com.ngl.idea.game.common.security.filter;

import com.ngl.idea.game.common.core.model.TokenUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class TokenUserHttpServletRequest extends HttpServletRequestWrapper {
    private final TokenUser tokenUser;

    public TokenUserHttpServletRequest(HttpServletRequest request, TokenUser tokenUser) {
        super(request);
        this.tokenUser = tokenUser;
    }

    public TokenUser getTokenUser() {
        return tokenUser;
    }
}
