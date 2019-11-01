package com.nawa.android.physix.model;

public class SQuiz {
    String ques, A, B, C, D, E, image, available;
    int num, anum, bnum, cnum , dnum ,enumb;
    long timer;

    public String getAvailable() {
        return available;
    }

    public int getDnum() {
        return dnum;
    }

    public int getEnumb() {
        return enumb;
    }

    public String getD() {
        return D;
    }

    public String getE() {
        return E;
    }

    public SQuiz(String ques, String a, String b, String c, String d, String e, String image, String available, int num, int anum, int bnum, int cnum, int dnum, int enumb, long timer) {
        this.ques = ques;
        A = a;
        B = b;
        C = c;
        D = d;
        E = e;
        this.image = image;
        this.available = available;
        this.num = num;
        this.anum = anum;
        this.bnum = bnum;
        this.cnum = cnum;
        this.dnum = dnum;
        this.enumb = enumb;
        this.timer = timer;
    }

    public String getImage() {
        return image;
    }

    public SQuiz() {
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {

        this.timer = timer;
    }

    public int getAnum() {
        return anum;
    }

    public void setAnum(int anum) {
        this.anum = anum;
    }

    public int getBnum() {
        return bnum;
    }

    public void setBnum(int bnum) {
        this.bnum = bnum;
    }

    public int getCnum() {
        return cnum;
    }

    public void setCnum(int cnum) {
        this.cnum = cnum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }
}
