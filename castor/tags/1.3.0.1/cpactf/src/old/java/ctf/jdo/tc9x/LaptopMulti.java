/*
 * Copyright 2005 Werner Guttmann
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
package ctf.jdo.tc9x;

public final class LaptopMulti extends ComputerMulti {
    private int _weight;
    private String _resolution;

    public String getResolution() { return _resolution; }
    public void setResolution(final String resolution) { _resolution = resolution; }

    public int getWeight() { return _weight; }
    public void setWeight(final int weight) { _weight = weight; }
}
