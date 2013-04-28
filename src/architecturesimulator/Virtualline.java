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
public class Virtualline {
   public enum Oriantation {horizontal,vertical};
   private Oriantation oriantation = Oriantation.horizontal;
   private int position = 0;

    public Virtualline() {

    }

    public Virtualline(int position, Oriantation oriantation) {
        this.position = position;
        this.oriantation = oriantation;
    }

    public Oriantation getOriantation() {
        return oriantation;
    }

    public void setOriantation(Oriantation oriantation) {
        this.oriantation = oriantation;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }




}
