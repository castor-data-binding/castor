/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.test.test72;

import java.util.Comparator;

import org.junit.Ignore;

@Ignore
public final class CustomComparator implements Comparator<SortedContainerItem> {
    public int compare(final SortedContainerItem arg0, final SortedContainerItem arg1) {
        if (arg0 == null || arg1 == null) {
            throw new NullPointerException ("Objects to compare cannot be null");
        }
        
        return arg0.getId().compareTo(arg1.getId());
    }
}
