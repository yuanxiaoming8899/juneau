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
package org.apache.juneau.serializer.annotation;

import static org.apache.juneau.serializer.OutputStreamSerializer.*;
import static org.apache.juneau.serializer.WriterSerializer.*;

import java.nio.charset.*;

import org.apache.juneau.*;
import org.apache.juneau.reflect.*;
import org.apache.juneau.serializer.*;
import org.apache.juneau.svl.*;

/**
 * Utility classes and methods for the {@link SerializerConfig @SerializerConfig} annotation.
 */
public class SerializerConfigAnnotation {

	/**
	 * Applies {@link SerializerConfig} annotations to a {@link PropertyStoreBuilder}.
	 */
	public static class Apply extends ConfigApply<SerializerConfig> {

		/**
		 * Constructor.
		 *
		 * @param c The annotation class.
		 * @param vr The resolver for resolving values in annotations.
		 */
		public Apply(Class<SerializerConfig> c, VarResolverSession vr) {
			super(c, vr);
		}

		@Override
		public void apply(AnnotationInfo<SerializerConfig> ai, PropertyStoreBuilder psb, VarResolverSession vr) {
			SerializerConfig a = ai.getAnnotation();

			psb.setIfNotEmpty(SERIALIZER_addBeanTypes, bool(a.addBeanTypes()));
			psb.setIfNotEmpty(SERIALIZER_addRootType, bool(a.addRootType()));
			psb.setIfNotEmpty(SERIALIZER_keepNullProperties, bool(a.keepNullProperties()));
			psb.setIf(a.listener() != SerializerListener.Null.class, SERIALIZER_listener, a.listener());
			psb.setIfNotEmpty(SERIALIZER_sortCollections, bool(a.sortCollections()));
			psb.setIfNotEmpty(SERIALIZER_sortMaps, bool(a.sortMaps()));
			psb.setIfNotEmpty(SERIALIZER_trimEmptyCollections, bool(a.trimEmptyCollections()));
			psb.setIfNotEmpty(SERIALIZER_trimEmptyMaps, bool(a.trimEmptyMaps()));
			psb.setIfNotEmpty(SERIALIZER_trimStrings, bool(a.trimStrings()));
			psb.setIfNotEmpty(SERIALIZER_uriContext, string(a.uriContext()));
			psb.setIfNotEmpty(SERIALIZER_uriRelativity, string(a.uriRelativity()));
			psb.setIfNotEmpty(SERIALIZER_uriResolution, string(a.uriResolution()));
			psb.setIfNotEmpty(OSSERIALIZER_binaryFormat, string(a.binaryFormat()));
			psb.setIfNotEmpty(WSERIALIZER_fileCharset, charset(a.fileCharset()));
			psb.setIfNotEmpty(WSERIALIZER_maxIndent, integer(a.maxIndent(), "maxIndent"));
			psb.setIfNotEmpty(WSERIALIZER_quoteChar, character(a.quoteChar(), "quoteChar"));
			psb.setIfNotEmpty(WSERIALIZER_streamCharset, charset(a.streamCharset()));
			psb.setIfNotEmpty(WSERIALIZER_useWhitespace, bool(a.useWhitespace()));
		}

		private Object charset(String in) {
			String s = string(in);
			if ("default".equalsIgnoreCase(s))
				return Charset.defaultCharset();
			return s;
		}

		private Character character(String in, String loc) {
			String s = string(in);
			if (s == null)
				return null;
			if (s.length() != 1)
				throw new ConfigException("Invalid syntax for character on annotation @{0}({1}): {2}", "SerializerConfig", loc, in);
			return s.charAt(0);
		}
	}
}