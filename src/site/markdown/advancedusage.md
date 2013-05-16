## Advanced message keys and -fallbacks

The following document specifying advanced usage of the message bundle interface declaration.

### Specifying the message key for the lookup

By convention, the name of the method, declared in the message bundle interface, is used as the message key, which is the name of the property to be looked up in the resource bundles.

To specify another message key, there is an annotation: `@MessageKey` to define the name of another message key.

Example:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
@MessageBundle
public interface MyMessages {

    @MessageKey("my.special.messagekey")
    String specialMessage();

}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

### Specifying fallbacks for messages

If a message is not found in the resource bundles, this module returns an empty string.

For some reasons, a developer would like to specify other fallbacks. Therefore this module provides two annotations:

* `@MessageFallback` - defines a fallback
* `@MessageFallbacks` - defines a list of `@MessageFallback`s for different locales.

Example:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
@MessageBundle
public interface MyMessages {

    @MessageFallback("Message not found!")
    String messageWithFallbackForAllLocales();

    @MessageFallback(locale="de-DE", value="Message nicht gefunden!")
    String messageWithFallbackOnlyForGermanyLocale();

    @MessageFallbacks({
        @MessageFallback(locale="de-DE", value="Deutsches Fallback"),
        @MessageFallback(locale="en", value="English fallback"),
        @MessageFallback("fallback for all other locales")
    })
    String messageWithFallbackForSomeLocales();

}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
