/*******************************************************************************
 * Copyright 2019-2020 Zsolt Kovari
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.zkovari.changelog.domain;

public enum EntryType {

    ADDED("Added"), //
    CHANGED("Changed"), //
    DEPRECATED("Deprecated"), //
    REMOVED("Removed"), //
    FIXED("Fixed"), //
    SECURITY("Security");

    private final String value;

    private EntryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
