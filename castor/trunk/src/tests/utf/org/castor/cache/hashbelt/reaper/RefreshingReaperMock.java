/*
 * Copyright 2006 Ralf Joachim
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
package utf.org.castor.cache.hashbelt.reaper;

import java.util.ArrayList;
import java.util.List;

import org.castor.cache.hashbelt.reaper.RefreshingReaper;

/**
 * Mock object for testing of RefreshingReaper.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class RefreshingReaperMock extends RefreshingReaper {
    private static final List REFRESHED = new ArrayList();
    
    public static List getExpiredObjects() {
        return REFRESHED;
    }
    
    protected Object refresh(final Object objectToBeRefreshed) {
        REFRESHED.add(objectToBeRefreshed);
        return ((String) objectToBeRefreshed) + " refreshed";
    }
}
