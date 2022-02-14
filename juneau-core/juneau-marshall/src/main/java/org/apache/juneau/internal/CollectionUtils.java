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
package org.apache.juneau.internal;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

/**
 * Utility methods for collections.
 *
 * <ul class='seealso'>
 * 	<li class='extlink'>{@source}
 * </ul>
 */
public final class CollectionUtils {

	/**
	 * Creates a new set from the specified collection.
	 *
	 * @param val The value to copy from.
	 * @return A new {@link LinkedHashSet}, or <jk>null</jk> if the input was null.
	 */
	public static <T> Set<T> setFrom(Collection<T> val) {
		return val == null ? null : new LinkedHashSet<>(val);
	}

	/**
	 * Creates a new set from the specified collection.
	 *
	 * @param val The value to copy from.
	 * @return A new {@link LinkedHashSet}, or <jk>null</jk> if the input was null.
	 */
	public static <T> Set<T> copyOf(Set<T> val) {
		return val == null ? null : new LinkedHashSet<>(val);
	}

	/**
	 * Creates a new map from the specified map.
	 *
	 * @param val The value to copy from.
	 * @return A new {@link LinkedHashMap}, or <jk>null</jk> if the input was null.
	 */
	public static <K,V> Map<K,V> copyOf(Map<K,V> val) {
		return val == null ? null : new LinkedHashMap<>(val);
	}

	/**
	 * Instantiates a new builder on top of the specified map.
	 *
	 * @param addTo The map to add to.
	 * @return A new builder on top of the specified map.
	 */
	public static <K,V> MapBuilder<K,V> mapBuilder(Map<K,V> addTo) {
		return new MapBuilder<>(addTo);
	}

	/**
	 * Instantiates a new builder of the specified map type.
	 *
	 * @param keyType The key type.
	 * @param valueType The value type.
	 * @param valueTypeArgs The value type args.
	 * @return A new builder on top of the specified map.
	 */
	public static <K,V> MapBuilder<K,V> mapBuilder(Class<K> keyType, Class<V> valueType, Type...valueTypeArgs) {
		return new MapBuilder<>(keyType, valueType, valueTypeArgs);
	}

	/**
	 * Instantiates a new builder on top of the specified list.
	 *
	 * @param addTo The list to add to.
	 * @return A new builder on top of the specified list.
	 */
	public static <E> ListBuilder<E> listBuilder(List<E> addTo) {
		return new ListBuilder<>(addTo);
	}

	/**
	 * Instantiates a new builder of the specified list type.
	 *
	 * @param elementType The element type.
	 * @param elementTypeArgs The element type args.
	 * @return A new builder on top of the specified list.
	 */
	public static <E> ListBuilder<E> listBuilder(Class<E> elementType, Type...elementTypeArgs) {
		return new ListBuilder<>(elementType, elementTypeArgs);
	}

	/**
	 * Instantiates a new builder on top of the specified set.
	 *
	 * @param addTo The set to add to.
	 * @return A new builder on top of the specified set.
	 */
	public static <E> SetBuilder<E> setBuilder(Set<E> addTo) {
		return new SetBuilder<>(addTo);
	}

	/**
	 * Instantiates a new builder of the specified set.
	 *
	 * @param elementType The element type.
	 * @param elementTypeArgs The element type args.
	 * @return A new builder on top of the specified set.
	 */
	public static <E> SetBuilder<E> setBuilder(Class<E> elementType, Type...elementTypeArgs) {
		return new SetBuilder<>(elementType, elementTypeArgs);
	}

	/**
	 * Simple passthrough to {@link Collections#emptyList()}
	 *
	 * @return A new unmodifiable empty list.
	 */
	public static <T> List<T> emptyList() {
		return Collections.emptyList();
	}

	/**
	 * Convenience method for creating an {@link ArrayList}.
	 *
	 * @param values The values to initialize the list with.
	 * @return A new modifiable list.
	 */
	@SafeVarargs
	public static <T> ArrayList<T> list(T...values) {
		ArrayList<T> l = new ArrayList<>(values.length);
		for (T v : values)
			l.add(v);
		return l;
	}

	/**
	 * Convenience method for creating an array-backed list by calling {@link Arrays#asList(Object...)}.
	 *
	 * @param values The values to initialize the list with.
	 * @return A new modifiable list.
	 */
	@SafeVarargs
	public static <T> List<T> alist(T...values) {
		return Arrays.asList(values);
	}

	/**
	 * Creates an {@link ArrayList} copy from a collection.
	 *
	 * @param value The collection to copy from.
	 * @return A new modifiable list.
	 */
	public static <T> ArrayList<T> listFrom(Collection<T> value) {
		if (value == null)
			return null;
		ArrayList<T> l = new ArrayList<>();
		value.forEach(x -> l.add(x));
		return l;
	}

	/**
	 * Convenience method for creating a {@link LinkedHashSet}.
	 *
	 * @param values The values to initialize the set with.
	 * @return A new modifiable set.
	 */
	@SafeVarargs
	public static <T> LinkedHashSet<T> set(T...values) {
		LinkedHashSet<T> l = new LinkedHashSet<>();
		for (T v : values)
			l.add(v);
		return l;
	}

	/**
	 * Convenience method for creating an unmodifiable {@link LinkedHashSet}.
	 *
	 * @param values The values to initialize the set with.
	 * @return A new unmodifiable set.
	 */
	@SafeVarargs
	public static <T> Set<T> uset(T...values) {
		return unmodifiable(set(values));
	}

	/**
	 * Convenience method for creating an unmodifiable list.
	 *
	 * @param values The values to initialize the list with.
	 * @return A new unmodifiable list.
	 */
	@SafeVarargs
	public static <T> List<T> ulist(T...values) {
		return unmodifiable(alist(values));
	}

	/**
	 * Convenience method for creating a {@link TreeSet}.
	 *
	 * @param values The values to initialize the set with.
	 * @return A new modifiable set.
	 */
	@SafeVarargs
	public static <T> TreeSet<T> sortedSet(T...values) {
		TreeSet<T> l = new TreeSet<>();
		for (T v : values)
			l.add(v);
		return l;
	}

	/**
	 * Convenience method for creating a {@link LinkedHashMap}.
	 *
	 * @return A new modifiable map.
	 */
	public static <K,V> LinkedHashMap<K,V> map() {
		LinkedHashMap<K,V> m = new LinkedHashMap<>();
		return m;
	}

	/**
	 * Convenience method for creating a {@link LinkedHashMap}.
	 *
	 * @param k1 Key 1.
	 * @param v1 Value 1.
	 * @return A new modifiable map.
	 */
	public static <K,V> LinkedHashMap<K,V> map(K k1, V v1) {
		LinkedHashMap<K,V> m = new LinkedHashMap<>();
		m.put(k1, v1);
		return m;
	}

	/**
	 * Convenience method for creating a {@link LinkedHashMap}.
	 *
	 * @param k1 Key 1.
	 * @param v1 Value 1.
	 * @param k2 Key 2.
	 * @param v2 Value 2.
	 * @return A new modifiable map.
	 */
	public static <K,V> LinkedHashMap<K,V> map(K k1, V v1, K k2, V v2) {
		LinkedHashMap<K,V> m = new LinkedHashMap<>();
		m.put(k1, v1);
		m.put(k2, v2);
		return m;
	}

	/**
	 * Convenience method for creating a {@link LinkedHashMap}.
	 *
	 * @param k1 Key 1.
	 * @param v1 Value 1.
	 * @param k2 Key 2.
	 * @param v2 Value 2.
	 * @param k3 Key 3.
	 * @param v3 Value 3.
	 * @return A new modifiable map.
	 */
	public static <K,V> LinkedHashMap<K,V> map(K k1, V v1, K k2, V v2, K k3, V v3) {
		LinkedHashMap<K,V> m = new LinkedHashMap<>();
		m.put(k1, v1);
		m.put(k2, v2);
		m.put(k3, v3);
		return m;
	}

	/**
	 * Convenience method for copying a list.
	 *
	 * @param value The list to copy.
	 * @return A new modifiable list.
	 */
	public static <T> ArrayList<T> copyOf(List<T> value) {
		return value == null ? null : new ArrayList<>(value);
	}

	/**
	 * Convenience method for creating an {@link ArrayList} and sorting it.
	 *
	 * @param values The values to initialize the list with.
	 * @return A new modifiable list.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SafeVarargs
	public static <T> ArrayList<T> sortedList(T...values) {
		ArrayList<T> l = list(values);
		Collections.sort((List<Comparable>) l);
		return l;
	}

	/**
	 * Convenience method for creating an {@link ArrayList} and sorting it.
	 *
	 * @param comparator The comparator to use to sort the list.
	 * @param values The values to initialize the list with.
	 * @return A new modifiable list.
	 */
	public static <T> ArrayList<T> sortedList(Comparator<T> comparator, T[] values) {
		ArrayList<T> l = list(values);
		Collections.sort(l, comparator);
		return l;
	}

	/**
	 * Convenience method for creating an {@link ArrayList} and sorting it.
	 *
	 * @param comparator The comparator to use to sort the list.
	 * @param value The values to initialize the list with.
	 * @return A new modifiable list.
	 */
	public static <T> ArrayList<T> sortedList(Comparator<T> comparator, Collection<T> value) {
		ArrayList<T> l = listFrom(value);
		Collections.sort(l, comparator);
		return l;
	}

	/**
	 * Wraps the specified list in {@link Collections#unmodifiableList(List)}.
	 *
	 * @param value The list to wrap.
	 * @return The wrapped list.
	 */
	public static <T> List<T> unmodifiable(List<T> value) {
		return value == null ? null: Collections.unmodifiableList(value);
	}

	/**
	 * Wraps the specified set in {@link Collections#unmodifiableSet(Set)}.
	 *
	 * @param value The set to wrap.
	 * @return The wrapped set.
	 */
	public static <T> Set<T> unmodifiable(Set<T> value) {
		return value == null ? null: Collections.unmodifiableSet(value);
	}

	/**
	 * Wraps the specified map in {@link Collections#unmodifiableMap(Map)}.
	 *
	 * @param value The map to wrap.
	 * @return The wrapped map.
	 */
	public static <K,V> Map<K,V> unmodifiable(Map<K,V> value) {
		return value == null ? null: Collections.unmodifiableMap(value);
	}

	/**
	 * Converts the specified collection to an array.
	 *
	 * @param value The collection to convert.
	 * @return A new array.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] array(Collection<T> value) {
		return array(value, (Class<T>)value.getClass().getComponentType());
	}

	/**
	 * Converts the specified collection to an array.
	 *
	 * @param value The collection to convert.
	 * @param componentType The component type of the array.
	 * @return A new array.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] array(Collection<T> value, Class<T> componentType) {
		if (value == null)
			return null;
		T[] array = (T[])Array.newInstance(componentType, value.size());
		return value.toArray(array);
	}

	/**
	 * Iterates the specified list in reverse order.
	 *
	 * @param value The list to iterate.
	 * @param action The action to perform.
	 */
	public static <T> void forEachReverse(List<T> value, Consumer<T> action) {
		if (value instanceof ArrayList) {
			for (int i = value.size()-1; i >= 0; i--)
				action.accept(value.get(i));
		} else {
			ListIterator<T> i = value.listIterator(value.size());
			while (i.hasPrevious())
				action.accept(i.previous());
		}
	}

	/**
	 * Iterates the specified array in reverse order.
	 *
	 * @param value The array to iterate.
	 * @param action The action to perform.
	 */
	public static <T> void forEachReverse(T[] value, Consumer<T> action) {
		for (int i = value.length-1; i >= 0; i--)
			action.accept(value[i]);
	}

	/**
	 * Adds all the specified values to the specified collection.
	 *
	 * @param value The collection to add to.
	 * @param entries The entries to add.
	 */
	@SafeVarargs
	public static <T> void addAll(Collection<T> value, T...entries) {
		Collections.addAll(value, entries);
	}

	/**
	 * Returns the last entry in a list.
	 *
	 * @param <T> The element type.
	 * @param l The list.
	 * @return The last element, or <jk>null</jk> if the list is <jk>null</jk> or empty.
	 */
	public static <T> T last(List<T> l) {
		if (l == null || l.isEmpty())
			return null;
		return l.get(l.size()-1);
	}

	/**
	 * Returns the last entry in an array.
	 *
	 * @param <T> The element type.
	 * @param l The array.
	 * @return The last element, or <jk>null</jk> if the array is <jk>null</jk> or empty.
	 */
	public static <T> T last(T[] l) {
		if (l == null || l.length == 0)
			return null;
		return l[l.length-1];
	}

	/**
	 * Returns an optional of the specified value.
	 *
	 * @param value The value.
	 * @return A new Optional.
	 */
	public static <T> Optional<T> optional(T value) {
		return Optional.ofNullable(value);
	}

	/**
	 * Returns an empty {@link Optional}.
	 *
	 * @return An empty {@link Optional}.
	 */
	public static <T> Optional<T> empty() {
		return Optional.empty();
	}
}
