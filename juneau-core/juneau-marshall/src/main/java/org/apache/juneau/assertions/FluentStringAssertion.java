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

import static org.apache.juneau.internal.StringUtils.*;

import java.util.function.*;
import java.util.regex.*;

import org.apache.juneau.internal.*;

/**
 * Used for fluent assertion calls.
 *
 * <h5 class='section'>Example:</h5>
 * <p class='bcode w800'>
 * 	<jc>// Validates the response body of an HTTP call is the text "OK".</jc>
 * 	client
 * 		.get(<jsf>URL</jsf>)
 * 		.run()
 * 		.assertBody().equals(<js>"OK"</js>);
 * </p>
 * @param <R> The return type.
 */
@FluentSetters(returns="FluentStringAssertion<R>")
public class FluentStringAssertion<R> extends FluentAssertion<R> {

	private final String text;
	private boolean javaStrings;

	/**
	 * Constructor.
	 *
	 * @param text The text being tested.
	 * @param returns The object to return after the test.
	 */
	public FluentStringAssertion(String text, R returns) {
		super(returns);
		this.text = text;
	}

	/**
	 * When enabled, text in the message is converted to valid Java strings.
	 *
	 * <p class='bcode w800'>
	 * 	value.replaceAll(<js>"\\\\"</js>, <js>"\\\\\\\\"</js>).replaceAll(<js>"\n"</js>, <js>"\\\\n"</js>).replaceAll(<js>"\t"</js>, <js>"\\\\t"</js>);
	 * </p>
	 *
	 * @return This object (for method chaining).
	 */
	@FluentSetter
	public FluentStringAssertion<R> javaStrings() {
		this.javaStrings = true;
		return this;
	}

	/**
	 * Asserts that the text equals the specified value.
	 *
	 * @param value The value to check against.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R equals(String value) throws AssertionError {
		if (! isEquals(value, text))
			throw error("Text did not equal expected.\n\tExpected=[{0}]\n\tActual=[{1}]", fix(value), fix(text));
		return returns();
	}

	/**
	 * Asserts that the text equals the specified value.
	 *
	 * <p>
	 * Equivalent to {@link #equals(String)}.
	 *
	 * @param value The value to check against.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R is(String value) throws AssertionError {
		return equals(value);
	}

	/**
	 * Asserts that the text equals the specified value after the text has been URL-decoded.
	 *
	 * @param value The value to check against.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R urlDecodedIs(String value) throws AssertionError {
		String t = urlDecode(text);
		if (! isEqualsIc(value, t))
			throw error("Text did not equal expected.\n\tExpected=[{0}]\n\tActual=[{1}]", fix(value), fix(t));
		return returns();
	}

	/**
	 * Asserts that the text equals the specified value ignoring case.
	 *
	 * @param value The value to check against.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R equalsIc(String value) throws AssertionError {
		if (! isEqualsIc(value, text))
			throw error("Text did not equal expected.\n\tExpected=[{0}]\n\tActual=[{1}]", fix(value), fix(text));
		return returns();
	}

	/**
	 * Asserts that the text equals the specified value.
	 *
	 * @param value The value to check against.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R doesNotEqual(String value) throws AssertionError {
		if (isEquals(value, text))
			throw error("Text equaled unexpected.\n\tText=[{1}]", fix(value), fix(text));
		return returns();
	}

	/**
	 * Asserts that the text equals the specified value.
	 *
	 * <p>
	 * Equivalent to {@link #doesNotEqual(String)}.
	 *
	 * @param value The value to check against.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R isNot(String value) throws AssertionError {
		return doesNotEqual(value);
	}

	/**
	 * Asserts that the text does not equal the specified value ignoring case.
	 *
	 * @param value The value to check against.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R doesNotEqualIc(String value) throws AssertionError {
		if (isEqualsIc(value, text))
			throw error("Text equaled unexpected.\n\tText=[{1}]", fix(value));
		return returns();
	}

	/**
	 * Asserts that the text contains all of the specified substrings.
	 *
	 * @param values The values to check against.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R contains(String...values) throws AssertionError {
		for (String substring : values)
			if (! StringUtils.contains(text, substring))
				throw error("Text did not contain expected substring.\n\tSubstring=[{0}]\n\tText=[{1}]", fix(substring), fix(text));
		return returns();
	}

	/**
	 * Asserts that the text doesn't contain any of the specified substrings.
	 *
	 * @param values The values to check against.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R doesNotContain(String...values) throws AssertionError {
		for (String substring : values)
			if (StringUtils.contains(text, substring))
				throw error("Text contained unexpected substring.\n\tSubstring=[{0}]\n\tText=[{1}]", fix(substring), fix(text));
		return returns();
	}

	/**
	 * Asserts that the text is not null.
	 *
	 * <p>
	 * Equivalent to {@link #isNotNull()}.
	 *
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R exists() throws AssertionError {
		return isNotNull();
	}

	/**
	 * Asserts that the text is not null.
	 *
	 * <p>
	 * Equivalent to {@link #isNull()}.
	 *
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R doesNotExist() throws AssertionError {
		return isNull();
	}

	/**
	 * Asserts that the text is not null.
	 *
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R isNull() throws AssertionError {
		if (text != null)
			throw error("Text was not null.  Text=[{0}]", fix(text));
		return returns();
	}

	/**
	 * Asserts that the text is not null.
	 *
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R isNotNull() throws AssertionError {
		if (text == null)
			throw error("Text was null.");
		return returns();
	}

	/**
	 * Asserts that the text is not empty.
	 *
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R isEmpty() throws AssertionError {
		if (! text.isEmpty())
			throw error("Text was not empty.");
		return returns();
	}

	/**
	 * Asserts that the text is not null or empty.
	 *
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R isNotEmpty() throws AssertionError {
		if (text == null)
			throw error("Text was null.");
		if (text.isEmpty())
			throw error("Text was empty.");
		return returns();
	}

	/**
	 * Asserts that the text passes the specified predicate test.
	 *
	 * @param test The predicate to use to test the value.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R passes(Predicate<String> test) throws AssertionError {
		if (! test.test(text))
			throw error("Text did not pass predicate test.\n\tText=[{0}]", fix(text));
		return returns();
	}

	/**
	 * Asserts that the text matches the specified regular expression.
	 *
	 * @param regex The pattern to test for.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R matches(String regex) throws AssertionError {
		return matches(regex, 0);
	}

	/**
	 * Asserts that the text matches the specified pattern containing <js>"*"</js> meta characters.
	 *
	 * <p>
	 * The <js>"*"</js> meta character can be used to represent zero or more characters..
	 *
	 * @param searchPattern The search pattern.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R matchesSimple(String searchPattern) throws AssertionError {
		return matches(getMatchPattern(searchPattern));
	}

	/**
	 * Asserts that the text doesn't match the specified regular expression.
	 *
	 * @param regex The pattern to test for.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R doesNotMatch(String regex) throws AssertionError {
		return doesNotMatch(regex, 0);
	}

	/**
	 * Asserts that the text matches the specified regular expression.
	 *
	 * @param regex The pattern to test for.
	 * @param flags Pattern match flags.  See {@link Pattern#compile(String, int)}.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R matches(String regex, int flags) throws AssertionError {
		Pattern p = Pattern.compile(regex, flags);
		if (! p.matcher(text).matches())
			throw error("Text did not match expected pattern.\n\tPattern=[{0}]\n\tText=[{1}]", fix(regex), fix(text));
		return returns();
	}

	/**
	 * Asserts that the text doesn't match the specified regular expression.
	 *
	 * @param regex The pattern to test for.
	 * @param flags Pattern match flags.  See {@link Pattern#compile(String, int)}.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R doesNotMatch(String regex, int flags) throws AssertionError {
		Pattern p = Pattern.compile(regex, flags);
		if (p.matcher(text).matches())
			throw error("Text matched unexpected pattern.\n\tPattern=[{0}]\n\tText=[{1}]", fix(regex), fix(text));
		return returns();
	}

	/**
	 * Asserts that the text matches the specified regular expression pattern.
	 *
	 * @param pattern The pattern to test for.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R matches(Pattern pattern) throws AssertionError {
		if (! pattern.matcher(text).matches())
			throw error("Text did not match expected pattern.\n\tPattern=[{0}]\n\tText=[{1}]", fix(pattern.pattern()), fix(text));
		return returns();
	}

	/**
	 * Asserts that the text doesn't match the specified regular expression pattern.
	 *
	 * @param pattern The pattern to test for.
	 * @return The response object (for method chaining).
	 * @throws AssertionError If assertion failed.
	 */
	public R doesNotMatch(Pattern pattern) throws AssertionError {
		if (pattern.matcher(text).matches())
			throw error("Text matched unexpected pattern.\n\tPattern=[{0}]\n\tText=[{1}]", fix(pattern.pattern()), fix(text));
		return returns();
	}

	//------------------------------------------------------------------------------------------------------------------
	// Utility methods
	//------------------------------------------------------------------------------------------------------------------

	private String fix(String text) {
		if (javaStrings)
			text = text.replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n").replaceAll("\t", "\\\\t");
		return text;
	}

	// <FluentSetters>

	@Override /* GENERATED - FluentAssertion */
	public FluentStringAssertion<R> msg(String msg, Object...args) {
		super.msg(msg, args);
		return this;
	}

	@Override /* GENERATED - FluentAssertion */
	public FluentStringAssertion<R> stderr() {
		super.stderr();
		return this;
	}

	@Override /* GENERATED - FluentAssertion */
	public FluentStringAssertion<R> stdout() {
		super.stdout();
		return this;
	}

	// </FluentSetters>
}
