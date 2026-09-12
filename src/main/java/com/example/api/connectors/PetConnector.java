package com.example.api.connectors;

import com.example.base.TestContext;
import com.example.utils.AssertionHelper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pet API connector for PetStore operations.
 * Extends BaseApiConnector for common functionality.
 */
@Slf4j
public class PetConnector extends BaseApiConnector {
    
    private static final String PET_ENDPOINT = "pet/";
    
    public PetConnector(Playwright playwright, TestContext testContext) {
        super(playwright, testContext);
    }
    
    /**
     * Add a new pet to the pet store
     */
    public APIResponse addNewPet(String petBody) {
        log.info("Adding new pet to store");
        APIRequestContext context = createRequestContext(configManager.getBaseUri());
        
        try {
            APIResponse response = post(context, PET_ENDPOINT, petBody);
            validateSuccessResponse(response, "Add new pet");
            log.info("Pet added successfully");
            return response;
        } finally {
            closeContext(context);
        }
    }
    
    /**
     * Get pet by ID
     */
    public APIResponse getPetById(int petId) {
        log.info("Fetching pet with ID: {}", petId);
        APIRequestContext context = createRequestContext(configManager.getBaseUri());
        
        try {
            APIResponse response = get(context, PET_ENDPOINT + petId);
            AssertionHelper.assertOkStatus(response.status(), "Get pet by ID: " + petId);
            log.info("Pet retrieved successfully");
            return response;
        } finally {
            closeContext(context);
        }
    }
    
    /**
     * Find pets by status
     */
    public APIResponse findPetsByStatus(List<String> statusList) {
        log.info("Finding pets with statuses: {}", statusList);
        APIRequestContext context = createRequestContext(configManager.getBaseUri());
        
        try {
            RequestOptions options = createQueryParamsFromList("status", statusList);
            APIResponse response = get(context, PET_ENDPOINT + "findByStatus", options);
            AssertionHelper.assertOkStatus(response.status(), "Find pets by status");
            log.info("Pets found successfully");
            return response;
        } finally {
            closeContext(context);
        }
    }
    
    /**
     * Update pet details
     */
    public APIResponse updatePetDetails(int petId, String attribute, String attributeValue) {
        log.info("Updating pet {} with {}={}", petId, attribute, attributeValue);
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        
        APIRequestContext context = createRequestContext(configManager.getBaseUri(), headers);
        
        try {
            FormData formData = FormData.create();
            formData.set(attribute, attributeValue);
            
            APIResponse response = post(context, PET_ENDPOINT + petId, 
                    RequestOptions.create().setForm(formData));
            AssertionHelper.assertOkStatus(response.status(), "Update pet details");
            log.info("Pet updated successfully");
            return response;
        } finally {
            closeContext(context);
        }
    }
    
    /**
     * Delete pet by ID
     */
    public APIResponse deletePet(int petId) {
        log.info("Deleting pet with ID: {}", petId);
        APIRequestContext context = createRequestContext(configManager.getBaseUri());
        
        try {
            APIResponse response = delete(context, PET_ENDPOINT + petId);
            AssertionHelper.assertOkStatus(response.status(), "Delete pet: " + petId);
            log.info("Pet deleted successfully");
            return response;
        } finally {
            closeContext(context);
        }
    }
    
    /**
     * Upload pet image
     */
    public APIResponse uploadPetImage(int petId, String imagePath, String additionalData) {
        log.info("Uploading image for pet ID: {}", petId);
        APIRequestContext context = createRequestContext(configManager.getBaseUri());
        
        try {
            FormData formData = FormData.create();
            formData.set("additionalMetadata", additionalData);
            
            APIResponse response = post(context, PET_ENDPOINT + petId + "/uploadImage",
                    RequestOptions.create().setForm(formData));
            AssertionHelper.assertOkStatus(response.status(), "Upload pet image");
            log.info("Image uploaded successfully");
            return response;
        } finally {
            closeContext(context);
        }
    }
}
