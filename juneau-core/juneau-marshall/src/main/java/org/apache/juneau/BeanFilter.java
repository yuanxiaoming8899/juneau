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
package org.apache.juneau;

import java.beans.*;
import java.util.*;

import static org.apache.juneau.internal.ClassUtils.*;
import static org.apache.juneau.internal.StringUtils.*;

import org.apache.juneau.annotation.*;
import org.apache.juneau.collections.*;
import org.apache.juneau.swap.*;

/**
 * Parent class for all bean filters.
 *
 * <p>
 * Bean filters are used to control aspects of how beans are handled during serialization and parsing.
 *
 * <p>
 * Bean filters are created by {@link Builder} which is the programmatic equivalent to the {@link Bean @Bean}
 * annotation.
 */
public final class BeanFilter {

	//-----------------------------------------------------------------------------------------------------------------
	// Static
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Create a new builder for this object.
	 *
	 * @param <T> The bean class being filtered.
	 * @param beanClass The bean class being filtered.
	 * @return A new builder.
	 */
	public static <T> Builder create(Class<T> beanClass) {
		return new Builder(beanClass);
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Builder
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Builder class.
	 */
	public static class Builder {

		Class<?> beanClass;
		String typeName, example;
		ASet<String>
			properties = ASet.of(),
			excludeProperties = ASet.of(),
			readOnlyProperties = ASet.of(),
			writeOnlyProperties = ASet.of();
		Class<?> implClass, interfaceClass, stopClass;
		boolean sortProperties, fluentSetters;
		Object propertyNamer;
		List<Class<?>> dictionary;
		Object interceptor;

		/**
		 * Constructor.
		 *
		 * @param beanClass The bean class that this filter applies to.
		 */
		protected Builder(Class<?> beanClass) {
			this.beanClass = beanClass;
		}

		/**
		 * Applies the information in the specified list of {@link Bean @Bean} annotations to this filter.
		 *
		 * @param annotations The annotations to apply.
		 * @return This object.
		 */
		public Builder applyAnnotations(List<Bean> annotations) {

			for (Bean b : annotations) {

				if (! (b.properties().isEmpty() && b.p().isEmpty()))
					properties(b.properties(), b.p());

				if (! b.typeName().isEmpty())
					typeName(b.typeName());

				if (b.sort())
					sortProperties(true);

				if (b.findFluentSetters())
					findFluentSetters();

				if (! (b.excludeProperties().isEmpty() && b.xp().isEmpty()))
					excludeProperties(b.excludeProperties(), b.xp());

				if (! (b.readOnlyProperties().isEmpty() && b.ro().isEmpty()))
					readOnlyProperties(b.readOnlyProperties(), b.ro());

				if (! (b.writeOnlyProperties().isEmpty() && b.wo().isEmpty()))
					writeOnlyProperties(b.writeOnlyProperties(), b.wo());

				if (b.propertyNamer() != BasicPropertyNamer.class)
					propertyNamer(b.propertyNamer());

				if (b.interfaceClass() != Null.class)
					interfaceClass(b.interfaceClass());

				if (b.stopClass() != Null.class)
					stopClass(b.stopClass());

				if (b.dictionary().length > 0)
					dictionary(b.dictionary());

				if (b.interceptor() != BeanInterceptor.Default.class)
					interceptor(b.interceptor());

				if (b.implClass() != Null.class)
					implClass(b.implClass());

				if (! b.example().isEmpty())
					example(b.example());
			}
			return this;
		}

		/**
		 * Configuration property:  Bean dictionary type name.
		 *
		 * <p>
		 * Specifies the dictionary type name for this bean.
		 *
		 * <h5 class='section'>Example:</h5>
		 * <p class='bcode w800'>
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> MyFilter <jk>extends</jk> Builder&lt;MyBean&gt; {
		 * 		<jk>public</jk> MyFilter() {
		 * 			typeName(<js>"mybean"</js>);
		 * 		}
		 * 	}
		 *
		 * 	<jc>// Register it with a serializer or parser.</jc>
		 * 	WriterSerializer s = JsonSerializer
		 * 		.<jsm>create</jsm>()
		 * 		.beanFilters(MyFilter.<jk>class</jk>)
		 * 		.build();
		 *
		 * 	<jc>// Produces:  "{_type:'mybean', ...}"</jc>
		 * 	String json = s.serialize(<jk>new</jk> MyBean());
		 * </p>
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#typeName()}
		 * </ul>
		 *
		 * @param value The new value for this setting.
		 * @return This object.
		 */
		public Builder typeName(String value) {
			this.typeName = value;
			return this;
		}

		/**
		 * Bean implementation class.
		 *
		 * @param value The new value for this setting.
		 * @return This object.
		 */
		public Builder implClass(Class<?> value) {
			this.implClass = value;
			return this;
		}

		/**
		 * Bean interface class.
		 *
		 * Identifies a class to be used as the interface class for this and all subclasses.
		 *
		 * <p>
		 * When specified, only the list of properties defined on the interface class will be used during serialization.
		 * <br>Additional properties on subclasses will be ignored.
		 *
		 * <p class='bcode w800'>
		 * 	<jc>// Parent class</jc>
		 * 	<jk>public abstract class</jk> A {
		 * 		<jk>public</jk> String <jf>f0</jf> = <js>"f0"</js>;
		 * 	}
		 *
		 * 	<jc>// Sub class</jc>
		 * 	<jk>public class</jk> A1 <jk>extends</jk> A {
		 * 		<jk>public</jk> String <jf>f1</jf> = <js>"f1"</js>;
		 * 	}
		 *
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> AFilter <jk>extends</jk> Builder&lt;A&gt; {
		 * 		<jk>public</jk> AFilter() {
		 * 			interfaceClass(A.<jk>class</jk>);
		 * 		}
		 * 	}
		 *
		 * 	<jc>// Register it with a serializer.</jc>
		 * 	WriterSerializer s = JsonSerializer
		 * 		.<jsm>create</jsm>()
		 * 		.beanFilters(AFilter.<jk>class</jk>)
		 * 		.build();
		 *
		 * 	<jc>// Use it.</jc>
		 * 	A1 a1 = <jk>new</jk> A1();
		 * 	String r = s.serialize(a1);
		 * 	<jsm>assertEquals</jsm>(<js>"{f0:'f0'}"</js>, r);  <jc>// Note f1 is not serialized</jc>
		 * </p>
		 *
		 * <p>
		 * Note that this filter can be used on the parent class so that it filters to all child classes, or can be set
		 * individually on the child classes.
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#interfaceClass()}
		 * </ul>
		 *
		 * @param value The new value for this setting.
		 * @return This object.
		 */
		public Builder interfaceClass(Class<?> value) {
			this.interfaceClass = value;
			return this;
		}

		/**
		 * Configuration property:  Bean stop class.
		 *
		 * <p>
		 * Identifies a stop class for this class and all subclasses.
		 *
		 * <p>
		 * Identical in purpose to the stop class specified by {@link Introspector#getBeanInfo(Class, Class)}.
		 * <br>Any properties in the stop class or in its base classes will be ignored during analysis.
		 *
		 * <p>
		 * For example, in the following class hierarchy, instances of <c>C3</c> will include property <c>p3</c>,
		 * but not <c>p1</c> or <c>p2</c>.
		 *
		 * <h5 class='section'>Example:</h5>
		 * <p class='bcode w800'>
		 * 	<jk>public class</jk> C1 {
		 * 		<jk>public int</jk> getP1();
		 * 	}
		 *
		 * 	<jk>public class</jk> C2 <jk>extends</jk> C1 {
		 * 		<jk>public int</jk> getP2();
		 * 	}
		 *
		 * 	<jk>public class</jk> C3 <jk>extends</jk> C2 {
		 * 		<jk>public int</jk> getP3();
		 * 	}
		 *
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> C3Filter <jk>extends</jk> Builder&lt;C3&gt; {
		 * 		<jk>public</jk> C3Filter() {
		 * 			stopClass(C2.<jk>class</jk>);
		 * 		}
		 * 	}
		 *
		 * 	<jc>// Register it with a serializer.</jc>
		 * 	WriterSerializer s = JsonSerializer
		 * 		.<jsm>create</jsm>()
		 * 		.beanFilters(C3Filter.<jk>class</jk>)
		 * 		.build();
		 *
		 * 	<jc>// Serializes property 'p3', but NOT 'p1' or 'p2'.</jc>
		 * 	String json = s.serialize(<jk>new</jk> C3());
		 * </p>
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#stopClass()}
		 * </ul>
		 *
		 * @param value The new value for this setting.
		 * @return This object.
		 */
		public Builder stopClass(Class<?> value) {
			this.stopClass = value;
			return this;
		}

		/**
		 * Configuration property:  Sort bean properties.
		 *
		 * <p>
		 * When <jk>true</jk>, all bean properties will be serialized and access in alphabetical order.
		 * <br>Otherwise, the natural order of the bean properties is used which is dependent on the JVM vendor.
		 *
		 * <h5 class='section'>Example:</h5>
		 * <p class='bcode w800'>
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> MyFilter <jk>extends</jk> Builder&lt;MyBean&gt; {
		 * 		<jk>public</jk> MyFilter() {
		 * 			sortProperties();
		 * 		}
		 * 	}
		 *
		 * 	<jc>// Register it with a serializer.</jc>
		 * 	WriterSerializer s = JsonSerializer
		 * 		.<jsm>create</jsm>()
		 * 		.beanFilters(MyFilter.<jk>class</jk>)
		 * 		.build();
		 *
		 * 	<jc>// Properties will be sorted alphabetically.</jc>
		 * 	String json = s.serialize(<jk>new</jk> MyBean());
		 * </p>
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#sort()}
		 * 	<li class='jf'>{@link BeanContext.Builder#sortProperties()}
		 * </ul>
		 *
		 * @param value
		 * 	The new value for this property.
		 * 	<br>The default is <jk>false</jk>.
		 * @return This object.
		 */
		public Builder sortProperties(boolean value) {
			this.sortProperties = value;
			return this;
		}

		/**
		 * Configuration property:  Sort bean properties.
		 *
		 * <p>
		 * Shortcut for calling <code>sortProperties(<jk>true</jk>)</code>.
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#sort()}
		 * 	<li class='jf'>{@link BeanContext.Builder#sortProperties()}
		 * </ul>
		 *
		 * @return This object.
		 */
		public Builder sortProperties() {
			this.sortProperties = true;
			return this;
		}

		/**
		 * Configuration property:  Find fluent setters.
		 *
		 * <p>
		 * When enabled, fluent setters are detected on beans.
		 *
		 * <p>
		 * Fluent setters must have the following attributes:
		 * <ul>
		 * 	<li>Public.
		 * 	<li>Not static.
		 * 	<li>Take in one parameter.
		 * 	<li>Return the bean itself.
		 * </ul>
		 *
		 * <h5 class='section'>Example:</h5>
		 * <p class='bcode w800'>
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> MyFilter <jk>extends</jk> Builder&lt;MyBean&gt; {
		 * 		<jk>public</jk> MyFilter() {
		 * 			findFluentSetters();
		 * 		}
		 * 	}
		 * </p>
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#findFluentSetters()}
		 * 	<li class='jm'>{@link BeanContext.Builder#findFluentSetters()}
		 * </ul>
		 *
		 * @return This object.
		 */
		public Builder findFluentSetters() {
			this.fluentSetters = true;
			return this;
		}

		/**
		 * Configuration property:  Bean property namer
		 *
		 * <p>
		 * The class to use for calculating bean property names.
		 *
		 * <h5 class='section'>Example:</h5>
		 * <p class='bcode w800'>
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> MyFilter <jk>extends</jk> Builder&lt;MyBean&gt; {
		 * 		<jk>public</jk> MyFilter() {
		 * 			<jc>// Use Dashed-Lower-Case property names.</jc>
		 * 			<jc>// (e.g. "foo-bar-url" instead of "fooBarURL")</jc>
		 * 			propertyNamer(PropertyNamerDLC.<jk>class</jk>);
		 * 		}
		 * 	}
		 *
		 * 	<jc>// Register it with a serializer or parser.</jc>
		 * 	WriterSerializer s = JsonSerializer
		 * 		.<jsm>create</jsm>()
		 * 		.beanFilters(MyFilter.<jk>class</jk>)
		 * 		.build();
		 *
		 * 	<jc>// Properties names will be Dashed-Lower-Case.</jc>
		 * 	String json = s.serialize(<jk>new</jk> MyBean());
		 * </p>
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#propertyNamer()}
		 * 	<li class='jm'>{@link BeanContext.Builder#propertyNamer(Class)}
		 * 	<li class='jc'>{@link PropertyNamer}
		 * </ul>
		 *
		 * @param value
		 * 	The new value for this setting.
		 * 	<br>The default is {@link BasicPropertyNamer}.
		 * @return This object.
		 */
		public Builder propertyNamer(Class<? extends PropertyNamer> value) {
			this.propertyNamer = value;
			return this;
		}

		/**
		 * Configuration property:  Bean property includes.
		 *
		 * <p>
		 * Specifies the set and order of names of properties associated with the bean class.
		 *
		 * <h5 class='section'>Example:</h5>
		 * <p class='bcode w800'>
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> MyFilter <jk>extends</jk> Builder&lt;MyBean&gt; {
		 * 		<jk>public</jk> MyFilter() {
		 * 			bpi(<js>"foo,bar,baz"</js>);
		 * 		}
		 * 	}
		 *
		 * 	<jc>// Register it with a serializer.</jc>
		 * 	WriterSerializer s = JsonSerializer
		 * 		.<jsm>create</jsm>()
		 * 		.beanFilters(MyFilter.<jk>class</jk>)
		 * 		.build();
		 *
		 * 	<jc>// Only serializes the properties 'foo', 'bar', and 'baz'.</jc>
		 * 	String json = s.serialize(<jk>new</jk> MyBean());
		 * </p>
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#properties()}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanProperties(Class, String)}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanProperties(String, String)}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanProperties(Map)}
		 * </ul>
		 *
		 * @param value
		 * 	The new value for this setting.
		 * 	<br>Values can contain comma-delimited list of property names.
		 * @return This object.
		 */
		public Builder properties(String...value) {
			this.properties = ASet.of();
			for (String v : value)
				if (!v.isEmpty())
					properties.a(split(v));
			return this;
		}

		/**
		 * Configuration property:  Bean property excludes.
		 *
		 * <p>
		 * Specifies properties to exclude from the bean class.
		 *
		 * <h5 class='section'>Example:</h5>
		 * <p class='bcode w800'>
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> MyFilter <jk>extends</jk> Builder&lt;MyBean&gt; {
		 * 		<jk>public</jk> MyFilter() {
		 * 			bpx(<js>"foo,bar"</js>);
		 * 		}
		 * 	}
		 *
		 * 	<jc>// Register it with a serializer.</jc>
		 * 	WriterSerializer s = JsonSerializer
		 * 		.<jsm>create</jsm>()
		 * 		.beanFilters(MyFilter.<jk>class</jk>)
		 * 		.build();
		 *
		 * 	<jc>// Serializes all properties except for 'foo' and 'bar'.</jc>
		 * 	String json = s.serialize(<jk>new</jk> MyBean());
		 * </p>
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#excludeProperties()}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanPropertiesExcludes(Class, String)}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanPropertiesExcludes(String, String)}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanPropertiesExcludes(Map)}
		 * </ul>
		 *
		 * @param value
		 * 	The new value for this setting.
		 * 	<br>Values can contain comma-delimited list of property names.
		 * @return This object.
		 */
		public Builder excludeProperties(String...value) {
			this.excludeProperties = ASet.of();
			for (String v : value)
				if (! v.isEmpty())
					excludeProperties.a(split(v));
			return this;
		}

		/**
		 * Configuration property:  Read-only bean properties.
		 *
		 * <p>
		 * Specifies one or more properties on a bean that are read-only despite having valid getters.
		 * Serializers will serialize such properties as usual, but parsers will silently ignore them.
		 *
		 * <h5 class='section'>Example:</h5>
		 * <p class='bcode w800'>
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> MyFilter <jk>extends</jk> Builder&lt;MyBean&gt; {
		 * 		<jk>public</jk> MyFilter() {
		 * 			bpro(<js>"foo,bar"</js>);
		 * 		}
		 * 	}
		 *
		 * 	<jc>// Register it with a parser.</jc>
		 *  ReaderParser p = JsonParser
		 * 		.<jsm>create</jsm>()
		 * 		.beanFilters(MyFilter.<jk>class</jk>)
		 * 		.build();
		 *
		 * 	<jc>// Parsers all properties except for 'foo' and 'bar'.</jc>
		 * 	MyBean b = p.parse(<js>"..."</js>, MyBean.<jk>class</jk>);
		 * </p>
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#readOnlyProperties()}
		 * 	<li class='ja'>{@link Beanp#ro()}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanPropertiesReadOnly(Class, String)}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanPropertiesReadOnly(String, String)}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanPropertiesReadOnly(Map)}
		 * </ul>
		 *
		 * @param value
		 * 	The new value for this setting.
		 * 	<br>Values can contain comma-delimited list of property names.
		 * @return This object.
		 */
		public Builder readOnlyProperties(String...value) {
			this.readOnlyProperties = ASet.of();
			for (String v : value)
				if (! v.isEmpty())
					readOnlyProperties.a(split(v));
			return this;
		}

		/**
		 * Configuration property:  Write-only bean properties.
		 *
		 * <p>
		 * Specifies one or more properties on a bean that are write-only despite having valid setters.
		 * Parsers will parse such properties as usual, but serializers will silently ignore them.
		 *
		 * <h5 class='section'>Example:</h5>
		 * <p class='bcode w800'>
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> MyFilter <jk>extends</jk> Builder&lt;MyBean&gt; {
		 * 		<jk>public</jk> MyFilter() {
		 * 			bpwo(<js>"foo,bar"</js>);
		 * 		}
		 * 	}
		 *
		 * 	<jc>// Register it with a serializer.</jc>
		 *  WriterSerializer s = JsonSerializer
		 * 		.<jsm>create</jsm>()
		 * 		.beanFilters(MyFilter.<jk>class</jk>)
		 * 		.build();
		 *
		 * 	<jc>// Serializes all properties except for 'foo' and 'bar'.</jc>
		 * 	String json = s.serialize(<jk>new</jk> MyBean());
		 * </p>
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#writeOnlyProperties()}
		 * 	<li class='ja'>{@link Beanp#wo()}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanPropertiesWriteOnly(Class, String)}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanPropertiesWriteOnly(String, String)}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanPropertiesWriteOnly(Map)}
		 * </ul>
		 *
		 * @param value
		 * 	The new value for this setting.
		 * 	<br>Values can contain comma-delimited list of property names.
		 * @return This object.
		 */
		public Builder writeOnlyProperties(String...value) {
			this.writeOnlyProperties = ASet.of();
			for (String v : value)
				if (! v.isEmpty())
					writeOnlyProperties.a(split(v));
			return this;
		}

		/**
		 * Configuration property:  Bean dictionary.
		 *
		 * <p>
		 * Adds to the list of classes that make up the bean dictionary for this bean.
		 *
		 * <h5 class='section'>Example:</h5>
		 * <p class='bcode w800'>
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> MyFilter <jk>extends</jk> Builder&lt;MyBean&gt; {
		 * 		<jk>public</jk> MyFilter() {
		 * 			<jc>// Our bean contains generic collections of Foo and Bar objects.</jc>
		 * 			beanDictionary(Foo.<jk>class</jk>, Bar.<jk>class</jk>);
		 * 		}
		 * 	}
		 *
		 * 	<jc>// Register it with a parser.</jc>
		 * 	ReaderParser p = JsonParser
		 * 		.<jsm>create</jsm>()
		 * 		.beanFilters(MyFilter.<jk>class</jk>)
		 * 		.build();
		 *
		 * 	<jc>// Instantiate our bean.</jc>
		 * 	MyBean myBean = p.parse(json);
		 * </p>
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#dictionary()}
		 * 	<li class='jm'>{@link BeanContext.Builder#beanDictionary(Class...)}
		 * </ul>
		 *
		 * @param values
		 * 	The values to add to this property.
		 * @return This object.
		 */
		public Builder dictionary(Class<?>...values) {
			if (dictionary == null)
				dictionary = Arrays.asList(values);
			else for (Class<?> cc : values)
				dictionary.add(cc);
			return this;
		}

		/**
		 * Example.
		 *
		 * @param value
		 * 	The new value for this property.
		 * @return This object.
		 */
		public Builder example(String value) {
			this.example = value;
			return this;
		}

		/**
		 * Configuration property:  Bean interceptor.
		 *
		 * <p>
		 * The interceptor to use for intercepting and altering getter and setter calls.
		 *
		 * <h5 class='section'>Example:</h5>
		 * <p class='bcode w800'>
		 * 	<jc>// Define our filter.</jc>
		 * 	<jk>public class</jk> MyFilter <jk>extends</jk> Builder&lt;MyBean&gt; {
		 * 		<jk>public</jk> MyFilter() {
		 * 			<jc>// Our bean contains generic collections of Foo and Bar objects.</jc>
		 * 			interceptor(AddressInterceptor.<jk>class</jk>);
		 * 		}
		 * 	}
		 *
		 * 	<jc>// Register it with a serializer or parser.</jc>
		 * 	WriterSerializer s = JsonSerializer
		 * 		.<jsm>create</jsm>()
		 * 		.beanFilters(MyFilter.<jk>class</jk>)
		 * 		.build();
		 * </p>
		 *
		 * <ul class='seealso'>
		 * 	<li class='ja'>{@link Bean#interceptor()}
		 * 	<li class='jc'>{@link BeanInterceptor}
		 * </ul>
		 *
		 * @param value
		 * 	The new value for this setting.
		 * 	<br>The default value is {@link BeanInterceptor}.
		 * @return This object.
		 */
		public Builder interceptor(Class<?> value) {
			this.interceptor = value;
			return this;
		}

		/**
		 * Creates a {@link BeanFilter} with settings in this builder class.
		 *
		 * @return A new {@link BeanFilter} instance.
		 */
		public BeanFilter build() {
			return new BeanFilter(this);
		}
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Instance
	//-----------------------------------------------------------------------------------------------------------------

	private final Class<?> beanClass;
	private final Set<String> properties, excludeProperties, readOnlyProperties, writeOnlyProperties;
	private final PropertyNamer propertyNamer;
	private final Class<?> implClass, interfaceClass, stopClass;
	private final boolean sortProperties, fluentSetters;
	private final String typeName, example;
	private final Class<?>[] beanDictionary;
	@SuppressWarnings("rawtypes")
	private final BeanInterceptor interceptor;

	/**
	 * Constructor.
	 */
	BeanFilter(Builder builder) {
		this.beanClass = builder.beanClass;
		this.typeName = builder.typeName;
		this.properties = new LinkedHashSet<>(builder.properties);
		this.excludeProperties = new LinkedHashSet<>(builder.excludeProperties);
		this.readOnlyProperties = new LinkedHashSet<>(builder.readOnlyProperties);
		this.writeOnlyProperties = new LinkedHashSet<>(builder.writeOnlyProperties);
		this.example = builder.example;
		this.implClass = builder.implClass;
		this.interfaceClass = builder.interfaceClass;
		this.stopClass = builder.stopClass;
		this.sortProperties = builder.sortProperties;
		this.fluentSetters = builder.fluentSetters;
		this.propertyNamer = castOrCreate(PropertyNamer.class, builder.propertyNamer);
		this.beanDictionary =
			builder.dictionary == null
			? null
			: builder.dictionary.toArray(new Class<?>[builder.dictionary.size()]);
		this.interceptor =
			builder.interceptor == null
			? BeanInterceptor.DEFAULT
			: castOrCreate(BeanInterceptor.class, builder.interceptor);
	}

	/**
	 * Returns the bean class that this filter applies to.
	 *
	 * @return The bean class that this filter applies to.
	 */
	public Class<?> getBeanClass() {
		return beanClass;
	}

	/**
	 * Returns the dictionary name associated with this bean.
	 *
	 * @return The dictionary name associated with this bean, or <jk>null</jk> if no name is defined.
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Returns the bean dictionary defined on this bean.
	 *
	 * @return The bean dictionary defined on this bean, or <jk>null</jk> if no bean dictionary is defined.
	 */
	public Class<?>[] getBeanDictionary() {
		return beanDictionary;
	}

	/**
	 * Returns the set and order of names of properties associated with a bean class.
	 *
	 * @return
	 * 	The names of the properties associated with a bean class, or and empty set if all bean properties should
	 * 	be used.
	 */
	public Set<String> getProperties() {
		return properties;
	}

	/**
	 * Returns the list of properties to ignore on a bean.
	 *
	 * @return The names of the properties to ignore on a bean, or an empty set to not ignore any properties.
	 */
	public Set<String> getExcludeProperties() {
		return excludeProperties;
	}

	/**
	 * Returns the list of read-only properties on a bean.
	 *
	 * @return The names of the read-only properties on a bean, or an empty set to not have any read-only properties.
	 */
	public Set<String> getReadOnlyProperties() {
		return readOnlyProperties;
	}

	/**
	 * Returns the list of write-only properties on a bean.
	 *
	 * @return The names of the write-only properties on a bean, or an empty set to not have any write-only properties.
	 */
	public Set<String> getWriteOnlyProperties() {
		return writeOnlyProperties;
	}

	/**
	 * Returns <jk>true</jk> if the properties defined on this bean class should be ordered alphabetically.
	 *
	 * <p>
	 * This method is only used when the {@link #getProperties()} method returns <jk>null</jk>.
	 * Otherwise, the ordering of the properties in the returned value is used.
	 *
	 * @return <jk>true</jk> if bean properties should be sorted.
	 */
	public boolean isSortProperties() {
		return sortProperties;
	}

	/**
	 * Returns <jk>true</jk> if we should find fluent setters.
	 *
	 * @return <jk>true</jk> if fluent setters should be found.
	 */
	public boolean isFluentSetters() {
		return fluentSetters;
	}

	/**
	 * Returns the {@link PropertyNamer} associated with the bean to tailor the names of bean properties.
	 *
	 * @return The property namer class, or <jk>null</jk> if no property namer is associated with this bean property.
	 */
	public PropertyNamer getPropertyNamer() {
		return propertyNamer;
	}

	/**
	 * Returns the implementation class associated with this class.
	 *
	 * @return The implementation class associated with this class, or <jk>null</jk> if no implementation class is associated.
	 */
	public Class<?> getImplClass() {
		return implClass;
	}

	/**
	 * Returns the interface class associated with this class.
	 *
	 * @return The interface class associated with this class, or <jk>null</jk> if no interface class is associated.
	 */
	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}

	/**
	 * Returns the stop class associated with this class.
	 *
	 * @return The stop class associated with this class, or <jk>null</jk> if no stop class is associated.
	 */
	public Class<?> getStopClass() {
		return stopClass;
	}

	/**
	 * Returns the example associated with this class.
	 *
	 * @return The example associated with this class, or <jk>null</jk> if no example is associated.
	 */
	public String getExample() {
		return example;
	}

	/**
	 * Calls the {@link BeanInterceptor#readProperty(Object, String, Object)} method on the registered property filters.
	 *
	 * @param bean The bean from which the property was read.
	 * @param name The property name.
	 * @param value The value just extracted from calling the bean getter.
	 * @return The value to serialize.  Default is just to return the existing value.
	 */
	@SuppressWarnings("unchecked")
	public Object readProperty(Object bean, String name, Object value) {
		return interceptor.readProperty(bean, name, value);
	}

	/**
	 * Calls the {@link BeanInterceptor#writeProperty(Object, String, Object)} method on the registered property filters.
	 *
	 * @param bean The bean from which the property was read.
	 * @param name The property name.
	 * @param value The value just parsed.
	 * @return The value to serialize.  Default is just to return the existing value.
	 */
	@SuppressWarnings("unchecked")
	public Object writeProperty(Object bean, String name, Object value) {
		return interceptor.writeProperty(bean, name, value);
	}
}
