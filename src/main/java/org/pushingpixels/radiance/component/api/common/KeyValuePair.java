/*
 * Copyright (c) 2005-2021 Radiance Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of the copyright holder nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pushingpixels.radiance.component.api.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic key-value pair with optional property map.
 *
 * @param <K> Key class.
 * @param <V> Value class.
 * @author Kirill Grouchnikov
 */
public class KeyValuePair<K, V> {
    /**
     * Pair key.
     */
    protected K key;

    /**
     * Pair value.
     */
    protected V value;

    /**
     * Property map.
     */
    protected Map<String, Object> propMap;

    /**
     * Creates a new pair.
     *
     * @param key   Pair key.
     * @param value Pair value.
     */
    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
        this.propMap = new HashMap<>();
    }

    /**
     * Returns the pair value.
     *
     * @return Pair value.
     */
    public V getValue() {
        return value;
    }

    /**
     * Returns the pair key.
     *
     * @return Pair key.
     */
    public K getKey() {
        return key;
    }

    /**
     * Returns the property attached to the specified key.
     *
     * @param propKey Property key.
     * @return Attached property.
     */
    public Object get(String propKey) {
        return this.propMap.get(propKey);
    }

    /**
     * Sets the property specified by the key and value.
     *
     * @param propKey   Property key.
     * @param propValue Property value.
     */
    public void set(String propKey, Object propValue) {
        this.propMap.put(propKey, propValue);
    }

    /**
     * Returns all attached properties.
     *
     * @return All attached properties.
     */
    public Map<String, Object> getProps() {
        return Collections.unmodifiableMap(this.propMap);
    }
}
