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

import java.io.*;
import java.time.*;
import java.util.*;

import org.apache.juneau.*;
import org.apache.juneau.internal.*;

/**
 * Main class for creation of assertions for testing.
 */
public class Assertions {

	/**
	 * Used for assertion calls against {@link Date} objects.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the specified date is after the current date.</jc>
	 * 	<jsm>assertDate</jsm>(<jv>myDate</jv>).isAfterNow();
	 * </p>
	 *
	 * @param value The date being wrapped.
	 * @return A new {@link DateAssertion} object.  Never <jk>null</jk>.
	 */
	public static DateAssertion assertDate(Date value) {
		return DateAssertion.create(value);
	}

	/**
	 * Used for assertion calls against {@link Date} objects.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the specified date is after the current date.</jc>
	 * 	<jsm>assertDate</jsm>(<jv>myDate</jv>).isAfterNow();
	 * </p>
	 *
	 * @param value The date being wrapped.
	 * @return A new {@link DateAssertion} object.  Never <jk>null</jk>.
	 */
	public static DateAssertion assertDate(Optional<Date> value) {
		assertArgNotNull("value", value);
		return assertDate(value.orElse(null));
	}

	/**
	 * Used for assertion calls against {@link ZonedDateTime} objects.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the specified date is after the current date.</jc>
	 * 	<jsm>assertZonedDateTime</jsm>(<jv>byZdt</jv>).isAfterNow();
	 * </p>
	 *
	 * @param value The date being wrapped.
	 * @return A new {@link ZonedDateTimeAssertion} object.  Never <jk>null</jk>.
	 */
	public static ZonedDateTimeAssertion assertZonedDateTime(ZonedDateTime value) {
		return ZonedDateTimeAssertion.create(value);
	}

	/**
	 * Used for assertion calls against {@link ZonedDateTime} objects.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the specified date is after the current date.</jc>
	 * 	<jsm>assertZonedDateTime</jsm>(<jv>byZdt</jv>).isAfterNow();
	 * </p>
	 *
	 * @param value The date being wrapped.
	 * @return A new {@link ZonedDateTimeAssertion} object.  Never <jk>null</jk>.
	 */
	public static ZonedDateTimeAssertion assertZonedDateTime(Optional<ZonedDateTime> value) {
		assertArgNotNull("value", value);
		return assertZonedDateTime(value.orElse(null));
	}

	/**
	 * Used for assertion calls against integers.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the response status code is 200 or 404.</jc>
	 * 	<jsm>assertInteger</jsm>(<jv>httpReponse<jv>).isAny(200,404);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link IntegerAssertion} object.  Never <jk>null</jk>.
	 */
	public static IntegerAssertion assertInteger(Integer value) {
		return IntegerAssertion.create(value);
	}

	/**
	 * Used for assertion calls against integers.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the response status code is 200 or 404.</jc>
	 * 	<jsm>assertInteger</jsm>(<jv>httpReponse<jv>).isAny(200,404);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link IntegerAssertion} object.  Never <jk>null</jk>.
	 */
	public static IntegerAssertion assertInteger(Optional<Integer> value) {
		assertArgNotNull("value", value);
		return assertInteger(value.orElse(null));
	}

	/**
	 * Used for assertion calls against longs.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the response length isn't too long.</jc>
	 * 	<jsm>assertLong</jsm>(<jv>responseLength</jv>).isLessThan(100000);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link LongAssertion} object.  Never <jk>null</jk>.
	 */
	public static LongAssertion assertLong(Long value) {
		return LongAssertion.create(value);
	}

	/**
	 * Used for assertion calls against longs.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the response length isn't too long.</jc>
	 * 	<jsm>assertLong</jsm>(<jv>responseLength</jv>).isLessThan(100000);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link LongAssertion} object.  Never <jk>null</jk>.
	 */
	public static LongAssertion assertLong(Optional<Long> value) {
		assertArgNotNull("value", value);
		return assertLong(value.orElse(null));
	}

	/**
	 * Used for assertion calls against longs.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the response length isn't too long.</jc>
	 * 	<jsm>assertLong</jsm>(<jv>responseLength</jv>).isLessThan(100000);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link LongAssertion} object.  Never <jk>null</jk>.
	 */
	public static ComparableAssertion assertComparable(Comparable<?> value) {
		return ComparableAssertion.create(value);
	}

	/**
	 * Used for assertion calls against arbitrary POJOs.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the specified POJO is the specified type and serializes to the specified value.</jc>
	 * 	<jsm>assertObject</jsm>(<jv>myPojo</jv>).isType(MyBean.<jk>class</jk>).asJson().is(<js>"{foo:'bar'}"</js>);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link ObjectAssertion} object.  Never <jk>null</jk>.
	 */
	public static ObjectAssertion assertObject(Object value) {
		return ObjectAssertion.create(value);
	}

	/**
	 * Used for assertion calls against arbitrary POJOs.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the specified POJO is the specified type and serializes to the specified value.</jc>
	 * 	<jsm>assertObject</jsm>(<jv>myPojo</jv>).isType(MyBean.<jk>class</jk>).asJson().is(<js>"{foo:'bar'}"</js>);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link ObjectAssertion} object.  Never <jk>null</jk>.
	 */
	public static ObjectAssertion assertObject(Optional<?> value) {
		assertArgNotNull("value", value);
		return assertObject(value.orElse(null));
	}

	/**
	 * Used for assertion calls against Java beans.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the specified POJO is the specified type and serializes to the specified value.</jc>
	 * 	<jsm>assertBean</jsm>(<jv>myBean</jv>).isType(MyBean.<jk>class</jk>).fields(<js>"foo"</js>).asJson().is(<js>"{foo:'bar'}"</js>);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link BeanAssertion} object.  Never <jk>null</jk>.
	 */
	public static BeanAssertion assertBean(Object value) {
		return BeanAssertion.create(value);
	}

	/**
	 * Used for assertion calls against Java beans.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the specified POJO is the specified type and serializes to the specified value.</jc>
	 * 	<jsm>assertBean</jsm>(<jv>myBean</jv>).isType(MyBean.<jk>class</jk>).fields(<js>"foo"</js>).asJson().is(<js>"{foo:'bar'}"</js>);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link BeanAssertion} object.  Never <jk>null</jk>.
	 */
	public static BeanAssertion assertBean(Optional<?> value) {
		assertArgNotNull("value", value);
		return assertBean(value.orElse(null));
	}

	/**
	 * Used for assertion calls against string objects.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the response body of an HTTP call is the text "OK".</jc>
	 * 	<jsm>assertString</jsm>(<jv>httpBody</jv>).is(<js>"OK"</js>);
	 * </p>
	 *
	 * @param value The string being wrapped.
	 * @return A new {@link StringAssertion} object.  Never <jk>null</jk>.
	 */
	public static StringAssertion assertString(Object value) {
		return StringAssertion.create(value);
	}

	/**
	 * Used for assertion calls against string objects.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the response body of an HTTP call is the text "OK".</jc>
	 * 	<jsm>assertString</jsm>(<jv>httpBody</jv>).is(<js>"OK"</js>);
	 * </p>
	 *
	 * @param value The string being wrapped.
	 * @return A new {@link StringAssertion} object.  Never <jk>null</jk>.
	 */
	public static StringAssertion assertString(Optional<?> value) {
		assertArgNotNull("value", value);
		return assertString(value.orElse(null));
	}

	/**
	 * Used for assertion calls against boolean objects.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates that the specified boolean object exists and is true.</jc>
	 * 	<jsm>assertBoolean</jsm>(<jv>myBoolean</jv>).exists().isTrue();
	 * </p>
	 *
	 * @param value The boolean being wrapped.
	 * @return A new {@link BooleanAssertion} object.  Never <jk>null</jk>.
	 */
	public static BooleanAssertion assertBoolean(Boolean value) {
		return BooleanAssertion.create(value);
	}

	/**
	 * Used for assertion calls against boolean objects.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates that the specified boolean object exists and is true.</jc>
	 * 	<jsm>assertBoolean</jsm>(<jv>myBoolean</jv>).exists().isTrue();
	 * </p>
	 *
	 * @param value The boolean being wrapped.
	 * @return A new {@link BooleanAssertion} object.  Never <jk>null</jk>.
	 */
	public static BooleanAssertion assertBoolean(Optional<Boolean> value) {
		assertArgNotNull("value", value);
		return assertBoolean(value.orElse(null));
	}

	/**
	 * Used for assertion calls against throwable objects.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the throwable message or one of the parent messages contain 'Foobar'.</jc>
	 * 	<jsm>assertThrowable</jsm>(<jv>throwable</jv>).contains(<js>"Foobar"</js>);
	 * </p>
	 *
	 * @param value The throwable being wrapped.
	 * @return A new {@link ThrowableAssertion} object.  Never <jk>null</jk>.
	 */
	public static ThrowableAssertion assertThrowable(Throwable value) {
		return ThrowableAssertion.create(value);
	}

	/**
	 * Used for assertion calls against arrays.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	String[] <jv>array</jv> = <jk>new</jk> String[]{<js>"foo"</js>};
	 * 	<jsm>assertArray</jsm>(<jv>array</jv>).isSize(1);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link ArrayAssertion} object.  Never <jk>null</jk>.
	 */
	public static ArrayAssertion assertArray(Object value) {
		return ArrayAssertion.create(value);
	}

	/**
	 * Used for assertion calls against {@link Collection} objects.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	List=&lt;String&gt; <jv>list</jv> = AList.<jsm>of</jsm>(<js>"foo"</js>);
	 * 	<jsm>assertCollection</jsm>(<jv>list</jv>).isNotEmpty();
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link CollectionAssertion} object.  Never <jk>null</jk>.
	 */
	public static CollectionAssertion assertCollection(Collection<?> value) {
		return CollectionAssertion.create(value);
	}

	/**
	 * Used for assertion calls against {@link Collection} objects.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	List=&lt;String&gt; <jv>list</jv> = AList.<jsm>of</jsm>(<js>"foo"</js>);
	 * 	<jsm>assertList</jsm>(<jv>list</jv>).item(0).isEqual(<js>"foo"</js>);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link ListAssertion} object.  Never <jk>null</jk>.
	 */
	public static ListAssertion assertList(List<?> value) {
		return ListAssertion.create(value);
	}

	/**
	 * Used for assertion calls against maps.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the specified POJO is the specified type and contains the specified key.</jc>
	 * 	<jsm>assertMap</jsm>(<jv>myMap</jv>).isType(HashMap.<jk>class</jk>).containsKey(<js>"foo"</js>);
	 * </p>
	 *
	 * @param value The object being wrapped.
	 * @return A new {@link MapAssertion} object.  Never <jk>null</jk>.
	 */
	@SuppressWarnings("rawtypes")
	public static MapAssertion assertMap(Map value) {
		return MapAssertion.create(value);
	}

	/**
	 * Executes an arbitrary snippet of code and captures anything thrown from it.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Asserts that the specified method throws a RuntimeException containing "Foobar" in the message. </jc>
	 * 	<jsm>assertThrown</jsm>(()-&gt;<jv>foo</jv>.getBar())
	 * 		.exists()
	 * 		.isType(RuntimeException.<jk>class</jk>)
	 * 		.contains(<js>"Foobar"</js>);
	 * </p>
	 *
	 * @param snippet The snippet of code to execute.
	 * @return A new assertion object.  Never <jk>null</jk>.
	 */
	public static ThrowableAssertion assertThrown(Snippet snippet) {
		try {
			snippet.run();
		} catch (Throwable e) {
			return assertThrowable(e);
		}
		return assertThrowable(null);
	}

	/**
	 * Used for assertion calls against the contents of input streams.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates that the stream contains the string "foo".</jc>
	 * 	<jsm>assertStream</jsm>(<jv>myStream</jv>).asHex().is(<js>"666F6F"</js>);
	 * </p>
	 *
	 * @param value The input stream being wrapped.
	 * @return A new {@link ByteArrayAssertion} object.  Never <jk>null</jk>.
	 * @throws IOException If thrown while reading contents from stream.
	 */
	public static ByteArrayAssertion assertStream(InputStream value) throws IOException {
		return assertBytes(value == null ? null : IOUtils.readBytes(value));
	}

	/**
	 * Used for assertion calls against the contents of input streams.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates that the stream contains the string "foo".</jc>
	 * 	<jsm>assertStream</jsm>(<jv>myStream</jv>).asHex().is(<js>"666F6F"</js>);
	 * </p>
	 *
	 * @param value The input stream being wrapped.
	 * @return A new {@link ByteArrayAssertion} object.  Never <jk>null</jk>.
	 * @throws IOException If thrown while reading contents from stream.
	 */
	public static ByteArrayAssertion assertStream(Optional<InputStream> value) throws IOException {
		assertArgNotNull("value", value);
		return assertStream(value.orElse(null));
	}

	/**
	 * Used for assertion calls against byte arrays.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates that the byte array contains the string "foo".</jc>
	 * 	<jsm>assertBytes</jsm>(<jv>myBytes</jv>).asHex().is(<js>"666F6F"</js>);
	 * </p>
	 *
	 * @param value The byte array being wrapped.
	 * @return A new {@link ByteArrayAssertion} object.  Never <jk>null</jk>.
	 */
	public static ByteArrayAssertion assertBytes(byte[] value) {
		return ByteArrayAssertion.create(value);
	}

	/**
	 * Used for assertion calls against byte arrays.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates that the byte array contains the string "foo".</jc>
	 * 	<jsm>assertBytes</jsm>(<jv>myBytes</jv>).asHex().is(<js>"666F6F"</js>);
	 * </p>
	 *
	 * @param value The byte array being wrapped.
	 * @return A new {@link ByteArrayAssertion} object.  Never <jk>null</jk>.
	 */
	public static ByteArrayAssertion assertBytes(Optional<byte[]> value) {
		assertArgNotNull("value", value);
		return assertBytes(value.orElse(null));
	}

	/**
	 * Used for assertion calls against the contents of readers.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the throwable message or one of the parent messages contain 'Foobar'.</jc>
	 * 	<jsm>assertReader</jsm>(<jv>myReader</jv>).is(<js>"foo"</js>);
	 * </p>
	 *
	 * @param value The reader being wrapped.
	 * @return A new {@link StringAssertion} object.  Never <jk>null</jk>.
	 * @throws IOException If thrown while reading contents from reader.
	 */
	public static StringAssertion assertReader(Reader value) throws IOException {
		return assertString(value == null ? null : IOUtils.read(value));
	}

	/**
	 * Used for assertion calls against the contents of readers.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Validates the throwable message or one of the parent messages contain 'Foobar'.</jc>
	 * 	<jsm>assertReader</jsm>(<jv>myReader</jv>).is(<js>"foo"</js>);
	 * </p>
	 *
	 * @param value The reader being wrapped.
	 * @return A new {@link StringAssertion} object.  Never <jk>null</jk>.
	 * @throws IOException If thrown while reading contents from reader.
	 */
	public static StringAssertion assertReader(Optional<Reader> value) throws IOException {
		assertArgNotNull("value", value);
		return assertReader(value.orElse(null));
	}

	/**
	 * Throws an {@link IllegalArgumentException} if the specified argument is <jk>null</jk>.
	 *
	 * @param <T> The argument data type.
	 * @param arg The argument name.
	 * @param o The object to check.
	 * @return The same argument.
	 * @throws IllegalArgumentException Constructed exception.
	 */
	public static <T> T assertArgNotNull(String arg, T o) throws IllegalArgumentException {
		if (o == null)
			throw new BasicIllegalArgumentException("Argument ''{0}'' cannot be null", arg);
		return o;
	}
}
