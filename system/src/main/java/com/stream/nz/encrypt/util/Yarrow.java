package com.stream.nz.encrypt.util;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Random;

@Slf4j
public class Yarrow extends Random {
    private static final long serialVersionUID = 1074890743336683644L;
    private static String seedfile;
    static final int[][] bitTable = new int[][]{{0, 0}, {1, 1}, {1, 3}, {1, 7}, {1, 15}, {1, 31}, {1, 63}, {1, 127}, {1, 255}, {2, 511}, {2, 1023}, {2, 2047}, {2, 4095}, {2, 8191}, {2, 16383}, {2, 32767}, {2, 65535}, {3, 131071}, {3, 262143}, {3, 524287}, {3, 1048575}, {3, 2097151}, {3, 4194303}, {3, 8388607}, {3, 16777215}, {4, 33554431}, {4, 67108863}, {4, 134217727}, {4, 268435455}, {4, 536870911}, {4, 1073741823}, {4, 2147483647}, {4, -1}};
    public byte[] ZERO_ARRAY = new byte[16384];
    private Hashtable<EntropySource, Integer> entropySeen;
    private MessageDigest fast_pool;
    private MessageDigest reseed_ctx;
    private MessageDigest slow_pool;
    private Rijndael cipher_ctx;
    private byte[] allZeroString;
    private byte[] counter;
    private byte[] output_buffer;
    private byte[] tmp;
    private boolean fast_select;
    protected int digestSize;
    protected int fast_entropy;
    protected int fetch_counter;
    protected int output_count;
    protected int slow_entropy;

    public Yarrow() {
        try {
            seedfile = (new File(new File(System.getProperty("java.io.tmpdir")), "prng.seed")).toString();
        } catch (Throwable var4) {
            seedfile = "prng.seed";
        }

        try {
            this.accumulator_init();
            this.reseed_init();
            this.generator_init(16);
            this.entropy_init(seedfile);
        } catch (RuntimeException var2) {
            throw var2;
        } catch (Exception var3) {
            throw new RuntimeException(var3.getMessage());
        }
    }

    public void acceptEntropy(EntropySource source, long data, int entropyGuess) {
        this.accept_entropy(data, source, Math.min(32, Math.min(this.estimateEntropy(source, data), entropyGuess)));
    }

    public void acceptTimerEntropy(EntropySource timer) {
        long now = System.currentTimeMillis();
        this.acceptEntropy(timer, now - timer.lastVal, 32);
    }

    public void makeKey(byte[] entropy, byte[] key, int offset, int len) {
        try {
            MessageDigest ctx = MessageDigest.getInstance("SHA1");

            int bc;
            for(int ic = 0; len > 0; len -= bc) {
                ++ic;

                for(bc = 0; bc < ic; ++bc) {
                    ctx.update((byte)0);
                }

                ctx.update(entropy, 0, entropy.length);
                if (len > 20) {
                    ctx.digest(key, offset, 20);
                    bc = 20;
                } else {
                    byte[] hash = ctx.digest();
                    bc = Math.min(len, hash.length);
                    System.arraycopy(hash, 0, key, offset, bc);
                }

                offset += bc;
            }

            this.wipe(entropy);
        } catch (Exception var9) {
            throw new RuntimeException("Could not generate key: " + var9.getMessage());
        }
    }

    public void wipe(byte[] data) {
        System.arraycopy(this.ZERO_ARRAY, 0, data, 0, data.length);
    }

    protected int next(int bits) {
        int[] parameters = bitTable[bits];
        int offset = this.getBytes(parameters[0]);
        int val = this.output_buffer[offset];
        if (parameters[0] == 4) {
            val += (this.output_buffer[offset + 1] << 24) + (this.output_buffer[offset + 2] << 16) + (this.output_buffer[offset + 3] << 8);
        } else if (parameters[0] == 3) {
            val += (this.output_buffer[offset + 1] << 16) + (this.output_buffer[offset + 2] << 8);
        } else if (parameters[0] == 2) {
            val += this.output_buffer[offset + 2] << 8;
        }

        return val & parameters[1];
    }

    private void accept_entropy(long data, EntropySource source, int actualEntropy) {
        MessageDigest pool = this.fast_select ? this.fast_pool : this.slow_pool;
        pool.update((byte)((int)data));
        pool.update((byte)((int)(data >> 8)));
        pool.update((byte)((int)(data >> 16)));
        pool.update((byte)((int)(data >> 24)));
        pool.update((byte)((int)(data >> 32)));
        pool.update((byte)((int)(data >> 40)));
        pool.update((byte)((int)(data >> 48)));
        pool.update((byte)((int)(data >> 56)));
        this.fast_select = !this.fast_select;
        if (this.fast_select) {
            this.fast_entropy += actualEntropy;
            if (this.fast_entropy > 100) {
                this.fast_pool_reseed();
            }
        } else {
            this.slow_entropy += actualEntropy;
            if (source != null) {
                Integer contributedEntropy = (Integer)this.entropySeen.get(source);
                if (contributedEntropy == null) {
                    contributedEntropy = Integer.valueOf(actualEntropy);
                } else {
                    contributedEntropy = Integer.valueOf(actualEntropy + contributedEntropy.intValue());
                }

                this.entropySeen.put(source, contributedEntropy);
                if (this.slow_entropy >= 320) {
                    int kc = 0;
                    Enumeration enums = this.entropySeen.keys();

                    while(enums.hasMoreElements()) {
                        Object key = enums.nextElement();
                        Integer v = (Integer)this.entropySeen.get(key);
                        if (v.intValue() > 160) {
                            ++kc;
                            if (kc >= 2) {
                                this.slow_pool_reseed();
                                break;
                            }
                        }
                    }
                }
            }
        }

    }

    private void accumulator_init() throws NoSuchAlgorithmException {
        this.fast_pool = MessageDigest.getInstance("SHA1");
        this.slow_pool = MessageDigest.getInstance("SHA1");
        this.digestSize = this.fast_pool.getDigestLength();
        this.entropySeen = new Hashtable();
    }

    private final void counterInc() {
        for(int i = this.counter.length - 1; i >= 0; --i) {
            byte[] tmp17_13 = this.counter;
            if (++tmp17_13[i] != 0) {
                break;
            }
        }

    }

    private void consumeBytes(byte[] bytes) {
        if (this.fast_select) {
            this.fast_pool.update(bytes, 0, bytes.length);
        } else {
            this.slow_pool.update(bytes, 0, bytes.length);
        }

        this.fast_select = !this.fast_select;
    }

    private void consumeString(String str) {
        if (str != null) {
            byte[] b = str.getBytes();
            this.consumeBytes(b);
        }
    }

    private void entropy_init(String seed) {
        Properties sys = System.getProperties();
        EntropySource startupEntropy = new EntropySource();
        Enumeration enums = sys.propertyNames();

        while(enums.hasMoreElements()) {
            String key = (String)enums.nextElement();
            this.consumeString(key);
            this.consumeString(sys.getProperty(key));
        }

        try {
            this.consumeString(InetAddress.getLocalHost().toString());
        } catch (Exception var6) {
            ;
        }

        this.acceptEntropy(startupEntropy, System.currentTimeMillis(), 0);
        this.read_seed(seed);
    }

    private final void generateOutput() {
        this.counterInc();
        this.cipher_ctx.encrypt(this.counter, this.output_buffer);
        if (this.output_count++ > 10) {
            this.output_count = 0;
            this.nextBytes(this.tmp);
            this.rekey(this.tmp);
        }

    }

    private synchronized int getBytes(int count) {
        if (this.fetch_counter + count > this.output_buffer.length) {
            this.fetch_counter = 0;
            this.generateOutput();
            return this.getBytes(count);
        } else {
            int rv = this.fetch_counter;
            this.fetch_counter += count;
            return rv;
        }
    }

    private int estimateEntropy(EntropySource source, long newVal) {
        int delta = (int)(newVal - source.lastVal);
        int delta2 = delta - source.lastDelta;
        source.lastDelta = delta;
        int delta3 = delta2 - source.lastDelta2;
        source.lastDelta2 = delta2;
        if (delta < 0) {
            delta = -delta;
        }

        if (delta2 < 0) {
            delta2 = -delta2;
        }

        if (delta3 < 0) {
            delta3 = -delta3;
        }

        if (delta > delta2) {
            delta = delta2;
        }

        if (delta > delta3) {
            delta = delta3;
        }

        delta >>= 1;
        delta &= 4095;
        delta |= delta >> 8;
        delta |= delta >> 4;
        delta |= delta >> 2;
        delta |= delta >> 1;
        delta >>= 1;
        delta -= delta >> 1 & 1365;
        delta = (delta & 819) + (delta >> 2 & 819);
        delta += delta >> 4;
        delta += delta >> 8;
        source.lastVal = newVal;
        return delta & 15;
    }

    private void fast_pool_reseed() {
        byte[] v0 = this.fast_pool.digest();
        byte[] vi = v0;

        for(byte i = 0; i < 5; ++i) {
            this.reseed_ctx.update(vi, 0, vi.length);
            this.reseed_ctx.update(v0, 0, v0.length);
            this.reseed_ctx.update(i);
            vi = this.reseed_ctx.digest();
        }

        this.makeKey(vi, this.tmp, 0, this.tmp.length);
        this.rekey(this.tmp);
        this.wipe(v0);
        this.fast_entropy = 0;
        this.write_seed(seedfile);
    }

    private void generator_init(int nBits) {
        this.cipher_ctx = new Rijndael();
        this.output_buffer = new byte[nBits];
        this.counter = new byte[nBits];
        this.allZeroString = new byte[nBits];
        this.tmp = new byte[nBits];
        this.fetch_counter = this.output_buffer.length;
    }

    private void read_seed(String filename) {
        EntropySource seedFile = new EntropySource();

        try {
            DataInputStream dis = null;

            try {
                dis = new DataInputStream(new FileInputStream(filename));

                for(int i = 0; i < 32; ++i) {
                    this.acceptEntropy(seedFile, dis.readLong(), 64);
                }
            } catch (Exception var13) {
                try {
                    Random rand = new Random();

                    for(int i = 0; i < 32; ++i) {
                        this.acceptEntropy(seedFile, rand.nextLong(), 64);
                    }
                } catch (Exception var12) {
                    log.warn("PRNG cannot do initial seed");
                }
            } finally {
                if (dis != null) {
                    dis.close();
                }

            }
        } catch (Exception var15) {
            log.warn("Could not read seed properly", var15);
        }

        this.fast_pool_reseed();
    }

    private void rekey(byte[] key) {
        this.cipher_ctx.makeKey(key, 128);
        this.cipher_ctx.encrypt(this.allZeroString, this.counter);
        this.wipe(key);
    }

    private void reseed_init() throws NoSuchAlgorithmException {
        this.reseed_ctx = MessageDigest.getInstance("SHA1");
    }

    private void slow_pool_reseed() {
        byte[] slow_hash = this.slow_pool.digest();
        this.fast_pool.update(slow_hash, 0, slow_hash.length);
        this.fast_pool_reseed();
        this.slow_entropy = 0;
        Integer ZERO = new Integer(0);
        Enumeration enums = this.entropySeen.keys();

        while(enums.hasMoreElements()) {
            this.entropySeen.put((EntropySource) enums.nextElement(), ZERO);
        }

    }

    private void write_seed(String filename) {
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename));

            for(int i = 0; i < 32; ++i) {
                dos.writeLong(this.nextLong());
            }

            dos.close();
        } catch (Exception var4) {
            log.warn("Could not write seed");
        }

    }

    public class EntropySource {
        public int lastDelta;
        public int lastDelta2;
        public long lastVal;

        public EntropySource() {
        }
    }
}
