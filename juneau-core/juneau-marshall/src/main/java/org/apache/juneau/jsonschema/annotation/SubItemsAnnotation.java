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
package org.apache.juneau.jsonschema.annotation;

import static org.apache.juneau.internal.ArrayUtils.*;
import static org.apache.juneau.jsonschema.SchemaUtils.*;

import java.lang.annotation.*;

import org.apache.juneau.*;
import org.apache.juneau.annotation.*;
import org.apache.juneau.collections.*;
import org.apache.juneau.parser.*;
import org.apache.juneau.svl.*;

/**
 * Utility classes and methods for the {@link SubItems @SubItems} annotation.
 */
public class SubItemsAnnotation {

	//-----------------------------------------------------------------------------------------------------------------
	// Static
	//-----------------------------------------------------------------------------------------------------------------

	/** Default value */
	public static final SubItems DEFAULT = create().build();

	/**
	 * Instantiates a new builder for this class.
	 *
	 * @return A new builder object.
	 */
	public static Builder create() {
		return new Builder();
	}

	/**
	 * Creates a copy of the specified annotation.
	 *
	 * @param a The annotation to copy.
	 * @param r The var resolver for resolving any variables.
	 * @return A copy of the specified annotation.
	 */
	public static SubItems copy(SubItems a, VarResolverSession r) {
		return
			create()
			._default(r.resolve(a._default()))
			._enum(r.resolve(a._enum()))
			.$ref(r.resolve(a.$ref()))
			.cf(r.resolve(a.cf()))
			.collectionFormat(r.resolve(a.collectionFormat()))
			.df(r.resolve(a.df()))
			.e(r.resolve(a.e()))
			.emax(a.emax())
			.emin(a.emin())
			.exclusiveMaximum(a.exclusiveMaximum())
			.exclusiveMinimum(a.exclusiveMinimum())
			.f(r.resolve(a.f()))
			.format(r.resolve(a.format()))
			.items(r.resolve(a.items()))
			.max(r.resolve(a.max()))
			.maxi(a.maxi())
			.maximum(r.resolve(a.maximum()))
			.maxItems(a.maxItems())
			.maxl(a.maxLength())
			.maxLength(a.maxLength())
			.min(r.resolve(a.min()))
			.mini(a.mini())
			.minimum(r.resolve(a.minimum()))
			.minItems(a.minItems())
			.minl(a.minl())
			.minLength(a.minLength())
			.mo(r.resolve(a.mo()))
			.multipleOf(r.resolve(a.mo()))
			.p(r.resolve(a.p()))
			.pattern(r.resolve(a.pattern()))
			.t(r.resolve(a.t()))
			.type(r.resolve(a.type()))
			.ui(a.ui())
			.uniqueItems(a.uniqueItems())
			.value(r.resolve(a.value()))
			.build();
	}

	/**
	 * Returns <jk>true</jk> if the specified annotation contains all default values.
	 *
	 * @param a The annotation to check.
	 * @return <jk>true</jk> if the specified annotation contains all default values.
	 */
	public static boolean empty(org.apache.juneau.jsonschema.annotation.SubItems a) {
		return a == null || DEFAULT.equals(a);
	}

	/**
	 * Merges the contents of the specified annotation into the specified generic map.
	 *
	 * @param om The map to copy the contents to.
	 * @param a The annotation to apply.
	 * @return The same map with the annotation contents applied.
	 * @throws ParseException Invalid JSON found in value.
	 */
	public static OMap merge(OMap om, SubItems a) throws ParseException {
		if (SubItemsAnnotation.empty(a))
			return om;
		if (a.value().length > 0)
			om.putAll(parseMap(a.value()));
		return om
			.appendSkipEmpty("collectionFormat", a.collectionFormat(), a.cf())
			.appendSkipEmpty("default", joinnl(a._default(), a.df()))
			.appendSkipEmpty("enum", parseSet(a._enum()), parseSet(a.e()))
			.appendSkipFalse("exclusiveMaximum", a.exclusiveMaximum() || a.emax())
			.appendSkipFalse("exclusiveMinimum", a.exclusiveMinimum() || a.emin())
			.appendSkipEmpty("format", a.format(), a.f())
			.appendSkipEmpty("items", parseMap(a.items()))
			.appendSkipEmpty("maximum", a.maximum(), a.max())
			.appendSkipMinusOne("maxItems", a.maxItems(), a.maxi())
			.appendSkipMinusOne("maxLength", a.maxLength(), a.maxl())
			.appendSkipEmpty("minimum", a.minimum(), a.min())
			.appendSkipMinusOne("minItems", a.minItems(), a.mini())
			.appendSkipMinusOne("minLength", a.minLength(), a.minl())
			.appendSkipEmpty("multipleOf", a.multipleOf(), a.mo())
			.appendSkipEmpty("pattern", a.pattern(), a.p())
			.appendSkipEmpty("type", a.type(), a.t())
			.appendSkipFalse("uniqueItems", a.uniqueItems() || a.ui())
			.appendSkipEmpty("$ref", a.$ref())
		;
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Builder
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Builder class.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jm'>{@link BeanContextBuilder#annotations(Annotation...)}
	 * </ul>
	 */
	public static class Builder extends AnnotationBuilder {

		String $ref="", cf="", collectionFormat="", f="", format="", max="", maximum="", min="", minimum="", mo="", multipleOf="", p="", pattern="", t="", type="";
		long maxItems=-1, maxLength=-1, maxi=-1, maxl=-1, minItems=-1, minLength=-1, mini=-1, minl=-1;
		boolean emax, emin, exclusiveMaximum, exclusiveMinimum, ui, uniqueItems;
		String[] _default={}, _enum={}, df={}, e={}, items={}, value={};

		/**
		 * Constructor.
		 */
		public Builder() {
			super(SubItems.class);
		}

		/**
		 * Instantiates a new {@link SubItems @SubItems} object initialized with this builder.
		 *
		 * @return A new {@link SubItems @SubItems} object.
		 */
		public SubItems build() {
			return new Impl(this);
		}

		/**
		 * Sets the <c>_default</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder _default(String...value) {
			this._default = value;
			return this;
		}

		/**
		 * Sets the <c>_enum</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder _enum(String...value) {
			this._enum = value;
			return this;
		}

		/**
		 * Sets the <c>$ref</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder $ref(String value) {
			this.$ref = value;
			return this;
		}

		/**
		 * Sets the <c>cf</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder cf(String value) {
			this.cf = value;
			return this;
		}

		/**
		 * Sets the <c>collectionFormat</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder collectionFormat(String value) {
			this.collectionFormat = value;
			return this;
		}

		/**
		 * Sets the <c>df</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder df(String...value) {
			this.df = value;
			return this;
		}

		/**
		 * Sets the <c>e</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder e(String...value) {
			this.e = value;
			return this;
		}

		/**
		 * Sets the <c>emax</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder emax(boolean value) {
			this.emax = value;
			return this;
		}

		/**
		 * Sets the <c>emin</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder emin(boolean value) {
			this.emin = value;
			return this;
		}

		/**
		 * Sets the <c>exclusiveMaximum</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder exclusiveMaximum(boolean value) {
			this.exclusiveMaximum = value;
			return this;
		}

		/**
		 * Sets the <c>exclusiveMinimum</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder exclusiveMinimum(boolean value) {
			this.exclusiveMinimum = value;
			return this;
		}

		/**
		 * Sets the <c>f</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder f(String value) {
			this.f = value;
			return this;
		}

		/**
		 * Sets the <c>format</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder format(String value) {
			this.format = value;
			return this;
		}

		/**
		 * Sets the <c>items</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder items(String...value) {
			this.items = value;
			return this;
		}

		/**
		 * Sets the <c>max</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder max(String value) {
			this.max = value;
			return this;
		}

		/**
		 * Sets the <c>maxi</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder maxi(long value) {
			this.maxi = value;
			return this;
		}

		/**
		 * Sets the <c>maximum</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder maximum(String value) {
			this.maximum = value;
			return this;
		}

		/**
		 * Sets the <c>maxItems</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder maxItems(long value) {
			this.maxItems = value;
			return this;
		}

		/**
		 * Sets the <c>maxl</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder maxl(long value) {
			this.maxl = value;
			return this;
		}

		/**
		 * Sets the <c>maxLength</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder maxLength(long value) {
			this.maxLength = value;
			return this;
		}

		/**
		 * Sets the <c>min</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder min(String value) {
			this.min = value;
			return this;
		}

		/**
		 * Sets the <c>mini</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder mini(long value) {
			this.mini = value;
			return this;
		}

		/**
		 * Sets the <c>minimum</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder minimum(String value) {
			this.minimum = value;
			return this;
		}

		/**
		 * Sets the <c>minItems</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder minItems(long value) {
			this.minItems = value;
			return this;
		}

		/**
		 * Sets the <c>minl</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder minl(long value) {
			this.minl = value;
			return this;
		}

		/**
		 * Sets the <c>minLength</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder minLength(long value) {
			this.minLength = value;
			return this;
		}

		/**
		 * Sets the <c>mo</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder mo(String value) {
			this.mo = value;
			return this;
		}

		/**
		 * Sets the <c>multipleOf</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder multipleOf(String value) {
			this.multipleOf = value;
			return this;
		}

		/**
		 * Sets the <c>p</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder p(String value) {
			this.p = value;
			return this;
		}

		/**
		 * Sets the <c>pattern</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder pattern(String value) {
			this.pattern = value;
			return this;
		}

		/**
		 * Sets the <c>t</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder t(String value) {
			this.t = value;
			return this;
		}

		/**
		 * Sets the <c>type</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder type(String value) {
			this.type = value;
			return this;
		}

		/**
		 * Sets the <c>ui</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder ui(boolean value) {
			this.ui = value;
			return this;
		}

		/**
		 * Sets the <c>uniqueItems</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder uniqueItems(boolean value) {
			this.uniqueItems = value;
			return this;
		}

		/**
		 * Sets the <c>value</c> property on this annotation.
		 *
		 * @param value The new value for this property.
		 * @return This object (for method chaining).
		 */
		public Builder value(String...value) {
			this.value = value;
			return this;
		}

		// <FluentSetters>
		// </FluentSetters>
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Implementation
	//-----------------------------------------------------------------------------------------------------------------

	private static class Impl extends AnnotationImpl implements SubItems {

		private final boolean emax, emin, exclusiveMaximum, exclusiveMinimum, ui, uniqueItems;
		private final long maxi, maxItems, maxl, maxLength, mini, minItems, minl, minLength;
		private final String $ref, cf, collectionFormat, f, format, max, maximum, min, minimum, mo, multipleOf, p, pattern, t, type;
		private final String[] _default, _enum, df, e, items, value;

		Impl(Builder b) {
			super(b);
			this.$ref = b.$ref;
			this._default = copyOf(b._default);
			this._enum = copyOf(b._enum);
			this.cf = b.cf;
			this.collectionFormat = b.collectionFormat;
			this.df = copyOf(b.df);
			this.e = copyOf(b.e);
			this.emax = b.emax;
			this.emin = b.emin;
			this.exclusiveMaximum = b.exclusiveMaximum;
			this.exclusiveMinimum = b.exclusiveMinimum;
			this.f = b.f;
			this.format = b.format;
			this.items = copyOf(b.items);
			this.max = b.max;
			this.maxi = b.maxi;
			this.maximum = b.maximum;
			this.maxItems = b.maxItems;
			this.maxl = b.maxl;
			this.maxLength = b.maxLength;
			this.min = b.min;
			this.mini = b.mini;
			this.minimum = b.minimum;
			this.minItems = b.minItems;
			this.minl = b.minl;
			this.minLength = b.minLength;
			this.mo = b.mo;
			this.multipleOf = b.multipleOf;
			this.p = b.p;
			this.pattern = b.pattern;
			this.t = b.t;
			this.type = b.type;
			this.ui = b.ui;
			this.uniqueItems = b.uniqueItems;
			this.value = copyOf(b.value);
			postConstruct();
		}

		@Override /* SubItems */
		public String[] _default() {
			return _default;
		}

		@Override /* SubItems */
		public String[] _enum() {
			return _enum;
		}

		@Override /* SubItems */
		public String $ref() {
			return $ref;
		}

		@Override /* SubItems */
		public String cf() {
			return cf;
		}

		@Override /* SubItems */
		public String collectionFormat() {
			return collectionFormat;
		}

		@Override /* SubItems */
		public String[] df() {
			return df;
		}

		@Override /* SubItems */
		public String[] e() {
			return e;
		}

		@Override /* SubItems */
		public boolean emax() {
			return emax;
		}

		@Override /* SubItems */
		public boolean emin() {
			return emin;
		}

		@Override /* SubItems */
		public boolean exclusiveMaximum() {
			return exclusiveMaximum;
		}

		@Override /* SubItems */
		public boolean exclusiveMinimum() {
			return exclusiveMinimum;
		}

		@Override /* SubItems */
		public String f() {
			return f;
		}

		@Override /* SubItems */
		public String format() {
			return format;
		}

		@Override /* SubItems */
		public String[] items() {
			return items;
		}

		@Override /* SubItems */
		public String max() {
			return max;
		}

		@Override /* SubItems */
		public long maxi() {
			return maxi;
		}

		@Override /* SubItems */
		public String maximum() {
			return maximum;
		}

		@Override /* SubItems */
		public long maxItems() {
			return maxItems;
		}

		@Override /* SubItems */
		public long maxl() {
			return maxl;
		}

		@Override /* SubItems */
		public long maxLength() {
			return maxLength;
		}

		@Override /* SubItems */
		public String min() {
			return min;
		}

		@Override /* SubItems */
		public long mini() {
			return mini;
		}

		@Override /* SubItems */
		public String minimum() {
			return minimum;
		}

		@Override /* SubItems */
		public long minItems() {
			return minItems;
		}

		@Override /* SubItems */
		public long minl() {
			return minl;
		}

		@Override /* SubItems */
		public long minLength() {
			return minLength;
		}

		@Override /* SubItems */
		public String mo() {
			return mo;
		}

		@Override /* SubItems */
		public String multipleOf() {
			return multipleOf;
		}

		@Override /* SubItems */
		public String p() {
			return p;
		}

		@Override /* SubItems */
		public String pattern() {
			return pattern;
		}

		@Override /* SubItems */
		public String t() {
			return t;
		}

		@Override /* SubItems */
		public String type() {
			return type;
		}

		@Override /* SubItems */
		public boolean ui() {
			return ui;
		}

		@Override /* SubItems */
		public boolean uniqueItems() {
			return uniqueItems;
		}

		@Override /* SubItems */
		public String[] value() {
			return value;
		}
	}
}