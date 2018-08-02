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
package org.apache.juneau.rest;

import static org.apache.juneau.internal.ClassUtils.*;
import static org.apache.juneau.internal.StringUtils.*;
import static org.apache.juneau.rest.RestParamType.*;
import static org.apache.juneau.rest.util.AnnotationUtils.*;

import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.*;

import org.apache.juneau.*;
import org.apache.juneau.dto.swagger.*;
import org.apache.juneau.http.*;
import org.apache.juneau.http.annotation.*;
import org.apache.juneau.http.annotation.Contact;
import org.apache.juneau.http.annotation.Items;
import org.apache.juneau.http.annotation.License;
import org.apache.juneau.http.annotation.Tag;
import org.apache.juneau.httppart.*;
import org.apache.juneau.internal.*;
import org.apache.juneau.json.*;
import org.apache.juneau.jsonschema.*;
import org.apache.juneau.parser.*;
import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.util.*;
import org.apache.juneau.serializer.*;
import org.apache.juneau.svl.*;
import org.apache.juneau.utils.*;

/**
 * Default implementation of {@link RestInfoProvider}.
 *
 * <p>
 * Subclasses can override these methods to tailor how HTTP REST resources are documented.
 *
 * <h5 class='section'>See Also:</h5>
 * <ul>
 * 	<li class='jf'>{@link RestContext#REST_infoProvider}
 * 	<li class='link'><a class="doclink" href="../../../../overview-summary.html#juneau-rest-server.Swagger">Overview &gt; juneau-rest-server &gt; OPTIONS Pages</a>
 * </ul>
 */
public class SwaggerGenerator {

	private final RestRequest req;
	private final VarResolverSession vr;
	private final Set<Pattern> ignoreTypes;
	private final BeanSession bs;
	private final Locale locale;
	private final RestContext context;
	private final JsonParser jp = JsonParser.DEFAULT;
	private final JsonSchemaSerializerSession js;
	private final Class<?> c;
	private final Object resource;
	private final MessageBundle mb;

	/**
	 * Constructor.
	 * @param req
	 * @param ignoreTypes
	 *
	 */
	public SwaggerGenerator(RestRequest req, Set<Pattern> ignoreTypes) {
		this.req = req;
		this.vr = req.getVarResolverSession();
		this.ignoreTypes = ignoreTypes;
		this.locale = req.getLocale();
		this.context = req.getContext();
		this.js = context.getJsonSchemaSerializer().createSession();
		this.c = context.getResource().getClass();
		this.resource = context.getResource();
		this.mb = context.getMessages();

		BeanSession bs = req.getBeanSession();
		if (bs == null)
			bs = BeanContext.DEFAULT.createBeanSession();
		this.bs = bs;
	}

	/**
	 * Returns the localized swagger for this REST resource.
	 *
	 * <p>
	 * Subclasses can override this method to customize the Swagger.
	 *
	 * @return
	 * 	A new Swagger instance.
	 * 	<br>Never <jk>null</jk>.
	 * @throws Exception
	 */
	public Swagger getSwagger() throws Exception {

		// Load swagger JSON from classpath.
		ObjectMap omSwagger = context.getClasspathResource(ObjectMap.class, MediaType.JSON, ClassUtils.getSimpleName(resource.getClass()) + ".json", locale);
		if (omSwagger == null)
			omSwagger = context.getClasspathResource(ObjectMap.class, MediaType.JSON, resource.getClass().getSimpleName() + ".json", locale);
		if (omSwagger == null)
			omSwagger = new ObjectMap();

		// Combine it with @RestResource(swagger)
		for (Map.Entry<Class<?>,RestResource> e : getAnnotationsMapParentFirst(RestResource.class, resource.getClass()).entrySet()) {
			RestResource rr = e.getValue();

			ObjectMap sInfo = omSwagger.getObjectMap("info", true);
			sInfo.appendSkipEmpty("title",
				firstNonEmpty(
					sInfo.getString("title"),
					resolve(rr.title())
				)
			);
			sInfo.appendSkipEmpty("description",
				firstNonEmpty(
					sInfo.getString("description"),
					resolve(rr.description())
				)
			);

			ResourceSwagger r = rr.swagger();

			omSwagger.appendAll(parseMap(r.value(), "@ResourceSwagger(value) on class {0}", c));

			if (! empty(r)) {
				ObjectMap info = omSwagger.getObjectMap("info", true);
				info.appendSkipEmpty("title", resolve(r.title()));
				info.appendSkipEmpty("description", resolve(r.description()));
				info.appendSkipEmpty("version", resolve(r.version()));
				info.appendSkipEmpty("termsOfService", resolve(r.termsOfService()));
				info.appendSkipEmpty("contact",
					merge(
						info.getObjectMap("contact"),
						toMap(r.contact(), "@ResourceSwagger(contact) on class {0}", c)
					)
				);
				info.appendSkipEmpty("license",
					merge(
						info.getObjectMap("license"),
						toMap(r.license(), "@ResourceSwagger(license) on class {0}", c)
					)
				);
			}

			omSwagger.appendSkipEmpty("externalDocs",
				merge(
					omSwagger.getObjectMap("externalDocs"),
					toMap(r.externalDocs(), "@ResourceSwagger(externalDocs) on class {0}", c)
				)
			);
			omSwagger.appendSkipEmpty("tags",
				merge(
					omSwagger.getObjectList("tags"),
					toList(r.tags(), "@ResourceSwagger(tags) on class {0}", c)
				)
			);
		}

		omSwagger.appendSkipEmpty("externalDocs", parseMap(mb.findFirstString(locale, "externalDocs"), "Messages/externalDocs on class {0}", c));

		ObjectMap info = omSwagger.getObjectMap("info", true);
		info.appendSkipEmpty("title", resolve(mb.findFirstString(locale, "title")));
		info.appendSkipEmpty("description", resolve(mb.findFirstString(locale, "description")));
		info.appendSkipEmpty("version", resolve(mb.findFirstString(locale, "version")));
		info.appendSkipEmpty("termsOfService", resolve(mb.findFirstString(locale, "termsOfService")));
		info.appendSkipEmpty("contact", parseMap(mb.findFirstString(locale, "contact"), "Messages/contact on class {0}", c));
		info.appendSkipEmpty("license", parseMap(mb.findFirstString(locale, "license"), "Messages/license on class {0}", c));
		if (info.isEmpty())
			omSwagger.remove("info");

		ObjectList
			produces = omSwagger.getObjectList("produces", true),
			consumes = omSwagger.getObjectList("consumes", true);
		if (consumes.isEmpty())
			consumes.addAll(context.getConsumes());
		if (produces.isEmpty())
			produces.addAll(context.getProduces());

		Map<String,ObjectMap> tagMap = new LinkedHashMap<>();
		if (omSwagger.containsKey("tags")) {
			for (ObjectMap om : omSwagger.getObjectList("tags").elements(ObjectMap.class)) {
				String name = om.getString("name");
				if (name == null)
					throw new SwaggerException(null, "Tag definition found without name in swagger JSON.");
				tagMap.put(name, om);
			}
		}

		String s = mb.findFirstString(locale, "tags");
		if (s != null) {
			for (ObjectMap m : parseListOrCdl(vr, s, "Messages/tags on class {0}", c).elements(ObjectMap.class)) {
				String name = m.getString("name");
				if (name == null)
					throw new SwaggerException(null, "Tag definition found without name in resource bundle on class {0}", c) ;
				if (tagMap.containsKey(name))
					tagMap.get(name).putAll(m);
				else
					tagMap.put(name, m);
			}
		}

		// Load our existing bean definitions into our session.
		ObjectMap definitions = omSwagger.getObjectMap("definitions", true);
		for (String defId : definitions.keySet())
			js.addBeanDef(defId, definitions.getObjectMap(defId));

		// Iterate through all the @RestMethod methods.
		for (RestJavaMethod sm : context.getCallMethods().values()) {

			// Skip it if user doesn't have access.
			if (! sm.isRequestAllowed(req))
				continue;

			Method m = sm.method;
			RestMethod rm = m.getAnnotation(RestMethod.class);
			String mn = m.getName();

			// Get the operation from the existing swagger so far.
			ObjectMap op = getOperation(omSwagger, sm.getPathPattern(), sm.getHttpMethod().toLowerCase());

			// Add @RestMethod(swagger)
			MethodSwagger ms = rm.swagger();

			op.appendAll(parseMap(ms.value(), "@MethodSwagger(value) on class {0} method {1}", c, m));
			op.appendSkipEmpty("operationId",
				firstNonEmpty(
					resolve(ms.operationId()),
					op.getString("operationId"),
					mn
				)
			);
			op.appendSkipEmpty("summary",
				firstNonEmpty(
					resolve(ms.summary()),
					resolve(mb.findFirstString(locale, mn + ".summary")),
					op.getString("summary"),
					resolve(rm.summary())
				)
			);
			op.appendSkipEmpty("description",
				firstNonEmpty(
					resolve(ms.description()),
					resolve(mb.findFirstString(locale, mn + ".description")),
					op.getString("description"),
					resolve(rm.description())
				)
			);
			op.appendSkipEmpty("deprecated",
				firstNonEmpty(
					resolve(ms.deprecated()),
					(m.getAnnotation(Deprecated.class) != null || m.getDeclaringClass().getAnnotation(Deprecated.class) != null) ? "true" : null
				)
			);
			op.appendSkipEmpty("tags",
				merge(
					parseListOrCdl(mb.findFirstString(locale, mn + ".tags"), "Messages/tags on class {0} method {1}", c, m),
					parseListOrCdl(ms.tags(), "@MethodSwagger(tags) on class {0} method {1}", c, m)
				)
			);
			op.appendSkipEmpty("schemes",
				merge(
					parseListOrCdl(mb.findFirstString(locale, mn + ".schemes"), "Messages/schemes on class {0} method {1}", c, m),
					parseListOrCdl(ms.schemes(), "@MethodSwagger(schemes) on class {0} method {1}", c, m)
				)
			);
			op.appendSkipEmpty("consumes",
				firstNonEmpty(
					parseListOrCdl(mb.findFirstString(locale, mn + ".consumes"), "Messages/consumes on class {0} method {1}", c, m),
					parseListOrCdl(ms.consumes(), "@MethodSwagger(consumes) on class {0} method {1}", c, m)
				)
			);
			op.appendSkipEmpty("produces",
				firstNonEmpty(
					parseListOrCdl(mb.findFirstString(locale, mn + ".produces"), "Messages/produces on class {0} method {1}", c, m),
					parseListOrCdl(ms.produces(), "@MethodSwagger(produces) on class {0} method {1}", c, m)
				)
			);
			op.appendSkipEmpty("parameters",
				merge(
					parseList(mb.findFirstString(locale, mn + ".parameters"), "Messages/parameters on class {0} method {1}", c, m),
					parseList(ms.parameters(), "@MethodSwagger(parameters) on class {0} method {1}", c, m)
				)
			);
			op.appendSkipEmpty("responses",
				merge(
					parseMap(mb.findFirstString(locale, mn + ".responses"), "Messages/responses on class {0} method {1}", c, m),
					parseMap(ms.responses(), "@MethodSwagger(responses) on class {0} method {1}", c, m)
				)
			);
			op.appendSkipEmpty("externalDocs",
				merge(
					op.getObjectMap("externalDocs"),
					parseMap(mb.findFirstString(locale, mn + ".externalDocs"), "Messages/externalDocs on class {0} method {1}", c, m),
					toMap(ms.externalDocs(), "@MethodSwagger(externalDocs) on class {0} method {1}", c, m)
				)
			);

			if (op.containsKey("tags"))
				for (String tag : op.getObjectList("tags").elements(String.class))
					if (! tagMap.containsKey(tag))
						tagMap.put(tag, new ObjectMap().append("name", tag));

			ObjectMap paramMap = new ObjectMap();
			if (op.containsKey("parameters"))
				for (ObjectMap param : op.getObjectList("parameters").elements(ObjectMap.class))
					paramMap.put(param.getString("in") + '.' + ("body".equals(param.getString("in")) ? "body" : param.getString("name")), param);

			// Finally, look for parameters defined on method.
			for (RestMethodParam mp : context.getRestMethodParams(m)) {

				RestParamType in = mp.getParamType();
				int index = mp.index;

				if (in.isAny(BODY, QUERY, FORM_DATA, HEADER, PATH)) {

					String key = in.toString() + '.' + (in == BODY ? "body" : mp.getName());

					ObjectMap param = paramMap.getObjectMap(key, true);

					param.append("in", in);

					if (in != BODY)
						param.append("name", mp.name);

					try {
						if (in == BODY) {
							for (Body a : getAnnotationsParentFirst(Body.class, mp.method, mp.index))
								merge(param, a);
						} else if (in == QUERY) {
							for (Query a : getAnnotationsParentFirst(Query.class, mp.method, mp.index))
								merge(param, a);
						} else if (in == FORM_DATA) {
							for (FormData a : getAnnotationsParentFirst(FormData.class, mp.method, mp.index))
								merge(param, a);
						} else if (in == HEADER) {
							for (Header a : getAnnotationsParentFirst(Header.class, mp.method, mp.index))
								merge(param, a);
						} else if (in == PATH) {
							for (Path a : getAnnotationsParentFirst(Path.class, mp.method, mp.index))
								merge(param, a);
						}
					} catch (ParseException e) {
						throw new SwaggerException(e, "Malformed swagger JSON object encountered in {0} class {1} method {2} parameter {3}", in, c, m, index);
					}

					if (! param.containsKey("schema"))
						param.put("schema", getSchema(param.getObjectMap("schema", true), mp.getType()));

					if ((in == BODY || in == PATH) && ! param.containsKeyNotEmpty("required"))
						param.put("required", true);

					addXExamples(sm, param, in.toString(), mp.getType());
				}
			}

			if (! paramMap.isEmpty())
				op.put("parameters", paramMap.values());

			ObjectMap responses = op.getObjectMap("responses", true);

			for (Class<?> ec : m.getExceptionTypes()) {
				if (hasAnnotation(Response.class, ec)) {
					for (Response a : getAnnotationsParentFirst(Response.class, ec)) {
						for (Integer code : getCodes(a, 500)) {
							ObjectMap om = responses.getObjectMap(String.valueOf(code), true);
							merge(om, a);
						}
					}
				}
			}

			if (hasAnnotation(Response.class, m)) {
				for (Response a : getAnnotationsParentFirst(Response.class, m)) {
					for (Integer code : getCodes(a, 200)) {
						ObjectMap om = responses.getObjectMap(String.valueOf(code), true);
						merge(om, a);
						if (! om.containsKey("schema"))
							om.appendSkipEmpty("schema", getSchema(om.getObjectMap("schema", true), m.getGenericReturnType()));
					}
				}
			}

			if (hasAnnotation(ResponseBody.class, m)) {
				ObjectMap om = responses.getObjectMap("200", true);
				for (ResponseBody a : getAnnotationsParentFirst(ResponseBody.class, m)) {
					merge(om, a);
					if (! om.containsKey("schema"))
						om.appendSkipEmpty("schema", getSchema(om.getObjectMap("schema", true), m.getGenericReturnType()));
				}
				addXExamples(sm, om, "ok", m.getGenericReturnType());
			}

			// Finally, look for @ResponseHeader parameters defined on method.
			for (RestMethodParam mp : context.getRestMethodParams(m)) {

				RestParamType in = mp.getParamType();

				if (in == RESPONSE_HEADER) {
					for (ResponseHeader a : getAnnotationsParentFirst(ResponseHeader.class, mp.method, mp.index)) {
						for (Integer code : getCodes(a, 200)) {
							ObjectMap header = responses.getObjectMap(String.valueOf(code), true).getObjectMap("headers", true).getObjectMap(mp.name, true);
							merge(header, a);
						}
					}

				} else if (in == RESPONSE) {
					for (Response a : getAnnotationsParentFirst(Response.class, mp.method, mp.index)) {
						for (Integer code : getCodes(a, 200)) {
							ObjectMap response = responses.getObjectMap(String.valueOf(code), true);
							merge(response, a);
							if (! response.containsKey("schema")) {
								Type type = Value.getParameterType(mp.type);
								if (type != null)
									response.appendSkipEmpty("schema", getSchema(response.getObjectMap("schema", true), type));
							}
						}
					}

				} else if (in == RESPONSE_BODY) {
					ObjectMap response = responses.getObjectMap("200", true);
					for (ResponseBody a : getAnnotationsParentFirst(ResponseBody.class, mp.method, mp.index)) {
						merge(response, a);
					}
					if (! response.containsKey("schema")) {
						Type type = Value.getParameterType(mp.type);
						if (type != null)
							response.appendSkipEmpty("schema", getSchema(response.getObjectMap("schema", true), type));
					}
				}
			}

			// Add default response descriptions.
			for (Map.Entry<String,Object> e : responses.entrySet()) {
				String key = e.getKey();
				ObjectMap val = responses.getObjectMap(key);
				if (StringUtils.isDecimal(key))
					val.appendIf(false, true, true, "description", RestUtils.getHttpResponseText(Integer.parseInt(key)));
			}

			if (responses.isEmpty())
				op.remove("responses");
			else
				op.put("responses", new TreeMap<>(responses));

			if (! op.containsKey("consumes")) {
				List<MediaType> mConsumes = sm.supportedContentTypes;
				if (! mConsumes.equals(consumes))
					op.put("consumes", mConsumes);
			}

			if (! op.containsKey("produces")) {
				List<MediaType> mProduces = sm.supportedAcceptTypes;
				if (! mProduces.equals(produces))
					op.put("produces", mProduces);
			}
		}

		if (js.getBeanDefs() != null)
			for (Map.Entry<String,ObjectMap> e : js.getBeanDefs().entrySet())
				definitions.put(e.getKey(), fixSwaggerExtensions(e.getValue()));
		if (definitions.isEmpty())
			omSwagger.remove("definitions");

		if (! tagMap.isEmpty())
			omSwagger.put("tags", tagMap.values());

		if (consumes.isEmpty())
			omSwagger.remove("consumes");
		if (produces.isEmpty())
			omSwagger.remove("produces");

//		try {
//			if (! omSwagger.isEmpty())
//				assertNoEmpties(omSwagger);
//		} catch (SwaggerException e1) {
//			System.err.println(omSwagger.toString(SimpleJsonSerializer.DEFAULT_LAX_READABLE));
//			throw e1;
//		}

		try {
			String swaggerJson = omSwagger.toString(SimpleJsonSerializer.DEFAULT_READABLE);
//			System.err.println(swaggerJson);
			return jp.parse(swaggerJson, Swagger.class);
		} catch (Exception e) {
			throw new RestServletException("Error detected in swagger.").initCause(e);
		}
	}

	//=================================================================================================================
	// Utility methods
	//=================================================================================================================

	private ObjectMap resolve(ObjectMap om) throws ParseException {
		ObjectMap om2 = null;
		if (om.containsKey("_value")) {
			om = om.modifiable();
			om2 = parseMap(om.remove("_value"));
		} else {
			om2 = new ObjectMap();
		}
		for (Map.Entry<String,Object> e : om.entrySet()) {
			Object val = e.getValue();
			if (val instanceof ObjectMap) {
				val = resolve((ObjectMap)val);
			} else if (val instanceof ObjectList) {
				val = resolve((ObjectList) val);
			} else if (val instanceof String) {
				val = resolve(val.toString());
			}
			om2.put(e.getKey(), val);
		}
		return om2;
	}

	private ObjectList resolve(ObjectList om) throws ParseException {
		ObjectList ol2 = new ObjectList();
		for (Object val : om) {
			if (val instanceof ObjectMap) {
				val = resolve((ObjectMap)val);
			} else if (val instanceof ObjectList) {
				val = resolve((ObjectList) val);
			} else if (val instanceof String) {
				val = resolve(val.toString());
			}
			ol2.add(val);
		}
		return ol2;
	}

	private String resolve(String[] ss) {
		if (ss.length == 0)
			return null;
		return resolve(joinnl(ss));
	}

	private String resolve(String s) {
		if (s == null)
			return null;
		return vr.resolve(s.trim());
	}

	private ObjectMap parseMap(String[] o, String location, Object...args) throws ParseException {
		if (o.length == 0)
			return ObjectMap.EMPTY_MAP;
		try {
			return parseMap(o);
		} catch (ParseException e) {
			throw new SwaggerException(e, "Malformed swagger JSON object encountered in " + location + ".", args);
		}
	}

	private ObjectMap parseMap(String o, String location, Object...args) throws ParseException {
		try {
			return parseMap(o);
		} catch (ParseException e) {
			throw new SwaggerException(e, "Malformed swagger JSON object encountered in " + location + ".", args);
		}
	}

	private ObjectMap parseMap(Object o) throws ParseException {
		if (o == null)
			return null;
		if (o instanceof String[])
			o = joinnl((String[])o);
		if (o instanceof String) {
			String s = o.toString();
			if (s.isEmpty())
				return null;
			s = resolve(s);
			if ("IGNORE".equalsIgnoreCase(s))
				return new ObjectMap().append("ignore", true);
			if (! isObjectMap(s, true))
				s = "{" + s + "}";
			return new ObjectMap(s);
		}
		if (o instanceof ObjectMap)
			return (ObjectMap)o;
		throw new SwaggerException(null, "Unexpected data type ''{0}''.  Expected ObjectMap or String.", o.getClass().getName());
	}

	private ObjectList parseList(Object o, String location, Object...locationArgs) throws ParseException {
		try {
			if (o == null)
				return null;
			String s = (o instanceof String[] ? joinnl((String[])o) : o.toString());
			if (s.isEmpty())
				return null;
			s = resolve(s);
			if (! isObjectList(s, true))
				s = "[" + s + "]";
			return new ObjectList(s);
		} catch (ParseException e) {
			throw new SwaggerException(e, "Malformed swagger JSON array encountered in "+location+".", locationArgs);
		}
	}

	private ObjectList parseListOrCdl(Object o, String location, Object...locationArgs) throws ParseException {
		try {
			if (o == null)
				return null;
			String s = (o instanceof String[] ? joinnl((String[])o) : o.toString());
			if (s.isEmpty())
				return null;
			s = resolve(s);
			return StringUtils.parseListOrCdl(s);
		} catch (ParseException e) {
			throw new SwaggerException(e, "Malformed swagger JSON array encountered in "+location+".", locationArgs);
		}
	}

	private ObjectMap newMap(ObjectMap om, String[] value, String location, Object...locationArgs) throws ParseException {
		if (value.length == 0)
			return om == null ? new ObjectMap() : om;
		ObjectMap om2 = parseMap(joinnl(value), location, locationArgs);
		if (om == null)
			return om2;
		return om.appendAll(om2);
	}

	private ObjectMap merge(ObjectMap...maps) {
		ObjectMap m = maps[0];
		for (int i = 1; i < maps.length; i++) {
			if (maps[i] != null) {
				if (m == null)
					m = new ObjectMap();
				m.putAll(maps[i]);
			}
		}
		return m;
	}

	private ObjectList merge(ObjectList...lists) {
		ObjectList l = lists[0];
		for (int i = 1; i < lists.length; i++) {
			if (lists[i] != null) {
				if (l == null)
					l = new ObjectList();
				l.addAll(lists[i]);
			}
		}
		return l;
	}

	@SafeVarargs
	private final <T> T firstNonEmpty(T...t) {
		return ObjectUtils.firstNonEmpty(t);
	}

	private ObjectMap toMap(ExternalDocs a, String location, Object...locationArgs) throws ParseException {
		if (empty(a))
			return null;
		ObjectMap om = newMap(new ObjectMap(), a.value(), location, locationArgs);
		om.appendSkipEmpty("description", resolve(joinnl(a.description())));
		om.appendSkipEmpty("url", resolve(a.url()));
		return om.isEmpty() ? null : om;
	}

	private ObjectMap toMap(Contact a, String location, Object...locationArgs) throws ParseException {
		if (empty(a))
			return null;
		ObjectMap om = newMap(new ObjectMap(), a.value(), location, locationArgs);
		om.appendSkipEmpty("name", resolve(a.name()));
		om.appendSkipEmpty("url", resolve(a.url()));
		om.appendSkipEmpty("email", resolve(a.email()));
		return om.isEmpty() ? null : om;
	}

	private ObjectMap toMap(License a, String location, Object...locationArgs) throws ParseException {
		if (empty(a))
			return null;
		ObjectMap om = newMap(new ObjectMap(), a.value(), location, locationArgs);
		om.appendSkipEmpty("name", resolve(a.name()));
		om.appendSkipEmpty("url", resolve(a.url()));
		return om.isEmpty() ? null : om;
	}

	private ObjectMap toMap(Tag a, String location, Object...locationArgs) throws ParseException {
		ObjectMap om = newMap(new ObjectMap(), a.value(), location, locationArgs);
		om.appendSkipEmpty("name", resolve(a.name()));
		om.appendSkipEmpty("description", resolve(joinnl(a.description())));
		om.appendSkipNull("externalDocs", merge(om.getObjectMap("externalDocs"), toMap(a.externalDocs(), location, locationArgs)));
		return om.isEmpty() ? null : om;
	}

	private ObjectList toList(Tag[] aa, String location, Object...locationArgs) throws ParseException {
		if (aa.length == 0)
			return null;
		ObjectList ol = new ObjectList();
		for (Tag a : aa)
			ol.add(toMap(a, location, locationArgs));
		return ol.isEmpty() ? null : ol;
	}

	private ObjectMap getSchema(ObjectMap schema, Type type) throws Exception {

		ClassMeta<?> cm = bs.getClassMeta(type);

		if (schema.getBoolean("ignore", false))
			return null;

		if (schema.containsKey("type") || schema.containsKey("$ref"))
			return schema;

		for (Pattern p : ignoreTypes)
			if (p.matcher(cm.getSimpleName()).matches() || p.matcher(cm.getName()).matches())
				return null;

		return fixSwaggerExtensions(schema.appendAll(js.getSchema(cm)));
	}

	/**
	 * Replaces non-standard JSON-Schema attributes with standard Swagger attributes.
	 */
	private ObjectMap fixSwaggerExtensions(ObjectMap om) {
		om.appendSkipNull("discriminator", om.remove("x-discriminator"));
		om.appendSkipNull("readOnly", om.remove("x-readOnly"));
		om.appendSkipNull("xml", om.remove("x-xml"));
		om.appendSkipNull("externalDocs", om.remove("x-externalDocs"));
		om.appendSkipNull("example", om.remove("x-example"));
		return om;
	}

	private void addXExamples(RestJavaMethod sm, ObjectMap piri, String in, Type type) throws Exception {

		String sex = piri.getString("x-example");

		if (sex == null) {
			ObjectMap schema = resolveRef(piri.getObjectMap("schema"));
			if (schema != null)
				sex = schema.getString("example", schema.getString("x-example"));
		}

		if (isEmpty(sex))
			return;

		boolean isOk = "ok".equals(in), isBody = "body".equals(in);

		Object example = null;
		if (isJson(sex)) {
			example = JsonParser.DEFAULT.parse(sex, type);
		} else {
			ClassMeta<?> cm = js.getClassMeta(type);
			if (cm.hasStringTransform()) {
				example = cm.getStringTransform().transform(sex);
			}
		}

		String examplesKey = isOk ? "examples" : "x-examples";  // Parameters don't have an examples attribute.

		ObjectMap examples = piri.getObjectMap(examplesKey);
		if (examples == null)
			examples = new ObjectMap();

		if (isOk || isBody) {
			List<MediaType> mediaTypes = isOk ? sm.getSerializers().getSupportedMediaTypes() : sm.getParsers().getSupportedMediaTypes();

			for (MediaType mt : mediaTypes) {
				if (mt != MediaType.HTML) {
					Serializer s2 = sm.getSerializers().getSerializer(mt);
					if (s2 != null) {
						SerializerSessionArgs args = new SerializerSessionArgs(null, req.getJavaMethod(), req.getLocale(), null, mt, req.isDebug() ? true : null, req.getUriContext(), true);
						try {
							String eVal = s2.createSession(args).serializeToString(example);
							examples.put(s2.getPrimaryMediaType().toString(), eVal);
						} catch (Exception e) {
							System.err.println("Could not serialize to media type ["+mt+"]: " + e.getLocalizedMessage());  // NOT DEBUG
						}
					}
				}
			}
		} else {
			String paramName = piri.getString("name");
			String s = sm.partSerializer.createSession(req.getSerializerSessionArgs()).serialize(HttpPartType.valueOf(in.toUpperCase()), null, example);
			if ("query".equals(in))
				s = "?" + urlEncodeLax(paramName) + "=" + urlEncodeLax(s);
			else if ("formData".equals(in))
				s = paramName + "=" + s;
			else if ("header".equals(in))
				s = paramName + ": " + s;
			else if ("path".equals(in))
				s = sm.getPathPattern().replace("{"+paramName+"}", urlEncodeLax(s));
 			examples.put("example", s);
		}

		if (! examples.isEmpty())
			piri.put(examplesKey, examples);
	}

	private ObjectMap resolveRef(ObjectMap m) {
		if (m == null)
			return null;
		if (m.containsKey("$ref") && js.getBeanDefs() != null) {
			String ref = m.getString("$ref");
			if (ref.startsWith("#/definitions/"))
				return js.getBeanDefs().get(ref.substring(14));
		}
		return m;
	}

	private ObjectMap getOperation(ObjectMap om, String path, String httpMethod) {
		if (! om.containsKey("paths"))
			om.put("paths", new ObjectMap());
		om = om.getObjectMap("paths");
		if (! om.containsKey(path))
			om.put(path, new ObjectMap());
		om = om.getObjectMap(path);
		if (! om.containsKey(httpMethod))
			om.put(httpMethod, new ObjectMap());
		return om.getObjectMap(httpMethod);
	}

	private static ObjectMap newMap(ObjectMap om) {
		if (om == null)
			return new ObjectMap();
		return om.modifiable();
	}

	private ObjectMap merge(ObjectMap om, Body a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.value().length > 0)
			om.putAll(parseMap(a.value()));
		if (a.api().length > 0)
			om.putAll(parseMap(a.api()));
		return om
			.appendSkipEmpty("description", resolve(a.description()))
			.appendSkipEmpty("x-example", resolve(a.example()))
			.appendSkipEmpty("x-examples", parseMap(a.examples()))
			.appendSkipFalse("required", a.required())
			.appendSkipEmpty("schema", merge(om.getObjectMap("schema"), a.schema()))
		;
	}

	private ObjectMap merge(ObjectMap om, Query a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.api().length > 0)
			om.putAll(parseMap(a.api()));
		return om
			.appendSkipFalse("allowEmptyValue", a.allowEmptyValue())
			.appendSkipEmpty("collectionFormat", a.collectionFormat())
			.appendSkipEmpty("default", joinnl(a._default()))
			.appendSkipEmpty("description", resolve(a.description()))
			.appendSkipEmpty("enum", toSet(a._enum()))
			.appendSkipEmpty("x-example", resolve(a.example()))
			.appendSkipFalse("exclusiveMaximum", a.exclusiveMaximum())
			.appendSkipFalse("exclusiveMinimum", a.exclusiveMinimum())
			.appendSkipEmpty("format", a.format())
			.appendSkipEmpty("items", merge(om.getObjectMap("items"), a.items()))
			.appendSkipEmpty("maximum", a.maximum())
			.appendSkipMinusOne("maxItems", a.maxItems())
			.appendSkipMinusOne("maxLength", a.maxLength())
			.appendSkipEmpty("minimum", a.minimum())
			.appendSkipMinusOne("minItems", a.minItems())
			.appendSkipMinusOne("minLength", a.minLength())
			.appendSkipEmpty("multipleOf", a.multipleOf())
			.appendSkipEmpty("pattern", a.pattern())
			.appendSkipFalse("required", a.required())
			.appendSkipEmpty("type", a.type())
			.appendSkipFalse("uniqueItems", a.uniqueItems())
		;
	}

	private ObjectMap merge(ObjectMap om, FormData a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.api().length > 0)
			om.putAll(parseMap(a.api()));
		return om
			.appendSkipFalse("allowEmptyValue", a.allowEmptyValue())
			.appendSkipEmpty("collectionFormat", a.collectionFormat())
			.appendSkipEmpty("default", joinnl(a._default()))
			.appendSkipEmpty("description", resolve(a.description()))
			.appendSkipEmpty("enum", toSet(a._enum()))
			.appendSkipEmpty("x-example", resolve(a.example()))
			.appendSkipFalse("exclusiveMaximum", a.exclusiveMaximum())
			.appendSkipFalse("exclusiveMinimum", a.exclusiveMinimum())
			.appendSkipEmpty("format", a.format())
			.appendSkipEmpty("items", merge(om.getObjectMap("items"), a.items()))
			.appendSkipEmpty("maximum", a.maximum())
			.appendSkipMinusOne("maxItems", a.maxItems())
			.appendSkipMinusOne("maxLength", a.maxLength())
			.appendSkipEmpty("minimum", a.minimum())
			.appendSkipMinusOne("minItems", a.minItems())
			.appendSkipMinusOne("minLength", a.minLength())
			.appendSkipEmpty("multipleOf", a.multipleOf())
			.appendSkipEmpty("pattern", a.pattern())
			.appendSkipFalse("required", a.required())
			.appendSkipEmpty("type", a.type())
			.appendSkipFalse("uniqueItems", a.uniqueItems())
		;
	}

	private ObjectMap merge(ObjectMap om, Header a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.api().length > 0)
			om.putAll(parseMap(a.api()));
		return om
			.appendSkipEmpty("collectionFormat", a.collectionFormat())
			.appendSkipEmpty("default", joinnl(a._default()))
			.appendSkipEmpty("description", resolve(a.description()))
			.appendSkipEmpty("enum", toSet(a._enum()))
			.appendSkipEmpty("x-example", resolve(a.example()))
			.appendSkipFalse("exclusiveMaximum", a.exclusiveMaximum())
			.appendSkipFalse("exclusiveMinimum", a.exclusiveMinimum())
			.appendSkipEmpty("format", a.format())
			.appendSkipEmpty("items", merge(om.getObjectMap("items"), a.items()))
			.appendSkipEmpty("maximum", a.maximum())
			.appendSkipMinusOne("maxItems", a.maxItems())
			.appendSkipMinusOne("maxLength", a.maxLength())
			.appendSkipEmpty("minimum", a.minimum())
			.appendSkipMinusOne("minItems", a.minItems())
			.appendSkipMinusOne("minLength", a.minLength())
			.appendSkipEmpty("multipleOf", a.multipleOf())
			.appendSkipEmpty("pattern", a.pattern())
			.appendSkipFalse("required", a.required())
			.appendSkipEmpty("type", a.type())
			.appendSkipFalse("uniqueItems", a.uniqueItems())
		;
	}

	private ObjectMap merge(ObjectMap om, Path a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.api().length > 0)
			om.putAll(parseMap(a.api()));
		return om
			.appendSkipEmpty("collectionFormat", a.collectionFormat())
			.appendSkipEmpty("description", resolve(a.description()))
			.appendSkipEmpty("enum", toSet(a._enum()))
			.appendSkipEmpty("x-example", resolve(a.example()))
			.appendSkipFalse("exclusiveMaximum", a.exclusiveMaximum())
			.appendSkipFalse("exclusiveMinimum", a.exclusiveMinimum())
			.appendSkipEmpty("format", a.format())
			.appendSkipEmpty("items", merge(om.getObjectMap("items"), a.items()))
			.appendSkipEmpty("maximum", a.maximum())
			.appendSkipMinusOne("maxLength", a.maxLength())
			.appendSkipEmpty("minimum", a.minimum())
			.appendSkipMinusOne("minLength", a.minLength())
			.appendSkipEmpty("multipleOf", a.multipleOf())
			.appendSkipEmpty("pattern", a.pattern())
			.appendSkipEmpty("type", a.type())
		;
	}

	private ObjectMap merge(ObjectMap om, Schema a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.value().length > 0)
			om.putAll(parseMap(a.value()));
		return om
			.appendSkipEmpty("additionalProperties", toObjectMap(a.additionalProperties()))
			.appendSkipEmpty("allOf", joinnl(a.allOf()))
			.appendSkipEmpty("collectionFormat", a.collectionFormat())
			.appendSkipEmpty("default", joinnl(a._default()))
			.appendSkipEmpty("discriminator", a.discriminator())
			.appendSkipEmpty("description", resolve(a.description()))
			.appendSkipEmpty("enum", toSet(a._enum()))
			.appendSkipEmpty("x-example", resolve(a.example()))
			.appendSkipEmpty("examples", parseMap(a.examples()))
			.appendSkipFalse("exclusiveMaximum", a.exclusiveMaximum())
			.appendSkipFalse("exclusiveMinimum", a.exclusiveMinimum())
			.appendSkipEmpty("externalDocs", merge(om.getObjectMap("externalDocs"), a.externalDocs()))
			.appendSkipEmpty("format", a.format())
			.appendSkipEmpty("ignore", a.ignore() ? "true" : null)
			.appendSkipEmpty("items", merge(om.getObjectMap("items"), a.items()))
			.appendSkipEmpty("maximum", a.maximum())
			.appendSkipMinusOne("maxItems", a.maxItems())
			.appendSkipMinusOne("maxLength", a.maxLength())
			.appendSkipMinusOne("maxProperties", a.maxProperties())
			.appendSkipEmpty("minimum", a.minimum())
			.appendSkipMinusOne("minItems", a.minItems())
			.appendSkipMinusOne("minLength", a.minLength())
			.appendSkipMinusOne("minProperties", a.minProperties())
			.appendSkipEmpty("multipleOf", a.multipleOf())
			.appendSkipEmpty("pattern", a.pattern())
			.appendSkipEmpty("properties", toObjectMap(a.properties()))
			.appendSkipFalse("readOnly", a.readOnly())
			.appendSkipFalse("required", a.required())
			.appendSkipEmpty("title", a.title())
			.appendSkipEmpty("type", a.type())
			.appendSkipFalse("uniqueItems", a.uniqueItems())
			.appendSkipEmpty("xml", joinnl(a.xml()))
			.appendSkipEmpty("$ref", a.$ref())
		;
	}

	private ObjectMap merge(ObjectMap om, ExternalDocs a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.value().length > 0)
			om.putAll(parseMap(a.value()));
		return om
			.appendSkipEmpty("description", resolve(a.description()))
			.appendSkipEmpty("url", a.url())
		;
	}

	private ObjectMap merge(ObjectMap om, Items a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.value().length > 0)
			om.putAll(parseMap(a.value()));
		return om
			.appendSkipEmpty("collectionFormat", a.collectionFormat())
			.appendSkipEmpty("default", joinnl(a._default()))
			.appendSkipEmpty("enum", toSet(a._enum()))
			.appendSkipEmpty("format", a.format())
			.appendSkipFalse("exclusiveMaximum", a.exclusiveMaximum())
			.appendSkipFalse("exclusiveMinimum", a.exclusiveMinimum())
			.appendSkipEmpty("items", merge(om.getObjectMap("items"), a.items()))
			.appendSkipEmpty("maximum", a.maximum())
			.appendSkipMinusOne("maxItems", a.maxItems())
			.appendSkipMinusOne("maxLength", a.maxLength())
			.appendSkipEmpty("minimum", a.minimum())
			.appendSkipMinusOne("minItems", a.minItems())
			.appendSkipMinusOne("minLength", a.minLength())
			.appendSkipEmpty("multipleOf", a.multipleOf())
			.appendSkipEmpty("pattern", a.pattern())
			.appendSkipFalse("uniqueItems", a.uniqueItems())
			.appendSkipEmpty("type", a.type())
			.appendSkipEmpty("$ref", a.$ref())
		;
	}

	private ObjectMap merge(ObjectMap om, SubItems a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.value().length > 0)
			om.putAll(parseMap(a.value()));
		return om
			.appendSkipEmpty("collectionFormat", a.collectionFormat())
			.appendSkipEmpty("default", joinnl(a._default()))
			.appendSkipEmpty("enum", toSet(a._enum()))
			.appendSkipFalse("exclusiveMaximum", a.exclusiveMaximum())
			.appendSkipFalse("exclusiveMinimum", a.exclusiveMinimum())
			.appendSkipEmpty("format", a.format())
			.appendSkipEmpty("items", toObjectMap(a.items()))
			.appendSkipEmpty("maximum", a.maximum())
			.appendSkipMinusOne("maxItems", a.maxItems())
			.appendSkipMinusOne("maxLength", a.maxLength())
			.appendSkipEmpty("minimum", a.minimum())
			.appendSkipMinusOne("minItems", a.minItems())
			.appendSkipMinusOne("minLength", a.minLength())
			.appendSkipEmpty("multipleOf", a.multipleOf())
			.appendSkipEmpty("pattern", a.pattern())
			.appendSkipEmpty("type", a.type())
			.appendSkipFalse("uniqueItems", a.uniqueItems())
			.appendSkipEmpty("$ref", a.$ref())
		;
	}

	private ObjectMap merge(ObjectMap om, ResponseBody a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.api().length > 0)
			om.putAll(parseMap(a.api()));
		return om
			.appendSkipEmpty("x-example", resolve(a.example()))
			.appendSkipEmpty("examples", joinnl(a.examples()))
			.appendSkipEmpty("schema", merge(om.getObjectMap("schema"), a.schema()))
		;
	}

	private ObjectMap merge(ObjectMap om, Response a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.api().length > 0)
			om.putAll(parseMap(a.api()));
		return om
			.appendSkipEmpty("description", resolve(a.description()))
			.appendSkipEmpty("x-example", resolve(a.example()))
			.appendSkipEmpty("examples", parseMap(a.examples()))
			.appendSkipEmpty("headers", merge(om.getObjectMap("headers"), a.headers()))
			.appendSkipEmpty("schema", merge(om.getObjectMap("schema"), a.schema()))
		;
	}

	private ObjectMap merge(ObjectMap om, ResponseHeader[] a) throws ParseException {
		if (a.length == 0)
			return om;
		om = newMap(om);
		for (ResponseHeader aa : a) {
			String name = StringUtils.firstNonEmpty(aa.name(), aa.value());
			if (isEmpty(name))
				throw new RuntimeException("@ResponseHeader used without name or value.");
			om.getObjectMap(name, true).putAll(merge(null, aa));
		}
		return om;
	}

	private ObjectMap merge(ObjectMap om, ResponseHeader a) throws ParseException {
		if (empty(a))
			return om;
		om = newMap(om);
		if (a.api().length > 0)
			om.putAll(parseMap(a.api()));
		return om
			.appendSkipEmpty("collectionFormat", a.collectionFormat())
			.appendSkipEmpty("default", joinnl(a._default()))
			.appendSkipEmpty("description", resolve(a.description()))
			.appendSkipEmpty("enum", toSet(a._enum()))
			.appendSkipEmpty("x-example", resolve(a.example()))
			.appendSkipFalse("exclusiveMaximum", a.exclusiveMaximum())
			.appendSkipFalse("exclusiveMinimum", a.exclusiveMinimum())
			.appendSkipEmpty("format", a.format())
			.appendSkipEmpty("items", merge(om.getObjectMap("items"), a.items()))
			.appendSkipEmpty("maximum", a.maximum())
			.appendSkipMinusOne("maxItems", a.maxItems())
			.appendSkipMinusOne("maxLength", a.maxLength())
			.appendSkipMinusOne("maxItems", a.maxItems())
			.appendSkipEmpty("minimum", a.minimum())
			.appendSkipMinusOne("minItems", a.minItems())
			.appendSkipMinusOne("minLength", a.minLength())
			.appendSkipEmpty("multipleOf", a.multipleOf())
			.appendSkipEmpty("pattern", a.pattern())
			.appendSkipEmpty("type", a.type())
			.appendSkipFalse("uniqueItems", a.uniqueItems())
			.appendSkipEmpty("$ref", a.$ref())
		;
	}

	private ObjectMap toObjectMap(String[] ss) throws ParseException {
		if (ss.length == 0)
			return null;
		String s = joinnl(ss);
		if (s.isEmpty())
			return null;
		if (! isObjectMap(s, true))
			s = "{" + s + "}";
		s = resolve(s);
		return new ObjectMap(s);
	}

	private Set<String> toSet(String[] ss) throws ParseException {
		if (ss.length == 0)
			return null;
		String s = joinnl(ss);
		if (s.isEmpty())
			return null;
		s = resolve(s);
		Set<String> set = new ASet<>();
		for (Object o : StringUtils.parseListOrCdl(s))
			set.add(o.toString());
		return set;
	}

	static String joinnl(String[] ss) {
		if (ss.length == 0)
			return "";
		return StringUtils.joinnl(ss).trim();
	}

	private static Set<Integer> getCodes(Response r, Integer def) {
		Set<Integer> codes = new LinkedHashSet<>();
		for (int i : r.value())
			codes.add(i);
		for (int i : r.code())
			codes.add(i);
		if (codes.isEmpty())
			codes.add(def);
		return codes;
	}

	private static Set<Integer> getCodes(ResponseHeader r, Integer def) {
		Set<Integer> codes = new LinkedHashSet<>();
		for (int i : r.code())
			codes.add(i);
		if (codes.isEmpty())
			codes.add(def);
		return codes;
	}
}