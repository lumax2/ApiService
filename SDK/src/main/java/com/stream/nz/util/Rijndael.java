package com.stream.nz.util;

public final class Rijndael {
    public static final int DIR_ENCRYPT = 1;
    public static final int DIR_DECRYPT = 2;
    public static final int DIR_BOTH = 3;
    public static final int BLOCK_BITS = 128;
    public static final int BLOCK_SIZE = 16;
    private static final String SS = "捼睻\uf26b濅、末ﻗ\uab76쪂쥽繁䟰귔ꊯ鲤狀럽錦㘿\uf7cc㒥\ue5f1燘ㄕӇ⏃ᢖ֚ܒ胢\ueb27뉵ঃⰚ᭮媠刻횳⧣⾄发í\u20fc녛櫋븹䩌壏탯\uaafb䍍㎅䗹ɿ值龨冣䂏銝㣵벶\uda21ჿ\uf3d2촌Ꮼ得䐗쒧總摝ᥳ悁俜∪邈䛮렔\ude5e\u0bdb\ue032㨊䤆\u245c싓걢醕\ue479\ue7c8㝭跕亩汖\uf4ea敺금멸┮\u1ca6듆\ue8dd琟䮽變瀾땦䠃\uf60e愵垹蛁ᶞ\ue1f8頑槙躔鬞蟩칕⣟財褍뿦䉨䆙ⴏ끔묖";
    private static final byte[] Se = new byte[256];
    private static final int[] Te0 = new int[256];
    private static final int[] Te1 = new int[256];
    private static final int[] Te2 = new int[256];
    private static final int[] Te3 = new int[256];
    private static final byte[] Sd = new byte[256];
    private static final int[] Td0 = new int[256];
    private static final int[] Td1 = new int[256];
    private static final int[] Td2 = new int[256];
    private static final int[] Td3 = new int[256];
    private static final int[] rcon = new int[10];
    private int Nr = 0;
    private int Nk = 0;
    private int Nw = 0;
    private int[] rek = null;
    private int[] rdk = null;

    public Rijndael() {
    }

    private void expandKey(byte[] cipherKey) {
        int r = 0;
        int i = 0;

        int n;
        for(n = 0; i < this.Nk; n += 4) {
            this.rek[i] = cipherKey[n] << 24 | (cipherKey[n + 1] & 255) << 16 | (cipherKey[n + 2] & 255) << 8 | cipherKey[n + 3] & 255;
            ++i;
        }

        i = this.Nk;

        for(n = 0; i < this.Nw; --n) {
            int temp = this.rek[i - 1];
            if (n == 0) {
                n = this.Nk;
                temp = Se[temp >>> 16 & 255] << 24 | (Se[temp >>> 8 & 255] & 255) << 16 | (Se[temp & 255] & 255) << 8 | Se[temp >>> 24] & 255;
                temp ^= rcon[r++];
            } else if (this.Nk == 8 && n == 4) {
                temp = Se[temp >>> 24] << 24 | (Se[temp >>> 16 & 255] & 255) << 16 | (Se[temp >>> 8 & 255] & 255) << 8 | Se[temp & 255] & 255;
            }

            this.rek[i] = this.rek[i - this.Nk] ^ temp;
            ++i;
        }

        int temp = 0;
    }

    private void invertKey() {
        int d = 0;
        int e = 4 * this.Nr;
        this.rdk[d] = this.rek[e];
        this.rdk[d + 1] = this.rek[e + 1];
        this.rdk[d + 2] = this.rek[e + 2];
        this.rdk[d + 3] = this.rek[e + 3];
        d = d + 4;
        e -= 4;

        for(int r = 1; r < this.Nr; ++r) {
            int w = this.rek[e];
            this.rdk[d] = Td0[Se[w >>> 24] & 255] ^ Td1[Se[w >>> 16 & 255] & 255] ^ Td2[Se[w >>> 8 & 255] & 255] ^ Td3[Se[w & 255] & 255];
            w = this.rek[e + 1];
            this.rdk[d + 1] = Td0[Se[w >>> 24] & 255] ^ Td1[Se[w >>> 16 & 255] & 255] ^ Td2[Se[w >>> 8 & 255] & 255] ^ Td3[Se[w & 255] & 255];
            w = this.rek[e + 2];
            this.rdk[d + 2] = Td0[Se[w >>> 24] & 255] ^ Td1[Se[w >>> 16 & 255] & 255] ^ Td2[Se[w >>> 8 & 255] & 255] ^ Td3[Se[w & 255] & 255];
            w = this.rek[e + 3];
            this.rdk[d + 3] = Td0[Se[w >>> 24] & 255] ^ Td1[Se[w >>> 16 & 255] & 255] ^ Td2[Se[w >>> 8 & 255] & 255] ^ Td3[Se[w & 255] & 255];
            d += 4;
            e -= 4;
        }

        this.rdk[d] = this.rek[e];
        this.rdk[d + 1] = this.rek[e + 1];
        this.rdk[d + 2] = this.rek[e + 2];
        this.rdk[d + 3] = this.rek[e + 3];
    }

    public void makeKey(byte[] cipherKey, int keyBits, int direction) throws RuntimeException {
        if (keyBits != 128 && keyBits != 192 && keyBits != 256) {
            throw new RuntimeException("Invalid AES key size (" + keyBits + " bits)");
        } else {
            this.Nk = keyBits >>> 5;
            this.Nr = this.Nk + 6;
            this.Nw = 4 * (this.Nr + 1);
            this.rek = new int[this.Nw];
            this.rdk = new int[this.Nw];
            if ((direction & 3) != 0) {
                this.expandKey(cipherKey);
                if ((direction & 2) != 0) {
                    this.invertKey();
                }
            }

        }
    }

    public void makeKey(byte[] cipherKey, int keyBits) throws RuntimeException {
        this.makeKey(cipherKey, keyBits, 3);
    }

    public void encrypt(byte[] pt, byte[] ct) {
        int k = 0;
        int t0 = (pt[0] << 24 | (pt[1] & 255) << 16 | (pt[2] & 255) << 8 | pt[3] & 255) ^ this.rek[0];
        int t1 = (pt[4] << 24 | (pt[5] & 255) << 16 | (pt[6] & 255) << 8 | pt[7] & 255) ^ this.rek[1];
        int t2 = (pt[8] << 24 | (pt[9] & 255) << 16 | (pt[10] & 255) << 8 | pt[11] & 255) ^ this.rek[2];
        int t3 = (pt[12] << 24 | (pt[13] & 255) << 16 | (pt[14] & 255) << 8 | pt[15] & 255) ^ this.rek[3];

        for(int r = 1; r < this.Nr; ++r) {
            k += 4;
            int a0 = Te0[t0 >>> 24] ^ Te1[t1 >>> 16 & 255] ^ Te2[t2 >>> 8 & 255] ^ Te3[t3 & 255] ^ this.rek[k];
            int a1 = Te0[t1 >>> 24] ^ Te1[t2 >>> 16 & 255] ^ Te2[t3 >>> 8 & 255] ^ Te3[t0 & 255] ^ this.rek[k + 1];
            int a2 = Te0[t2 >>> 24] ^ Te1[t3 >>> 16 & 255] ^ Te2[t0 >>> 8 & 255] ^ Te3[t1 & 255] ^ this.rek[k + 2];
            int a3 = Te0[t3 >>> 24] ^ Te1[t0 >>> 16 & 255] ^ Te2[t1 >>> 8 & 255] ^ Te3[t2 & 255] ^ this.rek[k + 3];
            t0 = a0;
            t1 = a1;
            t2 = a2;
            t3 = a3;
        }

        k += 4;
        int v = this.rek[k];
        ct[0] = (byte)(Se[t0 >>> 24] ^ v >>> 24);
        ct[1] = (byte)(Se[t1 >>> 16 & 255] ^ v >>> 16);
        ct[2] = (byte)(Se[t2 >>> 8 & 255] ^ v >>> 8);
        ct[3] = (byte)(Se[t3 & 255] ^ v);
        v = this.rek[k + 1];
        ct[4] = (byte)(Se[t1 >>> 24] ^ v >>> 24);
        ct[5] = (byte)(Se[t2 >>> 16 & 255] ^ v >>> 16);
        ct[6] = (byte)(Se[t3 >>> 8 & 255] ^ v >>> 8);
        ct[7] = (byte)(Se[t0 & 255] ^ v);
        v = this.rek[k + 2];
        ct[8] = (byte)(Se[t2 >>> 24] ^ v >>> 24);
        ct[9] = (byte)(Se[t3 >>> 16 & 255] ^ v >>> 16);
        ct[10] = (byte)(Se[t0 >>> 8 & 255] ^ v >>> 8);
        ct[11] = (byte)(Se[t1 & 255] ^ v);
        v = this.rek[k + 3];
        ct[12] = (byte)(Se[t3 >>> 24] ^ v >>> 24);
        ct[13] = (byte)(Se[t0 >>> 16 & 255] ^ v >>> 16);
        ct[14] = (byte)(Se[t1 >>> 8 & 255] ^ v >>> 8);
        ct[15] = (byte)(Se[t2 & 255] ^ v);
    }

    public void decrypt(byte[] ct, byte[] pt) {
        int k = 0;
        int t0 = (ct[0] << 24 | (ct[1] & 255) << 16 | (ct[2] & 255) << 8 | ct[3] & 255) ^ this.rdk[0];
        int t1 = (ct[4] << 24 | (ct[5] & 255) << 16 | (ct[6] & 255) << 8 | ct[7] & 255) ^ this.rdk[1];
        int t2 = (ct[8] << 24 | (ct[9] & 255) << 16 | (ct[10] & 255) << 8 | ct[11] & 255) ^ this.rdk[2];
        int t3 = (ct[12] << 24 | (ct[13] & 255) << 16 | (ct[14] & 255) << 8 | ct[15] & 255) ^ this.rdk[3];

        for(int r = 1; r < this.Nr; ++r) {
            k += 4;
            int a0 = Td0[t0 >>> 24] ^ Td1[t3 >>> 16 & 255] ^ Td2[t2 >>> 8 & 255] ^ Td3[t1 & 255] ^ this.rdk[k];
            int a1 = Td0[t1 >>> 24] ^ Td1[t0 >>> 16 & 255] ^ Td2[t3 >>> 8 & 255] ^ Td3[t2 & 255] ^ this.rdk[k + 1];
            int a2 = Td0[t2 >>> 24] ^ Td1[t1 >>> 16 & 255] ^ Td2[t0 >>> 8 & 255] ^ Td3[t3 & 255] ^ this.rdk[k + 2];
            int a3 = Td0[t3 >>> 24] ^ Td1[t2 >>> 16 & 255] ^ Td2[t1 >>> 8 & 255] ^ Td3[t0 & 255] ^ this.rdk[k + 3];
            t0 = a0;
            t1 = a1;
            t2 = a2;
            t3 = a3;
        }

        k += 4;
        int v = this.rdk[k];
        pt[0] = (byte)(Sd[t0 >>> 24] ^ v >>> 24);
        pt[1] = (byte)(Sd[t3 >>> 16 & 255] ^ v >>> 16);
        pt[2] = (byte)(Sd[t2 >>> 8 & 255] ^ v >>> 8);
        pt[3] = (byte)(Sd[t1 & 255] ^ v);
        v = this.rdk[k + 1];
        pt[4] = (byte)(Sd[t1 >>> 24] ^ v >>> 24);
        pt[5] = (byte)(Sd[t0 >>> 16 & 255] ^ v >>> 16);
        pt[6] = (byte)(Sd[t3 >>> 8 & 255] ^ v >>> 8);
        pt[7] = (byte)(Sd[t2 & 255] ^ v);
        v = this.rdk[k + 2];
        pt[8] = (byte)(Sd[t2 >>> 24] ^ v >>> 24);
        pt[9] = (byte)(Sd[t1 >>> 16 & 255] ^ v >>> 16);
        pt[10] = (byte)(Sd[t0 >>> 8 & 255] ^ v >>> 8);
        pt[11] = (byte)(Sd[t3 & 255] ^ v);
        v = this.rdk[k + 3];
        pt[12] = (byte)(Sd[t3 >>> 24] ^ v >>> 24);
        pt[13] = (byte)(Sd[t2 >>> 16 & 255] ^ v >>> 16);
        pt[14] = (byte)(Sd[t1 >>> 8 & 255] ^ v >>> 8);
        pt[15] = (byte)(Sd[t0 & 255] ^ v);
    }

    protected final void finalize() {
        int i;
        if (this.rek != null) {
            for(i = 0; i < this.rek.length; ++i) {
                this.rek[i] = 0;
            }

            this.rek = null;
        }

        if (this.rdk != null) {
            for(i = 0; i < this.rdk.length; ++i) {
                this.rdk[i] = 0;
            }

            this.rdk = null;
        }

    }

    static {
        int ROOT = 283;

        for(int i1 = 0; i1 < 256; ++i1) {
            char c = "捼睻\uf26b濅、末ﻗ\uab76쪂쥽繁䟰귔ꊯ鲤狀럽錦㘿\uf7cc㒥\ue5f1燘ㄕӇ⏃ᢖ֚ܒ胢\ueb27뉵ঃⰚ᭮媠刻횳⧣⾄发í\u20fc녛櫋븹䩌壏탯\uaafb䍍㎅䗹ɿ值龨冣䂏銝㣵벶\uda21ჿ\uf3d2촌Ꮼ得䐗쒧總摝ᥳ悁俜∪邈䛮렔\ude5e\u0bdb\ue032㨊䤆\u245c싓걢醕\ue479\ue7c8㝭跕亩汖\uf4ea敺금멸┮\u1ca6듆\ue8dd琟䮽變瀾땦䠃\uf60e愵垹蛁ᶞ\ue1f8頑槙躔鬞蟩칕⣟財褍뿦䉨䆙ⴏ끔묖".charAt(i1 >>> 1);
            int s1 = (byte)((i1 & 1) == 0 ? c >>> 8 : c) & 255;
            int s2 = s1 << 1;
            if (s2 >= 256) {
                s2 ^= ROOT;
            }

            int s3 = s2 ^ s1;
            int i2 = i1 << 1;
            if (i2 >= 256) {
                i2 ^= ROOT;
            }

            int i4 = i2 << 1;
            if (i4 >= 256) {
                i4 ^= ROOT;
            }

            int i8 = i4 << 1;
            if (i8 >= 256) {
                i8 ^= ROOT;
            }

            int i9 = i8 ^ i1;
            int ib = i9 ^ i2;
            int id = i9 ^ i4;
            int ie = i8 ^ i4 ^ i2;
            Se[i1] = (byte)s1;
            int t;
            Te0[i1] = t = s2 << 24 | s1 << 16 | s1 << 8 | s3;
            Te1[i1] = t >>> 8 | t << 24;
            Te2[i1] = t >>> 16 | t << 16;
            Te3[i1] = t >>> 24 | t << 8;
            Sd[s1] = (byte)i1;
            Td0[s1] = t = ie << 24 | i9 << 16 | id << 8 | ib;
            Td1[s1] = t >>> 8 | t << 24;
            Td2[s1] = t >>> 16 | t << 16;
            Td3[s1] = t >>> 24 | t << 8;
        }

        int r = 1;
        rcon[0] = r << 24;

        for(int i = 1; i < 10; ++i) {
            r <<= 1;
            if (r >= 256) {
                r ^= ROOT;
            }

            rcon[i] = r << 24;
        }

    }
}

