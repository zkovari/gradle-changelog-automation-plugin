/*******************************************************************************
 * Copyright 2019 Zsolt Kovari
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
package org.zkovari.changelog.core.processor;

public class InvalidChangelogEntryException extends Exception {

    private static final long serialVersionUID = 7904969661594993100L;

    public InvalidChangelogEntryException(String message, Throwable cause) {
	super(message, cause);
    }

    public InvalidChangelogEntryException(String message) {
	super(message);
    }

}
