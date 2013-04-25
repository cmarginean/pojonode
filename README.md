Pojonode
========

## About Pojonode

Pojonode is a lightweight framework designed for developing a strong-typed, POJO-based API on top of Alfresco's repository services.

Pojonode makes the development of Alfresco repository services easier by replacing the paradigm:
```
    String name = (String) nodeService.getProperty(nodeRef, ContentModel.PROP_NAME);
```

with the more conventional and OOP-ish approach:
```
    myContentNode.getName();
```

Pojonode is designed to be as non-intrusive as possible. While it can be used to drive an Alfresco content model definition, it can also be used as a simple add-in on top of an existing Alfresco environment.

The repository contains a working demo located under http://pojonode.googlecode.com/svn/trunk/src/demo/, exemplifying the basic usage of the Pojonode services and annotations and how are they wired in an Alfresco environment. Please check the [http://code.google.com/p/pojonode/wiki/Building Building] wiki page for details on how to build and use the demo.

## Documentation
 * [Introduction](http://code.google.com/p/pojonode/wiki/Introduction) - A high-level overview of the core concepts and how is the framework designed
 * [Prerequisites](http://code.google.com/p/pojonode/wiki/Prerequisites) - Requirements for deploying and using Pojonode. Long story short: you only need Alfresco.
 * [PojosAndAnnotations](http://code.google.com/p/pojonode/wiki/PojosAndAnnotations) - An introduction to how the Pojonode annotation system works.
 * [Integration](http://code.google.com/p/pojonode/wiki/Integration) - Steps and configuration required to integrate the framework in your Alfresco environment.
 * [Usage](http://code.google.com/p/pojonode/wiki/Usage) - A sample on how to wire and use the Pojonode API.
 * [ReverseEngineeringAlfModel](http://code.google.com/p/pojonode/wiki/ReverseEngineeringAlfModel) - Reverse engineering an Alfresco model to a POJO model
 * [AntTasks](http://code.google.com/p/pojonode/wiki/AntTasks) - A set of ant tasks to help with code generation and build automation
 * [Building](http://code.google.com/p/pojonode/wiki/Building) - Guide on building Pojonode library and demo from source and deploying them in an Alfresco environment.

## Downloads
Checkout [http://code.google.com/p/pojonode/downloads/list?q=label:Featured featured downloads] for the latest builds of the Pojonode library and demo.
