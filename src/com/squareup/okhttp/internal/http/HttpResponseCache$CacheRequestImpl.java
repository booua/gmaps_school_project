// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.internal.Util;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CacheRequest;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            HttpResponseCache

private final class _cls1.out extends CacheRequest
{

    private OutputStream body;
    private OutputStream cacheOut;
    private boolean done;
    private final com.squareup.okhttp.internal.Impl.body editor;
    final HttpResponseCache this$0;

    public void abort()
    {
label0:
        {
            synchronized (HttpResponseCache.this)
            {
                if (!done)
                {
                    break label0;
                }
            }
            return;
        }
        done = true;
        HttpResponseCache.access$508(HttpResponseCache.this);
        httpresponsecache;
        JVM INSTR monitorexit ;
        Util.closeQuietly(cacheOut);
        try
        {
            editor.editor();
            return;
        }
        catch (IOException ioexception)
        {
            return;
        }
        exception;
        httpresponsecache;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public OutputStream getBody()
        throws IOException
    {
        return body;
    }



/*
    static boolean access$302(_cls1 _pcls1, boolean flag)
    {
        _pcls1.done = flag;
        return flag;
    }

*/

    public _cls1.val.editor(com.squareup.okhttp.internal.Impl impl)
        throws IOException
    {
        this$0 = HttpResponseCache.this;
        super();
        editor = impl;
        cacheOut = impl.(1);
        body = new FilterOutputStream(impl) {

            final HttpResponseCache.CacheRequestImpl this$1;
            final com.squareup.okhttp.internal.DiskLruCache.Editor val$editor;
            final HttpResponseCache val$this$0;

            public void close()
                throws IOException
            {
label0:
                {
                    synchronized (HttpResponseCache.CacheRequestImpl.this.this$0)
                    {
                        if (!done)
                        {
                            break label0;
                        }
                    }
                    return;
                }
                done = true;
                HttpResponseCache.access$408(HttpResponseCache.CacheRequestImpl.this.this$0);
                httpresponsecache1;
                JVM INSTR monitorexit ;
                super.close();
                editor.commit();
                return;
                exception;
                httpresponsecache1;
                JVM INSTR monitorexit ;
                throw exception;
            }

            public void write(byte abyte0[], int i, int j)
                throws IOException
            {
                out.write(abyte0, i, j);
            }

            
            {
                this$1 = HttpResponseCache.CacheRequestImpl.this;
                this$0 = httpresponsecache;
                editor = editor1;
                super(final_outputstream);
            }
        };
    }
}
