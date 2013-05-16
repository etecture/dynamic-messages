## specifying and retrieving the current locale

To specify the current locale, there is a bean `LocaleProvider` that could be injected in a bean.

The `LocaleProvider` declares the methods `getCurrentLocale()` and `setCurrentLocale()` to change the current locale.

Example:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
public class MyBean {

    @Inject
    LocaleProvider localeProvider;

    public void setToEnglish() {
        localeProvider.setCurrentLocale(Locale.ENGLISH);
    }

    public void setToGerman() {
        localeProvider.setCurrentLocale(Locale.GERMAN);
    }

}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

## Injecting the current Locale

Another way - if the current Locale should only be retrieved - is to inject the current Locale in the bean.

Example:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
public class MyBean {
     
    @Inject
    @Current
    Locale currentLocale

    // ... work with the current locale
}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

## Locale Change Notification

When the locale is changed via the `LocaleProvider` service, a CDI-Event of type `LocaleChangedEvent` is fired, so a developer can retrieve the change of the current locale inside an observer method.

Example:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
public class MyBean {
     
    public void onLocaleChange(@Observes LocaleChangedEvent evt) {
        System.out.println("The locale was changed from "+ evt.getOldLocale() +" to "+ evt.getNewLocale() +"!");
}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

## Change the current locale by firing an event

> **TODO**   
> describe the way to fire an event to change the current locale.
