package org.pojonode.model;

/**
 * Defines how Alfresco-specific configuration (files) are generated for a content model
 *
 * @author Cosmin Marginean, 20/03/11
 */
public enum ModelConfigDeploymentPolicy {

    /**
     * Never generate the configuration
     */
    NEVER,

    /**
     * Always generate the configuration,, overwriting it if it already exists
     */
    ALWAYS,

    /**
     * Only generate the configuration if it's not already existent.
     */
    IF_NOT_PRESENT
}
