package com.example.api.connectors;

import com.example.base.TestContext;
import com.example.config.ConfigManager;
import com.example.utils.AssertionHelper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base API connector for all API requests.
 * Provides common functionality for HTTP operations with proper error handling.
 */
@Slf4j
public abstract class BaseApiConnector {
    
    protected Playwright playwright;
    protected TestContext testContext;
    protected ConfigManager configManager;
    
    protected BaseApiConnector(Playwright playwright, TestContext testContext) {
        this.playwright = playwright;
        this.testContext = testContext;
        this.configManager = ConfigManager.getInstance();
    }
    
    /**
     * Create API request context with base URL and default headers
     */
    protected APIRequestContext createRequestContext(String baseUrl) {
        log.info("Creating API request context with base URL: {}", baseUrl);
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        
        return playwright.request()
                .newContext(new APIRequest.NewContextOptions()
                        .setBaseURL(baseUrl)
                        .setExtraHTTPHeaders(headers)
                );
    }
    
    /**
     * Create API request context with custom headers
     */
    protected APIRequestContext createRequestContext(String baseUrl, Map<String, String> customHeaders) {
        log.info("Creating API request context with base URL: {} and custom headers", baseUrl);
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.putAll(customHeaders);
        
        return playwright.request()
                .newContext(new APIRequest.NewContextOptions()
                        .setBaseURL(baseUrl)
                        .setExtraHTTPHeaders(headers)
                );
    }
    
    /**
     * Execute GET request with error handling
     */
    protected APIResponse get(APIRequestContext context, String endpoint) {
        try {
            log.debug("Executing GET request to endpoint: {}", endpoint);
            APIResponse response = context.get(endpoint);
            logResponse(response, "GET", endpoint);
            return response;
        } catch (Exception e) {
            log.error("GET request failed for endpoint: {}", endpoint, e);
            throw new RuntimeException("GET request failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Execute GET request with query parameters
     */
    protected APIResponse get(APIRequestContext context, String endpoint, RequestOptions options) {
        try {
            log.debug("Executing GET request to endpoint: {} with options", endpoint);
            APIResponse response = context.get(endpoint, options);
            logResponse(response, "GET", endpoint);
            return response;
        } catch (Exception e) {
            log.error("GET request failed for endpoint: {}", endpoint, e);
            throw new RuntimeException("GET request failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Execute POST request with error handling
     */
    protected APIResponse post(APIRequestContext context, String endpoint, String body) {
        try {
            log.debug("Executing POST request to endpoint: {} with body", endpoint);
            APIResponse response = context.post(endpoint, 
                    RequestOptions.create().setData(body));
            logResponse(response, "POST", endpoint);
            return response;
        } catch (Exception e) {
            log.error("POST request failed for endpoint: {}", endpoint, e);
            throw new RuntimeException("POST request failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Execute POST request with options
     */
    protected APIResponse post(APIRequestContext context, String endpoint, RequestOptions options) {
        try {
            log.debug("Executing POST request to endpoint: {} with options", endpoint);
            APIResponse response = context.post(endpoint, options);
            logResponse(response, "POST", endpoint);
            return response;
        } catch (Exception e) {
            log.error("POST request failed for endpoint: {}", endpoint, e);
            throw new RuntimeException("POST request failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Execute PUT request with error handling
     */
    protected APIResponse put(APIRequestContext context, String endpoint, String body) {
        try {
            log.debug("Executing PUT request to endpoint: {} with body", endpoint);
            APIResponse response = context.put(endpoint, 
                    RequestOptions.create().setData(body));
            logResponse(response, "PUT", endpoint);
            return response;
        } catch (Exception e) {
            log.error("PUT request failed for endpoint: {}", endpoint, e);
            throw new RuntimeException("PUT request failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Execute DELETE request with error handling
     */
    protected APIResponse delete(APIRequestContext context, String endpoint) {
        try {
            log.debug("Executing DELETE request to endpoint: {}", endpoint);
            APIResponse response = context.delete(endpoint);
            logResponse(response, "DELETE", endpoint);
            return response;
        } catch (Exception e) {
            log.error("DELETE request failed for endpoint: {}", endpoint, e);
            throw new RuntimeException("DELETE request failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Log API response details
     */
    protected void logResponse(APIResponse response, String method, String endpoint) {
        log.debug("{} request to {} returned status: {}", method, endpoint, response.status());
        log.trace("Response headers: {}", response.headers());
    }
    
    /**
     * Validate successful response
     */
    protected void validateSuccessResponse(APIResponse response, String context) {
        AssertionHelper.assertSuccessStatus(response.status(), context);
        log.info("Response validation passed for: {}", context);
    }
    
    /**
     * Close API request context
     */
    protected void closeContext(APIRequestContext context) {
        if (context != null) {
            context.dispose();
            log.debug("API request context closed");
        }
    }
    
    /**
     * Handle query parameters for GET requests
     */
    protected RequestOptions createQueryParams(Map<String, String> params) {
        RequestOptions options = RequestOptions.create();
        params.forEach((key, value) -> options.setQueryParam(key, value));
        return options;
    }
    
    /**
     * Handle list query parameters (multiple values for same param)
     */
    protected RequestOptions createQueryParamsFromList(String paramName, List<String> paramValues) {
        RequestOptions options = RequestOptions.create();
        for (String value : paramValues) {
            options.setQueryParam(paramName, value);
        }
        return options;
    }
}
