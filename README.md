Pojonode
========

## About Pojonode

Pojonode is a lightweight framework designed for developing a strong-typed, POJO-based API on top of Alfresco's repository services.

Pojonode makes the development of Alfresco repository services easier by replacing the paradigm:
```
    String name = (String) nodeService.getProperty(nodeRef, ContentModel.PROP_NAME);
```

with the more conventional:
```
    myContentNode.getName();
```

Pojonode is designed to be as non-intrusive as possible. While it can be used to drive an Alfresco content model definition, it can also be used as a simple add-in on top of an existing Alfresco environment.

The repository contains a working demo located under http://pojonode.googlecode.com/svn/trunk/src/demo/, exemplifying the basic usage of the Pojonode services and annotations and how are they wired in an Alfresco environment. Please check the [http://code.google.com/p/pojonode/wiki/Building Building] wiki page for details on how to build and use the demo.

Check the [wiki](https://github.com/cosminaru/pojonode/wiki) for documentation on how to run and use Pojonode.

## Downloads
Checkout [http://code.google.com/p/pojonode/downloads/list?q=label:Featured featured downloads] for the latest builds of the Pojonode library and demo.
