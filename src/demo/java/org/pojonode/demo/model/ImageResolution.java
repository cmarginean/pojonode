package org.pojonode.demo.model;

import org.pojonode.aop.annotations.ListConstraint;
import org.pojonode.aop.annotations.ListConstraintValue;

/**
 * @author Cosmin Marginean, 3/17/11
 */
@ListConstraint(name = "pnd:imageResolution")
public enum ImageResolution {

    @ListConstraintValue("ldpi")
    LDPI,

    @ListConstraintValue("mdpi")
    MDPI,

    @ListConstraintValue("hdpi")
    HDPI
}
