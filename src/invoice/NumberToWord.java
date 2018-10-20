/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

/**
 *
 * @author Shailesh
 */
public class NumberToWord {

    static String string;
    static String st1[] = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
    static String st2[] = {"Hundred", "Thousand", "Lakh", "Crore"};
    static String st3[] = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    static String st4[] = {"Twenty", "Thirty", "Fourty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

    public static String convert(String num) {
        String converted = "";
        String words[] = num.split("\\.");
        for (int i = 0; i < words.length; i++) {
            int n = Integer.parseInt(words[i]);
            int word;
            int nu = 1;
            string = "";
            while (n != 0) {
                switch (nu) {
                    case 1:
                        word = n % 100;
                        pass(word);
                        if (n > 100 && n % 100 != 0) {
                            show("and ");
                        }
                        n = n / 100;
                        break;
                    case 2:
                        word = n % 10;
                        if (word != 0) {
                            show(" ");
                            show(st2[0]);
                            show(" ");
                            pass(word);
                        }
                        n = n / 10;
                        break;
                    case 3:
                        word = n % 100;
                        if (word != 0) {
                            show(" ");
                            show(st2[1]);
                            show(" ");
                            pass(word);
                        }
                        n = n / 100;
                        break;
                    case 4:
                        word = n % 100;
                        if (word != 0) {
                            show(" ");
                            show(st2[2]);
                            show(" ");
                            pass(word);
                        }
                        n = n / 100;
                        break;
                    case 5:
                        word = n % 100;
                        if (word != 0) {
                            show(" ");
                            show(st2[3]);
                            show(" ");
                            pass(word);
                        }
                        n = n / 100;
                        break;
                }
                nu++;
            }
            converted += i == 0 ? string + " Rupees And " : string + " Paise";
        }
        return converted;
    }

    public static void pass(int n) {
        int word, q;
        if (n < 10) {
            show(st1[n]);
        }
        if (n > 9 && n < 20) {
            show(st3[n - 10]);
        }
        if (n > 19) {
            word = n % 10;
            if (word == 0) {
                q = n / 10;
                show(st4[q - 2]);
            } else {
                q = n / 10;
                show(st1[word]);
                show(" ");
                show(st4[q - 2]);
            }
        }
    }

    public static void show(String s) {
        String st = string;
        string = s + st;
    }
}
