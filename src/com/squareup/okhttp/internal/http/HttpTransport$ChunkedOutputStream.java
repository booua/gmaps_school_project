// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.internal.AbstractOutputStream;
import com.squareup.okhttp.internal.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            HttpTransport

private static final class <init> extends AbstractOutputStream
{

    private static final byte CRLF[] = {
        13, 10
    };
    private static final byte FINAL_CHUNK[] = {
        48, 13, 10, 13, 10
    };
    private static final byte HEX_DIGITS[] = {
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 
        97, 98, 99, 100, 101, 102
    };
    private final ByteArrayOutputStream bufferedChunk;
    private final byte hex[] = {
        0, 0, 0, 0, 0, 0, 0, 0, 13, 10
    };
    private final int maxChunkLength;
    private final OutputStream socketOut;

    private int dataLength(int i)
    {
        int j = 4;
        for (int k = i - j; k > 0; k >>= 4)
        {
            j++;
        }

        return i - j;
    }

    private void writeBufferedChunkToSocket()
        throws IOException
    {
        int i = bufferedChunk.size();
        if (i <= 0)
        {
            return;
        } else
        {
            writeHex(i);
            bufferedChunk.writeTo(socketOut);
            bufferedChunk.reset();
            socketOut.write(CRLF);
            return;
        }
    }

    private void writeHex(int i)
        throws IOException
    {
        int j = 8;
        do
        {
            byte abyte0[] = hex;
            j--;
            abyte0[j] = HEX_DIGITS[i & 0xf];
            i >>>= 4;
        } while (i != 0);
        socketOut.write(hex, j, hex.length - j);
    }

    public void close()
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        boolean flag = closed;
        if (!flag) goto _L2; else goto _L1
_L1:
        this;
        JVM INSTR monitorexit ;
        return;
_L2:
        closed = true;
        writeBufferedChunkToSocket();
        socketOut.write(FINAL_CHUNK);
        if (true) goto _L1; else goto _L3
_L3:
        Exception exception;
        exception;
        throw exception;
    }

    public void flush()
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        boolean flag = closed;
        if (!flag) goto _L2; else goto _L1
_L1:
        this;
        JVM INSTR monitorexit ;
        return;
_L2:
        writeBufferedChunkToSocket();
        socketOut.flush();
        if (true) goto _L1; else goto _L3
_L3:
        Exception exception;
        exception;
        throw exception;
    }

    public void write(byte abyte0[], int i, int j)
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        checkNotClosed();
        Util.checkOffsetAndCount(abyte0.length, i, j);
_L2:
        if (j <= 0)
        {
            break MISSING_BLOCK_LABEL_128;
        }
        int k;
        if (bufferedChunk.size() > 0 || j < maxChunkLength)
        {
            k = Math.min(j, maxChunkLength - bufferedChunk.size());
            bufferedChunk.write(abyte0, i, k);
            if (bufferedChunk.size() == maxChunkLength)
            {
                writeBufferedChunkToSocket();
            }
            break MISSING_BLOCK_LABEL_131;
        }
        k = maxChunkLength;
        writeHex(k);
        socketOut.write(abyte0, i, k);
        socketOut.write(CRLF);
        break MISSING_BLOCK_LABEL_131;
        Exception exception;
        exception;
        throw exception;
        this;
        JVM INSTR monitorexit ;
        return;
        i += k;
        j -= k;
        if (true) goto _L2; else goto _L1
_L1:
    }


    private I(OutputStream outputstream, int i)
    {
        socketOut = outputstream;
        maxChunkLength = Math.max(1, dataLength(i));
        bufferedChunk = new ByteArrayOutputStream(i);
    }

    bufferedChunk(OutputStream outputstream, int i, bufferedChunk bufferedchunk)
    {
        this(outputstream, i);
    }
}
