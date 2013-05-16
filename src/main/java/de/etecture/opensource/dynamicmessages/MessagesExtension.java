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

import de.etecture.opensource.dynamicmessages.api.MessageBundle;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

/**
 *
 * @author rhk
 */
public class MessagesExtension implements Extension {

    private Map<? super Class<?>, MessageBean> messageBundleInterfaces = new HashMap<>();

	void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
		System.out.println("beginning the scanning process");
	}

	<T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat, BeanManager beanManager) {
		if (pat.getAnnotatedType().isAnnotationPresent(MessageBundle.class)) {
			System.out.println("found messagebundle interface: " + pat.getAnnotatedType().getJavaClass().getName());
			messageBundleInterfaces.put(pat.getAnnotatedType().getJavaClass(), new MessageBean(beanManager, pat.getAnnotatedType().getJavaClass()));
		}
	}

	<T> void processInjectionPoint(@Observes ProcessInjectionTarget<T> pit) {
		for (InjectionPoint point : pit.getInjectionTarget().getInjectionPoints()) {
            if (messageBundleInterfaces.containsKey(point.getType())) {
                System.out.printf("... found InjectionPoint for MessageBundle: %s of type %s%n", point.getMember().getName(), point.getType());
			}
		}
	}

	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
		System.out.println("finished the scanning process");
		for (MessageBean bean : messageBundleInterfaces.values()) {
			abd.addBean(bean);
		}
	}
}