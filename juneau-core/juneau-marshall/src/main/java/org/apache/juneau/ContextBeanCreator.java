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

import static org.apache.juneau.internal.ExceptionUtils.*;
import static java.util.Optional.*;

import java.util.*;

/**
 * Utility class for instantiating a Context bean.
 *
 * <p>
 * Contains either a pre-existing Context bean, or a builder for that bean.
 * If it's a builder, then annotations can be applied to it.
 *
 * @param <T> The bean type.
 */
public class ContextBeanCreator<T> {

	/**
	 * Creator.
	 *
	 * @param type The bean type.
	 * @return A new creator object.
	 */
	public static <T> ContextBeanCreator<T> create(Class<T> type) {
		return new ContextBeanCreator<>(type);
	}

	private Class<T> type;
	private T value;
	private ContextBuilder builder;

	/**
	 * Constructor.
	 *
	 * @param type The bean type.
	 */
	protected ContextBeanCreator(Class<T> type) {
		this.type = type;
	}

	/**
	 * Copy constructor.
	 *
	 * @param copyFrom The creator to copy from.
	 */
	protected ContextBeanCreator(ContextBeanCreator<T> copyFrom) {
		this.type = copyFrom.type;
		this.value = copyFrom.value;
		this.builder = copyFrom.builder == null ? null : copyFrom.builder.copy();
	}

	/**
	 * Sets an already instantiated object on this creator.
	 *
	 * @param value The bean to set.
	 * @return This object.
	 */
	public ContextBeanCreator<T> set(T value) {
		this.value = value;
		return this;
	}

	/**
	 * Sets the implementation type of the bean.
	 *
	 * <p>
	 * The class type must extend from {@link Context} and have a builder create method.
	 *
	 * @param value The bean type.
	 * @return This object.
	 */
	@SuppressWarnings("unchecked")
	public ContextBeanCreator<T> set(Class<? extends T> value) {
		builder = Context.createBuilder((Class<? extends Context>) value);
		if (builder == null)
			throw runtimeException("Creator for class {0} not found." + value.getName());
		return this;
	}

	/**
	 * Returns access to the inner builder if the builder exists and is of the specified type.
	 *
	 * @param c The builder class type.
	 * @return An optional containing the builder if it exists.
	 */
	@SuppressWarnings("unchecked")
	public <T2 extends ContextBuilder> Optional<T2> builder(Class<T2> c) {
		return ofNullable(c.isInstance(builder) ? (T2)builder : null);
	}

	/**
	 * Returns true if any of the annotations/appliers can be applied to the inner builder (if it has one).
	 *
	 * @param work The work to check.
	 * @return This object.
	 */
	public boolean canApply(AnnotationWorkList work) {
		if (builder != null)
			return (builder.canApply(work));
		return false;
	}

	/**
	 * Applies the specified annotations to all applicable serializer builders in this group.
	 *
	 * @param work The annotations to apply.
	 * @return This object (for method chaining).
	 */
	public ContextBeanCreator<T> apply(AnnotationWorkList work) {
		if (builder != null)
			builder.apply(work);
		return this;
	}

	/**
	 * Creates a new copy of this creator.
	 *
	 * @return A new copy of this creator.
	 */
	public ContextBeanCreator<T> copy() {
		return new ContextBeanCreator<>(this);
	}

	/**
	 * Returns the built bean.
	 *
	 * @return The built bean.
	 */
	@SuppressWarnings("unchecked")
	public T create() {
		if (value != null)
			return value;
		if (builder != null)
			return (T)builder.build();
		return null;
	}
}