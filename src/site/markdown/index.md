# Introduction

This module implements a fancy trick to use localized messages programmatically.

It uses CDI to let the developer:

* create an Interface that defines message-keys by declaring a method
* inject the Interface to wherever this messages should be used
* build a Resource-Bundle (*.properties file) that is automatically resolved and the corresponding message is returned when a method of this Interface is called.

See [usage](usage.html) for details.
