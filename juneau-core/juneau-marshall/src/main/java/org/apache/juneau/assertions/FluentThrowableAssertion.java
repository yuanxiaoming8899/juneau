// ***************************************************************************************************************************
// * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file *
// * distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file        *
// * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance            *
// * with the License.  You may obtain a copy of the License at                                                              *
// *                                                                                                                         *
// *  http://www.apache.org/licenses/LICENSE-2.0                                                                             *
// *                                                                                                                         *
// * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an  *
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the        *
// * specific language governing permissions and limitations under the License.                                              *
// ***************************************************************************************************************************
package org.apache.juneau.assertions;

import static org.apache.juneau.assertions.Assertions.*;

import java.io.*;

import org.apache.juneau.cp.*;
import org.apache.juneau.internal.*;

/**
 * Used for fluent assertion calls against throwables.
 *
 * @param <T> The throwable type.
 * @param <R> The return type.
 */
@FluentSetters(returns="FluentThrowableAssertion<T,R>")
public class FluentThrowableAssertion<T extends Throwable,R> extends FluentObjectAssertion<T,R> {

	private static final Messages MESSAGES = Messages.of(FluentThrowableAssertion.class, "Messages");
	static final String
		MSG_exceptionWasNotExpectedType = MESSAGES.getString("exceptionWasNotExpectedType"),
		MSG_exceptionMessageDidNotContainExpectedSubstring = MESSAGES.getString("exceptionMessageDidNotContainExpectedSubstring"),
		MSG_exceptionWasNotThrown = MESSAGES.getString("exceptionWasNotThrown"),
		MSG_exceptionWasThrown = MESSAGES.getString("exceptionWasThrown"),
		MSG_causedByExceptionNotExpectedType = MESSAGES.getString("causedByExceptionNotExpectedType");

	/**
	 * Constructor.
	 *
	 * @param value The throwable being tested.
	 * @param returns The object to return after the test.
	 */
	public FluentThrowableAssertion(T value, R returns) {
		this(null, value, returns);
	}

	/**
	 * Constructor.
	 *
	 * @param creator The assertion that created this assertion.
	 * @param value The throwable being tested.
	 * @param returns The object to return after the test.
	 */
	public FluentThrowableAssertion(Assertion creator, T value, R returns) {
		super(creator, value, returns);
	}

	/**
	 * Asserts that this throwable is of the specified type.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws a RuntimeException. </jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();})
	 * 		.isType(RuntimeException.<jk>class</jk>);
	 * </p>
	 *
	 * @param type The type.
	 * @return This object (for method chaining).
	 */
	@Override
	public R isType(Class<?> type) {
		assertArgNotNull("type", type);
		if (! type.isInstance(value()))
			throw error(MSG_exceptionWasNotExpectedType, className(type), className(value()));
		return returns();
	}

	/**
	 * Asserts that this throwable is exactly the specified type.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws a RuntimeException. </jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();})
	 * 		.isExactType(RuntimeException.<jk>class</jk>);
	 * </p>
	 *
	 * @param type The type.
	 * @return This object (for method chaining).
	 */
	public R isExactType(Class<?> type) {
		assertArgNotNull("type", type);
		if (type != value().getClass())
			throw error(MSG_exceptionWasNotExpectedType, className(type), className(value()));
		return returns();
	}

	/**
	 * Asserts that this throwable or any parent throwables contains all of the specified substrings.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws an exception with 'foobar' somewhere in the messages. </jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();}).contains(<js>"foobar"</js>);
	 * </p>
	 *
	 * @param substrings The substrings to check for.
	 * @return This object (for method chaining).
	 */
	public R contains(String...substrings) {
		assertArgNotNull("substrings", substrings);
		for (String substring : substrings) {
			if (substring != null) {
				Throwable e2 = value();
				boolean found = false;
				while (e2 != null && ! found) {
					found |= StringUtils.contains(e2.getMessage(), substring);
					e2 = e2.getCause();
				}
				if (! found) {
					throw error(MSG_exceptionMessageDidNotContainExpectedSubstring, substring, value().getMessage());
				}
			}
		}
		return returns();
	}

	/**
	 * Asserts that this throwable has the specified message.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws an exception with the message 'foobar'.</jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();}).is(<js>"foobar"</js>);
	 * </p>
	 *
	 * @param msg The message to check for.
	 * @return This object (for method chaining).
	 */
	public R is(String msg) {
		return message().is(msg);
	}

	/**
	 * Asserts that this throwable exists.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws any exception.</jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();}).exists();
	 * </p>
	 *
	 * @return This object (for method chaining).
	 */
	@Override
	public R exists() {
		if (valueIsNull())
			throw error(MSG_exceptionWasNotThrown);
		return returns();
	}

	/**
	 * Asserts that this throwable doesn't exist.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method doesn't throw any exception.</jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();}).notExists();
	 * </p>
	 *
	 * @return This object (for method chaining).
	 */
	@Override
	public R doesNotExist() {
		if (valueIsNotNull())
			throw error(MSG_exceptionWasThrown);
		return returns();
	}

	/**
	 * Returns an assertion against the throwable message.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws an exception with 'foobar' somewhere in the messages. </jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();}).message().matches(<js>".*foobar.*"</js>);
	 * </p>
	 *
	 * @return An assertion against the throwable message.  Never <jk>null</jk>.
	 */
	public FluentStringAssertion<R> message() {
		return new FluentStringAssertion<>(this, map(Throwable::getMessage).orElse(null), returns());
	}

	/**
	 * Returns an assertion against the throwable localized message.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws an exception with 'foobar' somewhere in the localized messages. </jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();}).localizedMessage().matches(<js>".*foobar.*"</js>);
	 * </p>
	 *
	 * @return An assertion against the throwable localized message.  Never <jk>null</jk>.
	 */
	public FluentStringAssertion<R> localizedMessage() {
		return new FluentStringAssertion<>(this, map(Throwable::getLocalizedMessage).orElse(null), returns());
	}

	/**
	 * Returns an assertion against the throwable localized message.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws an exception with 'foobar' somewhere in the stack trace. </jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();}).stackTrace().contains(<js>"foobar"</js>);
	 * </p>
	 *
	 * @return An assertion against the throwable stacktrace.  Never <jk>null</jk>.
	 */
	public FluentStringAssertion<R> stackTrace() {
		return new FluentStringAssertion<>(this, map(StringUtils::getStackTrace).orElse(null), returns());
	}

	/**
	 * Returns an assertion against the caused-by throwable.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws an exception whose caused-by message contains 'foobar'. </jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();}).causedBy().message().contains(<js>"foobar"</js>);
	 * </p>
	 *
	 * @return An assertion against the caused-by.  Never <jk>null</jk>.
	 */
	public FluentThrowableAssertion<Throwable,R> causedBy() {
		return causedBy(Throwable.class);
	}

	/**
	 * Returns an assertion against the caused-by throwable.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws an exception whose caused-by message contains 'foobar'. </jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();}).causedBy().message().contains(<js>"foobar"</js>);
	 * </p>
	 *
	 * @param type The expected exception type.
	 * @return An assertion against the caused-by.  Never <jk>null</jk>.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Throwable> FluentThrowableAssertion<E,R> causedBy(Class<E> type) {
		Throwable t = map(Throwable::getCause).orElse(null);
		if (t == null || type.isInstance(t))
			return new FluentThrowableAssertion<>(this, (E)t, returns());
		throw error(MSG_causedByExceptionNotExpectedType, type, t.getClass());
	}

	/**
	 * Returns an assertion against the throwable localized message.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws an exception with a caused-by RuntimeException containing 'foobar'</jc>
	 * 	ThrowableAssertion.<jsm>assertThrown</jsm>(() -&gt; {<jv>foo</jv>.getBar();}).causedBy(RuntimeException.<jk>class</jk>).exists().contains(<js>"foobar"</js>);
	 * </p>
	 *
	 * @param throwableClass The class type to search for in the caused-by chain.
	 * @return An assertion against the caused-by throwable.  Never <jk>null</jk>.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Throwable> FluentThrowableAssertion<E,R> find(Class<E> throwableClass) {
		Throwable t = orElse(null);
		while (t != null) {
			if (throwableClass.isInstance(t))
				return new FluentThrowableAssertion<>(this, (E)t, returns());
			t = t.getCause();
		}
		return new FluentThrowableAssertion<>(this, (E)null, returns());
	}

	// <FluentSetters>

	@Override /* GENERATED - Assertion */
	public FluentThrowableAssertion<T,R> msg(String msg, Object...args) {
		super.msg(msg, args);
		return this;
	}

	@Override /* GENERATED - Assertion */
	public FluentThrowableAssertion<T,R> out(PrintStream value) {
		super.out(value);
		return this;
	}

	@Override /* GENERATED - Assertion */
	public FluentThrowableAssertion<T,R> silent() {
		super.silent();
		return this;
	}

	@Override /* GENERATED - Assertion */
	public FluentThrowableAssertion<T,R> stdout() {
		super.stdout();
		return this;
	}

	@Override /* GENERATED - Assertion */
	public FluentThrowableAssertion<T,R> throwable(Class<? extends java.lang.RuntimeException> value) {
		super.throwable(value);
		return this;
	}

	// </FluentSetters>
}
