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

import java.util.Arrays;
import java.util.Locale;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author rhk
 */
@RunWith(Parameterized.class)
public class MessagesTest {

	private final static Weld weld = new Weld();

	private static WeldContainer container;

	private TestBean bean;
	private LocaleProvider localeProvider;

	@Parameters
	public static Iterable<Object[]> parameters() {
		return Arrays.asList(new Object[]{new ExpectedResource(Locale.FRENCH)}, new Object[]{new ExpectedResource(Locale.GERMANY)}, new Object[]{new ExpectedResource(Locale.ENGLISH)});
	}
	private ExpectedResource expected;

	public MessagesTest(ExpectedResource expected) {
		this.expected = expected;
	}

	@BeforeClass
	public static void bootstrap() {
		container = weld.initialize();
	}

	@Before
	public void init() {
		this.localeProvider = container.instance().select(LocaleProvider.class).get();
		this.localeProvider.setCurrentLocale(expected.getLocale());
		this.bean = container.instance().select(TestBean.class).get();
	}

	@Test
	public void testBeanInjection() {
		assertNotNull(this.localeProvider);
		assertNotNull(this.bean);
		assertNotNull(this.bean.messages);
		assertEquals(this.localeProvider, this.bean.localeProvider);
		assertEquals("wrong current locale", expected.getLocale(), this.bean.localeProvider.getCurrentLocale());
		assertEquals("wrong injected locale", expected.getLocale(), this.bean.currentLocale);
	}

	@Test
	public void testMessages() throws Exception {
		String text = this.bean.print("Robert");
		assertEquals(expected.getExpected(), text);
	}

	@AfterClass
	public static void shutdown() {
		weld.shutdown();
	}
}
