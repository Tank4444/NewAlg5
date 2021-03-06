import java.io.PrintWriter;
import java.util.*;

public class Lab5 {
    public static void main(String[] args)
    {
        new Lab5();
    }

    public class state {

        int len, link;
        HashMap<Character, Integer> next = new HashMap<Character, Integer>();

        boolean is_clon;
        int first_pos;
        ArrayList<Integer> inv_link = new ArrayList<Integer>();

        public state() {
        }

    };

    public state[] st=new state[1000];
    public int sz, last;
    public HashSet<Integer> res = new HashSet<Integer>();

    public void sa_init() {
        sz = last = 0;
        if (st[0] == null) {
            st[0] = new state();
        }
        st[0].len = 0;
        st[0].link = -1;
        ++sz;

    }

    public  void sa_extend(char c) {
        int cur = sz++;
        if (st[cur] == null) {
            st[cur] = new state();
        }
        st[cur].first_pos = st[last].len;
        st[cur].len = st[last].len + 1;
        int p;
        for (p = last; p != -1 && !st[p].next.containsKey(c); p = st[p].link) {
            st[p].next.put(c, cur);
        }

        if (p == -1) {
            st[cur].link = 0;
        } else {
            int q = st[p].next.get(c);
            if (st[p].len + 1 == st[q].len) {
                st[cur].link = q;
            } else {
                int clone = sz++;
                if (st[clone] == null) {
                    st[clone] = new state();
                }
                st[clone].is_clon = true;
                st[clone].len = st[p].len + 1;
                st[clone].next.putAll(st[q].next);
                st[clone].link = st[q].link;
                st[clone].first_pos = st[q].first_pos;
                for (; p != -1 && st[p].next.get(c) == q; p = st[p].link) {
                    st[p].next.remove(c);
                    st[p].next.put(c, clone);
                }
                st[q].link = st[cur].link = clone;
            }
        }
        last = cur;
    }

    public  void output_all_occurences(int v, int P_length) {
        if (!st[v].is_clon) {
            res.add(st[v].first_pos - P_length + 2);
        }
        for (int i = 0; i < st[v].inv_link.size(); ++i) {
            output_all_occurences(st[v].inv_link.get(i), P_length);
        }
    }

    public Lab5() {
        Scanner in=new Scanner(System.in);
        out("Введите текст");
        String s1=in.nextLine();
        out("Введите искомую строку");
        String s2=in.nextLine();

        sa_init();
        for (int i = 0; i < s1.length(); i++) {
            sa_extend(s1.charAt(i));
        }
        for (int v = 1; v < sz; ++v) {
            st[st[v].link].inv_link.add(v);
        }
        state elem = st[0];
        int a = 0;
        for (int i = 0; i < s2.length(); i++) {
            if (elem.next.containsKey(s2.charAt(i))) {

                a = elem.next.get(s2.charAt(i));
                elem = st[a];
            } else {
                a = -1;
                break;
            }
        }
        if (a != -1) {
            output_all_occurences(a, s2.length());
        }
        out("Количество вхождений = "+res.size());
        out("Все подстроки");
        outAutomat();
    }
    boolean checkInput(String s)
    {
        return true;
    }
     void out(String s){System.out.println(s);}
     void outAutomat(){
        outAutomat(0,"");
     }
     void outAutomat(int i,String str)
     {
         out(str);
         for (Map.Entry entry : st[i].next.entrySet()) {
             outAutomat((int)entry.getValue(),new String(str+entry.getKey()+" "));
         }
     }
}
