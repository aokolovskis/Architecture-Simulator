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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class BinaryFunctions {

    public static String normalizeStringlengt(String s2, int length) {
        if (!isBinaryString(s2)) { return null;}

        String s1 = s2;
        if (s1.length() > length) {
            return s1.substring(s1.length() - length, s1.length());
        }

        while (s1.length() < length) {
            s1 = "0" + s1;
        }
        return s1;

    }

    public static String xor(String s1, String s2) {

        if (!isBinaryString(s1)) { return null;}
        if (!isBinaryString(s2)) { return null;}

        StringPair sp = normalizeStringlengt(new StringPair(s1, s2));
        s1 = sp.getS1();
        s2 = sp.getS2();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            String append = "0";
            String sub1 = s1.substring(i, i + 1);
            String sub2 = s2.substring(i, i + 1);

            if (sub1.equals("0")) {
                if (sub2.equals("1")) {
                    append = "1";
                }

            } else if (sub1.equals("1")) {
                if (sub2.equals("0")) {
                    append = "1";
                }
            }

            sb.append(append);

        }


        return sb.toString();
    }

    public static String and(String s1, String s2) {
        if (!isBinaryString(s1)) { return null;}
        if (!isBinaryString(s2)) { return null;}

        StringPair sp = normalizeStringlengt(new StringPair(s1, s2));
        s1 = sp.getS1();
        s2 = sp.getS2();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            String append = "0";
            String sub1 = s1.substring(i, i + 1);
            String sub2 = s2.substring(i, i + 1);
            if (sub1.equals("1")) {
                if (sub2.equals("1")) {
                    append = "1";
                }
            }

            sb.append(append);

        }


        return sb.toString();
    }

    public static String or(String s1, String s2) {
        if (!isBinaryString(s1)) { return null;}
        if (!isBinaryString(s2)) { return null;}

        StringPair sp = normalizeStringlengt(new StringPair(s1, s2));
        s1 = sp.getS1();
        s2 = sp.getS2();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            String append = "0";
            String sub1 = s1.substring(i, i + 1);
            String sub2 = s2.substring(i, i + 1);

            if (sub1.equals("0")) {
                if (sub2.equals("1")) {
                    append = "1";
                }

            } else if (sub1.equals("1")) {
                append = "1";
            }

            sb.append(append);

        }


        return sb.toString();
    }

    public static String negate(String s1, int resultlength) {
        if (!isBinaryString(s1)) { return null;}
        while (s1.length() < resultlength) {
            s1 = "0" + s1;
        }
        return negate(s1);
    }

    public static String negate(String s1) {
        if (!isBinaryString(s1)) { return null;}
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            String append = "0";
            String sub1 = s1.substring(i, i + 1);

            if (sub1.equals("0")) {
                append = "1";
            }

            sb.append(append);

        }


        return sb.toString();
    }

    public static String twoK(String s1, int resultlength) {
        if (!isBinaryString(s1)) { return null;}
        return increment(
                negate(
                normalizeStringlengt(s1, resultlength),
                resultlength),
                resultlength, new Flag(false));

    }

    public static String add(String Op1, String Op2, int resultlength, Flag carry) {
        if (!isBinaryString(Op1)) { return null;}
        if (!isBinaryString(Op2)) { return null;}

        char op1[] = normalizeStringlengt(Op1, resultlength).toCharArray();
        char op2[] = normalizeStringlengt(Op2, resultlength).toCharArray();
        char result[] = op1;
        boolean smallcarry = false;

        for (int i = op2.length; i > 0;) {
            i--;
            if (op2[i] == 48 && op1[i] == 48) {
                if (smallcarry) {
                    result[i] = 49;
                    smallcarry = false;
                }
            } else if (op1[i] != op2[i]) {
                if (!smallcarry) {
                    result[i] = 49;
                } else {
                    smallcarry = true;
                    result[i] = 48;
                }

            } else if (op2[i] == 49 && op1[i] == 49) {
                if (smallcarry) {
                    result[i] = 49;
                } else {
                    smallcarry = true;
                    result[i] = 48;
                }
            }

        }

        if (carry != null) {
            carry.setFlag(smallcarry);
        }

        return new String(result);
    }

    public static String subTwoK(String Op1, String Op2, int resultlength, Flag carry) {

        return add(Op1, twoK(Op2, resultlength), resultlength, carry);

    }

    public static StringPair normalizeStringlengt(StringPair sp) {
        String s1 = sp.getS1();
        String s2 = sp.getS2();
        boolean swaped = false;


        if (s1.length() > s2.length()) {
            String tmp = s1;
            s1 = s2;
            s2 = tmp;
            swaped = true;
        }

        while (s1.length() < s2.length()) {
            s1 = "0" + s1;
        }

        return swaped ? new StringPair(s2, s1) : new StringPair(s1, s2);
    }

    public static String increment(String s1, int resultlength, Flag dst_carrie) {
        if (!isBinaryString(s1)) { return null;}

        boolean smallCarrie = true;
        char sb[] = normalizeStringlengt(s1, resultlength).toCharArray();

        for (int i = sb.length; i > 0 && smallCarrie;) {
            i--;
            char c = sb[i];
            if (c == 48) {

                c++;
                smallCarrie = false;
            } else if (c == 49) {
                c--;
            }

            sb[i] = c;
        }
        if (dst_carrie != null) {
            dst_carrie.setFlag(smallCarrie);
        }

        return new String(sb);
    }

    public static boolean isBinaryString(String stringToCheck) {
        boolean b = true;
        for (char c : stringToCheck.toCharArray()) {
            if (c != 48 && c != 49) {
                b = false;
                break;
            }
        }
        return b;

    }

    public static String hexToBinary(String string) {
        BigInteger bigInteger = new BigInteger(string, 16);
        bigInteger.toString(2);
        return bigInteger.toString(2);


    }
}
