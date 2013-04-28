/*
 * Copyright 2012 Aleksejs Okolovskis <oko@aloko.de>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecturesimulator;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class StringPair {

        String s1;
        String s2;

        public StringPair(String s1, String s2) {
            this.s1 = s1;
            this.s2 = s2;
        }

        public String getS1() {
            return s1;
        }

        public String getS2() {
            return s2;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof StringPair) {
                StringPair sp = (StringPair) obj;
                return (s1.equals(sp.getS1()) && s2.equals(sp.getS2())) ? true : false;
            }
            return false;
        }
    }
