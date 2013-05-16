/*
 * This file is part of the ETECTURE Open Source Community Projects.
 *
 * Copyright (c) 2013 by:
 *
 * ETECTURE GmbH
 * Darmstädter Landstraße 112
 * 60598 Frankfurt
 * Germany
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the author nor the names of its contributors may be
 *    used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package de.etecture.opensource.dynamicmessages;

import de.etecture.opensource.dynamicmessages.api.MessageFallback;
import de.etecture.opensource.dynamicmessages.api.MessageFallbacks;
import de.etecture.opensource.dynamicmessages.api.MessageKey;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author rhk
 */
public class MessageBundleInvocationHandler implements InvocationHandler {

	private final LocaleProvider localeProvider;

	public MessageBundleInvocationHandler(LocaleProvider localeProvider) {
		this.localeProvider = localeProvider;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String messageKey;
		if (method.isAnnotationPresent(MessageKey.class)) {
			messageKey = method.getAnnotation(MessageKey.class).value();
		} else {
			messageKey = method.getName();
		}
		return MessageFormat.format(getMessage(method, messageKey), args);
	}

	private String getMessage(Method method, String messageKey) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(method.getDeclaringClass().getName(), localeProvider.getCurrentLocale());
			if (bundle.containsKey(messageKey)) {
				return bundle.getString(messageKey);
			} else {
				if (method.isAnnotationPresent(MessageFallback.class)) {
					return method.getAnnotation(MessageFallback.class).value();
				} else if (method.isAnnotationPresent(MessageFallbacks.class)) {
					for (MessageFallback fallback : method.getAnnotation(MessageFallbacks.class).value()) {
						if (fallback.locale().equals(localeProvider.getCurrentLocale().toLanguageTag().replaceAll("\\-", "\\_"))) {
							return fallback.value();
						}
					}
					for (MessageFallback fallback : method.getAnnotation(MessageFallbacks.class).value()) {
						if (fallback.locale().equals("")) {
							return fallback.value();
						}
					}
				}
			}
			return "";
		} catch (MissingResourceException e) {
			return "???";
		}
	}
}
